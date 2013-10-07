package com.toprecipe.services.dataimport;

import java.io.BufferedReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avaje.ebean.annotation.Transactional;
import com.toprecipe.models.Category;
import com.toprecipe.repository.CategoryRepository;

@Service
public class CategoryImporter {

	@Autowired
	private CategoryRepository repo;
	@Autowired
	private CategoryParser parser;

	@Transactional
	public void importCategories(BufferedReader reader) throws IOException {
		long lineNo = 0;
		int categoriesImported = 0;

		String line = reader.readLine();
		lineNo++;
		while (line != null) {
			try {
				Category c = parser.parseForNewCategory(line);
				if (c != null) {
					repo.save(c);
					categoriesImported++;
				}
				line = reader.readLine();
			} catch (CategoryNotFoundException e) {
				System.out
						.println(String
								.format("Error importing category:%s on line %d due to error:- %s ",
										line, lineNo, e.getMessage()));
			}
		}

		System.out.println(String.format("Imported %d categories.",
				categoriesImported));
	}
}
