package com.toprecipe.services;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
import com.toprecipe.repository.CategoryRepository;
import com.toprecipe.repository.FoodItemRepository;
import com.toprecipe.repository.RecipeRepository;
import com.toprecipe.services.dataimport.CategoryNotFoundException;
import com.toprecipe.services.fetcher.ImageFileHelper;

public class RecipeServiceTest extends AbstractContainerTest {

	@Autowired
	RecipeService inTest;
	@Autowired
	CategoryService categoryService;
	@Autowired
	FoodItemRepository foodItemRepo;
	@Autowired
	RecipeRepository recipeRepo;
	@Autowired
	CategoryRepository categoryRepo;
	@Autowired
	ImageFileHelper imageFileHelper;

	@Autowired
	private PlatformTransactionManager transactionManager;
	private TransactionTemplate transactionTemplate;

	@Before
	public void setup() throws IOException {
		transactionTemplate = new TransactionTemplate(transactionManager);
		File outFolder = new File("/tmp/test.RecipeServiceTest");
		outFolder.deleteOnExit();
		outFolder.mkdirs();

		imageFileHelper.setImageFolder(outFolder.getCanonicalPath());
		imageFileHelper.setTempImageFolder("/tmp");
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
	public void whenNoCategorySuppliedRecipeWithNoFoodItemCreated() {
		Recipe created = transactionTemplate
				.execute(new TransactionCallback<Recipe>() {

					@Override
					public Recipe doInTransaction(TransactionStatus status) {
						Recipe r = new Recipe();
						r.setTitle("testRecipeService");
						try {
							return inTest.addRecipe(r, null);
						} catch (IOException e) {
							return null;
						}
					}
				});
		assertNotNull(created);
		assertNotNull(recipeRepo.findOne(created.getId()));
	}

	@Test(expected = CategoryNotFoundException.class)
	public void whenInvalidCategorySuppliedExceptionCauses() throws IOException {
		inTest.addRecipe(new Recipe(), "nonexistent");
	}

	@Test
	public void whenFoodItemAlreadyExistsThenItIsReused() throws IOException {
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

						return item;
					}
				});
		Recipe toCreate = new Recipe();
		toCreate.setTitle("Hyderabadi Chicken Dum Biryani");
		Recipe recipe = inTest.addRecipe(toCreate, "Rice->Biryani");

		assertEquals(item.getId(), recipe.getFoodItem().getId());
	}

	// TODO: use mockito to mock the ImageFileHelper
	@Test
	public void ensureImageIsMoved() throws IOException {
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

						return item;
					}
				});

		File src = File.createTempFile("tst", "imgFileHelper");

		FileWriter out = new FileWriter(src);
		out.write("A test string...");
		out.close();

		Recipe toCreate = new Recipe();

		toCreate.setImage(src.getName());

		toCreate.setTitle("Hyderabadi Chicken Dum Biryani");
		Recipe recipe = inTest.addRecipe(toCreate, "Rice->Biryani");

		System.out.println("Image file path :" + recipe.getImage());

		assertEquals(item.getId(), recipe.getFoodItem().getId());
		assertEquals("images/recipes/" + src.getName(), recipe.getImage());
	}

	@Test
	public void whenFoodItemDoesNotExistThenItIsCreated() throws IOException {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {

			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				Category rice = categoryService.createCategory("Rice");
				Category biryani = categoryService.createCategory("Biryani",
						rice);
				categoryService.createCategory("Hyderabadi", biryani);
			}
		});

		Recipe r = new Recipe();
		r.setTitle("Veggie Biryani");

		Recipe created = inTest.addRecipe(r, "Rice->Biryani");
		assertNotNull(created.getFoodItem());
		assertEquals("Veggie Biryani", created.getFoodItem().getTitle());
		assertEquals("Biryani", created.getFoodItem().getCategories().get(0)
				.getTitle());
	}

	@Test
	public void rateRecipe() {
		final Recipe created = transactionTemplate
				.execute(new TransactionCallback<Recipe>() {

					@Override
					public Recipe doInTransaction(TransactionStatus status) {
						Recipe r = new Recipe();
						r.setTitle("testRecipeService");
						try {
							return inTest.addRecipe(r, null);
						} catch (IOException e) {
							return null;
						}
					}
				});

		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				inTest.rateRecipe(created.getId(), 2.5f);
			}
		});

		Recipe r = recipeRepo.findOne(created.getId());

		assertEquals(2.5, r.getAverageRating(), 0.00001);
	}
}
