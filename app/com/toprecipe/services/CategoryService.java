package com.toprecipe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toprecipe.models.Category;
import com.toprecipe.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;
	
	@Transactional
	public Category createCategory (String title, Category parent)
	{
		Category category = new Category();
		category.setTitle(title);
		category.setParentCategory(parent);
		return repository.save(category);
	}
	
	@Transactional
	public Category createCategory(String title) {
		Category category = new Category();
		category.setTitle(title);
		return repository.save(category);
	}
}
