package graphService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import food.Food;

public interface IDataSet {

	public String getLegendLabel();

	public List<Food> getFoodList();

	public List<Map.Entry<LocalDate, Double>> getNutrientByDateList (TreeSet<LocalDate> uniqueDates, int nutrientChoice);

	public Map<String, Double> getFoodGroupPercentages();

	public Map<Integer, Double> getAvgNutrientAmounts();

	public Map<Integer, Double> getTotalNutrientAmounts(int distinctDaysCount);
	
	public List<LocalDate> getDefaultDateList();


}
