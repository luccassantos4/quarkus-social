package com.github.luccassantos4.resource;

import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.github.luccassantos4.dto.FollowDTO;
import com.github.luccassantos4.dto.FollowerPerUserResponse;
import com.github.luccassantos4.dto.FollowerResponse;
import com.github.luccassantos4.entities.Follower;
import com.github.luccassantos4.repository.FollowerRepository;
import com.github.luccassantos4.repository.UserRepository;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {

	@Inject
	FollowerRepository followerRepository;

	@Inject
	UserRepository userRepository;

	@PUT
	@Transactional
	public Response followUser(@PathParam("userId") Long userId, FollowDTO followDto) {

		// tratativa para impedir que um usuário siga ele mesmo
		if (userId.equals(followDto.getFollowerId())) {
			return Response.status(Response.Status.CONFLICT).entity("You can't follow yourself").build();
		}

		// tratativa caso o usuário no pathParam não existe
		var user = userRepository.findById(userId);

		if (user == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		// tratativa para impedir que um usuário siga 2x
		var follower = userRepository.findById(followDto.getFollowerId());

		boolean follows = followerRepository.follows(follower, user);

		if (!follows) {
			var entity = new Follower();
			entity.setUser(user);
			entity.setFollower(follower);

			followerRepository.persist(entity);
		}

		return Response.status(Response.Status.NO_CONTENT).build();
	}

	@GET
	public Response listFollowers(@PathParam("userId") Long userId) {

		// tratativa caso o usuário no pathParam não existe
		var user = userRepository.findById(userId);

		if (user == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		// tratativa para buscar todos os seguidores que estão salvos na tabela follower
		var list = followerRepository.findByUser(userId);
		FollowerPerUserResponse responseObject = new FollowerPerUserResponse();

//		populando campo count
		responseObject.setFollowersCount(list.size());

//		populando campo content
		var followerList = list.stream().map(FollowerResponse::new).collect(Collectors.toList());
		responseObject.setContent(followerList);

		return Response.ok(responseObject).build();
	}

	@DELETE
	@Transactional
	public Response unfollower(@PathParam("userId") Long userId, @QueryParam("followerId") Long followerId) {

		// tratativa caso o usuário no pathParam não existe
		var user = userRepository.findById(userId);

		if (user == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		followerRepository.deleteByFollowerAndUser(followerId, userId);

		return Response.status(Response.Status.OK).entity(followerId).build();

	}

}
