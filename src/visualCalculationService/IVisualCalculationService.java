package visualCalculationService;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import food.Food;
import nutritionRequests.Nutrient;

public interface IVisualCalculationService {

	String getMealTag(Food meal);
	    
    Map<Integer, Double> avgNutrients(List<Food> foodList);
    
    Map<Integer, Double> totalNutrients(List<Food> foodList);
    
    int countDistinctDays(List<Food> foodList);
    
    List<Entry<String, Double>> getNutrientPerMealList(List<Food> foodList, Nutrient nutrientChoice);
    
    List<Entry<String, Double>> getNutrientByDateList(List<Food> foodList, Nutrient nutrientChoice);
    
    Map<String, Double> getFoodGroupIntakePercentages(List<Food> foodList);
    
}
