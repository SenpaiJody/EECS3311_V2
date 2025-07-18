package cfgNutrientRecService;

import static java.util.Map.entry;

import java.util.HashMap;
import java.util.Map;

import userService.Profile;



/*Details of Calculations Nutrient IDs:
 * Protein (203), Fats (204), Carbs (205), Calories (208), Cholesterol (601), Sodium (307), Potassium (306), Calcium(301), Iron (303), Vitamin C(401) and Vitamin D (324)
 *
 */

public class CFGNutrientRecService implements ICFGNutrientRecService {


	private static final Map<Integer, Double> CFG_ADULT_FEMALE = Map.ofEntries(
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


	private static final Map<Integer, Double> CFG_14to18_FEMALE = Map.ofEntries(
		    entry(203, 46.0),   // Protein
		    entry(205, 130.0),  // Carbohydrates
		    entry(204, 60.0),   // Fat
		    entry(208, 2200.0), // Calories
		    entry(601, 300.0),  // Cholesterol
		    entry(307, 1500.0), // Sodium
		    entry(306, 2300.0), // Potassium
		    entry(301, 1300.0), // Calcium
		    entry(303, 15.0),   // Iron
		    entry(401, 65.0),   // Vitamin C
		    entry(324, 15.0)    // Vitamin D
		);

	private static final Map<Integer, Double> CFG_9to13_FEMALE = Map.ofEntries(
		    entry(203, 34.0),    // Protein
		    entry(205, 130.0),   // Carbohydrates
		    entry(204, 50.0),    // Fat
		    entry(208, 1800.0),  // Calories
		    entry(601, 300.0),   // Cholesterol
		    entry(307, 1200.0),  // Sodium
		    entry(306, 2300.0),  // Potassium
		    entry(301, 1300.0),  // Calcium
		    entry(303, 8.0),     // Iron
		    entry(401, 45.0),    // Vitamin C
		    entry(324, 15.0)     // Vitamin D
		);

    private static final Map<Integer, Double> CFG_ADULT_MALE = Map.ofEntries(
    	    entry(203, 56.0),    // Protein
    	    entry(205, 130.0),   // Carbohydrates
    	    entry(204, 75.0),    // Fat
    	    entry(208, 2500.0),  // Calories
    	    entry(601, 300.0),   // Cholesterol
    	    entry(307, 1500.0),  // Sodium
    	    entry(306, 3400.0),  // Potassium
    	    entry(301, 1000.0),  // Calcium
    	    entry(303, 8.0),     // Iron
    	    entry(401, 90.0),    // Vitamin C
    	    entry(324, 15.0)     // Vitamin D
    	);

    private static final Map<Integer, Double> CFG_14to18_MALE = Map.ofEntries(
    	    entry(203, 52.0),    // Protein
    	    entry(205, 130.0),   // Carbohydrates
    	    entry(204, 60.0),    // Fat
    	    entry(208, 2500.0),  // Calories
    	    entry(601, 300.0),   // Cholesterol
    	    entry(307, 1500.0),  // Sodium
    	    entry(306, 3000.0),  // Potassium
    	    entry(301, 1300.0),  // Calcium
    	    entry(303, 11.0),    // Iron
    	    entry(401, 75.0),    // Vitamin C
    	    entry(324, 15.0)     // Vitamin D
    	);

    private static final Map<Integer, Double> CFG_9to13_MALE = Map.ofEntries(
    	    entry(203, 34.0),    // Protein
    	    entry(205, 130.0),   // Carbohydrates
    	    entry(204, 50.0),    // Fat
    	    entry(208, 2500.0),  // Calories
    	    entry(601, 300.0),   // Cholesterol
    	    entry(307, 1200.0),  // Sodium
    	    entry(306, 2500.0),  // Potassium
    	    entry(301, 1300.0),  // Calcium
    	    entry(303, 8.0),     // Iron
    	    entry(401, 45.0),    // Vitamin C
    	    entry(324, 15.0)     // Vitamin D
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

    @Override
    public Double getCFGNutrientRecommendation(Profile.Gender gender, int age, int nutrientChoice) {
        Map<Integer, Double> cfgMap;

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

    @Override
    public Map<Integer, Double> getCFGNutrientAvgMap(Profile.Gender gender, int age){
        Map<Integer, Double> cfgAvgMap;

        if (gender == Profile.Gender.FEMALE) {
            if (age >= 14 && age <= 18) {
                cfgAvgMap = CFG_14to18_FEMALE;
            } else if (age >= 9 && age <= 13) {
                cfgAvgMap = CFG_9to13_FEMALE;
            } else {
                cfgAvgMap = CFG_ADULT_FEMALE;
            }
        } else if (gender == Profile.Gender.MALE) {
            if (age >= 14 && age <= 18) {
                cfgAvgMap = CFG_14to18_MALE;
            } else if (age >= 9 && age <= 13) {
                cfgAvgMap = CFG_9to13_MALE;
            } else {
                cfgAvgMap = CFG_ADULT_MALE;
            }
        } else {
            // Fallback for OTHER or UNSPECIFIED
            cfgAvgMap = CFG_ADULT_MALE;
        }

        return cfgAvgMap;
    }

    @Override
    public Map<Integer, Double> getCFGNutrientTotalMap(Profile.Gender gender, int age, int dayCount){

    	Map<Integer, Double> avgMap = getCFGNutrientAvgMap(gender, age);
        Map<Integer, Double> totalMap = new HashMap<>();

        for (Map.Entry<Integer, Double> entry : avgMap.entrySet()) {
            totalMap.put(entry.getKey(), entry.getValue() * dayCount);
        }

        return totalMap;

    }

    @Override
    public Map<String, Double> getCFGFoodGroupRecommendationsMap(){
    	return CFG_FOOD_GROUP_RECOMMENDATIONS;
    }


}
