package Advising;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        INutritionGoalManager goalManager = new NutritionGoalManager();

        // Create and add a goal
        NutritionGoal newGoal = goalManager.createGoal(
            123,           // profileId
            1,                     // nutrientId (e.g., protein)
            7,                     // intensity (1-10)
            GoalType.INCREASE,     // goalType
            2        // ingredientId
        );

        boolean added = goalManager.addGoal(123, newGoal);
        System.out.println("Goal added: " + added);
        System.out.println("Goal ID: " + newGoal.getgoalId());

        // Check profile's active goals
        List<NutritionGoal> profileGoals = goalManager.getActiveGoals(123);
        System.out.println("Profile has " + profileGoals.size() + " active goals");

        // Check if profile can add more goals
        boolean canAddMore = goalManager.canAddMoreGoals(123);
        System.out.println("Can add more goals: " + canAddMore);

        // Create another goal
        NutritionGoal anotherGoal = goalManager.createGoal(
           123,
            2,                     // different nutrientId
            5,                     // different intensity
            GoalType.DECREASE,
            2
        );

        goalManager.addGoal(123, anotherGoal);
        System.out.println("Second goal ID: " + anotherGoal.getgoalId());
        System.out.println("Total goals for profile: " + 
                          goalManager.getActiveGoals(123).size());
        
     // Print all goals added at the end
        System.out.println("\n=== All Goals for Profile123 ===");
        List<NutritionGoal> allGoals = goalManager.getActiveGoals(123);
        for (int i = 0; i < allGoals.size(); i++) {
            NutritionGoal goal = allGoals.get(i);
            System.out.println("Goal " + (i + 1) + ":");
            System.out.println("  Goal ID: " + goal.getgoalId());
            System.out.println("  Profile ID: " + goal.getprofileId());
            System.out.println("  Nutrient ID: " + goal.getnutrientId());
            System.out.println("  Intensity: " + goal.getintensity());
            System.out.println("  Goal Type: " + goal.getgoalType());
            System.out.println("  Ingredient ID: " + goal.getingredientId());
            System.out.println();
        }

        // Example of editing a goal using the proper goal ID
        System.out.println("\n=== Editing Goal ===");
        boolean updated = goalManager.updateGoal(
            123,           // profileId
            newGoal.getgoalId(),    // proper goalId
            3,                     // new nutrientId
            9,                     // new intensity
            GoalType.MAINTAIN,     // new goalType
            4        // new ingredientId
        );
        System.out.println("Goal updated: " + updated);

        // Print all goals added at the end
        System.out.println("\n=== All Goals for Profile123 ===");
        List<NutritionGoal> allGoals2 = goalManager.getActiveGoals(123);
        for (int i = 0; i < allGoals2.size(); i++) {
            NutritionGoal goal = allGoals2.get(i);
            System.out.println("Goal " + (i + 1) + ":");
            System.out.println("  Goal ID: " + goal.getgoalId());
            System.out.println("  Profile ID: " + goal.getprofileId());
            System.out.println("  Nutrient ID: " + goal.getnutrientId());
            System.out.println("  Intensity: " + goal.getintensity());
            System.out.println("  Goal Type: " + goal.getgoalType());
            System.out.println("  Ingredient ID: " + goal.getingredientId());
            System.out.println();
        }
    }
}