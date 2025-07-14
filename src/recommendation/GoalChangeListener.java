package recommendation;

import java.util.List;

public interface GoalChangeListener {
    void onGoalChanged(Integer profileId, List<NutritionGoal> updatedGoals);
}
