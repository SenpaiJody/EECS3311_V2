package visualization;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import database.CSVDatabase;
import food.Food;

public class NutrientLineChart extends SingleSourceGraph{
	
	public  NutrientLineChart(CSVDatabase database, LocalDate dateStart, LocalDate dateEnd, Map<Integer, Double> mealListNutrientAmounts) {
		super(database, dateStart, dateEnd, mealListNutrientAmounts);
    }
	
}
