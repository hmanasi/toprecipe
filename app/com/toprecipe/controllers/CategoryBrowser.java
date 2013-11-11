package com.toprecipe.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import play.mvc.Controller;
import play.mvc.Result;

import com.toprecipe.services.TopRecipeService;

@org.springframework.stereotype.Controller
public class CategoryBrowser extends Controller {

	@Autowired
	private TopRecipeService topRecipeService;

	public Result foodItems(String categoryTitle) {
		return ok(views.html.foodItems.index.render(topRecipeService
				.getTopRecipes(categoryTitle), categoryTitle));
	}

}
