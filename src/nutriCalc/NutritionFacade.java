/**
 * 
 */
/**
 * @author kunjalarora
 *
 */
package nutriCalc;

import java.util.*;
import nutrientService.INutrientService;
import nutrientService.NutrientServiceFactory;


/**
 * Service responsible for performing nutrition calculations
 */
class CalculationService {
    
    /**
     * Calculates nutrition profiles for a list of ingredients with their quantities
     * @param ingredients List of [ingredientId, quantity] pairs
     * @param nutritionDataPer100g Map of ingredient ID to nutrition data per 100g
     * @return NutrientProfile containing calculated values
     */
    public NutrientProfile calculateNutrientProfiles(List<List<Object>> ingredients, 
                                                   Map<Integer, Map<Integer, Double>> nutritionDataPer100g) {
        Map<Integer, Double> totalNutrients = new HashMap<>();
        
        for (List<Object> ingredient : ingredients) {
            int ingredientId = (Integer) ingredient.get(0);
            double quantity = (Double) ingredient.get(1);
            
            Map<Integer, Double> ingredientNutrients = nutritionDataPer100g.get(ingredientId);
            if (ingredientNutrients != null) {
                for (Map.Entry<Integer, Double> nutrientEntry : ingredientNutrients.entrySet()) {
                    int nutrientId = nutrientEntry.getKey();
                    double nutrientPer100g = nutrientEntry.getValue();
                    double calculatedAmount = applyQuantityFormula(nutrientPer100g, quantity);
                    
                    totalNutrients.merge(nutrientId, calculatedAmount, Double::sum);
                }
            }
        }
        
        return new NutrientProfile(totalNutrients);
    }
    
    /**
     * Combines multiple nutrition profiles into one
     * @param profiles List of NutrientProfile objects to combine
     * @return Combined NutrientProfile
     */
    public NutrientProfile combineNutritionProfiles(List<NutrientProfile> profiles) {
        Map<Integer, Double> combinedNutrients = new HashMap<>();
        
        for (NutrientProfile profile : profiles) {
            for (Map.Entry<Integer, Double> entry : profile.getAllNutrients().entrySet()) {
                combinedNutrients.merge(entry.getKey(), entry.getValue(), Double::sum);
            }
        }
        
        return new NutrientProfile(combinedNutrients);
    }
    
    /**
     * Applies the quantity formula: (nutrientPer100g * quantity) / 100
     * @param nutrientPer100g The nutrient amount per 100g
     * @param quantity The actual quantity of the ingredient
     * @return Calculated nutrient amount
     */
    private double applyQuantityFormula(double nutrientPer100g, double quantity) {
        return (nutrientPer100g * quantity) / 100.0;
    }
    
    public Map<Integer, Double> calculateNutrientDifference(NutrientProfile profile1, NutrientProfile profile2) {
        Map<Integer, Double> differences = new HashMap<>();
        
        // Get all unique nutrient IDs from both profiles
        Set<Integer> allNutrientIds = new HashSet<>();
        allNutrientIds.addAll(profile1.getAllNutrients().keySet());
        allNutrientIds.addAll(profile2.getAllNutrients().keySet());
        
        // Calculate differences for each nutrient
        for (Integer nutrientId : allNutrientIds) {
            double value1 = profile1.getAllNutrients().getOrDefault(nutrientId, 0.0);
            double value2 = profile2.getAllNutrients().getOrDefault(nutrientId, 0.0);
            double difference = value1 - value2;
            
            // Round to 2 decimal places for consistency
            difference = Math.round(difference * 100.0) / 100.0;
            
            // Only include nutrients with non-zero differences (using 0.01 threshold after rounding)
            if (Math.abs(difference) >= 0.01) {
                differences.put(nutrientId, difference);
            }
        }
        
        return differences;
    }
    
    /**
     * Creates an ideal ingredient nutrition profile with modified nutrient intensity
     * @param ingredientId The ID of the base ingredient
     * @param targetNutrientId The nutrient ID to modify
     * @param intensityPercentage The percentage change (e.g., 15 for +15% increase, -15 for -15% decrease)
     * @param nutritionDataPer100g Map of ingredient ID to nutrition data per 100g
     * @return NutrientProfile with modified nutrient intensity
     */
    public NutrientProfile createIdealIngredientProfile(int ingredientId, int targetNutrientId, 
                                                       double intensityPercentage, 
                                                       Map<Integer, Map<Integer, Double>> nutritionDataPer100g) {
        Map<Integer, Double> idealNutrients = new HashMap<>();
        
        Map<Integer, Double> originalNutrients = nutritionDataPer100g.get(ingredientId);
        if (originalNutrients == null) {
            // Return empty profile if ingredient not found
            return new NutrientProfile(idealNutrients);
        }
        
        // Copy all original nutrients
        for (Map.Entry<Integer, Double> nutrientEntry : originalNutrients.entrySet()) {
            int nutrientId = nutrientEntry.getKey();
            double originalValue = nutrientEntry.getValue();
            
            if (nutrientId == targetNutrientId) {
                // Apply intensity modification to target nutrient
                // If original value is 0, use 1g as default base value
                double baseValue = (originalValue == 0.0) ? 1.0 : originalValue;
                // Convert percentage change to multiplier: 15% increase = 1.15, -15% decrease = 0.85
                double multiplier = 1.0 + (intensityPercentage / 100.0);
                double modifiedValue = baseValue * multiplier;
                // Round to 2 decimal places for consistency
                modifiedValue = Math.round(modifiedValue * 100.0) / 100.0;
                idealNutrients.put(nutrientId, modifiedValue);
            } else {
                // Keep original value for other nutrients
                idealNutrients.put(nutrientId, originalValue);
            }
        }
        
        return new NutrientProfile(idealNutrients);
    }

}

/**
 * Facade class that simplifies complex nutrition calculations
 * Coordinates between data retrieval and calculation subsystems
 */
public class NutritionFacade implements INutriCalc{
    private INutrientService nutrientService;
    private CalculationService calculationService;
    
    public NutritionFacade() {
        this.nutrientService = NutrientServiceFactory.getService();
        this.calculationService = new CalculationService();
    }
    
    /**
     * Main method that takes ingredients with quantities and provides complete nutrition profile
     * Takes ingredients + per100g data, applies formula: (nutrientPer100g x quantity) ÷ 100
     * Creates one NutrientProfile for the list
     * 
     * @param ingredients List of [ingredientId, quantity] pairs
     * @return NutrientProfile containing calculated nutrition values
     */
    public NutrientProfile calculateNutritionProfiles(List<List<Object>> ingredients) {
        // Extract ingredient IDs for data retrieval
        List<Integer> ingredientIds = new ArrayList<>();
        for (List<Object> ingredient : ingredients) {
            ingredientIds.add((Integer) ingredient.get(0));
        }
        
        // Get nutrition data per 100g for all ingredients in one call
        Map<Integer, Map<Integer, Double>> nutritionDataPer100g = nutrientService.getNutrientsListPer100g(ingredientIds);
        
        // Calculate the nutrition profile
        return calculationService.calculateNutrientProfiles(ingredients, nutritionDataPer100g);
    }
    
    /**
     * Combines multiple nutrition profiles into one
     * @param profiles List of NutrientProfile objects to combine
     * @return Combined NutrientProfile
     */
    public NutrientProfile combineNutritionProfiles(List<NutrientProfile> profiles) {
        return calculationService.combineNutritionProfiles(profiles);
    }
    
    /**
     * Calculates the difference between two nutrition profiles
     * Formula: profile1 - profile2
     * @param profile1 First nutrition profile
     * @param profile2 Second nutrition profile  
     * @return Map of nutrient ID to difference value (only nutrients with non-zero differences)
     */
    
    
    /**
     * Calculates the nutritional difference between two ingredient lists
     * Formula: ingredients1 - ingredients2
     * @param ingredients1 First list of [ingredientId, quantity] pairs
     * @param ingredients2 Second list of [ingredientId, quantity] pairs
     * @return Map of nutrient ID to difference value (only nutrients with non-zero differences, positive values mean ingredients1 has more, negative means ingredients2 has more)
     */
    public Map<Integer, Double> calculateNutrientDifference(List<List<Object>> ingredients1, List<List<Object>> ingredients2) {
        // Calculate nutrition profiles for both ingredient lists
        NutrientProfile profile1 = calculateNutritionProfiles(ingredients1);
        NutrientProfile profile2 = calculateNutritionProfiles(ingredients2);
        
        // Calculate and return the difference
        return calculationService.calculateNutrientDifference(profile1, profile2);
    }
    
    /**
     * Creates an ideal ingredient with modified nutrient intensity
     * @param ingredientId The ID of the base ingredient
     * @param nutrientId The nutrient ID to modify
     * @param intensityPercentage The percentage change (e.g., 15 for +15% increase, -15 for -15% decrease)
     * @return NutrientProfile representing the ideal ingredient with modified nutrient intensity
     * Note: If original nutrient value is 0, uses 1g as default base value
     */
    public NutrientProfile createIdealIngredient(int ingredientId, int nutrientId, int intensityPercentage) {
        // Get nutrition data for the specific ingredient
        List<Integer> ingredientIds = Arrays.asList(ingredientId);
        Map<Integer, Map<Integer, Double>> nutritionDataPer100g = nutrientService.getNutrientsListPer100g(ingredientIds);
        
        // Create and return the ideal ingredient profile
        return calculationService.createIdealIngredientProfile(ingredientId, nutrientId, intensityPercentage, nutritionDataPer100g);
    }
    
    
}