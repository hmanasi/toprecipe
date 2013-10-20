package com.toprecipe.repository;

import org.springframework.data.repository.CrudRepository;

import com.toprecipe.models.User;

public interface UserRepository extends CrudRepository<User, Long> {
	public User findByEmail(String email);
}
