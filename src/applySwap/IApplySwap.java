package applySwap;

import java.util.List;

import food.Food;

/**
 * Interface for applying ingredient swaps to a list of foods
 */
public interface IApplySwap {
    
    /**
     * Applies ingredient swaps to a list of foods
     * 
     * @param newIngredients List of new ingredient IDs to swap in
     * @param oldIngredients List of old ingredient IDs to swap out
     * @param previousFoods List of previous meals/foods to modify
     * @return Updated list of foods with swapped ingredients
     */
    List<Food> applySwaps(List<Integer> newIngredients, 
                                 List<Integer> oldIngredients, 
                                 List <Food> previousFoods);
}