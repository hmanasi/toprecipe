package com.toprecipe.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.toprecipe.repository.CategoryRepository;
import com.toprecipie.config.AbstractContainerTest;

public class CategoryImporterTest extends AbstractContainerTest {

	private String categories = "#  Main Categories \n"
			+ "Bread\nRice\nAppetiser\nCurries\n" + "#Breads\n"
			+ "Bread.Naan\nBread.Roti\nBread.Paratha";

	@Autowired
	CategoryImporter importer;
	@Autowired
	CategoryRepository repository;

	@After
	public void deleteCategories() {
		repository.deleteAll();
	}

	@Test
	public void testImportPositive() throws IOException {
		BufferedReader reader = new BufferedReader(new StringReader(categories));
		importer.importCategories(reader);
	}
}
