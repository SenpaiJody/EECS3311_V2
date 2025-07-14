package applySwap;

/**
 * Factory class for creating ApplySwap instances
 */
class ApplySwapFactory {
    
    /**
     * Creates and returns an instance of IApplySwap
     */
    public static IApplySwap createApplySwap() {
        return new ApplySwap();
    }
}