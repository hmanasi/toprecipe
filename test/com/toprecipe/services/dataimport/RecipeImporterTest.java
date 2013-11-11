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
import com.toprecipe.beans.RecipeBean;
import com.toprecipe.config.AbstractContainerTest;
import com.toprecipe.models.Category;
import com.toprecipe.models.FoodItem;
import com.toprecipe.repository.CategoryRepository;
import com.toprecipe.repository.FoodItemRepository;
import com.toprecipe.repository.RecipeRepository;
import com.toprecipe.services.CategoryService;

public class RecipeImporterTest extends AbstractContainerTest{
	
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
	private RecipeImporter inTest;
	
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
	public void testImportPositive() throws JsonProcessingException,
			UnsupportedEncodingException, IOException, RecipeImportException {
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
				
			}
		});

		inTest.importRecipes(new ByteArrayInputStream(testString.getBytes()));

		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				assertNotNull(recipeRepo.findByTitle("Hyderabadi Chicken Dum Biryani"));
			}
		});
	}

	private String testString = "[ {" +
			"  \"title\" : \"Hyderabadi Chicken Dum Biryani\"," +
			"  \"image\" : \"images/recipe/vah_chicken_biryani.jpg\"," +
			"  \"youTubeVideo\" : \"https://www.youtube-nocookie.com/embed/QjvQ7T01tLo\"," +
			"  \"flashVideo\" : null," +
			"  \"foodItemTitle\" : \"Hyderabadi Chicken Dum Biryani\"," +
			"  \"sourceUrl\" : \"http://www.vahrehvah.com/Hyderabadi+Chicken+Biryani:3144\"" +
			"}, {" +
			"  \"title\" : \"Hyderabadi Chicken Dum Biryani\"," +
			"  \"image\" : \"images/recipe/vah_chicken_biryani.jpg\"," +
			"  \"youTubeVideo\" : \"http://www.youtube.com/embed/rcCTwFpWxpE\"," +
			"  \"flashVideo\" : null," +
			"  \"foodItemTitle\" : \"Hyderabadi Chicken Dum Biryani\"," +
			"  \"sourceUrl\" : \"http://hellohyderabad.com/Recipes/Non-Vegetarian/Hyderabad-Dum-Biryani/\"" +
			"} ]";
	
	@Test
	public void export() throws JsonProcessingException {
		RecipeBean recipeJson1 = new RecipeBean();
		recipeJson1.setTitle("Hyderabadi Chicken Dum Biryani");
		recipeJson1.setImage("images/recipe/vah_chicken_biryani.jpg");
		recipeJson1
				.setSourceUrl("http://www.vahrehvah.com/Hyderabadi+Chicken+Biryani:3144");
		recipeJson1
				.setYouTubeVideo("https://www.youtube-nocookie.com/embed/QjvQ7T01tLo");
		recipeJson1.setFoodItemTitle("Hyderabadi Chicken Dum Biryani");

		RecipeBean recipeJson2 = new RecipeBean();
		recipeJson2.setTitle("Hyderabadi Chicken Dum Biryani");
		recipeJson2.setImage("images/recipe/vah_chicken_biryani.jpg");
		recipeJson2
				.setSourceUrl("http://hellohyderabad.com/Recipes/Non-Vegetarian/Hyderabad-Dum-Biryani/");
		recipeJson2.setYouTubeVideo("http://www.youtube.com/embed/rcCTwFpWxpE");
		recipeJson2.setFoodItemTitle("Hyderabadi Chicken Dum Biryani");

		ObjectMapper mapper = new ObjectMapper();

		ObjectWriter w = mapper.writer().with(
				SerializationFeature.INDENT_OUTPUT);

		System.out.println(w.writeValueAsString(Arrays.asList(recipeJson1,
				recipeJson2)));

	}
}
