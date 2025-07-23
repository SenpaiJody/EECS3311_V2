package recommendation;

import java.util.List;


/**
 * Listener interface for receiving notifications when nutrition goals change.
 * 
 */

public interface IGoalChangeListener {
	
    /**
     * Called when nutrition goals are updated for a user profile.
     * 
     * @param profileId ID of the user profile whose goals changed
     * @param updatedGoals List of current nutrition goals for the profile
     */
    void onGoalChanged(Integer profileId, List<NutritionGoal> updatedGoals);
}
