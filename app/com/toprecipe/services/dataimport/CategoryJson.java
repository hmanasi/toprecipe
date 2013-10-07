package com.toprecipe.services.dataimport;

public class CategoryJson {
	
	/** category name in format Ancestor->Parent->Category */
	private String categoryTree;

	public String getCategoryTree() {
		return categoryTree;
	}

	public void setCategoryTree(String categoryTree) {
		this.categoryTree = categoryTree;
	}
}
