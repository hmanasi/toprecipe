package com.toprecipe.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import com.toprecipe.models.Recipe;
import com.toprecipe.repository.RecipeRepository;

@org.springframework.stereotype.Controller
public class CategoryBrowser extends Controller  {
	
	@Autowired
	RecipeRepository repo;
	
	public Result foodItems(String categoryTitle) {
		return TODO;//ok(views.html.recipes.index.render(repo.));
	}
	
	

}
