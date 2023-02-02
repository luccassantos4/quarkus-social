package com.github.luccassantos4.test.resources;

import static io.restassured.RestAssured.given;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.luccassantos4.dto.FollowDTO;
import com.github.luccassantos4.entities.User;
import com.github.luccassantos4.repository.UserRepository;
import com.github.luccassantos4.resource.FollowerResource;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
public class FollowerResourceTest {
	@Inject
	UserRepository userRepository;
	Long userId;
	Long followerId;

	@BeforeEach
	@Transactional
	void setUp() {

		// Usuário padrão de testes
		var user = new User();
		user.setAge(30);
		user.setName("Fulano");
		userRepository.persist(user);
		userId = user.getId();
		
		// Seguidor do usuário padrão
		var follower = new User();
		follower.setAge(34);
		follower.setName("Beltrano");
		userRepository.persist(follower);
		followerId = follower.getId();
	}
	
	@Test
	@DisplayName("shold return 409 when follower is equal to User id")
	public void sameUserAsFollowerTest() {
		
		var body = new FollowDTO();
		body.setFollowerId(userId);
		
		given()
			.contentType(ContentType.JSON)
			.body(body)
			.pathParam("userId", userId)
		.when()
			.put()
		.then()
			.statusCode(Response.Status.CONFLICT.getStatusCode())
		.body(Matchers.is("You can't follow yourself"));
	}
	
	@Test
	@DisplayName("shold return 404 on follow when User id doen't exist")
	public void userNotFoundWhenTryingToFollowTest() {
		
		var body = new FollowDTO();
		body.setFollowerId(userId);
		var inexistentUserId = 999;
		
		given()
			.contentType(ContentType.JSON)
			.body(body)
			.pathParam("userId", inexistentUserId)
		.when()
			.put()
		.then()
			.statusCode(Response.Status.NOT_FOUND.getStatusCode());
	}
	
	@Test
	@DisplayName("should follow a user")
	public void followUserTest() {
		
		var body = new FollowDTO();
		body.setFollowerId(followerId);
		
		given()
			.contentType(ContentType.JSON)
			.body(body)
			.pathParam("userId", userId)
		.when()
			.put()
		.then()
			.statusCode(Response.Status.NO_CONTENT.getStatusCode());
	}
	
	
	@Test
	@DisplayName("shold return 404 on list user followers and user id doen't exist")
	public void userNotFoundWhenListingFollowersTest() {
		
		var inexistentUserId = 999;
		
		given()
			.contentType(ContentType.JSON)
			.pathParam("userId", inexistentUserId)
		.when()
			.get()
		.then()
			.statusCode(Response.Status.NOT_FOUND.getStatusCode());
	}
	
//	@Test
//	@DisplayName("shold list a user's followers")
//	@Order(5)
//	public void ListingFollowersTest() {
//		
//		given()
//			.contentType(ContentType.JSON)
//			.pathParam("userId", userId)
//		.when()
//			.get()
//		.then()
//			.statusCode(Response.Status.OK.getStatusCode());
//	}
	
	
}
