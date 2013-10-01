package com.toprecipe.services;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avaje.ebean.annotation.Transactional;
import com.toprecipe.models.Category;
import com.toprecipe.models.FoodItem;
import com.toprecipe.repository.CategoryRepository;
import com.toprecipe.repository.FoodItemRepository;

/*
 * A temporary class to add categories to the database. This class should be removed once DB schema is not reset every-time the server starts.
 */
@Service
public class CategoryInitializer {

	@Autowired
	private CategoryService service;
	@Autowired
	private CategoryRepository categoryRepo;
	@Autowired
	private FoodItemRepository foodItemRepo;

	@PostConstruct
	@Transactional
	public void initializeCategories() {

		if (categoryRepo.findByTitle("Vegetarian") == null) { // Assume
																// categories
																// are not added
																// to the
																// database
			// Main categories
			Category veg = service.createCategory("Vegetarian");
			// Sub Categories
			Category veggieRice = service.createCategory("Rice", veg);
			Category veggieCurry = service.createCategory("Curry", veg);

			FoodItem veggiePaneerTikka = createFoodItem("Paneer Tikka Masala",
					veggieCurry);

			Category nonVeg = service.createCategory("Non Vegetarian");
			// Sub Categories
			Category nonVeggRice = service.createCategory("Rice", nonVeg);
			Category nonVeggCurry = service.createCategory("Curry", nonVeg);
		}
	}

	private FoodItem createFoodItem(String title, Category... categories) {
		FoodItem foodItem = new FoodItem();
		foodItem.setTitle(title);
		foodItem = foodItemRepo.save(foodItem);
		for (Category c : categories) {
			foodItem.addCategory(c);
			c.addFoodItem(foodItem);
			categoryRepo.save(c);
		}
		
		return foodItem;
	}
}
