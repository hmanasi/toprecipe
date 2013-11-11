package com.toprecipe.services;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.toprecipe.services.dataimport.CategoryImporterTest;
import com.toprecipe.services.dataimport.CategoryParserTest;
import com.toprecipe.services.dataimport.FoodItemImporterTest;
import com.toprecipe.services.dataimport.RecipeExporterTest;
import com.toprecipe.services.dataimport.RecipeImporterTest;
import com.toprecipe.services.fetcher.ImageFetcherTest;
import com.toprecipe.services.fetcher.MediaFetcherTest;
import com.toprecipe.services.fetcher.RecipeHtmlParserTest;

@RunWith(Suite.class)
@SuiteClasses({ CategoryImporterTest.class, CategoryParserTest.class,
		FoodItemImporterTest.class, RecipeImporterTest.class,
		CategoryServiceTest.class, TopRecipeServiceTest.class,
		PasswordHashServiceTest.class, UserServiceTest.class,
		RecipeHtmlParserTest.class, ImageFetcherTest.class,
		MediaFetcherTest.class, RecipeExporterTest.class })
public class ServiceTestSuite {

}
