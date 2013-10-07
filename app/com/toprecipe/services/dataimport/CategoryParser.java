package com.toprecipe.services.dataimport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toprecipe.models.Category;
import com.toprecipe.repository.CategoryRepository;

@Service
public class CategoryParser {

	@Autowired
	private CategoryRepository repo;

	public Category parseForExistingCategory(String input) {
		return resolveCategory(input, true);
	}

	public Category parseForNewCategory(String input) {
		return resolveCategory(input, false);
	}

	private Category resolveCategory(String input, boolean alreadyExisting) {
		String line = input.trim();

		if (line.startsWith("#") || line.isEmpty()) {
			return null;
		}

		String tokens[] = line.split("->");

		Category c = null;

		Category parent = null;

		if (tokens.length > 1) {
			parent = resolveParent(null, tokens, 0, repo);
		}

		String title = tokens[tokens.length - 1].trim();

		if (alreadyExisting) {
			c = repo.findByTitleAndParent(title, parent);

			if (c == null) {
				throw new CategoryNotFoundException(title, parent);
			}

		} else {
			c = new Category();
			c.setTitle(title);
			c.setParentCategory(parent);
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
