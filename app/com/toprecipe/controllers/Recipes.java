package com.toprecipe.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import com.toprecipe.models.Recipe;
import com.toprecipe.repository.RecipeRepository;

@org.springframework.stereotype.Controller
public class Recipes extends Controller {

	@Autowired
	RecipeRepository repo;

	static Form<Recipe> recipeForm = Form.form(Recipe.class);

	public Result recipes() {
		return ok(views.html.recipes.index.render(repo.findAll()));
	}

	public Result newRecipe() {
		return ok(views.html.recipes.create.render(recipeForm));
	}

	public Result createRecipe() {
		Form<Recipe> filledForm = recipeForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.recipes.create.render(filledForm));
		} else {
			repo.save(filledForm.get());
			return ok(views.html.recipes.create.render(recipeForm));
		}
	}

	public Result deleteRecipe(Long id) {
		repo.delete(id);
		return redirect(com.toprecipe.controllers.routes.Recipes.recipes());
	}

	public Result editRecipe(Long id) {
		Form<Recipe> filledForm = recipeForm.fill(repo.findOne(id));
		return ok(views.html.recipes.update.render(id, filledForm));
	}

	public Result updateRecipe(Long id) {
		Form<Recipe> filledForm = recipeForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.recipes.update.render(id, filledForm));
		} else {
			Recipe recipe = filledForm.get();
			recipe.setId (id);
			repo.save(recipe);
			return redirect(com.toprecipe.controllers.routes.Recipes.recipes());
		}

	}

}
