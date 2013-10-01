package com.toprecipe.repository;

import org.springframework.data.repository.CrudRepository;

import com.toprecipe.models.FoodItem;

public interface FoodItemRepository extends CrudRepository<FoodItem, Long> {

	public FoodItem findByTitle(String title);

}
