package Advising;

import java.util.List;

public class NutritionGoal{
	private int goalId;
	private Integer profileId;
	private Integer nutrientId;
	private int intensity;
	private Integer ingredientId;
	private GoalType goalType;
	private static int goalCounter = 1;
	
	public NutritionGoal(Integer profileId, Integer nutrientId, int intensity, GoalType goalType, Integer ingredientId ) {
		this.profileId = profileId;
		this.nutrientId = nutrientId;
		this.intensity = intensity;
		this.goalType = goalType;
		this.ingredientId = ingredientId;
		this.goalId = goalCounter++;
	}
	
	public int getgoalId() {
		return this.goalId;
	}
	public Integer getprofileId(){
		return this.profileId;
	}
	public Integer getnutrientId() {
		return this.nutrientId;
	}
	public int getintensity() {
		return this.intensity;
	}
	public GoalType getgoalType(){
		return this.goalType;
	}
	public int getingredientId(){
		return this.ingredientId;
	}
	
	public void setProfileId(Integer profileId) { 
		this.profileId = profileId; 
	}
    public void setNutrientId(int nutrientId) { 
    	this.nutrientId = nutrientId; 
    	}
    public void setIntensity(int intensity) { 
    	this.intensity = intensity; 
    	}
    public void setGoalType(GoalType goalType) { 
    	this.goalType = goalType; 
    	}
    public void setIngredientId(Integer ingredientId) { 
    	this.ingredientId = ingredientId; 
    	}
	
	
}