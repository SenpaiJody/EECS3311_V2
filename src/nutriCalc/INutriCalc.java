package nutriCalc;


import java.util.List;
import java.util.Map;

import food.Food;
import recommendation.NutritionGoal;

/**
 * Interface for nutrition calculation
 * Defines the contract for calculating nutrition profiles, combining profiles,
 * and calculating nutritional differences between ingredients
 */
public interface INutriCalc {
	/**
	  * Main method that takes list of Food object with ingredient ids and quantities and provides 
	  * complete nutrition profile
	  * 
	  * Creates one NutrientProfile for the entire list
	  * 
	  * @param List of Food object
	  * @return NutrientProfile containing calculated nutrition values using the formula (nutrientPer100g × quantity) ÷ 100
	  */	
	NutrientProfile calculateNutritionProfiles(List<Food> foods);

    /**
     * Main method that takes List of Nutrition Goals
     * Calculates a Nutrient Profiles for the ideal Ingredient which will be used by recommendation package
     * 
     * @param List of NutritionGoal 
     * @return NutrientProfile containing calculated nutrition values for the Ideal new Ingredient
     */
    List<NutrientProfile> createIdealIngredient(List<NutritionGoal> goals);
    
    /**
     * Method that takes ingredients with quantities and provides the difference between the two
     * Creates one Map with the nutrient id and difference
     * 
     * @param ingredients List of food
     * @return NutrientProfile containing calculated nutrition values
     */
  	Map<Integer, Double> calculateNutrientDifference(List<Food> Newfood, List<Food> Oldfood);
	
	/**
	  * Combines multiple nutrition profiles into one
	  * @param profiles List of NutrientProfile objects to combine
	  * @return Combined NutrientProfile
	  */
    NutrientProfile combineNutritionProfiles(List<NutrientProfile> profiles);    
		
	  /**
	  * Alternate Method that takes ingredients with quantities and provides complete nutrition profile
	  * Creates one NutrientProfile for the Map of ingredients
	  * 
	  * @param ingredients Map of [ingredientId, quantity] pairs
	  * @return NutrientProfile containing calculated nutrition values
	  */
	NutrientProfile calculateNutritionProfiles(Map<Integer, Double> ingredients);
	
	/**
	  * Main method that takes ingredients with quantities and provides complete nutrition profile
	  * Creates one NutrientProfile for the list
	  * 
	  * @param ingredients List of [ingredientId, quantity] pairs
	  * @return NutrientProfile containing calculated nutrition values
	  */
	NutrientProfile calculateNutritionProfilesFromMaps(List<Map<Integer, Double>> ingredientMaps);

}
