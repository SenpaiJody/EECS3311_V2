package visualCalculationService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import food.Food;

/*VisualCalculation Service performs the various calculations requested by graphService
 * While the logic is similar to NutriCalc, it has a different responsibility in that
 * it is transforming the data specifically for visualization
 */

public interface IVisualCalculationService {

	String getMealTag(Food meal);

    Map<Integer, Double> avgNutrients(List<Food> foodList);

    Map<Integer, Double> totalNutrients(List<Food> foodList);

    // necessary for calculating total amounts of nutrients (ie. for CFG data: expected value * number of distinct days = total)
    int countDistinctDays(List<Food> foodList);

    // this was used from a previous interpretation of the use cases, could be used for refactoring/adding feactors
    List<Entry<String, Double>> getNutrientPerMealList(List<Food> foodList, int nutrientChoice);

    List<Entry<LocalDate, Double>> getNutrientByDateList(List<Food> foodList, int nutrientChoice);

    Map<String, Double> getFoodGroupIntakePercentages(List<Food> foodList);

}
