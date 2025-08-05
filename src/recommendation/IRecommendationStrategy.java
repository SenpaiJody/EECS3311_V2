package recommendation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import ingredientService.IIngredientService;

import nutriCalc.NutrientProfile;

public interface IRecommendationStrategy {
    List<List<Integer>> getRecommendations(
        List<NutritionGoal> goals, 
        List<NutrientProfile> idealProfiles, 
        int limit,
        IIngredientService ingredientService
    );
}
