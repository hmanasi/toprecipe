package com.toprecipe.services.dataimport;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.toprecipe.config.AbstractContainerTest;
import com.toprecipe.models.Category;
import com.toprecipe.repository.CategoryRepository;
import com.toprecipe.repository.FoodItemRepository;
import com.toprecipe.services.CategoryService;

public class FoodItemImporterTest extends AbstractContainerTest {

	@Autowired
	CategoryRepository categoryRepo;
	@Autowired
	CategoryService categoryService;

	@Autowired
	FoodItemRepository foodItemRepo;
	@Autowired
	FoodItemImporter inTest;

	@Autowired
	private PlatformTransactionManager transactionManager;
	private TransactionTemplate transactionTemplate;

	private String testString = "[ {"
			+ "  \"title\" : \"Hyderabadi Chichen Dum Biryani\","
			+ "  \"categoryTree\" : \"Rice->Biryani->Hyderabadi\","
			+ "  \"vegetarian\" : false" + "}, {"
			+ "  \"title\" : \"Hyderabadi Vegetarian Dum Biryani\","
			+ "  \"categoryTree\" : \"Rice->Biryani->Hyderabadi\","
			+ "  \"vegetarian\" : true" + "} ]";

	@Before
	public void setup() {
		transactionTemplate = new TransactionTemplate(transactionManager);
	}

	@After
	public void cleanup() {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			public void doInTransactionWithoutResult(TransactionStatus status) {
				foodItemRepo.deleteAll();
				categoryRepo.deleteAll();
			}
		});
	}

	@Test
	public void testImportPositive() throws JsonProcessingException,
			UnsupportedEncodingException, IOException {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				Category rice = categoryService.createCategory("Rice");
				Category biryani = categoryService.createCategory("Biryani",
						rice);
				categoryService.createCategory("Hyderabadi", biryani);
			}
		});

		inTest.importFoodItems(new ByteArrayInputStream(testString.getBytes()));

		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				assertNotNull(foodItemRepo
						.findByTitle("Hyderabadi Chichen Dum Biryani"));
				assertNotNull(foodItemRepo
						.findByTitle("Hyderabadi Vegetarian Dum Biryani"));
			}
		});
	}

	@Test
	public void export() throws JsonProcessingException {
		FoodItemJson foodItem = new FoodItemJson();
		foodItem.setTitle("Hyderabadi Chichen Dum Biryani");
		foodItem.setCategoryTree("Rice->Biryani->Hyderabadi");
		foodItem.setVegetarian(false);

		FoodItemJson foodItem2 = new FoodItemJson();
		foodItem2.setTitle("Hyderabadi Vegetarian Dum Biryani");
		foodItem2.setCategoryTree("Rice->Biryani->Hyderabadi");
		foodItem2.setVegetarian(true);

		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer().with(
				SerializationFeature.INDENT_OUTPUT);
		System.out.println(writer.writeValueAsString(Arrays.asList(foodItem,
				foodItem2)));
	}

}
