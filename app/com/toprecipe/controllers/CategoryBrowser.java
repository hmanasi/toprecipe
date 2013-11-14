package com.toprecipe.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import play.mvc.Controller;
import play.mvc.Result;

import com.toprecipe.repository.RecipeRepository;
import com.toprecipe.services.TopRecipeService;

@org.springframework.stereotype.Controller
public class CategoryBrowser extends Controller {

	@Autowired
	private TopRecipeService topRecipeService;
	@Autowired
	private RecipeRepository recipeRepo;

	public Result foodItems(String categoryTitle) {
		return ok(views.html.foodItems.index.render(
				topRecipeService.getTopRecipes(categoryTitle), categoryTitle));
	}

	public Result recipes (Long foodItemId)
	{
		return ok(views.html.foodItems.recipes.render(recipeRepo.getRecipeByFoodItemId(foodItemId)));
	}
}
