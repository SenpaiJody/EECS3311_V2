package Advising;

import java.util.List;

/**
 * High-level facade for food swap operations using command pattern
 */
public class CommandBasedFoodSwap implements IApplySwapService {
    private final SwapCommandInvoker invoker;
    
    public CommandBasedFoodSwap() {
        this.invoker = new SwapCommandInvoker();
    }
    
    /**
     * Applies a swap operation
     * @param oldIngredientId ID of ingredient to replace
     * @param newIngredientId ID of replacement ingredient
     * @param currentIngredients Current ingredient list
     * @return SwapResult with changes
     */
    public SwapResult applySwap(int oldIngredientId, int newIngredientId, 
                               List<List<Object>> currentIngredients) {
        SwapCommand command = new IngredientSwapCommand(oldIngredientId, newIngredientId, currentIngredients);
        return invoker.executeCommand(command);
    }
    
    /**
     * Undoes the last swap operation
     * @return SwapResult from undo, null if no operations to undo
     */
    public SwapResult undoLastSwap() {
        return invoker.undoLastCommand();
    }
    
    /**
     * Redoes the last undone swap operation
     * @return SwapResult from redo, null if no operations to redo
     */
    public SwapResult redoLastSwap() {
        return invoker.redoLastCommand();
    }
    
    /**
     * Gets history of all swap operations
     * @return List of operation descriptions
     */
    public List<String> getSwapHistory() {
        return invoker.getCommandHistory();
    }
    
    /**
     * Clears all swap history
     */
    public void clearSwapHistory() {
        invoker.clearHistory();
    }
}
