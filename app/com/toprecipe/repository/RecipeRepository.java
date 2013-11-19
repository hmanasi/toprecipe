package com.toprecipe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.toprecipe.models.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
	public List<Recipe> findByTitle(String title);

	@Query("SELECT r from Recipe r WHERE r.foodItem.id=:foodItemId ORDER by r.averageRating DESC")
	public List<Recipe> getSortedRecipeByFoodItemId(
			@Param("foodItemId") Long foodItemId);
}
