package visualization;

import java.time.LocalDate;
import java.util.Map;

import database.CSVDatabase;

public abstract class SingleSourceGraph extends Graph {
	
	
	protected Map<Integer, Double> mealListNutrientAmounts;
	
    public SingleSourceGraph(CSVDatabase database,LocalDate dateStart, LocalDate dateEnd, Map<Integer, Double> mealListNutrientAmounts) {
        super(database,dateStart,dateEnd);
    	this.mealListNutrientAmounts = mealListNutrientAmounts;
    }
	

}
