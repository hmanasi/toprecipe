package com.toprecipe.services;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.toprecipe.models.Category;
import com.toprecipe.models.FoodItem;
import com.toprecipe.repository.CategoryRepository;
import com.toprecipe.repository.FoodItemRepository;
import com.toprecipie.config.AbstractContainerTest;

public class CategoryInitializerTest extends AbstractContainerTest {

	@Autowired
	private CategoryInitializer initializer;
	@Autowired
	private CategoryRepository categoryRepo;
	@Autowired
	private FoodItemRepository foodItemRepo;

	@Test
	@Transactional
	public void testCategoryAndFoodItemInitialized() {
		Category vegieCategory = categoryRepo.findByTitle("Vegetarian");
		assertNotNull(vegieCategory);

		Category vegieCurry = categoryRepo.findByTitleAndParent("Curry",
				vegieCategory);
		assertNotNull(vegieCurry);
		assertEquals(vegieCategory.getId(), vegieCurry.getParent().getId());

		FoodItem item = foodItemRepo.findByTitle("Paneer Tikka Masala");
		assertNotNull(item);
		Long curryId = vegieCurry.getId();
		boolean found = false;
		for (Category c : item.getCategories()) {
			if (c.getId() == curryId) {
				found = true;
				break;
			}
		}
		assertTrue(found);

	}

	@Test
	public void testReinvocationDoesNotCreateDuplicate() {
		initializer.initializeCategories();
		categoryRepo.findByTitle("Vegetarian");
	}
}
