package nutriCalc;

import java.time.LocalDate;
import java.util.*;
import food.FoodType;
import food.Lunch;
import food.Breakfast;
import food.Food;
import nutriCalc.INutriCalc;
import nutriCalc.NutritionFacade;
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