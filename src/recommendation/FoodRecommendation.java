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


/**
 * Implementation of food recommendation system that provides ingredient suggestions
 * based on nutrition goals. The system supports both single and multiple goal scenarios,
 * maintains cached recommendations per profile, and applies food group bonuses for scoring.
 */

//Step 4: Updated FoodRecommendation class using Strategy Pattern
public class FoodRecommendation implements IFoodRecommendation {
 private INutriCalc nutritionCalculator = new NutritionFacade();
 private IIngredientService ingredientService = IngredientServiceFactory.getService();
 
 // Strategy instances
 private final IRecommendationStrategy singleProfileStrategy = new SingleProfileStrategy();
 private final IRecommendationStrategy multipleProfileStrategy = new MultipleProfileStrategy();
 
 private Map<Integer, Map<Integer, Integer>> ingredientIndexMap = new HashMap<>();
 private Map<Integer, List<List<Integer>>> latestRecommendations = new HashMap<>();
 
 @Override
 public void onGoalChanged(Integer profileId, List<NutritionGoal> updatedGoals) {
     if (updatedGoals.isEmpty()) {
         latestRecommendations.remove(profileId);
         ingredientIndexMap.remove(profileId);
         return;
     }

     Map<Integer, List<NutritionGoal>> goalsByIngredient = updatedGoals.stream()
         .collect(Collectors.groupingBy(NutritionGoal::getingredientId));

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
             int index = ingredientToIndex.get(ingredientId);
             recList.set(index, newRec);
         } else {
             recList.add(newRec);
             ingredientToIndex.put(ingredientId, recList.size() - 1);
         }
     }
 }
 
 @Override
 public List<List<Integer>> getLatestRecommendations(int profileId) {
     return latestRecommendations.getOrDefault(profileId, new ArrayList<>());
 }
 
 public List<List<Integer>> getRecommendations(List<NutritionGoal> goals) {
     return getRecommendations(goals, 4);
 }
 
 // MUCH SIMPLIFIED main method using Strategy Pattern
 private List<List<Integer>> getRecommendations(List<NutritionGoal> goals, int limit) {
//     System.out.println("=== DEBUG: Starting getRecommendations with " + goals.size() + " goals ===");
     
     try {
         // Create ideal nutrient profiles
         List<NutrientProfile> idealProfiles = nutritionCalculator.createIdealIngredient(goals);
//         System.out.println("Number of ideal profiles created: " + idealProfiles.size());
         
         if (idealProfiles.isEmpty()) {
//             System.out.println("ERROR: No ideal profiles created!");
             return new ArrayList<>();
         }
         
         // Choose strategy based on profile count and delegate
         IRecommendationStrategy strategy = (idealProfiles.size() == 1) 
             ? singleProfileStrategy 
             : multipleProfileStrategy;
         
         List<List<Integer>> result = strategy.getRecommendations(goals, idealProfiles, limit, ingredientService);
//         
//         System.out.println("\n--- Final Result ---");
//         System.out.println("Returning " + result.size() + " recommendation lists");
//         System.out.println("=== DEBUG: End getRecommendations ===\n");
         
         return result;
         
     } catch (Exception e) {
         System.out.println("ERROR in getRecommendations: " + e.getMessage());
         e.printStackTrace();
         return new ArrayList<>();
     }
 }
}
