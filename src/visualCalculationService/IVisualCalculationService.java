package visualCalculationService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import food.Food;

/*VisualCalculation Service performs the various calculations requested by graphService
 *
 */

public interface IVisualCalculationService {

	String getMealTag(Food meal);

    Map<Integer, Double> avgNutrients(List<Food> foodList);

    Map<Integer, Double> totalNutrients(List<Food> foodList);

    int countDistinctDays(List<Food> foodList);

    List<Entry<String, Double>> getNutrientPerMealList(List<Food> foodList, int nutrientChoice);

    List<Entry<LocalDate, Double>> getNutrientByDateList(List<Food> foodList, int nutrientChoice);

    Map<String, Double> getFoodGroupIntakePercentages(List<Food> foodList);

}
