package com.toprecipe.services.dataimport;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avaje.ebean.annotation.Transactional;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.toprecipe.beans.RecipeBean;
import com.toprecipe.models.FoodItem;
import com.toprecipe.models.Recipe;
import com.toprecipe.repository.RecipeRepository;

@Service
public class RecipeExporter {

	@Autowired
	private RecipeRepository repository;

	@Transactional(readOnly = true)
	public void export(OutputStream out) throws JsonGenerationException,
			JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		ObjectWriter w = mapper.writer().with(
				SerializationFeature.INDENT_OUTPUT);

		List<RecipeBean> recipes = getRecipes(repository.findAll());

		w.writeValue(out, recipes);
	}

	private List<RecipeBean> getRecipes(Iterable<Recipe> recipes) {
		List<RecipeBean> recipeBeans = new ArrayList<>();

		for (Recipe recipe : recipes) {
			RecipeBean bean = new RecipeBean();
			bean.setFlashVideo(recipe.getFlashVideo());
			FoodItem foodItem = recipe.getFoodItem();
			if (foodItem != null) {
				bean.setFoodItemTitle(foodItem.getTitle());
			}
			bean.setImage(recipe.getImage());
			bean.setSourceUrl(recipe.getSourceUrl());
			bean.setTitle(recipe.getTitle());
			bean.setYouTubeVideo(recipe.getYouTubeVideo());
			recipeBeans.add(bean);
		}

		return recipeBeans;
	}
}
