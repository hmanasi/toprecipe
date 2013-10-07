package com.toprecipe.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import play.mvc.Controller;
import play.mvc.Result;

import com.toprecipe.services.CategoryImporter;

@org.springframework.stereotype.Controller
public class Admin extends Controller {

	@Autowired
	CategoryImporter categoryImporter;

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
}
