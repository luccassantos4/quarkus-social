package com.github.luccassantos4.dto;

import com.github.luccassantos4.entities.Follower;

import lombok.Data;

@Data
public class FollowerResponse {
	private Long id;
	private String name;
	
	
	public FollowerResponse() {
	}
	
	public FollowerResponse(Follower follower) {
		 this(follower.getId(), follower.getFollower().getName());
	}

	public FollowerResponse(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
}
