package com.github.luccassantos4.resource;

import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.github.luccassantos4.dto.ResponseError;
import com.github.luccassantos4.dto.UserDTO;
import com.github.luccassantos4.entities.User;
import com.github.luccassantos4.repository.UserRepository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

	@Inject
	UserRepository userRepository;
	
	@Inject
	Validator validator;

	@POST
	@Transactional
	public Response createUser(UserDTO userDto) {
		
		//Tratativa para as validações (Erros Personalizados)
		Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDto);
		if (!violations.isEmpty()) {
			return ResponseError.createFromValidation(violations)
					.withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);
		}

		User user = new User();
		user.setAge(userDto.getAge());
		user.setName(userDto.getName());

		userRepository.persist(user);
		
		return Response.status(Response.Status.CREATED.getStatusCode()).entity(user).build();
	}

	@GET
	public Response listAllUsers() {
		PanacheQuery<User> query = userRepository.findAll();
		return Response.ok(query.list()).build();
	}

	@DELETE
	@Path("{id}")
	@Transactional
	public Response deleteUser(@PathParam("id") Long id) {
		User user = userRepository.findById(id);

		// tratativa para verificar se o usuário do delete existe
		if (user != null) {
			userRepository.delete(user);
			return Response.noContent().build();
		}

		return Response.status(Status.NOT_FOUND).build();
	}

	@PUT
	@Path("{id}")
	@Transactional
	public Response updateUser(@PathParam("id") Long id, UserDTO userData) {
		User user = userRepository.findById(id);

		// tratativa para verificar se o usuário do update existe
		if (user != null) {
			user.setName(userData.getName());
			user.setAge(userData.getAge());

			return Response.noContent().build();
		}

		return Response.status(Status.NOT_FOUND).build();
	}
}
