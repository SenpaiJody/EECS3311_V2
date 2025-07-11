package Advising;

import java.util.List;

public interface IFoodRecommendation{
	public List<Integer> getRecommendations(NutritionGoal goal);
	
	public List<Integer> getRecommendations(NutritionGoal goal,int limit);
	
	
	
}
