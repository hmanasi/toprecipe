package com.toprecipe.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@SequenceGenerator(name = "FoodItem_Seq_Gen", sequenceName = "FoodItem_Seq", initialValue = 1)
@Entity
public class FoodItem {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FoodItem_Seq_Gen")
	private Long id;
	private String title;

	@ManyToMany(mappedBy = "foodItems")
	private List<Category> categories = new ArrayList<>();

	@OneToOne
	private Recipe topRecipe;

	@OneToMany(mappedBy="foodItem")
	private List<Recipe> recipes = new ArrayList<>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void addCategory(Category category) {
		this.categories.add(category);
	}

	public Recipe getTopRecipe() {
		return topRecipe;
	}

	public void setTopRecipe(Recipe topRecipe) {
		this.topRecipe = topRecipe;
	}
}
