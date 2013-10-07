package com.toprecipe.services.dataimport;

public class FoodItemJson {
	private String title;

	private String categoryTree;

	private boolean isVegetarian;

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getCategoryTree() {
		return categoryTree;
	}
	
	public void setCategoryTree(String categoryTree) {
		this.categoryTree = categoryTree;
	}
	
	public boolean isVegetarian() {
		return isVegetarian;
	}

	public void setVegetarian(boolean isVegetarian) {
		this.isVegetarian = isVegetarian;
	}

}
