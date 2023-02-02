package com.github.luccassantos4.test.resources;

import static io.restassured.RestAssured.given;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.luccassantos4.dto.PostDTO;
import com.github.luccassantos4.entities.Follower;
import com.github.luccassantos4.entities.Post;
import com.github.luccassantos4.entities.User;
import com.github.luccassantos4.repository.FollowerRepository;
import com.github.luccassantos4.repository.PostRepository;
import com.github.luccassantos4.repository.UserRepository;
import com.github.luccassantos4.resource.PostResource;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
public class PostResourceTest {
	
	@Inject
	UserRepository userRepository;
	@Inject
	FollowerRepository followerRepository;
	@Inject
	PostRepository postRepository;

	Long userId;
	Long userNotFollowerId;
	Long userFollowerId;
	
	@BeforeEach
	@Transactional
	public void setUP() {
		//Usuário padrão de testes
		var user = new User();
		user.setAge(30);
		user.setName("Fulano");
		userRepository.persist(user);
		userId = user.getId();
		
		//criação da postagem para o usuário padrão
		Post post = new Post();
		post.setText("Hello");
		post.setUser(user);
		postRepository.persist(post);
		
		//usuário que não segue ngm
		var userNotFollower = new User();
		userNotFollower.setAge(33);
		userNotFollower.setName("Cicrano");
		userRepository.persist(userNotFollower);
		userNotFollowerId = userNotFollower.getId();
		
		//usuário seguidor
		var userFollower = new User();
		userFollower.setAge(25);
		userFollower.setName("Beltrano");
		userRepository.persist(userFollower);
		userFollowerId = userFollower.getId();
		
		Follower follower = new Follower();
		follower.setUser(user);
		follower.setFollower(userFollower);
		followerRepository.persist(follower);
	}
	
	
	@Test
	@DisplayName("should create a post for a user")
	public void createPostTest() {
		
		var postRequest = new PostDTO();
		postRequest.setText("Some text");
		
		given()
			.contentType(ContentType.JSON)
			.body(postRequest)
			.pathParam("userId", userId)
		.when()
			.post()
		.then()
			.statusCode(201);
	}
	
	@Test
	@DisplayName("should return 404 when trying to make a post for an inexistent user")
	public void postForAnInexistentUserTest() {
		
		var postRequest = new PostDTO();
		postRequest.setText("Some text");
		
		var inexistentUserId = 999;
		
		given()
			.contentType(ContentType.JSON)
			.body(postRequest)
			.pathParam("userId", inexistentUserId)
		.when()
			.post()
		.then()
			.statusCode(404);
	}
	
	@Test
	@DisplayName("shold return 404 when user doesn't exist")
	public void listPostUserNotFoundTest() {
		
		var inexistentUserId = 999;
		
		given()
		   .pathParam("userId", inexistentUserId)
		.when()
		   .get()
		.then()
		   .statusCode(404);
	}
	
	
	@Test
	@DisplayName("shold return 400 when user is header not present")
	public void listPostFollowerHeaderNotSendTest() {
		
		given()
		   .pathParam("userId", userId)
		.when()
		   .get()
		.then()
		   .statusCode(400)
		   .body(Matchers.is("You forgot the header"));
	}
	
	@Test
	@DisplayName("shold return 400 when follower doesn't exist")
	public void listPostFollowerNotFoundTest() {
		
		var inexistentFollowerId = 999;
		
		given()
		   .pathParam("userId", userId)
		   .header("followerId", inexistentFollowerId)
		.when()
		   .get()
		.then()
		   .statusCode(400)
		   .body(Matchers.is("Inexistent followerId"));
	}
	
	@Test
	@DisplayName("shold return 403 when follower isn't a follower")
	public void listPostNotAFollower() {
		
		given()
		   .pathParam("userId", userId)
		   .header("followerId", userNotFollowerId)
		.when()
		   .get()
		.then()
		   .statusCode(403)
		   .body(Matchers.is("You can't see these posts"));
	}
	
	@Test
	@DisplayName("shold return posts")
	public void listPostsTest() {
		given()
		   .pathParam("userId", userId)
		   .header("followerId", userFollowerId)
		.when()
		   .get()
		.then()
		   .statusCode(200)
		   .body("size()", Matchers.is(0));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
