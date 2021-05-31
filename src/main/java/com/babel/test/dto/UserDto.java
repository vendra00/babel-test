package com.babel.test.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.babel.test.model.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

	private String name;
	private String email;
	private int accountNumber;

	public UserDto(User user) {
		this.name = user.getName();
		this.email = user.getEmail();
		this.accountNumber = user.getAccount().getAccountNumber();

	}

	public static Page<UserDto> convertPage(Page<User> users) {
		return users.map(UserDto::new);
	}
	
	public static List<UserDto> converter(List<User> users) {
		return users.stream().map(UserDto::new).collect(Collectors.toList());
	}

}
