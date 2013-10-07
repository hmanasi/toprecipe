package com.toprecipe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toprecipe.models.Category;
import com.toprecipe.repository.CategoryRepository;

@Service
public class CategoryParser {

	@Autowired
	private CategoryRepository repo;

	public Category parse(String input) {

		String line = input.trim();

		if (line.startsWith("#") || line.isEmpty()) {
			return null;
		}

		String tokens[] = line.split("->");

		Category c = new Category();
		c.setTitle(tokens[tokens.length - 1].trim());

		if (tokens.length > 1) {
			c.setParentCategory(resolveParent(null, tokens, 0, repo));
		}

		return c;
	}

	private Category resolveParent(Category parent, String[] tokens, int i,
			CategoryRepository repo) {

		Category resolved = repo.findByTitleAndParent(tokens[i].trim(), parent);

		if (resolved == null) {
			throw new CategoryNotFoundException(tokens[i]);
		}

		if (i < tokens.length - 2) {
			return resolveParent(resolved, tokens, i + 1, repo);
		} else {
			return resolved;
		}
	}
}
