package visualization;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import database.CSVDatabase;
import food.Food;

public class FoodGuideGraph extends SingleSourceGraph {

	public FoodGuideGraph(CSVDatabase database, LocalDate dateStart, LocalDate dateEnd, Map<Integer, Double> mealListNutrientAmounts) {
		super(database, dateStart, dateEnd, mealListNutrientAmounts);
    }
	
}