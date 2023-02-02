package com.github.luccassantos4.test.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.github.luccassantos4.dto.ResponseError;
import com.github.luccassantos4.dto.UserDTO;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserResourceTest {

	@TestHTTPResource("/users")
	URL apiURL;
	
	@Test
	@DisplayName("should create an user successfully")
	@Order(1)
	public void createUserTest() {
		var user = new UserDTO();
		user.setName("Fulano Santos");
		user.setAge(30);
		
		var response = 
			given()
				.contentType(ContentType.JSON)
				.body(user)
			.when()
				.post(apiURL)
			.then()
				.extract().response();
		
		assertEquals(201, response.statusCode());
		assertNotNull(response.jsonPath().getString("id"));
	}
	
	@Test
	@DisplayName("should return when json is not valid")
	@Order(2)
	public void createUserValidationErrorTest() {
		var user = new UserDTO();
		user.setName(null);
		user.setAge(null);
		
		var response =
				given()
					.contentType(ContentType.JSON)
					.body(user)
				.when()
					.post(apiURL)
				.then()
					.extract()
					.response();
		
		assertEquals(ResponseError.UNPROCESSABLE_ENTITY_STATUS, response.statusCode());
		assertEquals("Validation Error", response.jsonPath().getString("message"));
		
		List<Map<String, String>> errors = response.jsonPath().getList("error");
		assertNotNull(errors.get(0).get("message"));
		assertNotNull(errors.get(1).get("message"));
		
		
		
//		Especificando a mensagem que não pode ser nula
//		assertEquals("Name is Required", errors.get(0).get("message"));
//		assertEquals("Age is Required", errors.get(1).get("message"));
	}
	
	
	@Test
	@DisplayName("shold list all users")
	@Order(3)
	public void ListAllUsersTest() {
		given()
			.contentType(ContentType.JSON)
		.when()
			.get(apiURL)
		.then()
			.statusCode(200)
			.body("size()", Matchers.is(1));
		
	}
}
