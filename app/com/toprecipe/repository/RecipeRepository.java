package com.toprecipe.repository;

import org.springframework.data.repository.CrudRepository;

import com.toprecipe.models.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {

}
