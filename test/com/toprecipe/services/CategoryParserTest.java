package com.toprecipe.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.toprecipe.models.Category;
import com.toprecipe.repository.CategoryRepository;
import com.toprecipie.config.AbstractContainerTest;

public class CategoryParserTest extends AbstractContainerTest {

	@Autowired
	CategoryRepository repo;
	@Autowired
	CategoryParser inTest;
	
	@After
	public void deleteCategories() {
		repo.deleteAll();
	}
	
	@Test
	public void testBlankLine () {
		assertNull(inTest.parse("  "));
	}
	
	@Test
	public void testComment ()
	{
		assertNull (inTest.parse("# This is a comment"));
	}
	
	@Test
	public void testWhenCategorySpecified ()
	{
		Category c = (inTest.parse("Breads"));
		assertEquals("Breads", c.getTitle());
	}
	
	@Test
	public void testWhenCategoryHasAParent ()
	{
		Category parent = new Category();
		parent.setTitle("Breads");
		repo.save(parent);
		
		Category c = (inTest.parse("Breads->Paratha"));
		assertEquals("Paratha", c.getTitle());
		assertEquals ("Breads", c.getParent().getTitle());
	}
	
	@Test
	public void testWithSpacesWhenCategoryHasAParent ()
	{
		Category parent = new Category();
		parent.setTitle("Breads");
		repo.save(parent);
		
		Category c = (inTest.parse("Breads -> Paratha "));
		assertEquals("Paratha", c.getTitle());
		assertEquals ("Breads", c.getParent().getTitle());
	}
	
	@Test
	public void testWhenCategoryHasTwoAncesters ()
	{
		Category parent = new Category();
		parent.setTitle("Veg");
		repo.save(parent);
		
		Category parent2 = new Category ();
		parent2.setTitle("Breads");
		parent2.setParentCategory(parent);
		repo.save (parent2);
		
		Category c = (inTest.parse("Veg->Breads->Paratha"));
		assertEquals("Paratha", c.getTitle());
		assertEquals ("Breads", c.getParent().getTitle());
		assertEquals ("Veg", c.getParent().getParent().getTitle());
	}

	@Test(expected=CategoryNotFoundException.class)
	public void testWhenInvalidParentSpecified ()
	{
		inTest.parse("Veg->Non Existent->Paratha");
	}
}
