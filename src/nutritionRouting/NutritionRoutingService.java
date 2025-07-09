package nutritionRouting;


import Advising.*;

import java.util.List;

import org.jfree.chart.JFreeChart;
import foodService.*;
import graphService.*;
import nutritionRequests.AdvisedGraphRequest;
import nutritionRequests.HistoricalGraphRequest;
import food.Food;


public class NutritionRoutingService implements INutritionRoutingService {
	
	private IFoodService foodService;
	private AdvisingService advisingService;
	private IGraphService graphService;
	
	public NutritionRoutingService(IFoodService foodService, IGraphService graphService) {
	    this.foodService = foodService;
		this.graphService = graphService;
	    this.advisingService = new AdvisingService();
	}
	
	// for advised
	public JFreeChart createGraph(AdvisedGraphRequest request) {
		
		// sets mealList (from Database) and swapMealList (from Advising)
		Filter filter = request.getFilter();
		request.setMealList(foodService.getMeals(filter));
		
		List<Food> swapMealList = advisingService.produceSwapMeals(filter);
		request.setSwapMealList(swapMealList);
		
		return request.accept(graphService);		
	}
	
	// for historical
	public JFreeChart createGraph(HistoricalGraphRequest request) {
		
		// sets mealList (from Database)
		Filter filter = request.getFilter();
				
		request.setMealList(foodService.getMeals(filter));		
		
		return request.accept(graphService);		
	}
}
