package com.toprecipe.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

import com.toprecipe.config.AbstractContainerTest;
import com.toprecipe.models.Category;
import com.toprecipe.models.FoodItem;
import com.toprecipe.models.Recipe;
import com.toprecipe.services.CategoryService;

public class RecipeRepositoryTest extends AbstractContainerTest {

	@Autowired
	RecipeRepository recipeRepo;
	@Autowired
	CategoryRepository categoryRepo;
	@Autowired
	CategoryService categoryService;
	@Autowired
	FoodItemRepository foodItemRepo;

	@Autowired
	private PlatformTransactionManager transactionManager;
	private TransactionTemplate transactionTemplate;

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
	public void testBasicRepositoryFunctions() {
		Recipe r = new Recipe();
		r.setTitle("JPA repository recipe");

		r = recipeRepo.save(r);
		assertNotNull(r.getId());

		r = recipeRepo.findOne(r.getId());

		assertEquals("JPA repository recipe", r.getTitle());
	}

	@Test
	public void testFetchRecipesGivenFoodItemIdWhenNoneMatch() {
		recipeRepo.getRecipeByFoodItemId(2l);
	}

	@Test
	public void testFetchRecipeGivenFoodItemIdWhenFewMatch() {
		FoodItem item = transactionTemplate
				.execute(new TransactionCallback<FoodItem>() {

					@Override
					public FoodItem doInTransaction(TransactionStatus status) {
						Category rice = categoryService.createCategory("Rice");
						Category biryani = categoryService.createCategory(
								"Biryani", rice);
						categoryService.createCategory("Hyderabadi", biryani);

						FoodItem item = new FoodItem();

						item.setTitle("Hyderabadi Chicken Dum Biryani");
						item.addCategory(biryani);
						foodItemRepo.save(item);

						Recipe r1 = new Recipe();
						r1.setTitle("recipe 1");
						r1.setFoodItem(item);

						recipeRepo.save(r1);

						Recipe r2 = new Recipe();
						r2.setTitle("recipe 2");
						r2.setFoodItem(item);

						recipeRepo.save(r2);

						return item;
					}
				});

		List<Recipe> recipes = recipeRepo.getRecipeByFoodItemId(item.getId());

		assertEquals(2, recipes.size());
		assertEquals("recipe 1", recipes.get(0).getTitle());
		assertEquals("recipe 2", recipes.get(1).getTitle());
	}
}
