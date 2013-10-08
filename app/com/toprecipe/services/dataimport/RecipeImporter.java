package com.toprecipe.services.dataimport;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.toprecipe.models.FoodItem;
import com.toprecipe.models.Recipe;
import com.toprecipe.repository.FoodItemRepository;
import com.toprecipe.repository.RecipeRepository;

@Service
public class RecipeImporter {

	@Autowired
	private FoodItemRepository foodItemRepo;

	@Autowired
	private RecipeRepository recipeRepository;

	@Transactional
	public void importRecipes(InputStream in) throws JsonProcessingException,
			IOException, RecipeImportException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectReader reader = mapper.reader(RecipeJson.class);

		MappingIterator<RecipeJson> i = reader.readValues(in);

		while (i.hasNext()) {
			RecipeJson recipeJson = i.nextValue();
			createFoodItem(recipeJson);
		}

	}

	private void createFoodItem(RecipeJson recipeJson)
			throws RecipeImportException {

		if (recipeJson.getTitle() == null) {
			throw new RecipeImportException("title not specified for a recipe.");
		}

		if (recipeJson.getFoodItemTitle() == null) {
			throw new RecipeImportException(String.format(
					"Specify foodItemTitle for recipe %s ",
					recipeJson.getTitle()));
		}

		FoodItem foodItem = foodItemRepo.findByTitle(recipeJson
				.getFoodItemTitle());

		if (foodItem == null) {
			throw new RecipeImportException(String.format(
					"FoodItem not found with title %s (used for recipe %s) ",
					recipeJson.getFoodItemTitle(), recipeJson.getTitle()));
		}

		Recipe recipe = new Recipe();
		recipe.setTitle(recipeJson.getTitle());
		recipe.setImage(recipeJson.getImage());
		recipe.setSourceUrl(recipeJson.getSourceUrl());
		recipe.setVideoUrl(recipeJson.getVideoUrl());

		recipe.setFoodItem(foodItem);
		recipeRepository.save(recipe);
	}

}
