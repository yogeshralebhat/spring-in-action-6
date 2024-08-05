package com.learn.spring.data;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.learn.spring.model.Ingredient;

public interface SpringDataJdbcIngredientRepository extends Repository<Ingredient, String> {

	Iterable<Ingredient> findAll();

	Optional<Ingredient> findById(String id);

}
