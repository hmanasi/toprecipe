package com.toprecipe.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class PasswordHashServiceTest {

	PasswordHashService inTest = new PasswordHashService();

	@Test
	public void generateAndcompareTenHashes() throws NoSuchAlgorithmException,
			InvalidKeySpecException {
		Set<String> h = new HashSet<>();
		for (int i = 0; i < 10; i++) {
			String hash = inTest.createHash("p\r\nassw0Rd!");
			h.add(hash);
			System.out.println(hash);
		}

		assertEquals(10, h.size());
	}

	@Test
	public void testValidationHundresTimes() throws NoSuchAlgorithmException, InvalidKeySpecException {
		for (int i = 0; i < 100; i++) {
			String password = "" + i;
			String hash = inTest.createHash(password);
			String secondHash = inTest.createHash(password);
			if (hash.equals(secondHash)) {
				fail("FAILURE: TWO HASHES ARE EQUAL!");
			}
			String wrongPassword = "" + (i + 1);
			if (inTest.validatePassword(wrongPassword, hash)) {
				fail("FAILURE: WRONG PASSWORD ACCEPTED!");
			}
			if (!inTest.validatePassword(password, hash)) {
				fail("FAILURE: GOOD PASSWORD NOT ACCEPTED!");
			}
		}
	}
}
