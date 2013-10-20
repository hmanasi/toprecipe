package com.toprecipe.services;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.toprecipe.services.dataimport.CategoryImporterTest;
import com.toprecipe.services.dataimport.CategoryParserTest;
import com.toprecipe.services.dataimport.FoodItemImporterTest;
import com.toprecipe.services.dataimport.RecipeImporterTest;

@RunWith(Suite.class)
@SuiteClasses({ CategoryImporterTest.class, CategoryParserTest.class,
		FoodItemImporterTest.class, RecipeImporterTest.class,
		CategoryServiceTest.class, TopRecipeServiceTest.class,
		PasswordHashServiceTest.class, UserServiceTest.class })
public class ServiceTestSuite {

}
