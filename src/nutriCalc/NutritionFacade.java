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
     * Calculates nutrition profiles for a map of ingredients with their quantities
     * @param ingredients Map of ingredientId to quantity
     * @param nutritionDataPer100g Map of ingredient ID to nutrition data per 100g
     * @return NutrientProfile containing calculated values
     */
    public NutrientProfile calculateNutrientProfiles(Map<Integer, Double> ingredients, 
                                                   Map<Integer, Map<Integer, Double>> nutritionDataPer100g) {
        Map<Integer, Double> totalNutrients = new HashMap<>();
        
        for (Map.Entry<Integer, Double> ingredient : ingredients.entrySet()) {
            int ingredientId = ingredient.getKey();
            double quantity = ingredient.getValue();
            
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
    
    /**
     * Aggregates duplicate ingredients in a list by summing their quantities
     * @param ingredients List of [ingredientId, quantity] pairs that may contain duplicates
     * @return Map of ingredientId to total quantity (duplicates aggregated)
     */
    public Map<Integer, Double> aggregateIngredients(List<List<Object>> ingredients) {
        Map<Integer, Double> aggregatedIngredients = new HashMap<>();
        
        for (List<Object> ingredient : ingredients) {
            int ingredientId = (Integer) ingredient.get(0);
            double quantity = (Double) ingredient.get(1);
            
            // Use merge to add quantities for duplicate ingredients
            aggregatedIngredients.merge(ingredientId, quantity, Double::sum);
        }
        
        return aggregatedIngredients;
    }
}

/**
 * Facade class that simplifies complex nutrition calculations
 * Coordinates between data retrieval and calculation subsystems
 */
public class NutritionFacade implements INutriCalc {
    private INutrientService nutrientService;
    private CalculationService calculationService;
    
    public NutritionFacade() {
        this.nutrientService = NutrientServiceFactory.getService();
        this.calculationService = new CalculationService();
    }
    
    
    /**
     * Method that takes a list of ingredient maps and aggregates them before calculating nutrition profile
     * This allows you to have multiple maps with potentially overlapping ingredients that will be summed
     * @param ingredientMaps List of maps, each containing ingredientId to quantity mappings
     * @return NutrientProfile containing calculated nutrition values with aggregated ingredients
     */
    public NutrientProfile calculateNutritionProfilesFromMaps(List<Map<Integer, Double>> ingredientMaps) {
        Map<Integer, Double> aggregatedIngredients = new HashMap<>();
        
        // Aggregate all ingredient maps from the list
        for (Map<Integer, Double> ingredientMap : ingredientMaps) {
            for (Map.Entry<Integer, Double> entry : ingredientMap.entrySet()) {
                aggregatedIngredients.merge(entry.getKey(), entry.getValue(), Double::sum);
            }
        }
        
        // Use the single map method with aggregated ingredients
        return calculateNutritionProfiles(aggregatedIngredients);
    }
    
    
    /**
     * Main method that takes ingredients with quantities and provides complete nutrition profile
     * Takes ingredients + per100g data, applies formula: (nutrientPer100g x quantity) ÷ 100
     * Creates one NutrientProfile for the map
     * 
     * @param ingredients Map of ingredientId to quantity
     * @return NutrientProfile containing calculated nutrition values
     */
    public NutrientProfile calculateNutritionProfiles(Map<Integer, Double> ingredients) {
        // Extract ingredient IDs for data retrieval
        List<Integer> ingredientIds = new ArrayList<>(ingredients.keySet());
        
        // Get nutrition data per 100g for all ingredients in one call
        Map<Integer, Map<Integer, Double>> nutritionDataPer100g = nutrientService.getNutrientsListPer100g(ingredientIds);
        
        // Calculate the nutrition profile
        return calculationService.calculateNutrientProfiles(ingredients, nutritionDataPer100g);
    }
    
    /**
     * Method that aggregates ingredients from multiple Maps before calculating nutrition profile
     * Useful when you have multiple ingredient sources that need to be combined
     * @param ingredientMaps Multiple maps of ingredientId to quantity that should be aggregated
     * @return NutrientProfile containing calculated nutrition values with aggregated ingredients
     */
//    public NutrientProfile calculateNutritionProfiles(Map<Integer, Double>... ingredientMaps) {
//        Map<Integer, Double> aggregatedIngredients = new HashMap<>();
//        
//        // Aggregate all ingredient maps
//        for (Map<Integer, Double> ingredientMap : ingredientMaps) {
//            for (Map.Entry<Integer, Double> entry : ingredientMap.entrySet()) {
//                aggregatedIngredients.merge(entry.getKey(), entry.getValue(), Double::sum);
//            }
//        }
//        
//        // Use the single map method with aggregated ingredients
//        return calculateNutritionProfiles(aggregatedIngredients);
//    }
    
    /**
     * Overloaded method to maintain backward compatibility with List<List<Object>> format
     * This method now properly aggregates duplicate ingredients by summing their quantities
     * @param ingredients List of [ingredientId, quantity] pairs (duplicates will be aggregated)
     * @return NutrientProfile containing calculated nutrition values
     */
    public NutrientProfile calculateNutritionProfiles(List<List<Object>> ingredients) {
        // Convert List<List<Object>> to Map<Integer, Double> while aggregating duplicates
        Map<Integer, Double> aggregatedIngredients = calculationService.aggregateIngredients(ingredients);
        
        return calculateNutritionProfiles(aggregatedIngredients);
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
     * Calculates the nutritional difference between two ingredient maps
     * Formula: ingredients1 - ingredients2
     * @param ingredients1 First map of ingredientId to quantity
     * @param ingredients2 Second map of ingredientId to quantity
     * @return Map of nutrient ID to difference value (only nutrients with non-zero differences)
     */
    public Map<Integer, Double> calculateNutrientDifference(Map<Integer, Double> ingredientsNew, 
                                                           Map<Integer, Double> ingredientsOld) {
        // Calculate nutrition profiles for both ingredient maps
        NutrientProfile profile1 = calculateNutritionProfiles(ingredientsNew);
        NutrientProfile profile2 = calculateNutritionProfiles(ingredientsOld);
        
        // Calculate and return the difference
        return calculationService.calculateNutrientDifference(profile1, profile2);
    }
    
    /**
     * Overloaded method to maintain backward compatibility with List<List<Object>> format
     * This method now properly aggregates duplicate ingredients in both lists
     * @param ingredients1 First list of [ingredientId, quantity] pairs
     * @param ingredients2 Second list of [ingredientId, quantity] pairs
     * @return Map of nutrient ID to difference value
     */
    public Map<Integer, Double> calculateNutrientDifference(List<List<Object>> ingredientsNew, 
                                                           List<List<Object>> ingredientsOld) {
        // Convert both lists to maps while aggregating duplicates
        Map<Integer, Double> map1 = calculationService.aggregateIngredients(ingredientsNew);
        Map<Integer, Double> map2 = calculationService.aggregateIngredients(ingredientsOld);
        
        return calculateNutrientDifference(map1, map2);
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