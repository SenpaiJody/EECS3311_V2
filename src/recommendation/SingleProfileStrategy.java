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

public class SingleProfileStrategy implements IRecommendationStrategy {
    private static final double FOOD_GROUP_BONUS = 0.05;
    
    @Override
    public List<List<Integer>> getRecommendations(
        List<NutritionGoal> goals, 
        List<NutrientProfile> idealProfiles, 
        int limit,
        IIngredientService ingredientService
    ) {
        System.out.println("Single profile detected - using combined nutrient IDs and goal types");
        
        // Extract all nutrient IDs and goal types together
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
        
        // Get matching ingredients using combined approach
        List<Integer> matchingIngredients = ingredientService.getIngredientMatchingNutrients(
            profile.getAllNutrients(), 
            limit * 2,
            allNutrientIds, // Pass all nutrient IDs as list
            allGoalTypes    // Pass all goal types as list
        );
        
        System.out.println("Found " + matchingIngredients.size() + " matching ingredients for single profile");
        
        // Score ingredients
        List<ScoredIngredient> scoredIngredients = scoreIngredients(
            matchingIngredients, 
            ingredientService.getFoodGroup(firstGoal.getingredientId()),
            ingredientService
        );
        
        // Get final recommendations
        List<Integer> recommendations = scoredIngredients.stream()
            .sorted((a, b) -> Double.compare(b.score, a.score))
            .limit(limit)
            .map(si -> si.ingredientId)
            .collect(Collectors.toList());
        
        List<List<Integer>> result = new ArrayList<>();
        result.add(recommendations);
        
        System.out.println("Single profile final recommendations: " + recommendations);
        return result;
    }
    
    private List<ScoredIngredient> scoreIngredients(
        List<Integer> matchingIngredients, 
        int goalFoodGroup,
        IIngredientService ingredientService
    ) {
        List<ScoredIngredient> scoredIngredients = new ArrayList<>();
        
        for (int j = 0; j < matchingIngredients.size(); j++) {
            Integer ingredientId = matchingIngredients.get(j);
            double score = 1.0 - (j / (double) matchingIngredients.size());
            
            int ingredientFoodGroup = ingredientService.getFoodGroup(ingredientId);
            if (ingredientFoodGroup == goalFoodGroup) {
                score += FOOD_GROUP_BONUS;
                System.out.println("  Ingredient " + ingredientId + " gets food group bonus (group: " + ingredientFoodGroup + ")");
            }
            
            scoredIngredients.add(new ScoredIngredient(ingredientId, score));
            System.out.println("  Ingredient " + ingredientId + " scored: " + score);
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

