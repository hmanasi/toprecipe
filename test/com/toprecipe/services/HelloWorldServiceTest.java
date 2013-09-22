package com.toprecipe.services;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.toprecipe.configs.AppConfig;
import com.toprecipie.config.AbstractContainerTest;
import com.toprecipie.config.TestDataConfig;

public class HelloWorldServiceTest extends AbstractContainerTest {

	@Autowired
	HelloWorldService service;

	@Test
	public void test() {
		assertEquals("Hello world with spring!", service.hellowWorld());
	}

}
