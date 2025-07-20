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
    
    private Map<Integer, List<List<Integer>>> latestRecommendations = new HashMap<>();
    
    
 // Automatically triggered on goal changes
    @Override
    public void onGoalChanged(Integer profileId, List<NutritionGoal> updatedGoals) {
//        System.out.println("\n[INFO] Goals updated for profile " + profileId);
//        System.out.println("[INFO] Number of updated goals: " + updatedGoals.size());
        
        if (updatedGoals.isEmpty()) {
            // Clear recommendations if no goals
            latestRecommendations.remove(profileId);
            System.out.println("[INFO] Cleared recommendations for profile " + profileId);
        } else {
            // Auto-recalculate recommendations
            List<List<Integer>> recommendations = getRecommendations(updatedGoals);
            latestRecommendations.put(profileId, recommendations);
            System.out.println("[INFO] Updated recommendations for profile " + profileId);
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
            
            // 2. Extract nutrient IDs and goal types from all goals for the new method signature
            System.out.println("\n--- Step 2: Extracting nutrient IDs and goal types from all goals ---");
            List<Integer> allNutrientIds = goals.stream()
                .map(NutritionGoal::getnutrientId)
                .collect(Collectors.toList());
            List<GoalType> allGoalTypes = goals.stream()
                .map(NutritionGoal::getgoalType)
                .collect(Collectors.toList());
            
            System.out.println("All nutrient IDs: " + allNutrientIds);
            System.out.println("All goal types: " + allGoalTypes);
            
            // 3. Get matching ingredients for each profile and score them separately
            System.out.println("\n--- Step 3: Finding and scoring matching ingredients for each profile separately ---");
            List<List<Integer>> allRecommendations = new ArrayList<>();
            
            for (int i = 0; i < idealProfiles.size(); i++) {
                NutrientProfile profile = idealProfiles.get(i);
                NutritionGoal currentGoal = goals.get(i);
                System.out.println("Processing profile " + (i + 1) + " with " + 
                                 profile.getAllNutrients().size() + " nutrients" + " nutrientID in goal " + currentGoal.getnutrientId());
        
                // Get matching ingredients for this profile using the new method signature
                List<Integer> matchingIngredients = ingredientService.getIngredientMatchingNutrients(
                    profile.getAllNutrients(), 
                    limit * 2, // Get more candidates to have better selection
                    allNutrientIds, // Pass all nutrient IDs as list
                    allGoalTypes    // Pass all goal types as list
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
            
            System.out.println("\n--- Final Result ---");
            System.out.println("Returning " + allRecommendations.size() + " separate recommendation lists");
            for (int i = 0; i < allRecommendations.size(); i++) {
                System.out.println("Goal " + (i + 1) + " recommendations: " + allRecommendations.get(i));
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