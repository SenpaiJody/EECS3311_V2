package recommendation;

import java.util.List;

public interface IGoalChangeListener {
    void onGoalChanged(Integer profileId, List<NutritionGoal> updatedGoals);
}
