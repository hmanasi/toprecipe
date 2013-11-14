package com.toprecipe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.toprecipe.models.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
	public List<Recipe> findByTitle(String title);

	@Query("SELECT r from Recipe r WHERE r.foodItem.id=:foodItemId")
	public List<Recipe> getRecipeByFoodItemId(
			@Param("foodItemId") Long foodItemId);
}
