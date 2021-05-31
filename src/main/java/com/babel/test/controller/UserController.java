package com.babel.test.controller;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.babel.test.dto.UserDto;
import com.babel.test.exceptions.ResourceNotFoundException;
import com.babel.test.model.User;
import com.babel.test.service.UserService;

import io.swagger.annotations.Api;

@Api
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Cacheable(value = "listAllUsers")
	@GetMapping("/find-all-users")
	public ResponseEntity<List<UserDto>> getAllUsers() {
		List<User> users = userService.getUsers();
		return ResponseEntity.ok(UserDto.converter(users));
	}

	@Cacheable(value = "listAllUsers")
	@GetMapping("/find-all-users-page")
	public ResponseEntity<Page<UserDto>> getAllUsersPageable(@PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
		Page<User> users = userService.findAll(pageable);
		return ResponseEntity.ok(UserDto.convertPage(users));

	}

	@GetMapping({ "/find-users-by-id/{userId}" })
	public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) throws ResourceNotFoundException{	
		Optional<User> user = Optional.of(userService.getUserById(userId));
		if (user.isPresent()) {
			return ResponseEntity.ok(new UserDto(user.get()));
		}
		return ResponseEntity.notFound().build();
	}

	@CacheEvict(value = "listAllUsers", allEntries = true)
	@Transactional
	@PostMapping({ "/insert-user/" })
	public ResponseEntity<User> addUser(@RequestBody User newUser) {
		User userBody = userService.insertUser(newUser);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("user", "/users/insert-user/" + userBody.getId().toString());
		return new ResponseEntity<>(userBody, httpHeaders, HttpStatus.CREATED);
	}

	@CacheEvict(value = "listAllUsers", allEntries = true)
	@Transactional
	@PutMapping({ "/update-user/{userId}" })
	public ResponseEntity<User> updateUser(@PathVariable("userId") Long userId, @RequestBody User user) throws ResourceNotFoundException{
		userService.updateUser(userId, user);
		return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
	}

	@CacheEvict(value = "listAllUsers", allEntries = true)
	@Transactional
	@DeleteMapping({ "/delete-user/{userId}" })
	public ResponseEntity<User> deleteUser(@PathVariable("userId") Long userId) throws ResourceNotFoundException{
		userService.deleteUser(userId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
