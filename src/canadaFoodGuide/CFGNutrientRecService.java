package canadaFoodGuide;

import java.util.Map;
import static java.util.Map.entry;
import java.util.HashMap;
import nutritionRequests.Nutrient;
import userService.*;



/*
 * 	
 * 	EER = 1,004.82 – (10.83 × age [y]) + (6.52 × height [cm]) + (15.91 × weight [kg])
 */

public class CFGNutrientRecService {

	
	private static final Map<Nutrient, Double> CFG_ADULT_FEMALE = Map.ofEntries(
		    entry(Nutrient.PROTEIN, 46.0),
		    entry(Nutrient.CARBOHYDRATES, 130.0),
		    entry(Nutrient.FAT, 70.0),
		    entry(Nutrient.CALORIES, 2000.0),
		    entry(Nutrient.CHOLESTEROL, 300.0),
		    entry(Nutrient.SODIUM, 1500.0),
		    entry(Nutrient.POTASSIUM, 2600.0),
		    entry(Nutrient.CALCIUM, 1000.0),
		    entry(Nutrient.IRON, 18.0),
		    entry(Nutrient.VITAMIN_C, 75.0),
		    entry(Nutrient.VITAMIN_D, 15.0)
		);

	private static final Map<Nutrient, Double> CFG_14to18_FEMALE = Map.ofEntries(
		    entry(Nutrient.PROTEIN, 46.0),
		    entry(Nutrient.CARBOHYDRATES, 130.0),
		    entry(Nutrient.FAT, 60.0),
		    entry(Nutrient.CALORIES, 2200.0),
		    entry(Nutrient.CHOLESTEROL, 300.0),
		    entry(Nutrient.SODIUM, 1500.0),
		    entry(Nutrient.POTASSIUM, 2300.0),
		    entry(Nutrient.CALCIUM, 1300.0),
		    entry(Nutrient.IRON, 15.0),
		    entry(Nutrient.VITAMIN_C, 65.0),
		    entry(Nutrient.VITAMIN_D, 15.0)
		);

	private static final Map<Nutrient, Double> CFG_9to13_FEMALE = Map.ofEntries(
		    entry(Nutrient.PROTEIN, 34.0),
		    entry(Nutrient.CARBOHYDRATES, 130.0),
		    entry(Nutrient.FAT, 50.0),
		    entry(Nutrient.CALORIES, 1800.0),
		    entry(Nutrient.CHOLESTEROL, 300.0),
		    entry(Nutrient.SODIUM, 1200.0),
		    entry(Nutrient.POTASSIUM, 2300.0),
		    entry(Nutrient.CALCIUM, 1300.0),
		    entry(Nutrient.IRON, 8.0),
		    entry(Nutrient.VITAMIN_C, 45.0),
		    entry(Nutrient.VITAMIN_D, 15.0)
		);

    private static final Map<Nutrient, Double> CFG_ADULT_MALE = Map.ofEntries(
    	    entry(Nutrient.PROTEIN, 56.0),
    	    entry(Nutrient.CARBOHYDRATES, 130.0),
    	    entry(Nutrient.FAT, 75.0),
    	    entry(Nutrient.CALORIES, 2500.0),
    	    entry(Nutrient.CHOLESTEROL, 300.0),
    	    entry(Nutrient.SODIUM, 1500.0),
    	    entry(Nutrient.POTASSIUM, 3400.0),
    	    entry(Nutrient.CALCIUM, 1000.0),
    	    entry(Nutrient.IRON, 8.0),
    	    entry(Nutrient.VITAMIN_C, 90.0),
    	    entry(Nutrient.VITAMIN_D, 15.0)
    	);
    
    private static final Map<Nutrient, Double> CFG_14to18_MALE = Map.ofEntries(
    	    entry(Nutrient.PROTEIN, 52.0),
    	    entry(Nutrient.CARBOHYDRATES, 130.0),
    	    entry(Nutrient.FAT, 60.0),
    	    entry(Nutrient.CALORIES, 2500.0),
    	    entry(Nutrient.CHOLESTEROL, 300.0),
    	    entry(Nutrient.SODIUM, 1500.0),
    	    entry(Nutrient.POTASSIUM, 3000.0),
    	    entry(Nutrient.CALCIUM, 1300.0),
    	    entry(Nutrient.IRON, 11.0),
    	    entry(Nutrient.VITAMIN_C, 75.0),
    	    entry(Nutrient.VITAMIN_D, 15.0)
    	);
    
    private static final Map<Nutrient, Double> CFG_9to13_MALE = Map.ofEntries(
    	    entry(Nutrient.PROTEIN, 34.0),
    	    entry(Nutrient.CARBOHYDRATES, 130.0),
    	    entry(Nutrient.FAT, 50.0),
    	    entry(Nutrient.CALORIES, 2500.0),
    	    entry(Nutrient.CHOLESTEROL, 300.0),
    	    entry(Nutrient.SODIUM, 1200.0),
    	    entry(Nutrient.POTASSIUM, 2500.0),
    	    entry(Nutrient.CALCIUM, 1300.0),
    	    entry(Nutrient.IRON, 8.0),
    	    entry(Nutrient.VITAMIN_C, 45.0),
    	    entry(Nutrient.VITAMIN_D, 15.0)
    	);
	
    public static final Map<String, Double> CFG_FOOD_GROUP_RECOMMENDATIONS = Map.ofEntries(
            Map.entry("Dairy and Egg Products", 3.0),
            Map.entry("Spices and Herbs", 0.1),
            Map.entry("Babyfoods", 0.0), 
            Map.entry("Fats and Oils", 2.0),
            Map.entry("Poultry Products", 2.0),
            Map.entry("Soups, Sauces and Gravies", 1.0),
            Map.entry("Sausages and Luncheon meats", 1.0),
            Map.entry("Breakfast cereals", 2.0),
            Map.entry("Fruits and fruit juices", 7.0),
            Map.entry("Pork Products", 1.5),
            Map.entry("Vegetables and Vegetable Products", 7.0),
            Map.entry("Nuts and Seeds", 1.0),
            Map.entry("Beef Products", 2.0),
            Map.entry("Beverages", 3.0),  
            Map.entry("Finfish and Shellfish Products", 2.0),
            Map.entry("Legumes and Legume Products", 2.0),
            Map.entry("Lamb, Veal and Game", 1.0),
            Map.entry("Baked Products", 2.0),
            Map.entry("Sweets", 1.0),
            Map.entry("Cereals, Grains and Pasta", 6.0),
            Map.entry("Fast Foods", 1.0),
            Map.entry("Mixed Dishes", 2.0),
            Map.entry("Snacks", 1.0)
        );
    
    
    public static Double getCFGNutrientRecommendation(Profile.Gender gender, int age, Nutrient nutrientChoice) {
        Map<Nutrient, Double> cfgMap;

        if (gender == Profile.Gender.FEMALE) {
            if (age >= 14 && age <= 18) {
                cfgMap = CFG_14to18_FEMALE;
            } else if (age >= 9 && age <= 13) {
                cfgMap = CFG_9to13_FEMALE;
            } else {
                cfgMap = CFG_ADULT_FEMALE;
            }
        } else if (gender == Profile.Gender.MALE) {
            if (age >= 14 && age <= 18) {
                cfgMap = CFG_14to18_MALE;
            } else if (age >= 9 && age <= 13) {
                cfgMap = CFG_9to13_MALE;
            } else {
                cfgMap = CFG_ADULT_MALE;
            }
        } else {
            // For OTHER or UNSPECIFIED, fallback to adult male or female as you prefer
            cfgMap = CFG_ADULT_MALE;
        }

        return cfgMap.getOrDefault(nutrientChoice, 0.0);
    }
	    
    public static Map<Integer, Double> getCFGNutrientAvgMap(Profile.Gender gender, int age){
        Map<Nutrient, Double> cfgMap;

        if (gender == Profile.Gender.FEMALE) {
            if (age >= 14 && age <= 18) {
                cfgMap = CFG_14to18_FEMALE;
            } else if (age >= 9 && age <= 13) {
                cfgMap = CFG_9to13_FEMALE;
            } else {
                cfgMap = CFG_ADULT_FEMALE;
            }
        } else if (gender == Profile.Gender.MALE) {
            if (age >= 14 && age <= 18) {
                cfgMap = CFG_14to18_MALE;
            } else if (age >= 9 && age <= 13) {
                cfgMap = CFG_9to13_MALE;
            } else {
                cfgMap = CFG_ADULT_MALE;
            }
        } else {
            // Fallback for OTHER or UNSPECIFIED
            cfgMap = CFG_ADULT_MALE;
        }

        // Convert Nutrient keys to Integer IDs
        Map<Integer, Double> cfgAvgMap = new HashMap<>();
        for (Map.Entry<Nutrient, Double> entry : cfgMap.entrySet()) {
            cfgAvgMap.put(entry.getKey().getID(), entry.getValue());
        }

        return cfgAvgMap;
    }
    
    public static Map<Integer, Double> getCFGNutrientTotalMap(Profile.Gender gender, int age, int dayCount){
    	
    	Map<Integer, Double> avgMap = getCFGNutrientAvgMap(gender, age);
        Map<Integer, Double> totalMap = new HashMap<>();

        for (Map.Entry<Integer, Double> entry : avgMap.entrySet()) {
            totalMap.put(entry.getKey(), entry.getValue() * dayCount);
        }

        return totalMap;
    	
    }
    
    public static Map<String, Double> getCFGFoodGroupRecommendationsMap(){
    	return CFG_FOOD_GROUP_RECOMMENDATIONS;
    }
    
	
}
