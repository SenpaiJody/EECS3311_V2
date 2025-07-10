package nutritionRequests;


import foodService.Filter;
import graphService.GraphRequestVisitor;
import graphService.IGraphService;
import userService.IUserService;
import userService.Profile;
import userService.UserServiceFactory;

import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;

import food.Food;


/* If single source, it needs a meal list
 * 
 * if with advising it needs a subMeallist
 * 
 * NutritionGoal is used for Advising based GraphRequests
 * 
 * GraphRequest and GraphRequestAdvised
 *     PROGRESS_GRAPH,
    NUTRIENT_AVGS_BAR_GRAPH,
    NUTRIENT_AVGS_PIE_GRAPH,
    NUTRIENT_LINE_CHART,
    FOOD_GUIDE_GRAPH;
    
    Need to consider Averages / Cumulative
    
    Write out all the use cases and figure out what is the total number of graphs needed?
    Does every one need average + cumulative?\
    
    Just stick with polymorphism as we can justify it works w OCP
    
 * 
 *public JFreeChart generateGraph(GraphType graphType, CSVDatabase database,
 * LocalDate dateStart, LocalDate dateEnd, Map<Integer, Double> mealListNutrientAmounts,
 *  Map<Integer, Double> swapMealListNutrientAmounts) {
 */


public abstract class GraphRequest {

	private Profile profile;
	private Filter filter;
	Boolean CFGComparison;
	GraphMode mode;
	Nutrient nutrientChoice = Nutrient.PROTEIN;
	List<Food> mealList;
	Map<Integer, Double> mealListNutrientAmounts;
	List<Map.Entry<String, Double>> nutrientByDateList;
	List<Map.Entry<String, Double>> nutrientPerMealList;
	Map<Integer, Double> CFGNutrientAvgMap;
	Map<Integer, Double> CFGNutrientTotalMap;
	double CFGNUtrientRecommendation;
	IUserService userService;
	
	
	/* two different constructors: Historical and Advised
	 * Historical does not require a goal, whereas advised does
	 */
	
	public GraphRequest(Filter filter) {
		this.filter = filter;
		this.userService = UserServiceFactory.getService();;
		this.profile = userService.getCurrentProfile();
	}
	
    public abstract JFreeChart accept(GraphRequestVisitor visitor);
	
	public Profile getProfile() {return profile;}
	
	public void setProfile(Profile profile) {this.profile = profile;}
	
	public Filter getFilter() { return filter; }
	
	public Boolean getCFGComparison() { return CFGComparison;}
	
	public void setCFGComparison(Boolean CFGComparison) {this.CFGComparison = CFGComparison;}
	
	public GraphMode getGraphMode() {return mode;}
	
	public void setGraphMode(GraphMode mode) {this.mode = mode;}
	
	public Nutrient getNutrientChoice() { return nutrientChoice; }
	
	public void setNutrientChoice(Nutrient nutrientChoice) { this.nutrientChoice = nutrientChoice; }
	
	public void setMealList(List<Food> mealList) {this.mealList = mealList;}

	public List<Food> getMealList() { return mealList;}
	
	public void setMealListNutrientAmounts(Map<Integer, Double> mealListNutrientAmounts) {this.mealListNutrientAmounts = mealListNutrientAmounts;}
	
	public Map<Integer, Double> getMealListNutrientAmounts(){ return mealListNutrientAmounts; }
	
	public void setnutrientByDateList(List<Map.Entry<String, Double>> nutrientByDateList) {this.nutrientByDateList = nutrientByDateList;}
	
	public List<Map.Entry<String, Double>> getNutrientByDateList() {return nutrientByDateList; }
	

}
