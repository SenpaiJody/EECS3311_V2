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
    
    
    

    
    @Override
    public void addGoalChangeListener(IGoalChangeListener listener) {
        listeners.add(listener);
//        System.out.println("[INFO] Goal change listener added: " + listener.getClass().getSimpleName());
    }
    
    @Override
    public void removeGoalChangeListener(IGoalChangeListener listener) {
        listeners.remove(listener);
//        System.out.println("[INFO] Goal change listener removed: " + listener.getClass().getSimpleName());
    }
    
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
    
    @Override
    public NutritionGoal createGoal(Integer profileId, Integer nutrientId, int intensity, 
                                   GoalType goalType, Integer ingredientId) {


        NutritionGoal newGoal = new NutritionGoal(profileId, nutrientId, intensity, 
                                                  goalType, ingredientId);
        return newGoal;
    }

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

    @Override
    public boolean canAddMoreGoals(Integer profileId) {
        int userGoalCount = getActiveGoals(profileId).size();
        return userGoalCount < maxGoalsPerUser;
    }



    // Getter for maxGoalsPerUser (if needed for testing/debugging)
    public int getMaxGoalsPerUser() {
        return maxGoalsPerUser;
    }
    
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
