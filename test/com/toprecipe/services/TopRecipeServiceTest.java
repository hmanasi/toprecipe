package com.toprecipe.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.toprecipe.models.Category;
import com.toprecipe.models.FoodItem;
import com.toprecipe.repository.CategoryRepository;
import com.toprecipe.repository.FoodItemRepository;
import com.toprecipe.repository.RecipeRepository;
import com.toprecipe.services.dataimport.RecipeImportException;
import com.toprecipe.services.dataimport.RecipeImporter;
import com.toprecipie.config.AbstractContainerTest;

public class TopRecipeServiceTest extends AbstractContainerTest {

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
	private RecipeImporter recipeImporter;
	@Autowired
	private TopRecipeService inTest;

	@Before
	public void setup() {
		transactionTemplate = new TransactionTemplate(transactionManager);
		cleanup();
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
	public void testGetRecipiesGivenCategoryId() {
		transactionTemplate.execute(new TransactionCallback<Category>() {

			@Override
			public Category doInTransaction(TransactionStatus status) {
				Category rice = categoryService.createCategory("Rice");
				Category biryani = categoryService.createCategory("Biryani",
						rice);
				Category category = categoryService.createCategory(
						"Hyderabadi", biryani);

				FoodItem item = new FoodItem();

				item.setTitle("Hyderabadi Chicken Dum Biryani");
				item.addCategory(category);
				foodItemRepo.save(item);

				try {
					recipeImporter.importRecipes(new ByteArrayInputStream(
							testString.getBytes()));
				} catch (IOException | RecipeImportException e) {
					fail("Exception creating sample recipe.");
				}

				return category;
			}
		});

		List<TopRecipe> recipes = inTest
				.getTopRecipes("Rice->Biryani->Hyderabadi");
		assertEquals(1, recipes.size());
		System.out.println(String.format("Recipes = %s", recipes));

	}

	private String testString = "[ {"
			+ "  \"title\" : \"Hyderabadi Chichen Dum Biryani\","
			+ "  \"image\" : \"images/recipe/vah_chicken_biryani.jpg\","
			+ "  \"videoUrl\" : \"https://www.youtube-nocookie.com/embed/QjvQ7T01tLo\","
			+ "  \"sourceUrl\" : \"http://www.vahrehvah.com/Hyderabadi+Chicken+Biryani:3144\","
			+ "  \"foodItemTitle\" : \"Hyderabadi Chicken Dum Biryani\""
			+ "}, {"
			+ "  \"title\" : \"Hyderabadi Chichen Dum Biryani\","
			+ "  \"image\" : \"images/recipe/vah_chicken_biryani.jpg\","
			+ "  \"videoUrl\" : \"http://www.youtube.com/embed/rcCTwFpWxpE\","
			+ "  \"sourceUrl\" : \"http://hellohyderabad.com/Recipes/Non-Vegetarian/Hyderabad-Dum-Biryani/\","
			+ "  \"foodItemTitle\" : \"Hyderabadi Chicken Dum Biryani\""
			+ "} ]";

}
