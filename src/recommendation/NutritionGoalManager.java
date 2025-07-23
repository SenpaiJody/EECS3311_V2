package recommendation;

import java.util.*;

public class NutritionGoalManager implements INutritionGoalManager {
	private List<NutritionGoal> activeGoals;
    private final int maxGoalsPerUser = 2;
    private List<IGoalChangeListener> listeners;

    
   
    
    public NutritionGoalManager() {
        this.activeGoals = new ArrayList<>();
        this.listeners = new ArrayList<>();
    }

    /**
     * Adds a listener for goal change notifications.
     * 
     * @param listener Listener to add
     */
    @Override
    public void addGoalChangeListener(IGoalChangeListener listener) {
        listeners.add(listener);
//        System.out.println("[INFO] Goal change listener added: " + listener.getClass().getSimpleName());
    }
    
    /**
     * Removes a goal change listener.
     * 
     * @param listener Listener to remove
     */
    @Override
    public void removeGoalChangeListener(IGoalChangeListener listener) {
        listeners.remove(listener);
//        System.out.println("[INFO] Goal change listener removed: " + listener.getClass().getSimpleName());
    }
    
    /**
     * Notifies all listeners that goals have changed for a profile.
     * 
     * @param profileId ID of the affected profile
     * @param updatedGoals Current list of goals for the profile
     */
    @Override
    public void notifyGoalChanged(Integer profileId, List<NutritionGoal> updatedGoals) {
//        System.out.println("[INFO] Notifying " + listeners.size() + " listeners of goal change for profile " + profileId);
        for (IGoalChangeListener listener : listeners) {
            try {
                listener.onGoalChanged(profileId, updatedGoals);
            } catch (Exception e) {
//                System.err.println("[ERROR] Error notifying listener: " + e.getMessage());
            }
        }
    }
    
    
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
    @Override
    public NutritionGoal createGoal(Integer profileId, Integer nutrientId, int intensity, 
                                   GoalType goalType, Integer ingredientId) {


        NutritionGoal newGoal = new NutritionGoal(profileId, nutrientId, intensity, 
                                                  goalType, ingredientId);
        return newGoal;
    }
    
    
    /**
     * Adds a nutrition goal to a user profile.
     * 
     * @param profileId ID of the user profile
     * @param goal Goal to add
     * @return true if goal was added successfully, false otherwise
     */
    @Override
    public boolean addGoal(Integer profileId, NutritionGoal goal) {
        if (!canAddMoreGoals(profileId)) {
            return false;
        }

        activeGoals.add(goal);
     // ADDED: Notify observers
        notifyGoalChanged(profileId, getActiveGoals(profileId));
        return true;
    }

    /**
     * Removes a nutrition goal from a user profile.
     * 
     * @param profileId ID of the user profile
     * @param goalId ID of the goal to remove
     * @return true if goal was removed successfully, false if not found
     */
    @Override
    public boolean removeGoal(Integer profileId, int goalId) {
        int initialSize = activeGoals.size();
        activeGoals.removeIf(goal -> 
            goal.getprofileId().equals(profileId)
//            && goal.getgoalId().equals(goalId)
        );
        
        boolean removed = activeGoals.size() < initialSize;
        if (removed) {
//            System.out.println("[INFO] Removed goal " + goalId + " from profile " + profileId);
            // ADDED: Notify observers
            notifyGoalChanged(profileId, getActiveGoals(profileId));
        }
        return activeGoals.size() < initialSize;
    }

    /**
     * Gets all active nutrition goals for a user profile.
     * 
     * @param profileId ID of the user profile
     * @return List of active goals, empty list if no goals exist
     */
    @Override
    public List<NutritionGoal> getActiveGoals(Integer profileId) {
        List<NutritionGoal> userGoals = new ArrayList<>();
        for (NutritionGoal goal : activeGoals) {
            if (goal.getprofileId().equals(profileId)) {
                userGoals.add(goal);
            }
        }
        return userGoals;
    }

    /**
     * Checks if more goals can be added to a user profile.
     * 
     * @param profileId ID of the user profile
     * @return true if more goals can be added, false if limit reached
     */
    @Override
    public boolean canAddMoreGoals(Integer profileId) {
        int userGoalCount = getActiveGoals(profileId).size();
        return userGoalCount < maxGoalsPerUser;
    }


    /**
     * Primarily used for testing and debugging purposes.
     * 
     * @return The maximum number of goals allowed per user
     */
    // Getter for maxGoalsPerUser (if needed for testing/debugging)
    public int getMaxGoalsPerUser() {
        return maxGoalsPerUser;
    }
    
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
    @Override
    public boolean updateGoal(Integer profileId, int goalId, Integer newNutrientId, 
                             int newIntensity, GoalType newGoalType, Integer newIngredientId) {
        // Validate new inputs first
//        validateInputs(newNutrientId, newIntensity, newIngredientId);
        
        // Find the goal to update
        NutritionGoal goalToUpdate = findGoal(profileId, goalId);
        if (goalToUpdate == null) {
            return false; // Goal not found
        }
        
        // Update the goal's properties
        goalToUpdate.setNutrientId(newNutrientId);
        goalToUpdate.setIntensity(newIntensity);
        goalToUpdate.setGoalType(newGoalType);
        goalToUpdate.setIngredientId(newIngredientId);
        
        notifyGoalChanged(profileId, getActiveGoals(profileId));
        return true;
    }

    /**
     * Finds a specific nutrition goal by ID.
     * 
     * @param profileId ID of the user profile
     * @param goalId ID of the goal to find
     * @return NutritionGoal if found, null otherwise
     */
    @Override
    public NutritionGoal findGoal(Integer profileId, int goalId) {
        for (NutritionGoal goal : activeGoals) {
            if (goal.getprofileId().equals(profileId) && 
                goal.getgoalId() == (goalId)) {
                return goal;
            }
        }
        return null; // Goal not found
    }


}
