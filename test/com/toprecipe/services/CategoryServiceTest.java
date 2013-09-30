package com.toprecipe.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

public class CategoryServiceTest extends AbstractContainerTest {

	@Autowired
	private CategoryService inTest;
	@Autowired
	private CategoryRepository repository;
	@Autowired
	private PlatformTransactionManager transactionManager;

	private TransactionTemplate transactionTemplate;

	@Before
	public void setup() {
		transactionTemplate = new TransactionTemplate(transactionManager);
	}

	@After
	public void deleteAllCategories() {
		for (Category c : repository.findAll()) {
			System.out.println("deleting " + c.getTitle());
			deleteCategory(c);
		}
	}

	private void deleteCategory(final Category c) {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				deleteCategoryRecursive(c);
			}
		});
	}

	private void deleteCategoryRecursive(Category c) {
		Category reloaded = repository.findOne(c.getId());
		if (reloaded != null) {
			if (reloaded.getSubCategories().isEmpty()) {
				repository.delete(reloaded);
			} else {
				for (Category sub : reloaded.getSubCategories()) {
					deleteCategoryRecursive(sub);
				}
			}
		}
	}

	@Test
	public void testCreateCategory() {
		inTest.createCategory("testCategory");
		Category returned = repository.findByTitle("testCategory");

		assertNotNull(returned);
		assertEquals("testCategory", returned.getTitle());
	}

	@Test
	public void testCreateCategoryWithParent() {
		Category parent = inTest.createCategory("parent");
		Category child = inTest.createCategory("child", parent);

		assertNotNull(child.getParent());
		assertEquals(parent.getId(), child.getParent().getId());
	}

	@Test
	public void testGetCategoryChainWhenParentExists() {
		Category parent = inTest.createCategory("parent");
		inTest.createCategory("child2", parent);

		Category returned = inTest.getCategoryChain("child2");

		assertNotNull(returned);
		assertNotNull(returned.getParent());
		assertEquals(parent.getId(), returned.getParent().getId());
	}

	@Test
	public void testGetCategoryChainWhenNoParent() {
		inTest.createCategory("test");

		Category returned = inTest.getCategoryChain("test");

		assertNotNull(returned);
		assertNull(returned.getParent());
	}
}
