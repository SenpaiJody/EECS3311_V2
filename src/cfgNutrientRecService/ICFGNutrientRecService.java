package cfgNutrientRecService;

import java.util.Map;

import userService.Profile.Gender;

public interface ICFGNutrientRecService {

    /**
     * Returns the daily CFG nutrient recommendation for a given gender, age, and nutrient ID.
     */
    Double getCFGNutrientRecommendation(Gender gender, int age, int nutrientChoice);

    /**
     * Returns the average CFG nutrient recommendation map for a given gender and age.
     */
    Map<Integer, Double> getCFGNutrientAvgMap(Gender gender, int age);

    /**
     * Returns the total CFG nutrient recommendation map over a given number of days.
     */
    Map<Integer, Double> getCFGNutrientTotalMap(Gender gender, int age, int dayCount);

    /**
     * Returns CFG food group recommendations.
     */
    Map<String, Double> getCFGFoodGroupRecommendationsMap();
	
}
