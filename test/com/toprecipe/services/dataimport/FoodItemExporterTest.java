package com.toprecipe.services.dataimport;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.toprecipe.config.AbstractContainerTest;
import com.toprecipe.models.Category;
import com.toprecipe.models.FoodItem;
import com.toprecipe.repository.CategoryRepository;
import com.toprecipe.repository.FoodItemRepository;
import com.toprecipe.services.CategoryService;

public class FoodItemExporterTest extends AbstractContainerTest {

	@Autowired
	CategoryRepository categoryRepo;
	@Autowired
	FoodItemRepository foodItemRepo;
	@Autowired
	CategoryService categoryService;

	@Autowired
	private PlatformTransactionManager transactionManager;
	private TransactionTemplate transactionTemplate;

	@Autowired
	FoodItemExporter inTest;

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
	public void basicPositiveTestCase() throws JsonGenerationException,
			JsonMappingException, IOException {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				Category rice = categoryService.createCategory("Rice");
				Category pulav = categoryService.createCategory("Pulav", rice);
				categoryService.createCategory("Pulav", pulav);

				FoodItem item = new FoodItem();

				item.setTitle("Peas Pulav");
				item.addCategory(pulav);
				item.setVegetarian(true);
				foodItemRepo.save(item);
			}
		});

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		inTest.export(out);
		out.close();
		String exported = out.toString();
		System.out.println(exported);
		assertTrue(exported.contains("\"title\" : \"Peas Pulav\""));
		assertTrue(exported.contains("\"categoryTree\" : \"Rice->Pulav\""));
		assertTrue(exported.contains("\"vegetarian\" : true"));

	}
}
