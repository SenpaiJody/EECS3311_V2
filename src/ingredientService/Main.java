package ingredientService;

import nutriCalc.NutritionFacade;
import nutriCalc.NutrientProfile;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Step 1: Create an instance of NutritionFacade
        NutritionFacade nutritionFacade = new NutritionFacade();

        // Step 2: Define a list of [ingredientId, quantity] pairs
        List<List<Object>> ingredients = new ArrayList<>();
        ingredients.add(Arrays.asList(1, 50.0));  // 50g of ingredient with ID 1
        ingredients.add(Arrays.asList(2, 75.0));  // 75g of ingredient with ID 2

        // Step 3: Use NutritionFacade to get the nutrient profile
        NutrientProfile profile = nutritionFacade.calculateNutritionProfiles(ingredients);

        // Step 4: Extract nutrient map from the profile
        Map<Integer, Double> nutrientMap = profile.getAllNutrients();

        // Step 5: Get ingredient matches based on the nutrient profile
        IIngredientService ingredientService = IngredientServiceFactory.getService();  // Must return a valid instance
        List<Integer> matchedIngredients = ingredientService.getIngredientMatchingNutrients(nutrientMap, 4);

        // Step 6: Print the matched ingredient IDs
        System.out.println("Matched Ingredient IDs: " + matchedIngredients);
    }
}