package Advising;

import java.util.*;

public class NutritionGoalManager implements INutritionGoalManager {
	private List<NutritionGoal> activeGoals;
    private final int maxGoalsPerUser = 2;

    public NutritionGoalManager() {
        this.activeGoals = new ArrayList<>();
    }

    
    @Override
    public NutritionGoal createGoal(Integer profileId, Integer nutrientId, int intensity, 
                                   GoalType goalType, Integer ingredientId) {
        // Validate inputs first
//        validateInputs(nutrientId, intensity, ingredientId);

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
        return true;
    }

    @Override
    public boolean removeGoal(Integer profileId, int goalId) {
        int initialSize = activeGoals.size();
        activeGoals.removeIf(goal -> 
            goal.getprofileId().equals(profileId)
//            && goal.getgoalId().equals(goalId)
        );
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

//    @Override
//    public void validateInputs(Integer nutrientId, int intensity, Integer ingredientId) {
//        if (nutrientId <= 0) {
//            throw new IllegalArgumentException("Nutrient ID must be a positive integer");
//        }
//
//        if (intensity < 1 || intensity > 10) {
//            throw new IllegalArgumentException("Intensity must be between 1 and 10");
//        }
//
//        if (ingredientId == null || ingredientId.trim().isEmpty()) {
//            throw new IllegalArgumentException("Ingredient ID cannot be empty");
//        }
//    }

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
