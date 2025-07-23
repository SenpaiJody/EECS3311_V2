package recommendation;

import java.util.List;
import java.util.*;



/**
 * Interface for food recommendation services that provide ingredient suggestions
 * based on nutrition goals. Extends goal change listening capabilities to support
 * real-time updates when user goals are modified.
 * 
 */

public interface IFoodRecommendation extends IGoalChangeListener{
	

    /**
     * Retrieves the most recently generated recommendations for a user profile.
     * This the main method used by clients.
     * 
     * @param profileId ID of the user profile to get recommendations for
     * @return List of recommendation lists, or empty list
     */
	
	public List<List<Integer>> getLatestRecommendations(int profileId);
	
    /**
     * Generates ingredient recommendations based on the provided nutrition goals.
     * @param goal List of nutrition goals to generate recommendations for, must not be null or empty
     * @return List of lists containing recommended ingredient IDs, where each inner list
     *         represents recommendations for one goal. Returns empty list if no suitable
     *         ingredients can be found
     */
	public List<List<Integer>> getRecommendations(List <NutritionGoal> goal);
	
	
    /**
     * Event handler triggered when nutrition goals are updated for a user profile.
     * @param profileId ID of the user profile whose goals changed, must not be null
     * @param updatedGoals List of new/updated nutrition goals for the profile.
     *                     Empty list indicates all goals were removed
     */
	void onGoalChanged(Integer profileId, List<NutritionGoal> updatedGoals);
	


	
//    private List<List<Integer>> getRecommendations(List<NutritionGoal> goals, int limit);
//	
	
	
}
