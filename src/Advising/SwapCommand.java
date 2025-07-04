package Advising;


/**
 * Command interface for swap operations
 */
public interface SwapCommand {
    /**
     * Executes the swap command
     * @return SwapResult containing nutrient differences and new ingredients
     */
    SwapResult execute();
    
    /**
     * Undoes the swap command
     * @return SwapResult with reversed changes
     */
    SwapResult undo();
    
    /**
     * Gets description of the command
     * @return String description
     */
    String getDescription();
}

