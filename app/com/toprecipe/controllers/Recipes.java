package com.toprecipe.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import play.data.Form;
import play.libs.F.Function;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;

import com.toprecipe.beans.RecipeBean;
import com.toprecipe.models.Recipe;
import com.toprecipe.repository.RecipeRepository;
import com.toprecipe.services.fetcher.Media;
import com.toprecipe.services.fetcher.MediaFetcher;

@org.springframework.stereotype.Controller
public class Recipes extends Controller {

	@Autowired
	private MediaFetcher mediaFetcher;

	@Autowired
	RecipeRepository repo;

	static Form<RecipeBean> recipeForm = Form.form(RecipeBean.class);

	public Result recipes() {
		return ok(views.html.recipes.index.render(repo.findAll()));
	}

	public Result newRecipe() {
		return ok(views.html.recipes.create.render(recipeForm));
	}

	public Result createRecipe() {
		Form<RecipeBean> filledForm = recipeForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.recipes.create.render(filledForm));
		} else {
			Recipe recipe = new Recipe ();
			
			RecipeBean recipeForm = filledForm.get();
			recipe.setImage(recipeForm.getImage());
			recipe.setTitle(recipeForm.getTitle());
			recipe.setSourceUrl(recipeForm.getSourceUrl());
			recipe.setYouTubeVideo(recipeForm.getYouTubeVideo());
			repo.save(recipe);
			return redirect(com.toprecipe.controllers.routes.Recipes.recipes());
		}
	}

	public Result deleteRecipe(Long id) {
		repo.delete(id);
		return redirect(com.toprecipe.controllers.routes.Recipes.recipes());
	}

	/*
	 * TODO: proper exception handling TODO: Fix parsing of dimensions when they
	 * are in percentage or in points
	 */
	public Promise<Result> selectMedia() {
		final Form<RecipeBean> filledForm = recipeForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return Promise.promise(new Function0<Result>() {
				@Override
				public Result apply() throws Throwable {
					return badRequest(views.html.recipes.create
							.render(filledForm));
				}
			});
		} else {
			return mediaFetcher.fetch(filledForm.field("sourceUrl").value())
					.map(new Function<Media, Result>() {
						public Result apply(Media media) {
							return ok(views.html.recipes.selectMedia.render(
									filledForm, media));
						}
					});
		}
	}
}
