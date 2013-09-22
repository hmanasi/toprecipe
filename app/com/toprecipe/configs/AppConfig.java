package com.toprecipe.configs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages={"com.toprecipe.controllers","com.toprecipe.services"})
public class AppConfig {
	
	public AppConfig()
	{
		System.out.println("Loading app config...");
	}

}