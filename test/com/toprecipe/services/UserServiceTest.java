package com.toprecipe.services;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.toprecipe.config.AbstractContainerTest;
import com.toprecipe.models.User;
import com.toprecipe.repository.UserRepository;

public class UserServiceTest extends AbstractContainerTest {

	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;

	@After
	public void cleanup() {
		userRepository.deleteAll();
	}

	@Test
	public void testCreateUser() {
		userService.createUser("test@org.com", "password123");

		User u = userRepository.findByEmail("test@org.com");
		assertEquals("test@org.com", u.getEmail());
		assertNotNull(u.getPasswordHash());
		assertNotEquals("password123", u.getPasswordHash());
		assertFalse(u.isDisabled());
	}
	
	@Test(expected=DuplicateEmailException.class)
	public void testCreateUserWithDuplicateEmailFails ()
	{
		userService.createUser("test@org.com", "password123");
		userService.createUser("test@org.com", "password123");
	}
	
	@Test
	public void testLoginPositive (){
		userService.createUser("test@org.com", "password123");
		assertTrue (userService.login("test@org.com", "password123"));
	}
	
	@Test
	public void testLoginWithInvalidEmail (){
		assertFalse (userService.login("test@org.c", "password123"));
	}
	
	@Test
	public void testLoginWithInvalidPassword (){
		userService.createUser("test@org.com", "password123");
		assertFalse (userService.login("test@org.com", "password1234"));
	}

}
