package com.learn.spring.converter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.learn.spring.data.IngredientRepository;
import com.learn.spring.data.SpringDataJdbcIngredientRepository;
import com.learn.spring.model.Ingredient;
import com.learn.spring.model.Ingredient.Type;

@Component
public class IngredientByIdConverter implements Converter<String, Ingredient>{
	
	/*private Map<String, Ingredient> ingredientMap;
	
	public IngredientByIdConverter() {
		ingredientMap = new HashMap<>();
		ingredientMap.put("FLTO", new Ingredient("FLTO", "Flour Tortilla", Type.WRAP));
		ingredientMap.put("COTO", new Ingredient("COTO", "Corn Tortilla", Type.WRAP));
		ingredientMap.put("GRBF", new Ingredient("GRBF", "Ground Beef", Type.PROTEIN));
		ingredientMap.put("CARN", new Ingredient("CARN", "Carnitas", Type.PROTEIN));
		ingredientMap.put("TMTO", new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES));
		ingredientMap.put("LETC", new Ingredient("LETC", "Lettuce", Type.VEGGIES));
		ingredientMap.put("CHED", new Ingredient("CHED", "Cheddar", Type.CHEESE));
		ingredientMap.put("JACK", new Ingredient("JACK", "Monterrey Jack", Type.CHEESE));
		ingredientMap.put("SLSA", new Ingredient("SLSA", "Salsa", Type.SAUCE));
		ingredientMap.put("SRCR", new Ingredient("SRCR", "Sour Cream", Type.SAUCE));
	}

	@Override
	public Ingredient convert(String ingredientId) {
		return ingredientMap.get(ingredientId);
	}*/
	
	/*private IngredientRepository ingredientRepository;
	
	public IngredientByIdConverter(IngredientRepository ingredientRepository) {
		this.ingredientRepository = ingredientRepository;
	}*/
	
	private SpringDataJdbcIngredientRepository ingredientRepository;
	
	public IngredientByIdConverter(SpringDataJdbcIngredientRepository ingredientRepository) {
		this.ingredientRepository = ingredientRepository;
	}

	@Override
	public Ingredient convert(String ingredientId) {
		System.out.println(ingredientRepository.findById(ingredientId));
		return ingredientRepository.findById(ingredientId).orElse(null);
	}

}
