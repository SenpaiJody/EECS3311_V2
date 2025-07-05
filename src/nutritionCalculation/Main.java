package nutritionCalculation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
     * Main method demonstrating the usage of the nutrition calculation system
     */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Nutrition Calculation Demo ===\n");
        
        NutritionFacade facade = new NutritionFacade();
        
        // Case 1: Simple recipe calculation
        System.out.println("Case 1: Recipe with 200g ingredient 1 + 150g ingredient 2");
        List<List<Object>> recipe = Arrays.asList(
            Arrays.asList(4, 200.0),
            Arrays.asList(2, 150.0)
        );
        
        try {
            NutrientProfile profile = facade.calculateNutritionProfiles(recipe);
            System.out.println("Nutrition Profile:");
            Map<Integer, Double> nutrients = profile.getAllNutrients();
            if (nutrients.isEmpty()) {
                System.out.println("  No nutrition data available");
            } else {
                for (Map.Entry<Integer, Double> entry : nutrients.entrySet()) {
                    System.out.printf("  Nutrient ID %d: %.2f%n", 
                                    entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        System.out.println();
        
        // Case 2: Combining two profiles
        System.out.println("Case 2: Combining two separate recipes");
        List<List<Object>> recipe1 = Arrays.asList(Arrays.asList(1, 100.0));
        List<List<Object>> recipe2 = Arrays.asList(Arrays.asList(2, 100.0));
        
        try {
            NutrientProfile profile1 = facade.calculateNutritionProfiles(recipe1);
            NutrientProfile profile2 = facade.calculateNutritionProfiles(recipe2);
            
            List<NutrientProfile> profiles = Arrays.asList(profile1, profile2);
            NutrientProfile combined = facade.combineNutritionProfiles(profiles);
            
            System.out.println("Combined Nutrition Profile:");
            Map<Integer, Double> nutrients = combined.getAllNutrients();
            if (nutrients.isEmpty()) {
                System.out.println("  No nutrition data available");
            } else {
                for (Map.Entry<Integer, Double> entry : nutrients.entrySet()) {
                    System.out.printf("  Nutrient ID %d: %.2f%n", 
                                    entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        System.out.println("\n=== Demo Complete ===");
    }
}