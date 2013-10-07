package com.toprecipe.services.dataimport;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.toprecipe.models.Category;
import com.toprecipe.models.FoodItem;
import com.toprecipe.repository.FoodItemRepository;

@Service
public class FoodItemImporter {

	@Autowired
	public FoodItemRepository repository;
	@Autowired
	public CategoryParser categoryParser;

	@Transactional
	public void importFoodItems(InputStream in) throws JsonProcessingException,
			IOException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectReader reader = mapper.reader(FoodItemJson.class);

		MappingIterator<FoodItemJson> i = reader.readValues(in);

		while (i.hasNext()) {
			FoodItemJson foodItemJson = i.nextValue();
			createFoodItem(foodItemJson);
		}
	}

	private void createFoodItem(FoodItemJson foodItemJson) {
		FoodItem item = new FoodItem();
		item.setTitle(foodItemJson.getTitle());

		Category c = categoryParser.parseForExistingCategory(foodItemJson
				.getCategoryTree());
		item.addCategory(c);

		if (item.getCategories().isEmpty()) {
			throw new FoodItemImportException(String.format(
					"Specify one or more categories for food-Item %s",
					item.getTitle()));
		}
		repository.save(item);
	}
}
