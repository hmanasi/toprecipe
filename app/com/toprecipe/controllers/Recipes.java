package com.toprecipe.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import play.data.Form;
import play.libs.F.Function;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;
import play.mvc.Controller;
import play.mvc.Result;

import com.toprecipe.models.Recipe;
import com.toprecipe.repository.RecipeRepository;
import com.toprecipe.services.fetcher.Media;
import com.toprecipe.services.fetcher.RecipeHtmlParser;

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
			recipe.setId(id);
			repo.save(recipe);
			return redirect(com.toprecipe.controllers.routes.Recipes.recipes());
		}

	}

	public Result newSourceUrl() {
		return ok(views.html.recipes.createWithUrl.render(recipeForm));
	}

	@Autowired
	private RecipeHtmlParser parser;

	public Promise<Result> displaySourceUrl() {
		final Form<Recipe> filledForm = recipeForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return Promise.promise(new Function0<Result>() {
				@Override
				public Result apply() throws Throwable {
					return badRequest(views.html.recipes.createWithUrl
							.render(filledForm));
				}
			});
		} else {
			return WS.url(filledForm.field("sourceUrl").value()).get()
					.map(new Function<Response, Result>() {
						public Result apply(Response response) {
							try {
								Media media = parser.parseHtml(response
										.getBodyAsStream(),
										filledForm.field("sourceUrl").value());
								return ok(views.html.recipes.displayWithUrl
										.render(recipeForm, media));
							} catch (IOException e) {
								filledForm.reject(e.getMessage());
								return badRequest(views.html.recipes.createWithUrl
										.render(filledForm));
							}
						}
					});
		}

	}

}
