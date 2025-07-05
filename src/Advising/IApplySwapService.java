package Advising;

import java.util.List;

public interface IApplySwapService {
    /**
     * Applies a swap operation
     * @param oldIngredientId ID of ingredient to replace
     * @param newIngredientId ID of replacement ingredient
     * @param currentIngredients Current ingredient list
     * @return SwapResult with changes
     */
    SwapResult applySwap(int oldIngredientId, int newIngredientId, List<List<Object>> currentIngredients);
    
    /**
     * Undoes the last swap operation
     * @return SwapResult from undo, null if no operations to undo
     */
    SwapResult undoLastSwap();
    
    /**
     * Redoes the last undone swap operation
     * @return SwapResult from redo, null if no operations to redo
     */
    SwapResult redoLastSwap();
    
    /**
     * Gets history of all swap operations
     * @return List of operation descriptions
     */
    List<String> getSwapHistory();
    
    /**
     * Clears all swap history
     */
    void clearSwapHistory();
}


