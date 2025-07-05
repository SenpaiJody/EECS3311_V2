package Advising;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import nutriCalc.NutritionFacade;

/**
 * Concrete command for ingredient swapping
 */
public class IngredientSwapCommand implements SwapCommand {
    private final int oldIngredientId;
    private final int newIngredientId;
    private final List<List<Object>> originalIngredients;
    private final NutritionFacade nutritionFacade;
    private List<List<Object>> swappedIngredients;
    private boolean executed = false;
    
    public IngredientSwapCommand(int oldIngredientId, int newIngredientId, 
                               List<List<Object>> originalIngredients) {
        this.oldIngredientId = oldIngredientId;
        this.newIngredientId = newIngredientId;
        this.originalIngredients = deepCopyIngredients(originalIngredients); // Proper deep copy
        this.nutritionFacade = new NutritionFacade();
    }
    
    @Override
    public SwapResult execute() {
        if (executed) {
            throw new IllegalStateException("Command already executed. Use undo() first.");
        }
        
        // Create new ingredients list with swapped ingredient IDs
        swappedIngredients = new ArrayList<>();
        
        for (List<Object> ingredient : originalIngredients) {
            int ingredientId = (Integer) ingredient.get(0);
            double quantity = (Double) ingredient.get(1);
            
            // If this is the old ingredient, replace with new ingredient ID
            if (ingredientId == oldIngredientId) {
                swappedIngredients.add(Arrays.asList(newIngredientId, quantity));
            } else {
                // Keep the ingredient as is
                swappedIngredients.add(Arrays.asList(ingredientId, quantity));
            }
        }
        
        // Calculate nutrient difference (new - original)
        Map<Integer, Double> nutrientDifferences = nutritionFacade.calculateNutrientDifference(
            swappedIngredients, originalIngredients
        );
        
        executed = true;
        return new SwapResult(nutrientDifferences, swappedIngredients);
    }
    
    @Override
    public SwapResult undo() {
        if (!executed) {
            throw new IllegalStateException("Command not executed yet. Use execute() first.");
        }
        
        // Calculate reverse nutrient difference (original - swapped)
        Map<Integer, Double> reverseNutrientDifferences = nutritionFacade.calculateNutrientDifference(
            originalIngredients, swappedIngredients
        );
        
        executed = false;
        swappedIngredients = null;
        return new SwapResult(reverseNutrientDifferences, originalIngredients);
    }
    
    @Override
    public String getDescription() {
        return String.format("Swap ingredient %d with ingredient %d", oldIngredientId, newIngredientId);
    }
    
    private List<List<Object>> deepCopyIngredients(List<List<Object>> ingredients) {
        List<List<Object>> copy = new ArrayList<>();
        for (List<Object> ingredient : ingredients) {
            copy.add(new ArrayList<>(ingredient));
        }
        return copy;
    }
}
