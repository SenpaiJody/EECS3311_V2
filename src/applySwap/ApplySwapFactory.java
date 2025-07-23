package applySwap;

/**
 * Factory class for creating ApplySwap instances
 */
public class ApplySwapFactory {
    
    /**
     * Creates and returns a new ApplySwap instance.
     * 
     * @return A new IApplySwap implementation
     */
    public static IApplySwap createApplySwap() {
        return new ApplySwap();
    }
}