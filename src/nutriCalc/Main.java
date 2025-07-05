package nutriCalc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Main class demonstrating the usage of the nutrition calculation system
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Nutrition Calculation Demo ===\n");
        
        // Use interface reference for better design
        INutriCalc nutritionCalculator = new NutritionFacade();
        
        // Demo 1: Calculate nutrition for a recipe
        System.out.println("Demo 1: Recipe Nutrition");
        List<List<Object>> recipe = Arrays.asList(
            Arrays.asList(4, 200.0) // 200g of ingredient 4
            // Arrays.asList(2, 150.0)   // 150g of ingredient 2
        );
        
        NutrientProfile profile = nutritionCalculator.calculateNutritionProfiles(recipe);
        printNutrientProfile("Recipe", profile);
        
        // Demo 2: Combine two recipes
        System.out.println("\nDemo 2: Combining Recipes");
        List<List<Object>> recipe1 = Arrays.asList(Arrays.asList(4, 100.0));
        List<List<Object>> recipe2 = Arrays.asList(Arrays.asList(4, 100.0));
        
        NutrientProfile profile1 = nutritionCalculator.calculateNutritionProfiles(recipe1);
        NutrientProfile profile2 = nutritionCalculator.calculateNutritionProfiles(recipe2);
        
        List<NutrientProfile> profiles = Arrays.asList(profile1, profile2);
        NutrientProfile combined = nutritionCalculator.combineNutritionProfiles(profiles);
        printNutrientProfile("Combined", combined);
        
        // Demo 3: Calculate difference between recipes
        System.out.println("\nDemo 3: Recipe Comparison");
        Map<Integer, Double> differences = nutritionCalculator.calculateNutrientDifference(recipe, recipe1);
        printNutrientDifferences("Recipe vs Recipe1", differences);
        
        System.out.println("\n=== Demo Complete ===");
    }
    
    /**
     * Helper method to print nutrient profile in a clean format
     */
    private static void printNutrientProfile(String title, NutrientProfile profile) {
        System.out.println(title + " Nutrition:");
        Map<Integer, Double> nutrients = profile.getAllNutrients();
        
        if (nutrients.isEmpty()) {
            System.out.println("  No nutrition data available");
        } else {
            nutrients.forEach((nutrientId, value) -> 
                System.out.printf("  Nutrient ID %d: %.2f%n", nutrientId, value));
        }
    }
    
    /**
     * Helper method to print nutrient differences in a clean format
     */
    private static void printNutrientDifferences(String title, Map<Integer, Double> differences) {
        System.out.println(title + " Differences:");
        
        if (differences.isEmpty()) {
            System.out.println("  No differences found");
        } else {
            differences.forEach((nutrientId, difference) -> 
                System.out.printf("  Nutrient ID %d: %+.2f%n", nutrientId, difference));
        }
    }
}