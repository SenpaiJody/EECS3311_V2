package nutriCalc;

import java.time.LocalDate;
import java.util.*;
import food.FoodType;
import food.Lunch;
import food.Breakfast;
import food.Food;
import nutriCalc.INutriCalc;
import nutriCalc.NutritionFacade;
import recommendation.FoodRecommendation;
import recommendation.GoalType;
import recommendation.IFoodRecommendation;
import recommendation.INutritionGoalManager;
import recommendation.NutritionGoal;
import recommendation.NutritionGoalManager;
import nutriCalc.NutrientProfile;

public class Main {
    public static void main(String[] args) {
        // Create some sample foods
        List<Food> foods = new ArrayList<>();

        // Sample ingredients
        Map<Integer, Double> pancakeIngredients = new HashMap<>();
        pancakeIngredients.put(567, 100.0);
//        pancakeIngredients.put(2, 250.0);
//        pancakeIngredients.put(3, 2.0);

        Map<Integer, Double> saladIngredients = new HashMap<>();
        saladIngredients.put(733, 100.0);
//        saladIngredients.put(5, 50.0);
//        saladIngredients.put(6, 30.0);

        Breakfast breakfast = new Breakfast();
        Lunch lunch = new Lunch();

        // Create Food objects
        foods.add(new Food(1, "Pancakes", pancakeIngredients, LocalDate.now(), breakfast));
        foods.add(new Food(2, "Garden Salad", saladIngredients, LocalDate.now(), lunch));

        System.out.println("Food List:");
        for (Food food : foods) {
            System.out.println(food);
            System.out.println("---");
        }

        // Get ingredients from foods
        System.out.println("\nIngredients from foods:");
        List<Map<Integer, Double>> ingredientsList = getIngredientsFromFoods(foods);

        for (int i = 0; i < foods.size(); i++) {
            System.out.println("Food " + (i+1) + " ingredients: " + ingredientsList.get(i));
        }

        // Create nutrition calculator
        INutriCalc nutritionCalc = new NutritionFacade();

        // Demo 1: Calculate nutrition for individual ingredients
        System.out.println("\n=== Calculate Nutrition for Individual Ingredients ===");
        NutrientProfile pancakeProfile = nutritionCalc.calculateNutritionProfiles(pancakeIngredients);
        printNutrientProfile(pancakeProfile, "Pancakes");

        NutrientProfile saladProfile = nutritionCalc.calculateNutritionProfiles(saladIngredients);
        printNutrientProfile(saladProfile, "Salad");

//        // Demo 2: Calculate nutrition for Food objects
//        System.out.println("\n=== Calculate Nutrition for Food Objects ===");
//        NutrientProfile allFoodsProfile = nutritionCalc.calculateNutritionProfiles(foods);
//        printNutrientProfile(allFoodsProfile, "All Foods");
////
////        // Demo 3: Calculate nutrition from ingredient maps
////        System.out.println("\n=== Calculate Nutrition from Ingredient Maps ===");
////        NutrientProfile fromMaps = nutritionCalc.calculateNutritionProfilesFromMaps(ingredientsList);
////        printNutrientProfile(fromMaps, "From Maps");
//
//        // Demo 4: Combine nutrition profiles
//        System.out.println("\n=== Combine Nutrition Profiles ===");
//        List<NutrientProfile> profiles = new ArrayList<>();
//        profiles.add(pancakeProfile);
//        profiles.add(saladProfile);
//        
//        NutrientProfile combinedProfile = nutritionCalc.combineNutritionProfiles(profiles);
//        printNutrientProfile(combinedProfile, "Combined");
//
//        // Demo 5: Calculate nutrient differences
//        System.out.println("\n=== Calculate Nutrient Differences ===");
//        List<Food> pancakeList = Arrays.asList(foods.get(0));
//        List<Food> saladList = Arrays.asList(foods.get(1));
//        
//        Map<Integer, Double> differences = nutritionCalc.calculateNutrientDifference(pancakeList, saladList);
//        System.out.println("Differences (Pancakes - Salad):");
//        for (Map.Entry<Integer, Double> entry : differences.entrySet()) {
//            System.out.println("  Nutrient " + entry.getKey() + ": " + entry.getValue());
//        }
//        
//        
//        // Demo 5: NUTRIENT PROFILE BEFORE/AFTER IDEAL PROFILE CREATION
//        System.out.println("\n\n=== NUTRIENT PROFILE BEFORE/AFTER IDEAL PROFILE CREATION ===");
//        
//        // Get original nutrient profile for ingredient 67 (100g)
//        Map<Integer, Double> originalIngredient = new HashMap<>();
//        originalIngredient.put(67, 100.0); // 100g of ingredient 67
//        NutrientProfile originalProfile = nutritionCalc.calculateNutritionProfiles(originalIngredient);
//        printNutrientProfile(originalProfile, "ORIGINAL Profile for Ingredient 67 (100g)");
//
//        // Set up goal manager and recommendation system
//        INutritionGoalManager goalManager = new NutritionGoalManager();
//        IFoodRecommendation recommendationSystem = new FoodRecommendation();
//        goalManager.addGoalChangeListener(recommendationSystem);
//
//        // Create and add DECREASE goal
//        System.out.println("\n--- Creating DECREASE Goal ---");
//        NutritionGoal decreaseGoal = goalManager.createGoal(
//            123,           // profileId
//            401,           // nutrientId (e.g., protein)
//            60,            // intensity (1-100)
//            GoalType.DECREASE, // goalType
//            67             // ingredientId
//        );
//
//        boolean decreaseAdded = goalManager.addGoal(123, decreaseGoal);
//        System.out.println("DECREASE Goal added: " + decreaseAdded);
//        System.out.println("DECREASE Goal ID: " + decreaseGoal.getgoalId());
//
//        // Get recommendations for DECREASE goal
//        System.out.println("\n=== Recommendations after DECREASE Goal ===");
//        try {
//            List<List<Integer>> decreaseRecs = recommendationSystem.getLatestRecommendations(123);
//            if (decreaseRecs != null && !decreaseRecs.isEmpty()) {
//                for (int i = 0; i < decreaseRecs.size(); i++) {
//                    System.out.println("DECREASE Goal " + (i + 1) + " recommendations: " + decreaseRecs.get(i));
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("Error getting DECREASE recommendations: " + e.getMessage());
//        }
//
//        // Create and add INCREASE goal
//        System.out.println("\n--- Creating INCREASE Goal ---");
//        NutritionGoal increaseGoal = goalManager.createGoal(
//            124,           // profileId
//            401,           // nutrientId (e.g., protein)
//            60,            // intensity (1-100)
//            GoalType.INCREASE, // goalType
//            67             // ingredientId
//        );
//
//        boolean increaseAdded = goalManager.addGoal(124, increaseGoal);
//        System.out.println("INCREASE Goal added: " + increaseAdded);
//        System.out.println("INCREASE Goal ID: " + increaseGoal.getgoalId());
//
//        // Get recommendations for INCREASE goal
//        System.out.println("\n=== Recommendations after INCREASE Goal ===");
//        try {
//            List<List<Integer>> increaseRecs = recommendationSystem.getLatestRecommendations(124);
//            if (increaseRecs != null && !increaseRecs.isEmpty()) {
//                for (int i = 0; i < increaseRecs.size(); i++) {
//                    System.out.println("INCREASE Goal " + (i + 1) + " recommendations: " + increaseRecs.get(i));
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("Error getting INCREASE recommendations: " + e.getMessage());
//        }
//
//        // Compare results
//        System.out.println("\n=== COMPARISON SUMMARY ===");
//        System.out.println("Original ingredient 67 (100g) profile calculated above");
//        System.out.println("DECREASE goal should produce modified profiles with LESS nutrient 401");
//        System.out.println("INCREASE goal should produce modified profiles with MORE nutrient 401");
//        System.out.println("If both goals produce the same recommendations, there's a bug in the signing logic!");
//        
//        
//        
//        
//        // Demo 3: Calculate nutrition from ingredient maps
//        System.out.println("\n=== Calculate Nutrition from Ingredient Maps ===");
//        NutrientProfile fromMaps = nutritionCalc.calculateNutritionProfilesFromMaps(ingredientsList);
//        printNutrientProfile(fromMaps, "From Maps");

        // Demo 4: Combine nutrition profiles
        System.out.println("\n=== Combine Nutrition Profiles ===");
        List<NutrientProfile> profiles = new ArrayList<>();
        profiles.add(pancakeProfile);
        profiles.add(saladProfile);
        
        NutrientProfile combinedProfile = nutritionCalc.combineNutritionProfiles(profiles);
        printNutrientProfile(combinedProfile, "Combined");

        // Demo 5: Calculate nutrient differences
        System.out.println("\n=== Calculate Nutrient Differences ===");
        List<Food> pancakeList = Arrays.asList(foods.get(0));
        List<Food> saladList = Arrays.asList(foods.get(1));
        
        Map<Integer, Double> differences = nutritionCalc.calculateNutrientDifference(pancakeList, saladList);
        System.out.println("Differences (Pancakes - Salad):");
        for (Map.Entry<Integer, Double> entry : differences.entrySet()) {
            System.out.println("  Nutrient " + entry.getKey() + ": " + entry.getValue());
        }
//
//        // Demo 6: Create ideal ingredient
//        System.out.println("\n=== Create Ideal Ingredient ===");
//        NutrientProfile idealIngredient = nutritionCalc.createIdealIngredient(4, 208, 25);
//        printNutrientProfile(idealIngredient, "Ideal Ingredient (ID 1, nutrient 1, +25%)");
    }

    // Method to extract ingredients from a list of Food objects
    public static List<Map<Integer, Double>> getIngredientsFromFoods(List<Food> foods) {
        List<Map<Integer, Double>> ingredientsList = new ArrayList<>();

        for (Food food : foods) {
            ingredientsList.add(food.getIngredients());
        }

        return ingredientsList;
    }

    // Static method to print nutrient map from NutrientProfile
    public static void printNutrientProfile(NutrientProfile profile, String name) {
        System.out.println(name + " nutrition:");
        if (profile != null && profile.getAllNutrients() != null) {
            for (Map.Entry<Integer, Double> entry : profile.getAllNutrients().entrySet()) {
                System.out.println("  Nutrient " + entry.getKey() + ": " + entry.getValue());
            }
        } else {
            System.out.println("  No nutrition data available");
        }
        System.out.println();
    }
}