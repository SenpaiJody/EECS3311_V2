package recommendation;

import java.util.List;


/**
 * Represents a nutrition goal for a user profile, defining targets for specific
 * nutrients or ingredients with configurable intensity and goal type.
 * 
 */

public class NutritionGoal{
	private int goalId;
	private Integer profileId;
	private Integer nutrientId;
	private int intensity;
	private Integer ingredientId;
	private GoalType goalType;
	private static int goalCounter = 1;
	
    /**
     * Constructor to create a new nutrition goal with all required parameters.
     * 
     * @param profileId The ID of the user profile this goal belongs to
     * @param nutrientId The ID of the nutrient being targeted
     * @param intensity The positive numerical intensity/amount of the goal
     * @param goalType Whether to INCREASE or DECREASE the target (GoalType enum)
     * @param ingredientId Optional ingredient ID for ingredient-specific goals (can be null)
     */
	public NutritionGoal(Integer profileId, Integer nutrientId, int intensity, GoalType goalType, Integer ingredientId ) {
		this.profileId = profileId;
		this.nutrientId = nutrientId;
		this.intensity = intensity;
		this.goalType = goalType;
		this.ingredientId = ingredientId;
		this.goalId = goalCounter++;
	}
	
    /**
     * Gets the unique identifier for this nutrition goal.
     * @return The auto-generated goal ID
     */
	public int getgoalId() {
		return this.goalId;
	}
	
    /**
     * Gets the profile ID associated with this goal.
     * @return The user profile ID, or null if not set
     */
	public Integer getprofileId(){
		return this.profileId;
	}
	
    /**
     * Gets the nutrient ID that this goal targets.
     * @return The nutrient ID, or null if not set
     */
	public Integer getnutrientId() {
		return this.nutrientId;
	}
	
    /**
     * Gets the intensity value of this goal (always positive).
     * Use applyGoalTypeSign() to get the signed version.
     * @return The positive intensity value
     */
	public int getintensity() {
		return this.intensity;
	}
	
    /**
     * Gets the goal type (INCREASE or DECREASE).
     * @return The GoalType enum value
     */
	public GoalType getgoalType(){
		return this.goalType;
	}
	
    /**
     * Gets the ingredient ID for ingredient-specific goals.
     * @return The ingredient ID
     */
	public int getingredientId(){
		return this.ingredientId;
	}
	
    /**
     * Updates the profile ID for this goal.
     * @param profileId The new profile ID to associate with this goal
     */
	public void setProfileId(Integer profileId) { 
		this.profileId = profileId; 
	}
	
    /**
     * Updates the nutrient ID that this goal targets.
     * @param nutrientId The new nutrient ID to target
     */
    public void setNutrientId(int nutrientId) { 
    	this.nutrientId = nutrientId; 
    	}
    
    /**
     * Updates the intensity value for this goal.
     * @param intensity The new positive intensity value
     */
    public void setIntensity(int intensity) { 
    	this.intensity = intensity; 
    	}
    
    /**
     * Updates the goal type (INCREASE or DECREASE).
     * @param goalType The new GoalType enum value
     */
    public void setGoalType(GoalType goalType) { 
    	this.goalType = goalType; 
    	}
    
    /**
     * Updates the ingredient ID for ingredient-specific goals.
     * @param ingredientId The new ingredient ID
     */
    public void setIngredientId(Integer ingredientId) { 
    	this.ingredientId = ingredientId; 
    	}
    
    
    /**
     * Applies the appropriate sign to intensity based on goal type
     * @param goal The nutrition goal
     * @return Signed intensity: negative for DECREASE, positive for INCREASE
     */
    public int applyGoalTypeSign() {
        int signedIntensity = this.getgoalType() == GoalType.DECREASE ?
            -this.getintensity() : this.getintensity();
        return signedIntensity;
    }
	
	
}