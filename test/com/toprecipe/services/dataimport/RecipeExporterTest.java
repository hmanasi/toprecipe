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
import com.toprecipe.models.Recipe;
import com.toprecipe.repository.CategoryRepository;
import com.toprecipe.repository.FoodItemRepository;
import com.toprecipe.repository.RecipeRepository;
import com.toprecipe.services.CategoryService;

public class RecipeExporterTest extends AbstractContainerTest {

	@Autowired
	CategoryRepository categoryRepo;
	@Autowired
	CategoryService categoryService;
	@Autowired
	FoodItemRepository foodItemRepo;
	@Autowired
	RecipeRepository recipeRepo;

	@Autowired
	private PlatformTransactionManager transactionManager;
	private TransactionTemplate transactionTemplate;

	@Autowired
	RecipeRepository repository;
	@Autowired
	RecipeExporter inTest;

	@Before
	public void setup() {
		transactionTemplate = new TransactionTemplate(transactionManager);
	}

	@After
	public void cleanup() {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			public void doInTransactionWithoutResult(TransactionStatus status) {
				recipeRepo.deleteAll();
				foodItemRepo.deleteAll();
				categoryRepo.deleteAll();
			}
		});
	}

	@Test
	public void testExport() throws JsonGenerationException,
			JsonMappingException, IOException {

		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				Category rice = categoryService.createCategory("Rice");
				Category biryani = categoryService.createCategory("Biryani",
						rice);
				categoryService.createCategory("Hyderabadi", biryani);

				FoodItem item = new FoodItem();

				item.setTitle("Hyderabadi Chicken Dum Biryani");
				item.addCategory(biryani);
				foodItemRepo.save(item);

				Recipe r = new Recipe();
				r.setFlashVideo("flashVideo1");
				r.setFoodItem(item);
				r.setImage("image1");
				r.setSourceUrl("sourceUrl1");
				r.setTitle("title1");
				r.setYouTubeVideo("youTubeVideo1");
				repository.save(r);

				r = new Recipe();
				r.setFlashVideo("flashVideo2");
				r.setImage("image2");
				r.setSourceUrl("sourceUrl2");
				r.setTitle("title2");
				r.setYouTubeVideo("youTubeVideo2");
				repository.save(r);
			}
		});

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		inTest.export(out);
		out.close();
		String exported = out.toString();
		System.out.println(exported);

		assertTrue(exported.contains("flashVideo1"));
		assertTrue(exported.contains("image1"));
		assertTrue(exported.contains("sourceUrl1"));
		assertTrue(exported.contains("title1"));
		assertTrue(exported.contains("youTubeVideo1"));
		assertTrue(exported.contains("Hyderabadi Chicken Dum Biryani"));

		assertTrue(exported.contains("flashVideo2"));
		assertTrue(exported.contains("image2"));
		assertTrue(exported.contains("sourceUrl2"));
		assertTrue(exported.contains("title2"));
		assertTrue(exported.contains("youTubeVideo2"));
		assertTrue(exported.contains("Hyderabadi Chicken Dum Biryani"));
	}

}
