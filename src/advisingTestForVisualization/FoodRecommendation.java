package Advising;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ingredientService.IIngredientService;
import ingredientService.IngredientServiceFactory;
import nutriCalc.INutriCalc;
import nutriCalc.NutrientProfile;
import nutriCalc.NutritionFacade;

public class FoodRecommendation implements IFoodRecommendation{
    private static final double FOOD_GROUP_BONUS = 0.05;
    private INutriCalc nutritionCalculator = new NutritionFacade();;
    private IIngredientService ingredientService = IngredientServiceFactory.getService();
    
//    // Constructor injection - takes required dependencies
//    public FoodRecommendation(INutriCalc nutritionCalculator, 
//                             IIngredientService ingredientService) {
//        this.nutritionCalculator = nutritionCalculator;
//        this.ingredientService = ingredientService;
//    }
    
    // Main method - returns list of recommended ingredient IDs
    public List<Integer> getRecommendations(NutritionGoal goal) {
        return getRecommendations(goal, 10); // Default limit of 10
    }
    
    // Overloaded method with custom limit
    public List<Integer> getRecommendations(NutritionGoal goal, 
                                          int limit) {
        // 1. Create ideal nutrient profile for the goal
        NutrientProfile idealProfile = nutritionCalculator.createIdealIngredient(
            goal.getnutrientId(), 
            goal.getintensity(), 
            goal.getgoalType().ordinal()
        );
        
        // 2. Use IngredientService to find matching ingredients - it already does the scoring!
        List<Integer> matchingIngredients = ingredientService.getIngredientMatchingNutrients(
            idealProfile.getAllNutrients(), 
            limit
        );
        
        // 3. Apply food group bonus if needed
        List<ScoredIngredient> scoredIngredients = new ArrayList<>();
        int goalFoodGroup = ingredientService.getFoodGroup(goal.getingredientId());
        
        for (Integer ingredientId : matchingIngredients) {
            double score = 1.0; // Base score (IngredientService already ranked them)
            
            // Add bonus for same food group
            int ingredientFoodGroup = ingredientService.getFoodGroup(ingredientId);
            if (goalFoodGroup == ingredientFoodGroup) {
                score += FOOD_GROUP_BONUS;
            }
            
            scoredIngredients.add(new ScoredIngredient(ingredientId, score));
        }
        
        // 4. Re-sort with food group bonus and return
        return scoredIngredients.stream()
            .sorted((a, b) -> Double.compare(b.score, a.score))
            .map(si -> si.ingredientId)
            .collect(Collectors.toList());
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
