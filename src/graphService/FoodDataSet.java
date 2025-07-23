package graphService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import food.Food;
import visualCalculationService.IVisualCalculationService;
import visualCalculationService.VisualCalculationServiceFactory;

public class FoodDataSet implements IDataSet {

	String legendLabel;
	List<Food> foodList;
	List<LocalDate> defaultDateList;

    IVisualCalculationService visualCalc = VisualCalculationServiceFactory.getService();

	public FoodDataSet (String legendLabel, List<Food> foodList) {
		this.legendLabel = legendLabel;
		this.foodList = foodList;
		this.defaultDateList = new ArrayList<>();
		defaultDateList.add(LocalDate.of(1970, 1, 1));
	}

	@Override
	public String getLegendLabel() { return legendLabel; }

	@Override
	public List<Food> getFoodList() { return foodList; }

	@Override
	public List<Map.Entry<LocalDate, Double>> getNutrientByDateList (TreeSet<LocalDate> uniqueDates, int nutrientChoice) {
		List<Map.Entry<LocalDate, Double>> nutrientByDateList = visualCalc.getNutrientByDateList(foodList, nutrientChoice) ;
		return nutrientByDateList;
	}

	@Override
	public Map<String, Double> getFoodGroupPercentages() {

		Map<String, Double> foodGroupPercentages = visualCalc.getFoodGroupIntakePercentages(foodList);

		return foodGroupPercentages;
	}

	@Override
	public Map<Integer, Double> getAvgNutrientAmounts() {

		Map<Integer, Double> foodListNutrientAmounts = visualCalc.avgNutrients(foodList);

		return foodListNutrientAmounts;
	}

	@Override
	public Map<Integer, Double> getTotalNutrientAmounts(int distinctDaysCount) {

		Map<Integer, Double> foodListNutrientAmounts = visualCalc.totalNutrients(foodList);

		return foodListNutrientAmounts;
	}

	@Override
	public List<LocalDate> getDefaultDateList() { return defaultDateList; }


}
