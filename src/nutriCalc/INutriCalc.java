package nutriCalc;


import java.util.List;
import java.util.Map;

/**
 * Interface for nutrition calculation
 * Defines the contract for calculating nutrition profiles, combining profiles,
 * and calculating nutritional differences between ingredients
 */
public interface INutriCalc {
    
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
}
