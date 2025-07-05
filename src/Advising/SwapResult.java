package Advising;


import java.util.*;
import nutriCalc.NutritionFacade;

/**
 * Result object containing the outcome of a swap operation
 */
public class SwapResult {
    private final Map<Integer, Double> nutrientDifferences;
    private final List<List<Object>> newIngredients;
    
    public SwapResult(Map<Integer, Double> nutrientDifferences, List<List<Object>> newIngredients) {
        this.nutrientDifferences = new HashMap<>(nutrientDifferences);
        this.newIngredients = deepCopyIngredients(newIngredients);
    }
    
    public Map<Integer, Double> getNutrientDifferences() {
        return new HashMap<>(nutrientDifferences);
    }
    
    public List<List<Object>> getNewIngredients() {
        return deepCopyIngredients(newIngredients);
    }
    
    private List<List<Object>> deepCopyIngredients(List<List<Object>> ingredients) {
        List<List<Object>> copy = new ArrayList<>();
        for (List<Object> ingredient : ingredients) {
            copy.add(new ArrayList<>(ingredient));
        }
        return copy;
    }
}







