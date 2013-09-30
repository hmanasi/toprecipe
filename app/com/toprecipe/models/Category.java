package com.toprecipe.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import play.data.validation.Constraints.Required;

@Entity
@SequenceGenerator(name = "Category_Seq_Gen", sequenceName = "Category_Seq", initialValue = 1)
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Category_Seq_Gen")
	private Long id;

	@Required
	private String title;

	/**
	 * A category can have many sub-categories but it belongs to only one
	 * category
	 */
	// Veg -> Rice -> Biryani (1)
	// Veg -> Curry ->
	// Nonveg -> Rice -> Biryani (2) -> Chicken Biryani (FoodItem)
	// Rice -> Biryani(3) -> Chicken Biryani (FoodItem)
	@OneToMany(mappedBy="parent")
	private List<Category> subCategories = new ArrayList<>();
	
	@ManyToMany
	@JoinTable(name="CATEGORY_FOODITEM")
	private List<FoodItem> foodItems = new ArrayList<>();
	
	@ManyToOne
	private Category parent;

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

	public List<Category> getSubCategories() {
		return subCategories;
	}

	public void addSubCategories(Category subCategory) {
		this.subCategories.add(subCategory);
	}

	public Category getParent() {
		return parent;
	}

	public void setParentCategory(Category parent) {
		this.parent = parent;
	}
}
