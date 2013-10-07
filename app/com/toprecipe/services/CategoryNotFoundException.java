package com.toprecipe.services;

@SuppressWarnings("serial")
public class CategoryNotFoundException extends RuntimeException {
	public CategoryNotFoundException(String category) {
		super(String.format("Category %s not found", category));
	}
}
