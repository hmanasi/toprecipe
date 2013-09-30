package com.toprecipe.services;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avaje.ebean.annotation.Transactional;
import com.toprecipe.models.Category;

/*
 * A temporary class to add categories to the database. This class should be removed once DB schema is not reset every-time the server starts.
 */
@Service
public class CategoryInitializer {

	@Autowired
	private CategoryService service;
	
	//@PostConstruct
	@Transactional
	public void initializeCategories ()
	{
		System.out.println("Creating built in categories...");
		//Main categories
		Category veg = service.createCategory("Veg");
		//Sub Categories
		Category veggieRice = service.createCategory("Rice", veg);
	}
}
