package com.toprecipe.services;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toprecipe.models.Category;
import com.toprecipe.models.FoodItem;
import com.toprecipe.models.Recipe;
import com.toprecipe.repository.FoodItemRepository;
import com.toprecipe.repository.RecipeRepository;
import com.toprecipe.services.dataimport.CategoryParser;
import com.toprecipe.services.fetcher.ImageFileHelper;

@Service
public class RecipeService {
	private static String RECIPE_IMAGE_URL_PATH = "images/recipes/";
	@Autowired
	private CategoryParser categoryParser;
	@Autowired
	private FoodItemRepository foodItemRepository;
	@Autowired
	private RecipeRepository recipeRepository;
	@Autowired
	private ImageFileHelper imageFileHelper;

	@Transactional
	public Recipe addRecipe(Recipe recipe, String categoryTitle)
			throws IOException {
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

		if (recipe.getImage() != null) {
			File permanent = imageFileHelper.makeImagePermanent(recipe
					.getImage());
			recipe.setImage(RECIPE_IMAGE_URL_PATH + permanent.getName());
		}
		return recipeRepository.save(recipe);
	}
}