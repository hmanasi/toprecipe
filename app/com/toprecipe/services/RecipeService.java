package com.toprecipe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toprecipe.models.Category;
import com.toprecipe.models.FoodItem;
import com.toprecipe.models.Recipe;
import com.toprecipe.repository.FoodItemRepository;
import com.toprecipe.repository.RecipeRepository;
import com.toprecipe.services.dataimport.CategoryParser;

@Service
public class RecipeService {

	@Autowired
	private CategoryParser categoryParser;
	@Autowired
	private FoodItemRepository foodItemRepository;
	@Autowired
	private RecipeRepository recipeRepository;

	@Transactional
	public Recipe addRecipe(Recipe recipe, String categoryTitle) {
		Category category = null;
		if (categoryTitle != null) {
			category = categoryParser.parseForExistingCategory(categoryTitle);

			FoodItem foodItem = foodItemRepository.findByTitle(recipe
					.getTitle());

			if (foodItem == null) {
				foodItem = new FoodItem();
				foodItem.setTitle(recipe.getTitle());
			}

			foodItem.addCategory(category);
			foodItemRepository.save(foodItem);
			recipe.setFoodItem(foodItem);
		}

		return recipeRepository.save(recipe);
	}
}
