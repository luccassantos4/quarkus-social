package com.github.luccassantos4.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.github.luccassantos4.entities.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
	
	@NotBlank(message = "Name is Required")
	private String name;
	
	@NotNull(message = "Age is Required")
	private Integer age;
	
	
	public UserDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public UserDTO(User user) {
		name = user.getName();
		age = user.getAge();
	}



	

	
}
