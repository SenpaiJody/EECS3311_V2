package Advising;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Demo class to show Command pattern working
 */
public class DemoMain {
    public static void main(String[] args) {
        System.out.println("=== Food Swap Command Pattern Demo ===\n");
        
        // Create sample ingredients: [ingredientId, quantity]
        List<List<Object>> originalIngredients = createSampleIngredients();
        System.out.println("Original ingredients:");
        printIngredients(originalIngredients);
        
        // Create command-based food swap
        CommandBasedFoodSwap foodSwap = new CommandBasedFoodSwap();
        
        // Execute first swap: replace ingredient 101 with 201
        System.out.println("\n--- Executing Swap 1: Replace ingredient 101 with 201 ---");
        SwapResult result1 = foodSwap.applySwap(101, 201, originalIngredients);
        printSwapResult(result1, "Swap 1 Result");
        
        // Execute second swap: replace ingredient 102 with 202 (using result from first swap)
        System.out.println("\n--- Executing Swap 2: Replace ingredient 102 with 202 ---");
        SwapResult result2 = foodSwap.applySwap(102, 202, result1.getNewIngredients());
        printSwapResult(result2, "Swap 2 Result");
        
        // Show command history
        System.out.println("\n--- Command History ---");
        List<String> history = foodSwap.getSwapHistory();
        for (int i = 0; i < history.size(); i++) {
            System.out.println((i + 1) + ". " + history.get(i));
        }
        
        // Undo last command
        System.out.println("\n--- Undoing Last Swap ---");
        SwapResult undoResult = foodSwap.undoLastSwap();
        if (undoResult != null) {
            printSwapResult(undoResult, "Undo Result");
        } else {
            System.out.println("No commands to undo");
        }
        
        // Undo another command
        System.out.println("\n--- Undoing Another Swap ---");
        SwapResult undoResult2 = foodSwap.undoLastSwap();
        if (undoResult2 != null) {
            printSwapResult(undoResult2, "Second Undo Result");
        } else {
            System.out.println("No more commands to undo");
        }
        
        // Try to undo when no commands left
        System.out.println("\n--- Trying to Undo Again ---");
        SwapResult undoResult3 = foodSwap.undoLastSwap();
        if (undoResult3 != null) {
            printSwapResult(undoResult3, "Third Undo Result");
        } else {
            System.out.println("No commands left to undo");
        }
        
        // Redo commands
        System.out.println("\n--- Redoing Commands ---");
        SwapResult redoResult1 = foodSwap.redoLastSwap();
        if (redoResult1 != null) {
            printSwapResult(redoResult1, "First Redo Result");
        } else {
            System.out.println("No commands to redo");
        }
        
        SwapResult redoResult2 = foodSwap.redoLastSwap();
        if (redoResult2 != null) {
            printSwapResult(redoResult2, "Second Redo Result");
        } else {
            System.out.println("No more commands to redo");
        }
        
        // Final command history
        System.out.println("\n--- Final Command History ---");
        List<String> finalHistory = foodSwap.getSwapHistory();
        for (int i = 0; i < finalHistory.size(); i++) {
            System.out.println((i + 1) + ". " + finalHistory.get(i));
        }
        
        // Clear history
        System.out.println("\n--- Clearing History ---");
        foodSwap.clearSwapHistory();
        System.out.println("History cleared. Current history size: " + foodSwap.getSwapHistory().size());
    }
    
    /**
     * Creates sample ingredients for demonstration
     * @return List of sample ingredients
     */
    private static List<List<Object>> createSampleIngredients() {
        List<List<Object>> ingredients = new ArrayList<>();
        ingredients.add(Arrays.asList(101, 150.0));  // Ingredient 101, 150g
        ingredients.add(Arrays.asList(102, 200.0));  // Ingredient 102, 200g  
        ingredients.add(Arrays.asList(103, 75.0));   // Ingredient 103, 75g
        return ingredients;
    }
    
    /**
     * Prints swap result information
     * @param result The swap result to print
     * @param title Title for the output
     */
    private static void printSwapResult(SwapResult result, String title) {
        System.out.println(title + ":");
        
        System.out.println("  New Ingredients:");
        printIngredients(result.getNewIngredients());
        
        System.out.println("  Nutrient Differences:");
        Map<Integer, Double> differences = result.getNutrientDifferences();
        if (differences.isEmpty()) {
            System.out.println("    No nutrient differences found");
        } else {
            for (Map.Entry<Integer, Double> entry : differences.entrySet()) {
                String change = entry.getValue() > 0 ? "increase" : "decrease";
                System.out.printf("    Nutrient ID %d: %.2f (%s)%n", 
                    entry.getKey(), Math.abs(entry.getValue()), change);
            }
        }
    }
    
    /**
     * Prints ingredients list
     * @param ingredients List of ingredients to print
     */
    private static void printIngredients(List<List<Object>> ingredients) {
        for (List<Object> ingredient : ingredients) {
            System.out.printf("    Ingredient ID: %d, Quantity: %.1fg%n", 
                (Integer) ingredient.get(0), (Double) ingredient.get(1));
        }
    }
}
