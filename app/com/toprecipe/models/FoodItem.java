package com.toprecipe.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@SequenceGenerator(name = "food_item_seq_gen", sequenceName = "food_item_seq", initialValue = 1)
@Entity
@Table(name = "food_item", uniqueConstraints = { @UniqueConstraint(name = "food_item_u1", columnNames = { "title" }) })
public class FoodItem {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "food_item_seq_gen")
	private Long id;

	@Column(nullable = false)
	private String title;

	@ManyToMany(mappedBy = "foodItems", cascade = { CascadeType.PERSIST,
			CascadeType.REFRESH, CascadeType.MERGE })
	private List<Category> categories = new ArrayList<>();

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "top_recipe_id")
	private Recipe topRecipe;

	@OneToMany(mappedBy = "foodItem")
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
		if (!getCategories().contains(categories)) {
			getCategories().add(category);
		}
		if (!category.getFoodItems().contains(this)) {
			category.getFoodItems().add(this);
		}
	}

	public Recipe getTopRecipe() {
		return topRecipe;
	}

	public void setTopRecipe(Recipe topRecipe) {
		this.topRecipe = topRecipe;
	}
}
