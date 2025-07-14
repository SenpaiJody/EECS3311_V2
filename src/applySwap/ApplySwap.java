package applySwap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import food.Food;

/**
 * Implementation of ingredient swapping logic
 */
class ApplySwap implements IApplySwap {
    
    @Override
    public List<Food> applySwaps(List<Integer> newIngredients, 
                                List<Integer> oldIngredients, 
                                List<Food> previousFoods) {
        
        // Validate input parameters
        if (newIngredients == null || oldIngredients == null || previousFoods == null) {
            throw new IllegalArgumentException("Input parameters cannot be null");
        }
        
        if (newIngredients.size() != oldIngredients.size()) {
            throw new IllegalArgumentException("New ingredients and old ingredients lists must have the same size");
        }
        
        System.out.println("=== DEBUG: Starting ingredient swaps ===");
        System.out.println("Number of swaps to perform: " + newIngredients.size());
        System.out.println("Original foods count: " + previousFoods.size());
        
        // Create a deep copy of the original foods to avoid modifying the original
        List<Food> updatedFoods = deepCopyFoods(previousFoods);
        
        // Apply each swap
        for (int i = 0; i < newIngredients.size(); i++) {
            Integer oldIngredientId = oldIngredients.get(i);
            Integer newIngredientId = newIngredients.get(i);
            
            System.out.println("Applying swap " + (i + 1) + ": " + oldIngredientId + " -> " + newIngredientId);
            
            // Apply the swap to all foods in the list
            int swapCount = 0;
            for (Food food : updatedFoods) {
                Map<Integer, Double> ingredients = food.getIngredients();
                
                // Check if this food contains the old ingredient
                if (ingredients.containsKey(oldIngredientId)) {
                    // Get the quantity of the old ingredient
                    Double quantity = ingredients.get(oldIngredientId);
                    
                    // Remove the old ingredient and add the new one with the same quantity
                    ingredients.remove(oldIngredientId);
                    ingredients.put(newIngredientId, quantity);
                    
                    swapCount++;
                    System.out.println("  Swapped ingredient in food '" + food.getName() + "': " + 
                                     oldIngredientId + " (" + quantity + "g) -> " + 
                                     newIngredientId + " (" + quantity + "g)");
                }
            }
            
            System.out.println("  Total ingredients swapped: " + swapCount);
        }
        
        System.out.println("Updated foods count: " + updatedFoods.size());
        System.out.println("=== DEBUG: Completed ingredient swaps ===\n");
        
        return updatedFoods;
    }
    
    /**
     * Creates a deep copy of the foods list to avoid modifying the original
     */
    private List<Food> deepCopyFoods(List<Food> foods) {
        List<Food> copy = new ArrayList<>();
        for (Food food : foods) {
            // Create a deep copy of the ingredients map
            Map<Integer, Double> ingredientsCopy = new HashMap<>();
            for (Map.Entry<Integer, Double> entry : food.getIngredients().entrySet()) {
                ingredientsCopy.put(entry.getKey(), entry.getValue());
            }
            
            // Create a new Food object with copied data
            Food foodCopy = new Food(
                food.getID(),
                food.getName(),
                ingredientsCopy,
                food.getDate(),
                food.getType()
            );
            
            copy.add(foodCopy);
        }
        return copy;
    }
}
