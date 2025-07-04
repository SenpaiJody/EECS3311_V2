package gui;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import ingredientService.IIngredientService;
import ingredientService.IngredientServiceFactory;
import nutrientService.INutrientService;
import nutrientService.NutrientServiceFactory;


//a temporary class just for testing. to be removed in final implementation.
public class CLI {

	public static void main(String[] args) throws IOException {

		IIngredientService ingredientService = IngredientServiceFactory.getService();
		INutrientService nutrientService = NutrientServiceFactory.getService();

		int ingredient = ingredientService.searchIngredientByName("pork", 1).get(0); //get the first thing matching this search term
		System.out.println(String.format("Searching for Ingredient: %s", ingredientService.getIngredientName(ingredient)));
		
		Map<Integer, Double> nutrients = nutrientService.getNutrientsPer100g(ingredient);
		for (Map.Entry<Integer, Double> entries : nutrients.entrySet()) {
			System.out.println(String.format("%s : %s", nutrientService.getNutrientName(entries.getKey()), entries.getValue()));
		}
		
		List<Integer> possibilities = ingredientService.getIngredientMatchingNutrients(nutrientService.getNutrientsPer100g(ingredient), 50);
		 
		for (Integer i: possibilities) {
			System.out.println(String.format("Possible Substitute: (%d) %s", i, ingredientService.getIngredientName(i)));
		}
		

	}
}
