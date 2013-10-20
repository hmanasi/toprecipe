package com.toprecipe.services;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.toprecipe.models.User;
import com.toprecipe.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private PasswordHashService passwordHashService;
	@Autowired
	private UserRepository userRepository;

	@Transactional(propagation=Propagation.NEVER)
	public void createUser(String email, String password) {
		User user = new User();
		user.setEmail(email);
		user.setDisabled(false);
		try {
			user.setPasswordHash(passwordHashService.createHash(password));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RuntimeException(e);
		}

		try {
			userRepository.save(user);
		} catch (DataIntegrityViolationException e) {
			if (e.getMessage() != null && e.getMessage().contains("USER_U1")) {
				throw new DuplicateEmailException();
			}
		} 
	}

	@Transactional(readOnly = true)
	public boolean login(String email, String password) {
		User u = userRepository.findByEmail(email);

		if (u == null || u.isDisabled()) {
			try { // This code ensures same amount of time is taken even if user
					// was not found or if user account was disabled.
				passwordHashService.validatePassword(" ", "1000:2000:24023");
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				throw new RuntimeException(e);
			}
			return false;
		}

		try {
			return passwordHashService.validatePassword(password,
					u.getPasswordHash());
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RuntimeException(e);
		}
	}

	@Transactional
	public void disableUser(String email) {

	}
}
