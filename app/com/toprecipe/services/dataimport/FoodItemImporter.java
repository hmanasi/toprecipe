package com.toprecipe.services.dataimport;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.toprecipe.models.Category;
import com.toprecipe.models.FoodItem;
import com.toprecipe.repository.CategoryRepository;
import com.toprecipe.repository.FoodItemRepository;

@Service
public class FoodItemImporter {

	@Autowired
	public FoodItemRepository foodItemRepo;
	@Autowired
	public CategoryParser categoryParser;
	@Autowired
	public CategoryRepository categoryRepo;
	@Autowired
	private PlatformTransactionManager transactionManager;
	private TransactionTemplate transactionTemplate;

	@Transactional
	public void importFoodItems(final InputStream in)
			throws JsonProcessingException, IOException {

		transactionTemplate = new TransactionTemplate(transactionManager);

		transactionTemplate.execute(new TransactionCallbackWithoutResult() {

			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				try {
					importItems(in);
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		});
	}

	private void importItems(InputStream in) throws IOException,
			JsonProcessingException {
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
		c.addFoodItem(item);
		foodItemRepo.save(item);

		if (item.getCategories().isEmpty()) {
			throw new FoodItemImportException(String.format(
					"Specify one or more categories for food-Item %s",
					item.getTitle()));
		}
	}
}
