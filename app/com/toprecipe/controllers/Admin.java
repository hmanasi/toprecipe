package com.toprecipe.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import play.mvc.Controller;
import play.mvc.Result;

import com.toprecipe.services.CategoryImporter;
import com.toprecipe.services.dataimport.FoodItemImporter;

@org.springframework.stereotype.Controller
public class Admin extends Controller {

	@Autowired
	CategoryImporter categoryImporter;
	@Autowired
	FoodItemImporter foodItemImporter;
	
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
}
