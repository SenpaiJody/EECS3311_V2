package recommendation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        INutritionGoalManager goalManager = new NutritionGoalManager();
        
        IFoodRecommendation recommendationSystem = new FoodRecommendation();
        goalManager.addGoalChangeListener(recommendationSystem);

        // Create and add a goal
        NutritionGoal newGoal = goalManager.createGoal(
            123,           // profileId
            204,                     // nutrientId (e.g., protein)
            62,                     // intensity (1-100)
            GoalType.DECREASE,     // goalType
            567       // ingredientId
        );
        
        
        NutritionGoal newGoal2 = goalManager.createGoal(
                123,           // profileId
                401,                     // nutrientId (e.g., protein)
                100,                     // intensity (1-100)
                GoalType.DECREASE,     // goalType
                501841     // ingredientId
            );
        
//        boolean added2 = goalManager.addGoal(124, newGoal2);

        boolean added = goalManager.addGoal(123, newGoal);
        System.out.println("Goal added: " + added);
        System.out.println("Goal ID: " + newGoal.getgoalId());

        // Show recommendations after first goal
        System.out.println("\n=== Recommendations after Goal 1 ===");
        try {
            List<List<Integer>> recs1 = recommendationSystem.getLatestRecommendations(123);
            if (recs1 != null && !recs1.isEmpty()) {
                for (int i = 0; i < recs1.size(); i++) {
                    System.out.println("Goal " + (i + 1) + " recommendations: " + recs1.get(i));
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting recommendations: " + e.getMessage());
        }

        // Check profile's active goals
        List<NutritionGoal> profileGoals = goalManager.getActiveGoals(123);
        System.out.println("\nProfile has " + profileGoals.size() + " active goals");
//        
//        List<NutritionGoal> profileGoals2 = goalManager.getActiveGoals(124);
//        System.out.println("\nProfile has " + profileGoals.size() + " active goals");
//
//        // Check if profile can add more goals
//        boolean canAddMore = goalManager.canAddMoreGoals(123);
//        System.out.println("Can add more goals: " + canAddMore);
//
//        // Create another goal
//        NutritionGoal anotherGoal = goalManager.createGoal(
//           123,
//            1,                     // different nutrientId
//            35,                     // different intensity
//            GoalType.DECREASE,
//            2
//        );
//
//        goalManager.addGoal(123, newGoal2);
//        System.out.println("Second goal ID: " + anotherGoal.getgoalId());
//        System.out.println("Total goals for profile: " + 
//                          goalManager.getActiveGoals(123).size());
//
//        // Show recommendations after second goal
//        System.out.println("\n=== Recommendations after Goal 2 ===");
//        try {
//            List<List<Integer>> recs2 = recommendationSystem.getLatestRecommendations(123);
//            if (recs2 != null && !recs2.isEmpty()) {
//                for (int i = 0; i < recs2.size(); i++) {
//                    System.out.println("Goal " + (i + 1) + " recommendations: " + recs2.get(i));
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("Error getting recommendations: " + e.getMessage());
//        }
//        
//        // Print all goals added at the end
//        System.out.println("\n=== All Goals for Profile 123 ===");
//        List<NutritionGoal> allGoals = goalManager.getActiveGoals(123);
//        for (int i = 0; i < allGoals.size(); i++) {
//            NutritionGoal goal = allGoals.get(i);
//            System.out.println("Goal " + (i + 1) + ":");
//            System.out.println("  Goal ID: " + goal.getgoalId());
//            System.out.println("  Profile ID: " + goal.getprofileId());
//            System.out.println("  Nutrient ID: " + goal.getnutrientId());
//            System.out.println("  Intensity: " + goal.getintensity());
//            System.out.println("  Goal Type: " + goal.getgoalType());
//            System.out.println("  Ingredient ID: " + goal.getingredientId());
//            System.out.println();
//        }
//        
//        System.out.println("\n=== All Goals for Profile 124 ===");
//        List<NutritionGoal> allGoals2 = goalManager.getActiveGoals(124);
//        for (int i = 0; i < allGoals2.size(); i++) {
//            NutritionGoal goal = allGoals2.get(i);
//            System.out.println("Goal " + (i + 1) + ":");
//            System.out.println("  Goal ID: " + goal.getgoalId());
//            System.out.println("  Profile ID: " + goal.getprofileId());
//            System.out.println("  Nutrient ID: " + goal.getnutrientId());
//            System.out.println("  Intensity: " + goal.getintensity());
//            System.out.println("  Goal Type: " + goal.getgoalType());
//            System.out.println("  Ingredient ID: " + goal.getingredientId());
//            System.out.println();
//        }
//
//        // Example of editing a goal using the proper goal ID
//        System.out.println("\n=== Editing Goal (Currently Commented Out) ===");
//        // Uncomment below to test goal editing
//        /*
//        boolean updated = goalManager.updateGoal(
//            123,           // profileId
//            newGoal.getgoalId(),    // proper goalId
//            3,                     // new nutrientId
//            9,                     // new intensity
//            GoalType.MAINTAIN,     // new goalType
//            4        // new ingredientId
//        );
//        System.out.println("Goal updated: " + updated);
//        */
//
//        // Get and display final summary of recommendations
//        System.out.println("\n=== Final Summary - All Recommendations ===");
//        List<List<Integer>> finalRecs = new ArrayList<>();
//
//        try {
//            finalRecs = recommendationSystem.getLatestRecommendations(123);
//            
//            if (finalRecs != null && !finalRecs.isEmpty()) {
//                for (int i = 0; i < finalRecs.size(); i++) {
//                    System.out.println("Goal " + (i + 1) + " final recommendations: " + finalRecs.get(i));
//                }
//            } else {
//                System.out.println("No recommendations found for profile 123");
//            }
//            
//        } catch (Exception e) {
//            System.out.println("Error getting recommendations: " + e.getMessage());
//            e.printStackTrace();
//        }
//        
//        
        System.out.println("\n=== Final Summary - All Recommendations2 ===");
        List<List<Integer>> finalRecs2 = new ArrayList<>();

        try {
            finalRecs2 = recommendationSystem.getLatestRecommendations(123);
            
            if (finalRecs2 != null && !finalRecs2.isEmpty()) {
                for (int i = 0; i < finalRecs2.size(); i++) {
                    System.out.println("Goal " + (i + 1) + " final recommendations: " + finalRecs2.get(i));
                }
            } else {
                System.out.println("No recommendations found for profile 124");
            }
            
        } catch (Exception e) {
            System.out.println("Error getting recommendations: " + e.getMessage());
            e.printStackTrace();
        }
        
        List<NutritionGoal> profileGoals2 = goalManager.getActiveGoals(124);
        System.out.println("\nProfile has " + profileGoals.size() + " active goals");

        // Check if profile can add more goals
        boolean canAddMore = goalManager.canAddMoreGoals(123);
        System.out.println("Can add more goals: " + canAddMore);

        // Create another goal
        NutritionGoal anotherGoal = goalManager.createGoal(
           123,
            1,                     // different nutrientId
            35,                     // different intensity
            GoalType.DECREASE,
            2
        );

        goalManager.addGoal(123, anotherGoal);
        System.out.println("Second goal ID: " + anotherGoal.getgoalId());
        System.out.println("Total goals for profile: " + 
                          goalManager.getActiveGoals(123).size());

        // Show recommendations after second goal
        System.out.println("\n=== Recommendations after Goal 2 ===");
        try {
            List<List<Integer>> recs2 = recommendationSystem.getLatestRecommendations(123);
            if (recs2 != null && !recs2.isEmpty()) {
                for (int i = 0; i < recs2.size(); i++) {
                    System.out.println("Goal " + (i + 1) + " recommendations: " + recs2.get(i));
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting recommendations: " + e.getMessage());
        }
        
        // Print all goals added at the end
        System.out.println("\n=== All Goals for Profile 123 ===");
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
        
        System.out.println("\n=== All Goals for Profile 124 ===");
        List<NutritionGoal> allGoals2 = goalManager.getActiveGoals(124);
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

        // Example of editing a goal using the proper goal ID
        System.out.println("\n=== Editing Goal (Currently Commented Out) ===");
        // Uncomment below to test goal editing
        /*
        boolean updated = goalManager.updateGoal(
            123,           // profileId
            newGoal.getgoalId(),    // proper goalId
            3,                     // new nutrientId
            9,                     // new intensity
            GoalType.MAINTAIN,     // new goalType
            4        // new ingredientId
        );
        System.out.println("Goal updated: " + updated);
        */

        // Get and display final summary of recommendations
        System.out.println("\n=== Final Summary - All Recommendations ===");
        List<List<Integer>> finalRecs = new ArrayList<>();

        try {
            finalRecs = recommendationSystem.getLatestRecommendations(123);
            
            if (finalRecs != null && !finalRecs.isEmpty()) {
                for (int i = 0; i < finalRecs.size(); i++) {
                    System.out.println("Goal " + (i + 1) + " final recommendations: " + finalRecs.get(i));
                }
            } else {
                System.out.println("No recommendations found for profile 123");
            }
            
        } catch (Exception e) {
            System.out.println("Error getting recommendations: " + e.getMessage());
            e.printStackTrace();
        }
        
        
        System.out.println("\n=== Final Summary - All Recommendations2 ===");
        List<List<Integer>> finalRecs21 = new ArrayList<>();

        try {
            finalRecs = recommendationSystem.getLatestRecommendations(124);
            
            if (finalRecs != null && !finalRecs.isEmpty()) {
                for (int i = 0; i < finalRecs.size(); i++) {
                    System.out.println("Goal " + (i + 1) + " final recommendations: " + finalRecs.get(i));
                }
            } else {
                System.out.println("No recommendations found for profile 124");
            }
            
        } catch (Exception e) {
            System.out.println("Error getting recommendations: " + e.getMessage());
            e.printStackTrace();
        }
    }
}