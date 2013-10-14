package com.toprecipe.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.toprecipe.models.FoodItem;
import com.toprecipe.services.CategoryService;
import com.toprecipie.config.AbstractContainerTest;

public class FoodItemRepositoryTest extends AbstractContainerTest {

	@Autowired
	private FoodItemRepository inTest;
	@Autowired
	private CategoryRepository repository;
	@Autowired
	private CategoryService service;
	@Autowired
	private PlatformTransactionManager transactionManager;
	private TransactionTemplate transactionTemplate;

	@Before
	public void setup() {
		transactionTemplate = new TransactionTemplate(transactionManager);
	}
	
	@After
	public void deleteCategories() {
		inTest.deleteAll();
		repository.deleteAll();
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void testCreateWithSameNameFails() {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			public void doInTransactionWithoutResult(TransactionStatus status) {
				FoodItem item = new FoodItem();
				item.setTitle("foogItem");
				item.addCategory(service.createCategory("parent1"));
				inTest.save(item);
				
				item = new FoodItem();
				item.setTitle("foogItem");
				item.addCategory(service.createCategory("parent2"));
				inTest.save(item);
			}
		});
	}
}
