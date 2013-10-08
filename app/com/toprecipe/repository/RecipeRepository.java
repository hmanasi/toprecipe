package com.toprecipe.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.toprecipe.models.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
	public List<Recipe> findByTitle (String title);
}
