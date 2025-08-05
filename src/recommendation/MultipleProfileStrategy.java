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

public class MultipleProfileStrategy implements IRecommendationStrategy {
    private static final double FOOD_GROUP_BONUS = 0.05;
    
    @Override
    public List<List<Integer>> getRecommendations(List<NutritionGoal> goals, List<NutrientProfile> idealProfiles, int limit,
        IIngredientService ingredientService
    ) {
        System.out.println("Multiple profiles detected - using separate calls for each profile");
        
        List<List<Integer>> allRecommendations = new ArrayList<>();
        
        for (int i = 0; i < idealProfiles.size(); i++) {
            NutrientProfile profile = idealProfiles.get(i);
            NutritionGoal currentGoal = goals.get(i);
            
            System.out.println("Processing profile " + (i + 1) + " with " + 
                             profile.getAllNutrients().size() + " nutrients" + 
                             " nutrientID in goal " + currentGoal.getnutrientId());
    
            // Create separate lists for this profile's goal only
            List<Integer> profileNutrientIds = new ArrayList<>();
            profileNutrientIds.add(currentGoal.getnutrientId());
            List<GoalType> profileGoalTypes = new ArrayList<>();
            profileGoalTypes.add(currentGoal.getgoalType());
            
            System.out.println("  Profile " + (i + 1) + " nutrient IDs: " + profileNutrientIds);
            System.out.println("  Profile " + (i + 1) + " goal types: " + profileGoalTypes);
    
            // Get matching ingredients for this profile
            List<Integer> matchingIngredients = ingredientService.getIngredientMatchingNutrients(
                profile.getAllNutrients(), 
                limit * 2,
                profileNutrientIds, // Pass only this profile's nutrient ID as list
                profileGoalTypes    // Pass only this profile's goal type as list
            );
            
            System.out.println("  Found " + matchingIngredients.size() + " matching ingredients for profile " + (i + 1));
            
            // Score ingredients for this profile
            List<ScoredIngredient> scoredIngredients = scoreIngredients(
                matchingIngredients,
                ingredientService.getFoodGroup(currentGoal.getingredientId()),
                ingredientService,
                i + 1 // for logging
            );
            
            // Get final recommendations for this profile
            List<Integer> profileRecommendations = scoredIngredients.stream()
                .sorted((a, b) -> Double.compare(b.score, a.score))
                .limit(limit)
                .map(si -> si.ingredientId)
                .collect(Collectors.toList());
            
            allRecommendations.add(profileRecommendations);
            System.out.println("  Profile " + (i + 1) + " final recommendations: " + profileRecommendations);
        }
        
        return allRecommendations;
    }
    
    private List<ScoredIngredient> scoreIngredients(
        List<Integer> matchingIngredients, 
        int goalFoodGroup,
        IIngredientService ingredientService,
        int profileNumber
    ) {
        List<ScoredIngredient> scoredIngredients = new ArrayList<>();
        
        for (int j = 0; j < matchingIngredients.size(); j++) {
            Integer ingredientId = matchingIngredients.get(j);
            double score = 1.0 - (j / (double) matchingIngredients.size());
            
            int ingredientFoodGroup = ingredientService.getFoodGroup(ingredientId);
            if (ingredientFoodGroup == goalFoodGroup) {
                score += FOOD_GROUP_BONUS;
                System.out.println("  Ingredient " + ingredientId + " gets food group bonus for profile " + 
                                 profileNumber + " (group: " + ingredientFoodGroup + ")");
            }
            
            scoredIngredients.add(new ScoredIngredient(ingredientId, score));
            System.out.println("  Profile " + profileNumber + " - Ingredient " + ingredientId + " scored: " + score);
        }
        
        return scoredIngredients;
    }
    
    // Inner class for scoring (can be moved to shared utility later)
    private static class ScoredIngredient {
        final int ingredientId;
        final double score;
        
        ScoredIngredient(int ingredientId, double score) {
            this.ingredientId = ingredientId;
            this.score = score;
        }
    }
}
