package com.github.luccassantos4.dto;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;

import lombok.Data;

@Data
public class ResponseError {
	
	public static final int UNPROCESSABLE_ENTITY_STATUS = 422;
	
	private String message;
	private Collection<FieldError> error;
	
	public ResponseError(String message, Collection<FieldError> error) {
		super();
		this.message = message;
		this.error = error;
	}

	public static <T>ResponseError createFromValidation(Set<ConstraintViolation<T>> violations){
		
		List<FieldError> erros = violations.stream().map(cv -> new FieldError(cv.getPropertyPath().toString(), cv.getMessage()) )
		.collect(Collectors.toList());
		
		String message = "Validation Error";
		
		var responseError = new ResponseError(message, erros);
		return responseError;
		
	}
	
	public Response withStatusCode(int code) {
		return Response.status(code).entity(this).build();
	}
	
}
