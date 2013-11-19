package com.toprecipe.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toprecipe.models.Category;
import com.toprecipe.services.dataimport.CategoryParser;

@Service
public class TopRecipeService {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private CategoryParser categoryParser;
	
	private static final String TOP_RECIPE_QUERY = "SELECT NEW com.toprecipe.services.TopRecipe (f.id, f.title, r.id, r.id , r.image, r.youTubeVideo, r.flashVideo, r.sourceUrl) "
			+ "FROM Recipe r JOIN r.foodItem f JOIN f.categories c "
			+ "WHERE c.id = :category_id AND r.id IN "
			+ " (SELECT max(r1.id) FROM Recipe r1 JOIN r1.foodItem f1 GROUP BY f1.id)";
	
	public List<TopRecipe> getTopRecipes (String category)
	{
		Category c = categoryParser.parseForExistingCategory(category);
		return getTopRecipes(c.getId());
	}
	
	private List<TopRecipe> getTopRecipes(Long categoryId) {
		return em.createQuery(TOP_RECIPE_QUERY, TopRecipe.class)
				.setParameter("category_id", categoryId).getResultList();
	}
}
