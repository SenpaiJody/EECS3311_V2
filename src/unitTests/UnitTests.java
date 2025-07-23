package unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Map.entry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import applySwap.ApplySwapFactory;
import applySwap.IApplySwap;
import cfgNutrientRecService.*;
import food.*;
import food.FoodType;
import foodService.Filter;
import foodService.FoodServiceFactory;
import foodService.IFoodService;
import graphService.GraphServiceFactory;
import graphService.IGraphService;
import nutrientService.*;
import nutrientService.NutrientServiceFactory;
import recommendation.FoodRecommendation;
import recommendation.GoalType;
import recommendation.IFoodRecommendation;
import recommendation.INutritionGoalManager;
import recommendation.NutritionGoal;
import recommendation.NutritionGoalManager;
import userService.IUserService;
import userService.IncorrectLoginException;
import userService.Profile;
import userService.ProfileDoesNotExistException;
import userService.User;
import userService.UserServiceFactory;
import visualCalculationService.*;


public class UnitTests {

	IUserService userService = UserServiceFactory.getService();
	IFoodService foodService = FoodServiceFactory.getService();
	IApplySwap applySwap = ApplySwapFactory.createApplySwap();
	IGraphService graphService = GraphServiceFactory.getService();
    ICFGNutrientRecService cfgNutrientRecService = CFGNutrientRecServiceFactory.getService();
    IVisualCalculationService visualCalculationService = VisualCalculationServiceFactory.getService();
    INutrientService nutrientService = NutrientServiceFactory.getService();
    
	@BeforeEach
    public void setUp() {

              
        try {
            userService.attemptLogin("bobtest3", "mypassword");
        } catch (IncorrectLoginException e) {
            throw new RuntimeException("Login failed", e);
        }

        User currentUser = userService.getCurrentUser();
        if (currentUser == null) throw new RuntimeException("Current user is null");

        for (Profile p : currentUser.getProfiles()) {
            if (p.getID() == 11) {
                try {
                    userService.setCurrentProfile(p);
                    break;
                } catch (ProfileDoesNotExistException e) {
                    throw new RuntimeException("Profile error", e);
                }
            }
        }
    }


	
	 @Test
	    public void UnitTest2_0() {

	        Filter filter = new Filter();
	        LocalDate dateStart = LocalDate.of(2025, 6, 1);
	        LocalDate dateEnd = LocalDate.of(2025, 6, 1);
	        filter.setDateRange(dateStart, dateEnd);

	        List<Food> foodList = foodService.getMeals(filter);

	        assertNotNull(foodList);

	        Food food = foodList.get(0);
	        assertEquals(14, food.getID());
	        assertEquals("Chicken Lunch 1", food.getName());
	        assertEquals(LocalDate.of(2025, 6, 1), food.getDate());
	        assertEquals("Lunch", food.getType().getTypeName());

	        Map<Integer, Double> ingredients = food.getIngredients();
	        assertEquals(3, ingredients.size());
	        
	        assertEquals(55.00, ingredients.get(16), 0.0001);
	        assertEquals(88.50, ingredients.get(501841), 0.0001);
	        assertEquals(210.00, ingredients.get(567), 0.0001);
	        
	    }
	 
	 @Test
	    public void UnitTest3_0() {
		 	INutritionGoalManager goalManager;
		    IFoodRecommendation recommendationSystem;
		    INutrientService nutrientService;
		    IUserService userService;
	        goalManager = new NutritionGoalManager();
	        recommendationSystem = new FoodRecommendation();
	        nutrientService = NutrientServiceFactory.getService();
	        goalManager.addGoalChangeListener(recommendationSystem);
		 
	        int profileId = 123;
	        int nutrientId = 203; // Protein
	        int originalIngredientId = 3331; // Tofu (example)

	        // Create and add a goal to decrease Fat in ingredient 92
	        NutritionGoal goal = goalManager.createGoal(
	            profileId,
	            nutrientId,
	            80,
	            GoalType.INCREASE,
	            originalIngredientId
	        );
	        goalManager.addGoal(profileId, goal);

	        // Get original ingredient's fat amount
	        Map<Integer, Double> originalNutrients = nutrientService.getNutrientsPer100g(originalIngredientId);
	        Double originalAmount = originalNutrients.get(nutrientId);
	        System.out.println("Original ingredient fat: " + originalAmount);

	        // Get recommendations
	        List<List<Integer>> recs = recommendationSystem.getLatestRecommendations(profileId);

	        assertTrue(recs != null && !recs.isEmpty(), "Recommendations should not be empty.");

	        // Test that each recommendation has less fat than the original
	        for (Integer recommendedId : recs.get(0)) {
	            Map<Integer, Double> recNutrients = nutrientService.getNutrientsPer100g(recommendedId);
	            Double recAmount = recNutrients.get(nutrientId);

	            System.out.println("Recommended " + recommendedId + " fat: " + recAmount);

	            // Only assert if the nutrient info is present
	            if (recAmount != null && originalAmount != null) {
	                assertTrue(recAmount < originalAmount,
	                    "Recommended ingredient " + recommendedId + " should have less fat than " + originalIngredientId);
	            }
	        }
	    }
		
	 @Test
	 	public void UnitTest3_1() {
	        INutritionGoalManager goalManager;
	        IFoodRecommendation recommendationSystem;
	        INutrientService nutrientService;
	        goalManager = new NutritionGoalManager();
	        recommendationSystem = new FoodRecommendation();
	        nutrientService = NutrientServiceFactory.getService();
	        goalManager.addGoalChangeListener(recommendationSystem);

	        int profileId = 123;
	        int nutrientId = 306; // Potassium
	        int originalIngredientId = 5404; // Some ingredient with low potassium

	        // Create and add a goal to increase Potassium in ingredient 5404
	        NutritionGoal goal = goalManager.createGoal(
	            profileId,
	            nutrientId,
	            80,
	            GoalType.INCREASE,
	            originalIngredientId
	        );
	        goalManager.addGoal(profileId, goal);

	        // Get original ingredient's nutrient amount
	        Map<Integer, Double> originalNutrients = nutrientService.getNutrientsPer100g(originalIngredientId);
	        Double originalAmount = originalNutrients.get(nutrientId);
	        System.out.println("Original ingredient potassium: " + originalAmount);

	        // Get recommendations
	        List<List<Integer>> recs = recommendationSystem.getLatestRecommendations(profileId);
	        assertTrue(recs != null && !recs.isEmpty(), "Recommendations should not be empty.");

	        // Test that each recommendation has more potassium than the original
	        for (Integer recommendedId : recs.get(0)) {
	            Map<Integer, Double> recNutrients = nutrientService.getNutrientsPer100g(recommendedId);
	            Double recAmount = recNutrients.get(nutrientId);
	            System.out.println("Recommended " + recommendedId + " potassium: " + recAmount);

	            if (recAmount != null && originalAmount != null) {
	                assertTrue(recAmount > originalAmount,
	                    "Recommended ingredient " + recommendedId + " should have more potassium than " + originalIngredientId);
	            }
	        }
	    }
	     
	 /* Visualizing differences from before swaps and after swaps
	  * Tests both avg and total nutrient calcultaionts 
	  */
		@Test
		public void UnitTest4_0() {
			LocalDate today = LocalDate.of(2025, 6, 1);

	        // Create FoodType instances
	        FoodType breakfast = new Breakfast();
	        FoodType lunch = new Lunch();
	        FoodType dinner = new Dinner();
	        FoodType snack = new Snack();

	        // Build Food objects
	        List<Food> foodList = List.of(
	            new Food(1, "Beef Pot Roast Meal", Map.of(7, 100.0), today, dinner),
	            new Food(2, "Fried Chicken Meal", Map.of(8, 100.0), today, lunch),
	            new Food(3, "Meat Loaf Meal", Map.of(9, 100.0), today, dinner),
	            new Food(4, "Turkey Meal", Map.of(10, 100.0), today, lunch),
	            new Food(5, "Butter, whipped", Map.of(16, 10.0), today, breakfast),
	            new Food(6, "Blue Cheese", Map.of(18, 20.0), today, snack),
	            new Food(7, "Egg, dried", Map.of(83, 30.0), today, breakfast),
	            new Food(8, "Dried Apricot w/ Sugar", Map.of(1509, 40.0), today, snack),
	            new Food(9, "Pork Chop, Braised", Map.of(1786, 120.0), today, dinner),
	            new Food(10, "Snow Peas, Boiled", Map.of(2146, 50.0), today, lunch),
	            new Food(11, "Tomato Sauce, Canned", Map.of(2465, 30.0), today, lunch),
	            new Food(12, "Spot Fish, Raw", Map.of(3068, 130.0), today, dinner),
	            new Food(13, "Beef Jerky Snack", Map.of(4086, 25.0), today, snack),
	            new Food(14, "Canned Pineapple, Drained", Map.of(5394, 80.0), today, breakfast),
	            new Food(15, "Fried Tofu", Map.of(501799, 70.0), today, lunch),
	            new Food(16, "Cake with Icing", Map.of(501845, 90.0), today, snack),
	            new Food(17, "Artichoke", Map.of(501841, 60.0), today, lunch),
	            new Food(18, "Chicken", Map.of(567, 110.0), today, dinner),
	            new Food(19, "Frozen Broccoli", Map.of(2025, 100.0), today, lunch),
	            new Food(20, "Breaded Onion Rings", Map.of(2404, 60.0), today, snack),
	            new Food(21, "Italian Bread", Map.of(3700, 50.0), today, breakfast)
	        );
			
	        List<Food> swapList = List.of(
	        	    new Food(1, "Beef Pot Roast Meal", Map.of(7, 180.0, 2146, 60.0, 2025, 70.0), today, dinner),
	        	    new Food(2, "Fried Chicken Meal", Map.of(8, 170.0, 2404, 65.0, 2025, 60.0), today, lunch),
	        	    new Food(3, "Meat Loaf Meal", Map.of(9, 160.0, 2146, 50.0, 2025, 80.0), today, dinner),
	        	    new Food(4, "Turkey Meal", Map.of(10, 150.0, 2146, 45.0, 2025, 65.0), today, lunch),
	        	    new Food(5, "Butter Toast Breakfast", Map.of(16, 10.0, 3700, 60.0), today, breakfast),
	        	    new Food(6, "Blue Cheese Snack", Map.of(18, 30.0, 2404, 40.0), today, snack),
	        	    new Food(7, "Egg and Toast", Map.of(83, 25.0, 3700, 50.0), today, breakfast),
	        	    new Food(8, "Apricot & Pineapple", Map.of(1509, 45.0, 5394, 60.0), today, snack),
	        	    new Food(9, "Pork Chop Dinner", Map.of(1786, 170.0, 2025, 60.0), today, dinner),
	        	    new Food(10, "Snow Peas with Tofu", Map.of(2146, 70.0, 501799, 90.0), today, lunch),
	        	    new Food(11, "Pasta with Tomato Sauce", Map.of(2465, 50.0, 3700, 40.0), today, lunch),
	        	    new Food(12, "Fish and Veggies", Map.of(3068, 140.0, 2025, 60.0, 501841, 50.0), today, dinner),
	        	    new Food(13, "Jerky Snack Pack", Map.of(4086, 28.0, 1509, 30.0), today, snack),
	        	    new Food(14, "Pineapple Egg Breakfast", Map.of(5394, 90.0, 83, 20.0), today, breakfast),
	        	    new Food(15, "Tofu Stir Fry", Map.of(501799, 85.0, 2146, 50.0, 2025, 70.0), today, lunch),
	        	    new Food(16, "Cake and Butter", Map.of(501845, 120.0, 16, 15.0), today, snack),
	        	    new Food(17, "Artichoke Veggie Mix", Map.of(501841, 90.0, 2025, 55.0), today, lunch),
	        	    new Food(18, "Chicken Dinner", Map.of(567, 160.0, 2025, 60.0, 3700, 50.0), today, dinner),
	        	    new Food(19, "Broccoli Tofu Bowl", Map.of(2025, 100.0, 501799, 80.0), today, lunch),
	        	    new Food(20, "Onion Rings and Cheese", Map.of(2404, 70.0, 18, 25.0), today, snack),
	        	    new Food(21, "Italian Bread Breakfast", Map.of(3700, 70.0, 16, 8.0, 5394, 50.0), today, breakfast)
	        	);
	        
	        Map<Integer, Double> avgNutrientResult1 = visualCalculationService.avgNutrients(foodList);
	        Map<Integer, Double> avgNutrientResult2 = visualCalculationService.avgNutrients(swapList);
	        
	        Map<Integer, Double> totalNutrientsResult1 = visualCalculationService.totalNutrients(foodList);
	        Map<Integer, Double> totalNutrientsResult2 = visualCalculationService.totalNutrients(swapList);

	        assertNotEquals("Average nutrients should differ", avgNutrientResult1, avgNutrientResult2);
	        assertNotEquals("Total nutrients should differ", totalNutrientsResult1, totalNutrientsResult2);
	       
	        avgNutrientResult1.values().forEach(v -> assertTrue("Average nutrient should be non-negative", v >= 0));
	        avgNutrientResult2.values().forEach(v -> assertTrue("Average nutrient should be non-negative", v >= 0));

	        totalNutrientsResult1.values().forEach(v -> assertTrue("Total nutrient should be non-negative", v >= 0));
	        totalNutrientsResult2.values().forEach(v -> assertTrue("Total nutrient should be non-negative", v >= 0));
	    }
	        	 
	 // Testing  VisualCalculationService for getting Average Amount and Total Amount of nutrients for displaying graphs
	 @Test
	    public void UnitTest5_0() {
	    

		 	Map<Integer, Double> cheeseNutrients = Map.ofEntries(
		            entry(401, 0.0),
		            entry(301, 528.0),
					entry(303, 0.31),
					entry(306, 256.0),
					entry(307, 1146.0),
					entry(324, 21.0),
					entry(203, 21.4),
					entry(204, 28.74),
					entry(205, 2.34),
					entry(208, 353.0),
					entry(601, 75.0)
		        );

		        // Nutrients for Bread
		        Map<Integer, Double> breadNutrients = Map.ofEntries(
	        	    entry(401, 0.0),
	        	    entry(301, 68.0),
	        	    entry(303, 3.44),
	        	    entry(306, 117.0),
	        	    entry(307, 552.0),
	        	    entry(324, 13.0),
	        	    entry(203, 8.82),
	        	    entry(204, 2.51),
	        	    entry(205, 49.44),
	        	    entry(208, 260.0),
	        	    entry(601, 0.0)
		        );
		 
		        Map<Integer, Double> sandwichNutrients = new HashMap<>(cheeseNutrients);
		        breadNutrients.forEach((id, amt) ->
		            sandwichNutrients.merge(id, amt, Double::sum)
		        );
		        
		        FoodType lunch = new Lunch();

		        //just cheese
		        Map<Integer, Double> food1Ingredients = Map.of(18, 200.0);
		        
		        // just bread
		        Map<Integer, Double> food2Ingredients = Map.of(3700, 200.0);
		        
		        // cheese and bread
		        Map<Integer, Double> food3Ingredients = Map.of(
		        	    18, 200.0,    // Cheese
		        	    3700, 200.0   // Bread
		        	);
		        
		        // Create food objects
		        Food food1 = new Food(1, "Cheese", food1Ingredients, LocalDate.of(2025, 6, 1), lunch);
		        Food food2 = new Food(2, "Bread", food2Ingredients, LocalDate.of(2025, 6, 2), lunch);
		        Food food3 = new Food(3, "Cheese Sandwich", food3Ingredients, LocalDate.of(2025, 6, 3),lunch);
	   
		 
		     // Create a list of food on that date
		        List<Food> foodsJune = List.of(food1, food2, food3);

		        // Map to hold total nutrients
		        Map<Integer, Double> totalNutrients = new HashMap<>();

		        
		        for (Food food : foodsJune) {
		            Map<Integer, Double> ingredients = food.getIngredients();

		            for (Map.Entry<Integer, Double> ingredientEntry : ingredients.entrySet()) {
		                Integer ingredientId = ingredientEntry.getKey();
		                Double ingredientAmount = ingredientEntry.getValue();

		                Map<Integer, Double> nutrientsPer100g;

		                // Manually assign nutrient maps based on ingredientId
		                if (ingredientId == 18) {  // Cheese
		                    nutrientsPer100g = cheeseNutrients;
		                    
		                } else if (ingredientId == 3700) {  // Bread
		                    nutrientsPer100g = breadNutrients;
		                    
		                } else {
		                    // Unknown ingredient, skip or continue
		                    continue;
		                }

		                for (Map.Entry<Integer, Double> nutrientEntry : nutrientsPer100g.entrySet()) {
		                    Integer nutrientId = nutrientEntry.getKey();
		                    Double nutrientPer100g = nutrientEntry.getValue();

		                    Double nutrientAmount = (nutrientPer100g / 100.0) * ingredientAmount;

		                    totalNutrients.merge(nutrientId, nutrientAmount, Double::sum);
		                }
		            }
		        }
		        

		        Map<Integer, Double> avgNutrients = new HashMap<>();
		        
		        Set<LocalDate> distinctDays = foodsJune.stream()
                        .map(Food::getDate)
                        .collect(Collectors.toSet());

				int dayCount = distinctDays.size();
				if (dayCount == 0) dayCount = 1; // avoid division by zero
				
				
				for (Map.Entry<Integer, Double> entry : totalNutrients.entrySet()) {
				Integer nutrientId = entry.getKey();
				Double totalAmount = entry.getValue();
				avgNutrients.put(nutrientId, totalAmount / dayCount);
}

		 
		        assertEquals(avgNutrients, visualCalculationService.avgNutrients(foodsJune));
		        assertEquals(totalNutrients, visualCalculationService.totalNutrients(foodsJune));
		        
	 }
	 
	 @Test
	 	public void UnitTest6_0() {
	 		LocalDate today = LocalDate.of(2025, 6, 1);

	        // Create FoodType instances
	        FoodType breakfast = new Breakfast();
	        FoodType lunch = new Lunch();
	        FoodType dinner = new Dinner();
	        FoodType snack = new Snack();

	        // Build Food objects
	        List<Food> foodList = List.of(
	            new Food(1, "Beef Pot Roast Meal", Map.of(7, 100.0), today, dinner),
	            new Food(2, "Fried Chicken Meal", Map.of(8, 100.0), today, lunch),
	            new Food(3, "Meat Loaf Meal", Map.of(9, 100.0), today, dinner),
	            new Food(4, "Turkey Meal", Map.of(10, 100.0), today, lunch),
	            new Food(5, "Butter, whipped", Map.of(16, 10.0), today, breakfast),
	            new Food(6, "Blue Cheese", Map.of(18, 20.0), today, snack),
	            new Food(7, "Egg, dried", Map.of(83, 30.0), today, breakfast),
	            new Food(8, "Dried Apricot w/ Sugar", Map.of(1509, 40.0), today, snack),
	            new Food(9, "Pork Chop, Braised", Map.of(1786, 120.0), today, dinner),
	            new Food(10, "Snow Peas, Boiled", Map.of(2146, 50.0), today, lunch),
	            new Food(11, "Tomato Sauce, Canned", Map.of(2465, 30.0), today, lunch),
	            new Food(12, "Spot Fish, Raw", Map.of(3068, 130.0), today, dinner),
	            new Food(13, "Beef Jerky Snack", Map.of(4086, 25.0), today, snack),
	            new Food(14, "Canned Pineapple, Drained", Map.of(5394, 80.0), today, breakfast),
	            new Food(15, "Fried Tofu", Map.of(501799, 70.0), today, lunch),
	            new Food(16, "Cake with Icing", Map.of(501845, 90.0), today, snack),
	            new Food(17, "Artichoke", Map.of(501841, 60.0), today, lunch),
	            new Food(18, "Chicken", Map.of(567, 110.0), today, dinner),
	            new Food(19, "Frozen Broccoli", Map.of(2025, 100.0), today, lunch),
	            new Food(20, "Breaded Onion Rings", Map.of(2404, 60.0), today, snack),
	            new Food(21, "Italian Bread", Map.of(3700, 50.0), today, breakfast)
	        );

	        Map<String, Double> expectedPercentages = Map.ofEntries(
	        	    Map.entry("Finfish and Shellfish Products", 8.81),
	        	    Map.entry("Mixed Dishes", 27.12),
	        	    Map.entry("Poultry Products", 7.46),
	        	    Map.entry("Legumes and Legume Products", 4.75),
	        	    Map.entry("Dairy and Egg Products", 4.07),
	        	    Map.entry("Vegetables and Vegetable Products", 20.34),
	        	    Map.entry("Snacks", 1.69),
	        	    Map.entry("Pork Products", 8.14),
	        	    Map.entry("Baked Products", 9.49),
	        	    Map.entry("Fruits and fruit juices", 8.14)
	        	);

	        Map<String, Double> actualPercentages = visualCalculationService.getFoodGroupIntakePercentages(foodList);
	        
	        assertEquals(expectedPercentages.keySet(), actualPercentages.keySet());
	        for (String key : expectedPercentages.keySet()) {
	            double expected = expectedPercentages.get(key);
	            double actual = actualPercentages.get(key);
	            assertEquals(expected, actual, 0.01); // up to 0.01 difference
	        }
		
	 		
	 	}
	  
	 // Testing for Nutrient Recommendation based on Test User's age and gender (adult female)
	 @Test
	    public void UnitTest7_0() {
	    
	    		
		Profile testProfileAdultFemale = new Profile(
			    1,                                 // id
			    "Jane Doe",                        // name
			    Profile.Gender.FEMALE,                    // gender (assuming an enum Gender.FEMALE exists)
			    LocalDate.of(1990, 5, 15),         // date of birth (e.g., May 15, 1990)
			    165.0,                             // height in cm (assuming metric)
			    60.0,                              // weight in kg
			    Profile.Unit.METRIC                        // preferred unit (assuming Unit.METRIC is valid)
			);		
	    		
	    		
	    int age = Period.between(testProfileAdultFemale.getDateOfBirth(),LocalDate.now()).getYears();
	 	
	    Map<Integer, Double> expectedRecommendations = Map.ofEntries(
	    	    entry(203, 46.0),   // Protein
	    	    entry(205, 130.0),  // Carbohydrates
	    	    entry(204, 70.0),   // Fat
	    	    entry(208, 2000.0), // Calories
	    	    entry(601, 300.0),  // Cholesterol
	    	    entry(307, 1500.0), // Sodium
	    	    entry(306, 2600.0), // Potassium
	    	    entry(301, 1000.0), // Calcium
	    	    entry(303, 18.0),   // Iron
	    	    entry(401, 75.0),   // Vitamin C
	    	    entry(324, 15.0)    // Vitamin D
	        );

	        for (Map.Entry<Integer, Double> entry : expectedRecommendations.entrySet()) {
	            int nutrientCode = entry.getKey();
	            double expectedValue = entry.getValue();
	            double actualValue = cfgNutrientRecService.getCFGNutrientRecommendation(testProfileAdultFemale.getGender(), age, nutrientCode);
	            assertEquals("Mismatch for nutrient code: " + nutrientCode, expectedValue, actualValue, 0.0001);
	        }
	        
	        
	 }
	 

	 

	
	 
	 
}
