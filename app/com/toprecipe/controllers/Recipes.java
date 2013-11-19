package com.toprecipe.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import play.data.Form;
import play.libs.F.Function;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.toprecipe.beans.RecipeBean;
import com.toprecipe.models.Recipe;
import com.toprecipe.repository.RecipeRepository;
import com.toprecipe.services.RecipeService;
import com.toprecipe.services.fetcher.Media;
import com.toprecipe.services.fetcher.MediaFetcher;

@org.springframework.stereotype.Controller
public class Recipes extends Controller {

	@Autowired
	private MediaFetcher mediaFetcher;

	@Autowired
	RecipeRepository repo;
	@Autowired
	RecipeService recipeService;

	static Form<RecipeBean> recipeForm = Form.form(RecipeBean.class);

	public Result recipes() {
		List<List<Recipe>> array = new ArrayList<>();
		array.add(new ArrayList<Recipe>());
		array.add(new ArrayList<Recipe>());
		array.add(new ArrayList<Recipe>());
		array.add(new ArrayList<Recipe>());

		Iterator<Recipe> recipes = repo.findAll().iterator();

		int i = 0;

		while (recipes.hasNext()) {
			array.get(i % 4).add(recipes.next());
			i++;
		}

		return ok(views.html.recipes.index.render(array));
	}

	public Result admin() {
		return ok(views.html.recipes.admin.render(repo.findAll()));
	}

	public Result newRecipe() {
		return ok(views.html.recipes.create.render(recipeForm));
	}

	public Result newRecipe(String categoryTitle) {
		RecipeBean bean = new RecipeBean();
		bean.setCategoryTitle(categoryTitle);
		Form<RecipeBean> form = recipeForm.fill(bean);
		return ok(views.html.recipes.create.render(form));
	}

	public Result createRecipe() {
		Form<RecipeBean> filledForm = recipeForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.recipes.create.render(filledForm));
		} else {

			try {
				saveRecipe(filledForm.get());
			} catch (IOException e) {
				// TODO Find a better way to handle this exception
				e.printStackTrace();
				return internalServerError("Internal error processing request.");
			}
			return redirect(com.toprecipe.controllers.routes.CategoryBrowser
					.foodItems(filledForm.get().getCategoryTitle()));
		}
	}

	private void saveRecipe(RecipeBean recipeForm) throws IOException {
		Recipe recipe = new Recipe();
		recipe.setImage(recipeForm.getImage());
		recipe.setTitle(recipeForm.getTitle());
		recipe.setSourceUrl(recipeForm.getSourceUrl());
		recipe.setYouTubeVideo(recipeForm.getYouTubeVideo());
		recipe.setFlashVideo(recipeForm.getFlashVideo());
		recipeService.addRecipe(recipe, recipeForm.getCategoryTitle());
	}

	public Result deleteRecipe(Long id) {
		Recipe r = repo.findOne(id);
		if (r != null) {
			if (r.getImage() != null) {
				File f = new File("public/" + r.getImage());
				f.delete();
			}
			repo.delete(id);
		}
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
			RecipeBean recipeForm = filledForm.get();
			String srcUrlLowercase = recipeForm.getSourceUrl().toLowerCase();
			if (srcUrlLowercase.startsWith("http://www.youtube.com/watch?v=")) {
				recipeForm.setYouTubeVideo("http://www.youtube.com/embed/"
						+ recipeForm.getSourceUrl().substring(31));

				try {
					saveRecipe(recipeForm);
				} catch (IOException e) {
					// TODO Find a better way to handle this exception
					e.printStackTrace();
					return Promise.promise(new Function0<Result>() {
						@Override
						public Result apply() throws Throwable {
							return internalServerError("Internal error processing request.");
						}
					});
				}
				return Promise.promise(new Function0<Result>() {
					@Override
					public Result apply() throws Throwable {

						return redirect(com.toprecipe.controllers.routes.Recipes
								.recipes());
					}
				});
			}

			return mediaFetcher.fetch(recipeForm.getSourceUrl()).map(
					new Function<Media, Result>() {
						public Result apply(Media media) {
							return ok(views.html.recipes.selectMedia.render(
									filledForm, media));
						}
					});
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result rateRecipe(Long id, Float rating) {
		ObjectNode result = Json.newObject();
		recipeService.rateRecipe(id, rating);
		result.put("status", "OK");
		return ok(result);
	}
}
