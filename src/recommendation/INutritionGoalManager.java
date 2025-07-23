package recommendation;

import java.util.List;

/**
 * Manager interface for creating and managing nutrition goals for user profiles.
 */

public interface INutritionGoalManager{
	
    /**
     * Creates a new nutrition goal with the specified parameters.
     * 
     * @param profileId ID of the user profile
     * @param nutrientId ID of the target nutrient
     * @param intensity Goal intensity level
     * @param goalType Type of goal (INCREASE or DECREASE)
     * @param ingredientId ID of the related ingredient
     * @return New NutritionGoal instance, or null if creation fails
     */
	public NutritionGoal createGoal(Integer profileId, Integer nutrientId, int intensity, GoalType goalType, Integer ingredientId);
	
    /**
     * Adds a nutrition goal to a user profile.
     * 
     * @param profileId ID of the user profile
     * @param goal Goal to add
     * @return true if goal was added successfully, false otherwise
     */
	public boolean addGoal(Integer profileId, NutritionGoal goal);
	
    /**
     * Removes a nutrition goal from a user profile.
     * 
     * @param profileId ID of the user profile
     * @param goalId ID of the goal to remove
     * @return true if goal was removed successfully, false if not found
     */
    public boolean removeGoal(Integer profileId, int goalId);
    
    /**
     * Gets all active nutrition goals for a user profile.
     * 
     * @param profileId ID of the user profile
     * @return List of active goals, empty list if no goals exist
     */
    public List<NutritionGoal> getActiveGoals(Integer profileId);
    
    /**
     * Checks if more goals can be added to a user profile.
     * 
     * @param profileId ID of the user profile
     * @return true if more goals can be added, false if limit reached
     */
    public boolean canAddMoreGoals(Integer profileId);
    
    /**
     * Updates an existing nutrition goal with new parameters.
     * 
     * @param profileId ID of the user profile
     * @param goalId ID of the goal to update
     * @param newNutrientId New nutrient ID
     * @param newIntensity New intensity level
     * @param newGoalType New goal type
     * @param newIngredientId New ingredient ID
     * @return true if goal was updated successfully, false if not found
     */
    public boolean updateGoal(Integer profileId, int goalId, Integer newNutrientId, int newIntensity, GoalType newGoalType, Integer newIngredientId);
    
    /**
     * Finds a specific nutrition goal by ID.
     * 
     * @param profileId ID of the user profile
     * @param goalId ID of the goal to find
     * @return NutritionGoal if found, null otherwise
     */
    public NutritionGoal findGoal(Integer profileId, int goalId);
    
    /**
     * Adds a listener for goal change notifications.
     * 
     * @param listener Listener to add
     */
    void addGoalChangeListener(IGoalChangeListener listener);
    
    /**
     * Removes a goal change listener.
     * 
     * @param listener Listener to remove
     */
    void removeGoalChangeListener(IGoalChangeListener listener);
    
    /**
     * Notifies all listeners that goals have changed for a profile.
     * 
     * @param profileId ID of the affected profile
     * @param updatedGoals Current list of goals for the profile
     */
    void notifyGoalChanged(Integer profileId, List<NutritionGoal> updatedGoals);
	
}