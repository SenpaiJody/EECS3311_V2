package recommendation;

import java.util.List;
import java.util.*;

public interface IFoodRecommendation extends IGoalChangeListener{
	public List<List<Integer>> getRecommendations(List <NutritionGoal> goal);

	void onGoalChanged(Integer profileId, List<NutritionGoal> updatedGoals);
	
	List<List<Integer>> getLatestRecommendations(int profileId);

	
//    private List<List<Integer>> getRecommendations(List<NutritionGoal> goals, int limit);
//	
	
	
}
