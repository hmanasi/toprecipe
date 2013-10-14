package com.toprecipe.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.toprecipe.config.AbstractContainerTest;
import com.toprecipe.models.Recipe;

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
