package com.toprecipe.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import play.mvc.Controller;
import play.mvc.Result;

import com.toprecipe.models.Recipe;
import com.toprecipe.repository.RecipeRepository;
import com.toprecipe.services.TopRecipe;
import com.toprecipe.services.TopRecipeService;

@org.springframework.stereotype.Controller
public class CategoryBrowser extends Controller {

	@Autowired
	private TopRecipeService topRecipeService;
	@Autowired
	private RecipeRepository recipeRepo;

	public Result foodItems(String categoryTitle) {
		List<TopRecipe> recipes = topRecipeService.getTopRecipes(categoryTitle);

		List<List<TopRecipe>> recipesArray = new ArrayList<>();
		recipesArray.add(new ArrayList<TopRecipe>());
		recipesArray.add(new ArrayList<TopRecipe>());

		int i = 0;
		for (TopRecipe recipe : recipes) {
			recipesArray.get(i % 2).add(recipe);
			i++;
		}

		return ok(views.html.foodItems.index
				.render(recipesArray, categoryTitle));
	}

	public Result recipes(Long foodItemId) {
		List<Recipe> recipes = recipeRepo.getSortedRecipeByFoodItemId(foodItemId);

		List<List<Recipe>> recipesArray = new ArrayList<>();
		recipesArray.add(new ArrayList<Recipe>());

		int i = 0;
		int len = recipesArray.size();
		for (Recipe recipe : recipes) {
			recipesArray.get(i % len).add(recipe);
			i++;
		}
		return ok(views.html.foodItems.recipes.render(recipesArray));
	}
}
