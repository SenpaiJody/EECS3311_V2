package visualization;

import java.time.LocalDate;
import java.util.*;

import database.CSVDatabase;
import foodService.Filter;
import food.Food;

import org.jfree.chart.JFreeChart;

public class GraphService {

	private foodService.Filter filter;
	private final GraphFactory graphFactory;
	private final database.CSVDatabase csvDatabase;
	//private NutriCalc calc;
	
	
	// GraphService's constructor
	public GraphService(GraphFactory graphFactory, database.CSVDatabase csvDatabase) {
	    this.graphFactory = graphFactory;
	    this.csvDatabase = csvDatabase;
	}
    
    public JFreeChart generateGraph(GraphType graphType, CSVDatabase database, LocalDate dateStart, LocalDate dateEnd, Map<Integer, Double> mealListNutrientAmounts, Map<Integer, Double> swapMealListNutrientAmounts) {
    	
    	
    	Graph graph = graphFactory.generateGraph(graphType, database, dateStart, dateEnd, mealListNutrientAmounts, swapMealListNutrientAmounts);
 	
    	JFreeChart chart = graph.createGraph();
    	
        return chart;
    }
	

}