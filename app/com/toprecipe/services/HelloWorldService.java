package com.toprecipe.services;

import org.springframework.stereotype.Service;

@Service
public class HelloWorldService {
	
	public HelloWorldService ()
	{
		System.out.println("service initialized...");
	}

	public String hellowWorld ()
	{
		return "Hello world with spring!";
	}
	
}
