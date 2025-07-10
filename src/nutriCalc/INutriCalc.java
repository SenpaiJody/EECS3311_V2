package nutriCalc;


import java.util.List;
import java.util.Map;

/**
 * Interface for nutrition calculation
 * Defines the contract for calculating nutrition profiles, combining profiles,
 * and calculating nutritional differences between ingredients
 */
public interface INutriCalc {
	
	
	NutrientProfile calculateNutritionProfiles(Map<Integer, Double> ingredients);
	Map<Integer, Double> calculateNutrientDifference(Map<Integer, Double> ingredients1, Map<Integer, Double> ingredients2);
	NutrientProfile calculateNutritionProfilesFromMaps(List<Map<Integer, Double>> ingredientMaps);
	
	
    
    /**
     * Main method that takes ingredients with quantities and provides complete nutrition profile
     * Takes ingredients + per100g data, applies formula: (nutrientPer100g x quantity) ÷ 100
     * Creates one NutrientProfile for the list
     * 
     * @param ingredients List of [ingredientId, quantity] pairs
     * @return NutrientProfile containing calculated nutrition values
     */
    NutrientProfile calculateNutritionProfiles(List<List<Object>> ingredients);
    
    /**
     * Combines multiple nutrition profiles into one
     * @param profiles List of NutrientProfile objects to combine
     * @return Combined NutrientProfile
     */
    NutrientProfile combineNutritionProfiles(List<NutrientProfile> profiles);
    
    /**
     * Calculates the nutritional difference between two ingredient lists
     * Formula: ingredients1 - ingredients2
     * @param ingredients1 First list of [ingredientId, quantity] pairs
     * @param ingredients2 Second list of [ingredientId, quantity] pairs
     * @return Map of nutrient ID to difference value (only nutrients with non-zero differences, 
     *         positive values mean nutrient in ingredients1(new ingredient) has more, negative means nutreint in ingredients2(old ingredient) has more)
     */
    Map<Integer, Double> calculateNutrientDifference(List<List<Object>> ingredients1, List<List<Object>> ingredients2);

    /**
     * Calculates the nutritionProfile for a given intensity and nutrientID
     * @param ingredientID initial ingredient to create the initial profile
     * @param nutreintID nutreint whose values need changes
     * @param intensity the percentage by which the nutrient value needs changing
     * @return NutrientProfile containing calculated nutrition values 
     */
    NutrientProfile createIdealIngredient(int ingredientId, int nutrientId, int intensity);
}
