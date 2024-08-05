package com.learn.spring.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.learn.spring.data.IngredientRepository;
import com.learn.spring.data.SpringDataJdbcIngredientRepository;
import com.learn.spring.model.Ingredient;
import com.learn.spring.model.Taco;
import com.learn.spring.model.TacoOrder;
import com.learn.spring.model.Ingredient.Type;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
public class DesignTacoController {
	
	private SpringDataJdbcIngredientRepository ingredientRepository;
	
	//private IngredientRepository ingredientRepository;
	
	/*public DesignTacoController(IngredientRepository ingredientRepository) {
		this.ingredientRepository = ingredientRepository;
	}*/
	
	public DesignTacoController(SpringDataJdbcIngredientRepository ingredientRepository) {
		this.ingredientRepository = ingredientRepository;
	}
	
	@ModelAttribute
	public void addIngredientsToModel(Model model) {
		/*List<Ingredient> ingredients = Arrays.asList(
				new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
				new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
				new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
				new Ingredient("CARN", "Carnitas", Type.PROTEIN),
				new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
				new Ingredient("LETC", "Lettuce", Type.VEGGIES),
				new Ingredient("CHED", "Cheddar", Type.CHEESE),
				new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
				new Ingredient("SLSA", "Salsa", Type.SAUCE),
				new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
		);*/
		
		Iterable<Ingredient> ingredients = ingredientRepository.findAll();
		
		Type[] types = Ingredient.Type.values();
		for(Type type : types) {
			model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
		}
	}
	
	@ModelAttribute(name = "taco")
	public Taco taco() {
		return new Taco();
	}
	
	@ModelAttribute(name = "tacoOrder")
	public TacoOrder order() {
		return new TacoOrder();
	}
	
	@GetMapping
	public String showDesignForm() {
		return "design";
	}
	
	@PostMapping
	public String processTaco(@Valid Taco taco, Errors errors, @ModelAttribute TacoOrder tacoOrder) {
		if(errors.hasErrors()) {
			log.error(errors.toString());
			return "design";
		}

		log.info("Processing taco: {}", taco);
		tacoOrder.addTaco(taco);
		return "redirect:/orders/current";
	}

	/*private Iterable<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
		return ingredients
				.stream()
				.filter(ingredient -> ingredient.getType().equals(type))
				.collect(Collectors.toList());
	}*/
	
	private Iterable<Ingredient> filterByType(Iterable<Ingredient> ingredients, Type type) {
		ArrayList<Ingredient> filteredIngredients = new ArrayList<>();
		for(Ingredient ingredient : ingredients) {
			if(ingredient.getType().equals(type)) {
				filteredIngredients.add(ingredient);
			}
		}
		return filteredIngredients;
	}
}
