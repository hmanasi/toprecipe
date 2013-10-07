package com.toprecipe.repository;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.toprecipe.models.FoodItem;
import com.toprecipe.services.CategoryService;
import com.toprecipie.config.AbstractContainerTest;

public class FoodItemRepositoryTest extends AbstractContainerTest {

	@Autowired
	private FoodItemRepository inTest;
	@Autowired
	private CategoryRepository repository;
	@Autowired
	private CategoryService service;

	@After
	public void deleteCategories() {
		repository.deleteAll();
		inTest.deleteAll();
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void testCreateWithSameNameFails() {
		FoodItem item = new FoodItem();
		item.setTitle("foogItem");
		item.addCategory(service.createCategory("parent1"));
		inTest.save(item);

		item = new FoodItem();
		item.setTitle("foogItem");
		item.addCategory(service.createCategory("parent2"));
		inTest.save(item);
	}
}
