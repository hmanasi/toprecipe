package com.toprecipe.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.toprecipe.models.Category;
import com.toprecipe.repository.CategoryRepository;
import com.toprecipie.config.AbstractContainerTest;

public class CategoryParserTest extends AbstractContainerTest {

	@Autowired
	private PlatformTransactionManager transactionManager;
	private TransactionTemplate transactionTemplate;
	
	@Autowired
	CategoryRepository repo;
	@Autowired
	CategoryParser inTest;

	@Before
	public void setup() {
		transactionTemplate = new TransactionTemplate(transactionManager);
	}
	
	@After
	public void deleteCategories() {
		repo.deleteAll();
	}
	
	@Test
	public void testBlankLine () {
		assertNull(inTest.parseForNewCategory("  "));
	}
	
	@Test
	public void testComment ()
	{
		assertNull (inTest.parseForNewCategory("# This is a comment"));
	}
	
	@Test
	public void testParseForNewWhenCategorySpecified ()
	{
		assertNull (repo.findByTitle("Breads"));
		
		Category c = (inTest.parseForNewCategory("Breads"));
		assertEquals("Breads", c.getTitle());
		assertNull(c.getId());
	}
	
	@Test(expected=CategoryNotFoundException.class)
	public void testParseForExistingWhenCategoryDoesNotExists ()
	{
		inTest.parseForExistingCategory("Breads");
	}
	
	@Test
	public void testParseForExistingWhenCategoryHasAParent ()
	{
		
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				Category parent = new Category();
				parent.setTitle("Breads");
				repo.save(parent);
				
				Category paratha = new Category ();
				paratha.setTitle("Paratha");
				paratha.setParentCategory(parent);
				repo.save(paratha);
			}
		});

		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				Category c = (inTest
						.parseForExistingCategory("Breads->Paratha"));
				assertEquals("Paratha", c.getTitle());
				assertEquals("Breads", c.getParent().getTitle());
				assertNotNull (c.getId());
			}
		});
	}
	
	@Test
	public void testParseForNewWhenCategoryHasAParent ()
	{
		Category parent = new Category();
		parent.setTitle("Breads");
		repo.save(parent);
		
		Category c = (inTest.parseForNewCategory("Breads->Paratha"));
		assertEquals("Paratha", c.getTitle());
		assertEquals ("Breads", c.getParent().getTitle());
	}
	
	@Test
	public void testParseForNewWithSpacesWhenCategoryHasAParent ()
	{
		Category parent = new Category();
		parent.setTitle("Breads");
		repo.save(parent);
		
		Category c = (inTest.parseForNewCategory("Breads -> Paratha "));
		assertEquals("Paratha", c.getTitle());
		assertEquals ("Breads", c.getParent().getTitle());
	}
	
	@Test
	public void testParseForNewWhenCategoryHasTwoAncesters ()
	{
		Category parent = new Category();
		parent.setTitle("Veg");
		repo.save(parent);
		
		Category parent2 = new Category ();
		parent2.setTitle("Breads");
		parent2.setParentCategory(parent);
		repo.save (parent2);
		
		Category c = (inTest.parseForNewCategory("Veg->Breads->Paratha"));
		assertEquals("Paratha", c.getTitle());
		assertEquals ("Breads", c.getParent().getTitle());
		assertEquals ("Veg", c.getParent().getParent().getTitle());
	}

	@Test(expected=CategoryNotFoundException.class)
	public void testParseForNewWhenInvalidParentSpecified ()
	{
		inTest.parseForNewCategory("Veg->Non Existent->Paratha");
	}
}
