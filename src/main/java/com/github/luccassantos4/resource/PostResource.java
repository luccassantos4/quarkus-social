package com.github.luccassantos4.resource;

import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.github.luccassantos4.dto.PostDTO;
import com.github.luccassantos4.entities.Post;
import com.github.luccassantos4.entities.User;
import com.github.luccassantos4.repository.FollowerRepository;
import com.github.luccassantos4.repository.PostRepository;
import com.github.luccassantos4.repository.UserRepository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

	@Inject
	UserRepository userRepository;
	@Inject
	PostRepository postRepository;
	@Inject
	FollowerRepository followerRepository;
	
	@POST
	@Transactional
	public Response savePost(@PathParam("userId") Long userId, PostDTO request) {
		User user = userRepository.findById(userId);

		if (user == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		Post post = new Post();
		post.setText(request.getText());
		post.setUser(user);
		
		postRepository.persist(post);
		
		return Response.status(Response.Status.CREATED).build();
	}

	@GET
	public Response listPost(@PathParam("userId") Long userId, @HeaderParam("followerId") Long followerId) {
		
		User user = userRepository.findById(userId);
		
		//tratativa para saber se o usuário dos posts existe
		if (user == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		//tratativa para impedir que seja digitado nulo
		if (followerId == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("You forgot the header").build();
		}
		
		User follower = userRepository.findById(followerId);
		
		//tratativa para saber se o seguidor existe
		if (follower == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Inexistent followerId").build();
		}
		
		boolean follows = followerRepository.follows(follower, user);
		
		//tratativa filtras as portagens somente de quem o user segue
		if(!follows) {
			return Response.status(Response.Status.FORBIDDEN).entity("You can't see these posts").build();
		}
		
		PanacheQuery<Post> query = postRepository.find("user", Sort.by("dateTime", Sort.Direction.Descending), user);
		
		var listPosts = query.list();
		
		var postResponseList = listPosts.stream().map(post -> PostDTO.fromEntity(post)).collect(Collectors.toList());

		return Response.ok(postResponseList).build();
	}
}
