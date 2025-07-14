package foodService;

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
    }
    
    // Method to extract ingredients from a list of Food objects
    public static List<Map<Integer, Double>> getIngredientsFromFoods(List<Food> foods) {
        List<Map<Integer, Double>> ingredientsList = new ArrayList<>();
        
        for (Food food : foods) {
            ingredientsList.add(food.getIngredients());
        }
        
        return ingredientsList;
    }
}