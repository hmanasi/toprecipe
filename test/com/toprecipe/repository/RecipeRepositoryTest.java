package com.toprecipe.repository;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.toprecipe.models.Recipe;
import com.toprecipie.config.AbstractContainerTest;

public class RecipeRepositoryTest extends AbstractContainerTest {

	@Autowired
	RecipeRepository repository;

	@Test
	public void test() {
		Recipe r = new Recipe();
		r.setTitle("JPA repository recipe");
		
		r = repository.save(r);
		assertNotNull(r.getId());

		r = repository.findOne(r.getId());

		assertEquals("JPA repository recipe", r.getTitle());
	}

}
