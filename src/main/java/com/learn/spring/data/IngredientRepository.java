package com.learn.spring.data;

import java.util.Optional;

import com.learn.spring.model.Ingredient;

public interface IngredientRepository {
	
	Iterable<Ingredient> findAll();
	
	Optional<Ingredient> findById(String id);
	
	Ingredient save(Ingredient ingredient);
	
}
