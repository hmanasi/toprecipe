package com.toprecipe.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import com.toprecipe.models.Recipe;
import com.toprecipe.repository.RecipeRepository;

@org.springframework.stereotype.Controller
public class Application extends Controller {

	@Autowired
	RecipeRepository repo;
	static Form<Recipe> recipeForm = Form.form(Recipe.class);

	public Result index() {
		return redirect(com.toprecipe.controllers.routes.Recipes.recipes());
	}

	/*
	 * public Result deleteRecipe(Long id) { repo.delete(id); return
	 * redirect(com.toprecipe.controllers.routes.Application.recipes()); }
	 */

}
