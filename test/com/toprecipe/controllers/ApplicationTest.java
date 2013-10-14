package com.toprecipe.controllers;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.toprecipe.config.AbstractContainerTest;

public class ApplicationTest extends AbstractContainerTest{

	@Autowired
	Application app;
	
	@Test
	public void testOutcome ()
	{
		assertNotNull (app.index());
	}
	
}
