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
  * Main method that takes ingredients with quantities and provides complete nutrition profile
  * Creates one NutrientProfile for the list
  * 
  * @param ingredients List of [ingredientId, quantity] pairs
  * @return NutrientProfile containing calculated nutrition values
  */
	NutrientProfile calculateNutritionProfiles(Map<Integer, Double> ingredients);
	
  /**
   * Main method that takes ingredients with quantities and provides the difference between the two
   * Creates one Map with the nutrient id and difference
   * 
   * @param ingredients List of food
   * @return NutrientProfile containing calculated nutrition values
   */
	Map<Integer, Double> calculateNutrientDifference(List<Food> Newfood, List<Food> Oldfood);
	NutrientProfile calculateNutritionProfilesFromMaps(List<Map<Integer, Double>> ingredientMaps);
	

	NutrientProfile calculateNutritionProfiles(List<Food> foods);
    

    /**
     * Combines multiple nutrition profiles into one
     * @param profiles List of NutrientProfile objects to combine
     * @return Combined NutrientProfile
     */
    NutrientProfile combineNutritionProfiles(List<NutrientProfile> profiles);

//    
    List<NutrientProfile> createIdealIngredient(List<NutritionGoal> goals);
}
