package com.babel.test.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.babel.test.model.User;

public interface UserService {
	
	User getUserById(Long id);
	
	List<User> getUsers();
	
	Page<User> findAll(Pageable pageable);

	User insertUser(User user);
	
	void updateUser(Long id, User user);

    void deleteUser(Long userId);
}
