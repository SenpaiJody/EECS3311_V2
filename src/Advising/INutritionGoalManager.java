package Advising;

import java.util.List;

public interface INutritionGoalManager{
	public NutritionGoal createGoal(Integer profileId, Integer nutrientId, int intensity, GoalType goalType, Integer ingredientId);
	
	public boolean addGoal(Integer profileId, NutritionGoal goal);
    
    public boolean removeGoal(Integer profileId, int goalId);
    
    public List<NutritionGoal> getActiveGoals(Integer profileId);
    
    public boolean canAddMoreGoals(Integer profileId);
    
    public boolean updateGoal(Integer profileId, int goalId, Integer newNutrientId, int newIntensity, GoalType newGoalType, Integer newIngredientId);
    
    public NutritionGoal findGoal(Integer profileId, int goalId);
    
//    void validateInputs(int nutrientId, int intensity, Integer ingredientId);
	
}