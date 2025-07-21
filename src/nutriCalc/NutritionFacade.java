
package nutriCalc;

import java.util.*;

import food.Food;
import nutrientService.INutrientService;
import nutrientService.NutrientServiceFactory;
import recommendation.NutritionGoal;


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
     * Main method that takes Food objects and provides complete nutrition profile
     * Creates one NutrientProfile for the a list of Food objects
     * 
     * @param ingredients Map of ingredientId to quantity
     * @return NutrientProfile containing calculated nutrition values
     */
    
    public NutrientProfile calculateNutritionProfiles(List<Food> foods) {
    	List<Map<Integer, Double>> ingredientsList = new ArrayList<>();
        
        for (Food food : foods) {
            ingredientsList.add(food.getIngredients());
        }
        NutrientProfile finalProfile= calculateNutritionProfilesFromMaps(ingredientsList);
        
        return finalProfile;
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
     * Calculates the nutritional difference between two List of Food objects
     * @param ingredients1 First map of ingredientId to quantity
     * @param ingredients2 Second map of ingredientId to quantity
     * @return Map of nutrient ID to difference value (only nutrients with non-zero differences)
     */
    public Map<Integer, Double> calculateNutrientDifference(List<Food> NewFood, List<Food> Oldfood) {
        // Calculate nutrition profiles for both ingredient maps
        NutrientProfile profile1 = calculateNutritionProfiles(NewFood);
        NutrientProfile profile2 = calculateNutritionProfiles(Oldfood);
        
        // Calculate and return the difference
        return calculationService.calculateNutrientDifference(profile1, profile2);
    }
    

    /**
     * Combines multiple nutrition profiles into one
     * @param profiles List of NutrientProfile objects to combine
     * @return Combined NutrientProfile
     */
    public NutrientProfile combineNutritionProfiles(List<NutrientProfile> profiles) {
    	NutrientProfile finalprofile = calculationService.combineNutritionProfiles(profiles);
        return finalprofile;
    }
    
    /**
     * Method that takes ingredients with quantities and provides complete nutrition profile
     * Takes ingredients + per100g data, applies formula: (nutrientPer100g x quantity) ÷ 100
     * Creates one NutrientProfile for the map
     * This is also where the Map of nutritionDataPer100g is retrieved form the nutrient Service
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
     * method that takes a list of Goals and calculates an ideal ingredient nutrition profile based on the case
     * if 2 differnt ingredients then 2 nutrient profiles returned
     * if 1 ingredient but different nutreintIDs then then one nutrient profiles returned
     * 
     * @param List of NutritionGoals
     * @return List of NutrientProfile with all specified nutrient modifications
     */

    @Override
    public List<NutrientProfile> createIdealIngredient(List<NutritionGoal> goals) {
//        if (goals == null || goals.isEmpty()) {
//            return new ArrayList<>();
//        }

        // Group goals by ingredient ID
        Map<Integer, List<NutritionGoal>> goalsByIngredient = new HashMap<>();
        for (NutritionGoal goal : goals) {
            if (goal.getingredientId() != 0 && goal.getnutrientId() != null) {
                goalsByIngredient.computeIfAbsent(goal.getingredientId(), k -> new ArrayList<>()).add(goal);
            }
        }

//        if (goalsByIngredient.isEmpty()) {
//            return new ArrayList<>();
//        }

        // Get nutrition data for all ingredients
        List<Integer> ingredientIds = new ArrayList<>(goalsByIngredient.keySet());
        Map<Integer, Map<Integer, Double>> nutritionDataPer100g =
                nutrientService.getNutrientsListPer100g(ingredientIds);

        List<NutrientProfile> profiles = new ArrayList<>();

        // Process each ingredient group
        for (Map.Entry<Integer, List<NutritionGoal>> entry : goalsByIngredient.entrySet()) {
            Integer ingredientId = entry.getKey();
            List<NutritionGoal> ingredientGoals = entry.getValue();

            // Group goals by nutrient ID to handle same nutrient modifications
            Map<Integer, Integer> nutrientIntensities = new HashMap<>();
            for (NutritionGoal goal : ingredientGoals) {
                Integer nutrientId = goal.getnutrientId();
                int signedIntensity = goal.applyGoalTypeSign();
                // Add intensities for same nutrient (handles same ingredient + same nutrient case)
                nutrientIntensities.merge(nutrientId, signedIntensity, Integer::sum);
            }

            // Use the modified calculationService method with multiple nutrients
            NutrientProfile ingredientProfile = calculationService.createIdealIngredientProfile(
                    ingredientId, nutrientIntensities, nutritionDataPer100g);

            if (ingredientProfile != null) {
                profiles.add(ingredientProfile);
            }
        }

        return profiles;
    }


/**
 * Private helper Service responsible for performing nutrition calculations
 */
class CalculationService {
    
    /**
     * Calculates nutrition profiles for a map of ingredients with their quantities 
     * because dependent on the NutrientSerivce which returns a Map
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
    
    
    /**
     * Creates an ideal ingredient nutrition profile with multiple modified nutrient intensities
     * @param ingredientId The ID of the base ingredient
     * @param nutrientIntensities Map of nutrient ID to intensity percentage
     * @param nutritionDataPer100g Map of ingredient ID to nutrition data per 100g
     * @return NutrientProfile with all specified nutrient modifications
     */
    public NutrientProfile createIdealIngredientProfile(int ingredientId, 
                                                       Map<Integer, Integer> nutrientIntensities,
                                                       Map<Integer, Map<Integer, Double>> nutritionDataPer100g) {
        Map<Integer, Double> idealNutrients = new HashMap<>();
        
        Map<Integer, Double> originalNutrients = nutritionDataPer100g.get(ingredientId);
        if (originalNutrients == null) {
            return new NutrientProfile(idealNutrients);
        }
        
        // Process all nutrients for this ingredient
        for (Map.Entry<Integer, Double> nutrientEntry : originalNutrients.entrySet()) {
            int nutrientId = nutrientEntry.getKey();
            double originalValue = nutrientEntry.getValue();
            
            if (nutrientIntensities.containsKey(nutrientId)) {
                // Apply intensity modification to this nutrient
                double intensityPercentage = nutrientIntensities.get(nutrientId);
                double baseValue = (originalValue == 0.0) ? 1.0 : originalValue;
                double multiplier = 1.0 + (intensityPercentage / 100.0);
                double modifiedValue = baseValue * multiplier;
                modifiedValue = Math.round(modifiedValue * 100.0) / 100.0;
                idealNutrients.put(nutrientId, modifiedValue);
            } else {
                // Keep original value for unmodified nutrients
                idealNutrients.put(nutrientId, originalValue);
            }
        }
        
        return new NutrientProfile(idealNutrients);
    }
    
    /**
     * Calculates nutrition difference for 2 nutrient Profiles 
     * @param Nutreint Profiles for which the difference is to tbe calculated
     * @param nutritionDataPer100g Map of ingredient ID to nutrition data per 100g
     * @return NutrientProfile containting the differnce between profile1 and profile2
     */
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
    

}






    
    
}