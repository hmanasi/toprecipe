package com.toprecipe.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;

import play.mvc.Controller;
import play.mvc.Result;

import com.toprecipe.services.dataimport.CategoryImporter;
import com.toprecipe.services.dataimport.FoodItemImporter;
import com.toprecipe.services.dataimport.RecipeExporter;
import com.toprecipe.services.dataimport.RecipeImportException;
import com.toprecipe.services.dataimport.RecipeImporter;

@org.springframework.stereotype.Controller
public class Admin extends Controller {

	@Autowired
	CategoryImporter categoryImporter;
	@Autowired
	FoodItemImporter foodItemImporter;
	@Autowired
	RecipeImporter recipeImporter;
	@Autowired
	RecipeExporter recipeExporter;

	public Result importCategories() {
		File file = new File("data/categories.txt");

		if (file.exists()) {
			try {
				categoryImporter.importCategories(new BufferedReader(
						new FileReader(file)));
			} catch (IOException e) {
				return internalServerError("error importing categories");
			}
			return ok("Categories imported.");
		} else {
			System.out.println("File not found" + file.getAbsolutePath());
			return internalServerError("Did not find categories.");
		}
	}

	public Result importFoodItems() {
		File file = new File("data/food_items.txt");

		if (file.exists()) {
			try {
				foodItemImporter.importFoodItems(new FileInputStream(file));
			} catch (IOException e) {
				return internalServerError("error importing food items");
			}
			return ok("Food items imported.");
		} else {
			System.out.println("File not found" + file.getAbsolutePath());
			return internalServerError("Did not find food items.");
		}
	}

	public Result importRecipes() {
		File file = new File("data/recipes.txt");

		if (file.exists()) {
			try {
				recipeImporter.importRecipes(new FileInputStream(file));
			} catch (IOException | RecipeImportException e) {
				e.printStackTrace();
				return internalServerError("error importing recipes");
			}
			return ok("Recipes imported.");
		} else {
			System.out.println("File not found" + file.getAbsolutePath());
			return internalServerError("Did not find recipes.");
		}
	}

	public Result exportRecipes() {
		File file = new File("data/recipes.txt");
		OutputStream out = null;

		if (file.exists()) {
			try {
				out = new FileOutputStream(file);
				recipeExporter.export(out);
			} catch (IOException e) {
				e.printStackTrace();
				return internalServerError("error exporting recipes");
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						// Ignore
					}
				}
			}
			return ok("Recipes exported.");
		} else {
			System.out.println("File not found" + file.getAbsolutePath());
			return internalServerError("Did not find recipes.");
		}
	}
}
