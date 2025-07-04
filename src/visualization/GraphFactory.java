package visualization;

import java.time.LocalDate;
import java.util.*;

import database.CSVDatabase;
import food.Food;

public class GraphFactory {

	
	//database, dateStart, dateEnd, mealListNutrientAmounts, swapMealListNutrientAmounts
	
	public Graph generateGraph(GraphType graphType, CSVDatabase database, LocalDate dateStart, LocalDate dateEnd, Map<Integer, Double> mealListNutrientAmounts, Map<Integer, Double> swapMealListNutrientAmounts) {
	    
		
		switch (graphType) {

		
        case NUTRIENT_AVGS_BAR_GRAPH: 
        	return new NutrientAvgsBarGraph(database, dateStart, dateEnd, mealListNutrientAmounts, swapMealListNutrientAmounts);
	
        case NUTRIENT_AVGS_PIE_GRAPH:
         	return new NutrientAvgsPieGraph(database, dateStart, dateEnd, mealListNutrientAmounts, swapMealListNutrientAmounts);
        	
         	
        	// other cases to be implemented in future
        	
	        /*case PROGRESS_GRAPH:
	        	return new ProgressGraph(mealList,subMealList);
	        	
	        case FOOD_SWAP_GRAPH:
	        	return new FoodSwapGraph(mealList,subMealList);


	        	

	        	
	        case NUTRIENT_LINE_CHART:
	        	return new NutrientLineChart(mealList,subMealList);
	        	
	        case FOOD_GUIDE_GRAPH:
	        	return new FoodGuideGraph(mealList,subMealList);*/
	       
	        default: throw new IllegalArgumentException("Unknown graph type");
	    }
	}
	
	
}
