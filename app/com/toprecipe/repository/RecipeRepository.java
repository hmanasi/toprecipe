package com.toprecipe.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.toprecipe.models.Recipe;

@Repository
public class RecipeRepository {

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void save(Recipe r) {
		em.persist(r);
	}

	@Transactional
	public Recipe find(Integer id) {
		return em.find(Recipe.class, id);
	}

}
