package com.babel.test.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.babel.test.model.User;
import com.babel.test.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;


	@Override
	public User insertUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public void updateUser(Long id, User user) {
		User userFromDb = userRepository.findById(id).get();
		userFromDb.setName(user.getName());
		userFromDb.setEmail(user.getEmail());
		userRepository.save(userFromDb);

	}

	@Override
	public void deleteUser(Long userId) {
		userRepository.deleteById(userId);
	}

	@Override
	public List<User> getUsers() {
		List<User> users = new ArrayList<>();
		userRepository.findAll().forEach(users::add);
		return users;
	}

	@Override
	public User getUserById(Long id) {
		return userRepository.findById(id).get();
	}

	@Override
	public Page<User> findAll(Pageable pageable) {
		Page<User> users = userRepository.findAll(pageable);
		return users;
	}

}
