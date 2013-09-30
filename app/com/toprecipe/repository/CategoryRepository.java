package com.toprecipe.repository;

import org.springframework.data.repository.CrudRepository;

import com.toprecipe.models.Category;

public interface CategoryRepository extends CrudRepository<Category, Long>{
	public Category findByTitle (String title);
}
