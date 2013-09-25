package com.toprecipe.controllers;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import com.toprecipe.models.Recipe;
import com.toprecipe.repository.RecipeRepository;
import com.toprecipe.services.HelloWorldService;

import play.*;
import play.data.Form;
import play.mvc.*;
import views.html.*;

@org.springframework.stereotype.Controller
public class Application extends Controller {

	public Application() {
		System.out.println("controller initialization");
	}

	@Autowired
	HelloWorldService helloService;

	@Autowired
	RecipeRepository repo;
	static Form<Recipe> recipeForm = Form.form(Recipe.class);

	/*
	 * public Result index() { //repo.findAll(); return
	 * ok(helloService.hellowWorld()); }
	 */

	public Result index() {
		return redirect(routes.Application.recipes());
	}

	public Result recipes() {
		ArrayList<Recipe> recipes = new ArrayList<>();
		for (Recipe r : repo.findAll()){
			recipes.add(r);
		}
		
		return ok(views.html.index.render(repo.findAll(),
				recipeForm));
	}

	public Result newRecipe() {
		  Form<Recipe> filledForm = recipeForm.bindFromRequest();
		  if(filledForm.hasErrors()) {
		    return badRequest(
		      views.html.index.render(repo.findAll(), filledForm)
		    );
		  } else {
		    repo.save(filledForm.get());
		    return redirect(routes.Application.recipes());  
		  }	}

	public Result deleteRecipe(Long id) {
		return TODO;
	}
	
}
