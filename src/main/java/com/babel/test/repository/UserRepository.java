package com.babel.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.babel.test.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	User findByName(String name);
	User findByEmail(String email);
}
