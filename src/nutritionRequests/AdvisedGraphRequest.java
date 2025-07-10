package nutritionRequests;

import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;

import advisingTestForVisualization.NutritionGoal;
import food.Food;
import foodService.Filter;
import graphService.GraphRequestVisitor;

public abstract class AdvisedGraphRequest extends GraphRequest  {

	List<Food> swapMealList;
	Map<Integer, Double> swapMealListNutrientAmounts;
	NutritionGoal goal;
	
	public AdvisedGraphRequest(Filter filter, NutritionGoal goal) {
		super(filter);
		this.goal = goal;
	}

	public void setSwapMealList(List<Food> swapMealList) {this.swapMealList = swapMealList;}

	public List<Food> getSwapMealList() { return swapMealList;}
	
	public void setSwapMealListNutrientAmounts(Map<Integer, Double> swapMealListNutrientAmounts) {this.swapMealListNutrientAmounts = swapMealListNutrientAmounts;}
	
	public Map<Integer, Double> getSwapMealListNutrientAmounts(){ return swapMealListNutrientAmounts; }
	
	public void setGoal(NutritionGoal goal) {this.goal = goal; }
	
	public NutritionGoal getGoal() { return goal; }
	
	
}