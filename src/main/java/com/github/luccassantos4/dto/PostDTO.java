package com.github.luccassantos4.dto;

import java.time.LocalDateTime;

import com.github.luccassantos4.entities.Post;

import lombok.Data;

@Data
public class PostDTO {
	
	private String text;
	private LocalDateTime dateTime;
	
	public static PostDTO fromEntity(Post post) {
		var response = new PostDTO();
		response.setText(post.getText());
		response.setDateTime(post.getDateTime());
		
		return response;
	}
}
