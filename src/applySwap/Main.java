package applySwap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.util.*;
import food.FoodType;
import food.Lunch;
import food.Breakfast;
import food.Food;

public class Main {
    public static void main(String[] args) {
        // Create some sample foods
        List<Food> foods = new ArrayList<>();
        
        // Sample ingredients
        Map<Integer, Double> pancakeIngredients = new HashMap<>();
        pancakeIngredients.put(1, 200.0);
        pancakeIngredients.put(2, 250.0); 
        pancakeIngredients.put(3, 2.0);
        
        Map<Integer, Double> saladIngredients = new HashMap<>();
        saladIngredients.put(4, 100.0); 
        saladIngredients.put(5, 50.0);  
        saladIngredients.put(6, 30.0);  
        
        Map<Integer, Double> omeletteIngredients = new HashMap<>();
        omeletteIngredients.put(3, 3.0);
        omeletteIngredients.put(7, 50.0);
        omeletteIngredients.put(8, 20.0);
        
        Breakfast breakfast = new Breakfast();
        Lunch lunch = new Lunch();
        
        // Create Food objects
        foods.add(new Food(1, "Pancakes", pancakeIngredients, LocalDate.now(), breakfast));
        foods.add(new Food(2, "Garden Salad", saladIngredients, LocalDate.now(), lunch));
        foods.add(new Food(3, "Cheese Omelette", omeletteIngredients, LocalDate.now(), breakfast));
        
        System.out.println("=== ORIGINAL FOOD LIST ===");
        printFoodList(foods);
        
        // Get ingredients from foods
        System.out.println("\n=== INGREDIENTS FROM FOODS ===");
        List<Map<Integer, Double>> ingredientsList = getIngredientsFromFoods(foods);
        
        for (int i = 0; i < foods.size(); i++) {
            System.out.println("Food " + (i+1) + " ingredients: " + ingredientsList.get(i));
        }
        
        // Create ApplySwap instance
        ApplySwap applySwap = new ApplySwap();
        
        // Demo 1: Single ingredient swap
        System.out.println("\n=== DEMO 1: Single Ingredient Swap ===");
        System.out.println("Swapping ingredient 1 (flour) with ingredient 101 (almond flour)");
        
        List<Integer> oldIngredients1 = Arrays.asList(1);
        List<Integer> newIngredients1 = Arrays.asList(101);
        
        List<Food> singleSwapResult = applySwap.applySwaps(newIngredients1, oldIngredients1, foods);
        printFoodList(singleSwapResult);
        
        // Demo 2: Multiple ingredient swaps
        System.out.println("\n=== DEMO 2: Multiple Ingredient Swaps ===");
        System.out.println("Swapping multiple ingredients:");
        System.out.println("  - ingredient 2 (milk) -> ingredient 102 (almond milk)");
        System.out.println("  - ingredient 3 (eggs) -> ingredient 103 (egg substitute)");
        System.out.println("  - ingredient 7 (cheese) -> ingredient 107 (vegan cheese)");
        
        List<Integer> oldIngredients2 = Arrays.asList(2, 3, 7);
        List<Integer> newIngredients2 = Arrays.asList(102, 103, 107);
        
        List<Food> multipleSwapResult = applySwap.applySwaps(newIngredients2, oldIngredients2, foods);
        printFoodList(multipleSwapResult);

    }
    
    /**
     * Method to extract ingredients from a list of Food objects
     * @param foods List of Food objects
     * @return List of ingredient maps
     */
    public static List<Map<Integer, Double>> getIngredientsFromFoods(List<Food> foods) {
        List<Map<Integer, Double>> ingredientsList = new ArrayList<>();
        
        for (Food food : foods) {
            ingredientsList.add(food.getIngredients());
        }
        
        return ingredientsList;
    }
    
    /**
     * Utility method to print the food list in a formatted way
     * @param foods List of foods to print
     */
    private static void printFoodList(List<Food> foods) {
        for (Food food : foods) {
            System.out.println(food);
            System.out.println("---");
        }
    }
}