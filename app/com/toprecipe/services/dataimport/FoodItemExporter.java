package com.toprecipe.services.dataimport;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.toprecipe.models.Category;
import com.toprecipe.models.FoodItem;
import com.toprecipe.repository.CategoryRepository;
import com.toprecipe.repository.FoodItemRepository;

@Service
public class FoodItemExporter {
	@Autowired
	private FoodItemRepository foodItemRepo;
	@Autowired
	private CategoryRepository categoryRepo;

	@Transactional(readOnly=true)
	public void export(OutputStream out) throws JsonGenerationException,
			JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		ObjectWriter w = mapper.writer().with(
				SerializationFeature.INDENT_OUTPUT);

		List<FoodItemJson> foodItems = mapFoodItemToJson(foodItemRepo.findAll());

		w.writeValue(out, foodItems);
	}

	private List<FoodItemJson> mapFoodItemToJson(Iterable<FoodItem> foodItems) {
		List<FoodItemJson> toReturn = new ArrayList<>();
		for (FoodItem foodItem : foodItems) {
			FoodItemJson foodItemJson = new FoodItemJson();
			foodItemJson.setTitle(foodItem.getTitle());
			foodItemJson.setVegetarian(foodItem.isVegetarian ());
			foodItemJson.setCategoryTree(resolveCategory(foodItem
					.getCategories().get(0)));
			toReturn.add(foodItemJson);
		}
		return toReturn;
	}

	private String resolveCategory(Category category) {

		Stack<String> stack = new Stack<>();
		stack.push(category.getTitle());

		Category parent = category.getParent();

		while (parent != null) {
			stack.push(parent.getTitle());
			parent = parent.getParent();
		}

		StringBuilder categoryTitle = new StringBuilder();

		while (stack.size() > 0) {
			categoryTitle.append(stack.pop());
			if (stack.size() > 0) {
				categoryTitle.append("->");
			}
		}

		return categoryTitle.toString();
	}
}
