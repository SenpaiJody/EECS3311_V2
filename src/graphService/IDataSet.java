package graphService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import food.Food;

/*IDataSet makes some standaridizations in that while the FoodData sets already have some
 * data available, the CFG does not. Thus the methods are designed as such that when iterating
 * over a List<IDataSet> object, the same function can be called for any dataset and get the desired result
 */

public interface IDataSet {

	public String getLegendLabel();

	public List<Food> getFoodList();

	public List<Map.Entry<LocalDate, Double>> getNutrientByDateList (TreeSet<LocalDate> uniqueDates, int nutrientChoice);

	public Map<String, Double> getFoodGroupPercentages();

	public Map<Integer, Double> getAvgNutrientAmounts();

	public Map<Integer, Double> getTotalNutrientAmounts(int distinctDaysCount);
	
	public List<LocalDate> getDefaultDateList();


}
