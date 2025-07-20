package recommendation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import ingredientService.IIngredientService;
import ingredientService.IngredientServiceFactory;
import nutriCalc.INutriCalc;
import nutriCalc.NutrientProfile;
import nutriCalc.NutritionFacade;

public class FoodRecommendation implements IFoodRecommendation{
    private static final double FOOD_GROUP_BONUS = 0.05;
    private INutriCalc nutritionCalculator = new NutritionFacade();
    private IIngredientService ingredientService = IngredientServiceFactory.getService();
    
    private Map<Integer, Map<Integer, Integer>> ingredientIndexMap = new HashMap<>();
    private Map<Integer, Map<Integer, List<NutritionGoal>>> goalsByProfile = new HashMap<>();
    
    private Map<Integer, List<List<Integer>>> latestRecommendations = new HashMap<>();
    
    
 // Automatically triggered on goal changes
    @Override
    public void onGoalChanged(Integer profileId, List<NutritionGoal> updatedGoals) {
        if (updatedGoals.isEmpty()) {
            latestRecommendations.remove(profileId);
            ingredientIndexMap.remove(profileId);
            return;
        }

        Map<Integer, List<NutritionGoal>> goalsByIngredient = updatedGoals.stream()
            .collect(Collectors.groupingBy(NutritionGoal::getingredientId));

        // Create if missing
        latestRecommendations.putIfAbsent(profileId, new ArrayList<>());
        ingredientIndexMap.putIfAbsent(profileId, new HashMap<>());

        List<List<Integer>> recList = latestRecommendations.get(profileId);
        Map<Integer, Integer> ingredientToIndex = ingredientIndexMap.get(profileId);

        for (Map.Entry<Integer, List<NutritionGoal>> entry : goalsByIngredient.entrySet()) {
            Integer ingredientId = entry.getKey();
            List<NutritionGoal> goals = entry.getValue();

            List<List<Integer>> recs = getRecommendations(goals, 4);
            List<Integer> newRec = recs.isEmpty() ? new ArrayList<>() : recs.get(0);

            if (ingredientToIndex.containsKey(ingredientId)) {
                // Replace existing
                int index = ingredientToIndex.get(ingredientId);
                recList.set(index, newRec);
            } else {
                // Append new and track index
                recList.add(newRec);
                ingredientToIndex.put(ingredientId, recList.size() - 1);
            }
        }
    }
    
    @Override
    public List<List<Integer>> getLatestRecommendations(int profileId) {
        return latestRecommendations.getOrDefault(profileId, new ArrayList<>());
    }
    
    // Main method - returns list of lists of recommended ingredient IDs
    public List<List<Integer>> getRecommendations(List <NutritionGoal> goal) {
        return getRecommendations(goal, 4); // Default limit of 4
    }
    
    // Method to handle multiple goals
    private List<List<Integer>> getRecommendations(List<NutritionGoal> goals, int limit) {
        System.out.println("=== DEBUG: Starting getRecommendations with " + goals.size() + " goals ===");
        
        try {
            // 1. Create ideal nutrient profiles for all goals
            System.out.println("\n--- Step 1: Creating ideal nutrient profiles ---");
            List<NutrientProfile> idealProfiles = nutritionCalculator.createIdealIngredient(goals);
            System.out.println("Number of ideal profiles created: " + idealProfiles.size());
            
            if (idealProfiles.isEmpty()) {
                System.out.println("ERROR: No ideal profiles created!");
                return new ArrayList<>();
            }
            
            // 2. Determine strategy based on number of ideal profiles
            System.out.println("\n--- Step 2: Determining matching strategy based on profile count ---");
            List<List<Integer>> allRecommendations = new ArrayList<>();
            
            if (idealProfiles.size() == 1) {
                // Single profile strategy: Use all nutrient IDs and goal types together
                System.out.println("Single profile detected - using combined nutrient IDs and goal types");
                
                List<Integer> allNutrientIds = goals.stream()
                    .map(NutritionGoal::getnutrientId)
                    .collect(Collectors.toList());
                List<GoalType> allGoalTypes = goals.stream()
                    .map(NutritionGoal::getgoalType)
                    .collect(Collectors.toList());
                
                System.out.println("All nutrient IDs: " + allNutrientIds);
                System.out.println("All goal types: " + allGoalTypes);
                
                NutrientProfile profile = idealProfiles.get(0);
                NutritionGoal firstGoal = goals.get(0);
                
                List<Integer> matchingIngredients = ingredientService.getIngredientMatchingNutrients(
                    profile.getAllNutrients(), 
                    limit * 2,
                    allNutrientIds, // Pass all nutrient IDs as list
                    allGoalTypes    // Pass all goal types as list
                );
                
                System.out.println("Found " + matchingIngredients.size() + " matching ingredients for single profile");
                
                // Score and process for single profile
                List<ScoredIngredient> scoredIngredientsForProfile = new ArrayList<>();
                int goalFoodGroup = ingredientService.getFoodGroup(firstGoal.getingredientId());
                
                for (int j = 0; j < matchingIngredients.size(); j++) {
                    Integer ingredientId = matchingIngredients.get(j);
                    double score = 1.0 - (j / (double) matchingIngredients.size());
                    
                    int ingredientFoodGroup = ingredientService.getFoodGroup(ingredientId);
                    if (ingredientFoodGroup == goalFoodGroup) {
                        score += FOOD_GROUP_BONUS;
                        System.out.println("  Ingredient " + ingredientId + " gets food group bonus (group: " + ingredientFoodGroup + ")");
                    }
                    
                    scoredIngredientsForProfile.add(new ScoredIngredient(ingredientId, score));
                    System.out.println("  Ingredient " + ingredientId + " scored: " + score);
                }
                
                List<Integer> profileRecommendations = scoredIngredientsForProfile.stream()
                    .sorted((a, b) -> Double.compare(b.score, a.score))
                    .limit(limit)
                    .map(si -> si.ingredientId)
                    .collect(Collectors.toList());
                
                allRecommendations.add(profileRecommendations);
                System.out.println("Single profile final recommendations: " + profileRecommendations);
                
            } else {
                // Multiple profiles strategy: Separate calls for each profile
                System.out.println("Multiple profiles detected - using separate calls for each profile");
                
                for (int i = 0; i < idealProfiles.size(); i++) {
                    NutrientProfile profile = idealProfiles.get(i);
                    NutritionGoal currentGoal = goals.get(i); // This mapping is correct since profiles are in same order as goals
                    System.out.println("Processing profile " + (i + 1) + " with " + 
                                     profile.getAllNutrients().size() + " nutrients" + " nutrientID in goal " + currentGoal.getnutrientId());
            
                    // Create separate lists for this profile's goal only
                    List<Integer> profileNutrientIds = new ArrayList<>();
                    profileNutrientIds.add(currentGoal.getnutrientId());
                    List<GoalType> profileGoalTypes = new ArrayList<>();
                    profileGoalTypes.add(currentGoal.getgoalType());
                    
                    System.out.println("  Profile " + (i + 1) + " nutrient IDs: " + profileNutrientIds);
                    System.out.println("  Profile " + (i + 1) + " goal types: " + profileGoalTypes);
            
                    // Get matching ingredients for this profile using only its own nutrient ID and goal type
                    List<Integer> matchingIngredients = ingredientService.getIngredientMatchingNutrients(
                        profile.getAllNutrients(), 
                        limit * 2, // Get more candidates to have better selection
                        profileNutrientIds, // Pass only this profile's nutrient ID as list
                        profileGoalTypes    // Pass only this profile's goal type as list
                    );
                    
                    System.out.println("  Found " + matchingIngredients.size() + " matching ingredients for profile " + (i + 1));
                    
                    // Score ingredients for this profile separately
                    List<ScoredIngredient> scoredIngredientsForProfile = new ArrayList<>();
                    int goalFoodGroup = ingredientService.getFoodGroup(currentGoal.getingredientId());
                    
                    for (int j = 0; j < matchingIngredients.size(); j++) {
                        Integer ingredientId = matchingIngredients.get(j);
                        
                        // Score based on ranking position (higher rank = lower index = higher score)
                        double score = 1.0 - (j / (double) matchingIngredients.size());
                        
                        // Apply food group bonus for this specific profile's goal
                        int ingredientFoodGroup = ingredientService.getFoodGroup(ingredientId);
                        if (ingredientFoodGroup == goalFoodGroup) {
                            score += FOOD_GROUP_BONUS;
                            System.out.println("  Ingredient " + ingredientId + " gets food group bonus for profile " + (i + 1) + " (group: " + ingredientFoodGroup + ")");
                        }
                        
                        scoredIngredientsForProfile.add(new ScoredIngredient(ingredientId, score));
                        System.out.println("  Profile " + (i + 1) + " - Ingredient " + ingredientId + " scored: " + score);
                    }
                    
                    // Sort this profile's scored ingredients and get top recommendations
                    List<Integer> profileRecommendations = scoredIngredientsForProfile.stream()
                        .sorted((a, b) -> Double.compare(b.score, a.score))
                        .limit(limit)
                        .map(si -> si.ingredientId)
                        .collect(Collectors.toList());
                    
                    allRecommendations.add(profileRecommendations);
                    System.out.println("  Profile " + (i + 1) + " final recommendations: " + profileRecommendations);
                }
            }
            
            System.out.println("\n--- Final Result ---");
            System.out.println("Returning " + allRecommendations.size() + " recommendation lists");
            for (int i = 0; i < allRecommendations.size(); i++) {
                System.out.println("Result " + (i + 1) + " recommendations: " + allRecommendations.get(i));
            }
            System.out.println("=== DEBUG: End getRecommendations ===\n");
            
            return allRecommendations;
            
        } catch (Exception e) {
            System.out.println("ERROR in getRecommendations: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    
    // Private nested class for internal scoring
    private static class ScoredIngredient {
        final int ingredientId;
        final double score;
        
        ScoredIngredient(int ingredientId, double score) {
            this.ingredientId = ingredientId;
            this.score = score;
        }
    }

}