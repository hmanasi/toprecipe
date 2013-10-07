package com.toprecipe.services.dataimport;

import com.toprecipe.models.Category;

@SuppressWarnings("serial")
public class CategoryNotFoundException extends RuntimeException {
	public CategoryNotFoundException(String category) {
		super(String.format("Category %s not found", category));
	}

	public CategoryNotFoundException(String title, Category parent) {
		super(String.format("Category %s not found for parent %s", title,
				parent == null ? "<none>":parent.getTitle()));
	}
}
