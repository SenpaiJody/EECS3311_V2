package visualization;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;

import database.CSVDatabase;
import food.Food;

public abstract class AvgsGraph extends Graph {

	
	protected Map<Integer, Double> mealListNutrientAmounts;
	protected Map<Integer, Double> swapMealListNutrientAmounts;
	
    public AvgsGraph(CSVDatabase database,LocalDate dateStart, LocalDate dateEnd, Map<Integer, Double> mealListNutrientAmounts, Map<Integer, Double> swapMealListNutrientAmounts) {
        super(database,dateStart,dateEnd);
    	this.mealListNutrientAmounts = mealListNutrientAmounts;
        this.swapMealListNutrientAmounts = swapMealListNutrientAmounts;
    }

	
	public JFreeChart createGraph() {
		// TODO Auto-generated method stub
		return null;
	}

}