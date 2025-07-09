package graphService;



import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import nutritionRequests.*;
import userService.Profile.Gender;
import visualCalculationService.*;

import org.jfree.chart.JFreeChart;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import canadaFoodGuide.CFGNutrientRecService;
import visualizationService.IVisualizationService;


public class GraphService implements IGraphService, GraphRequestVisitor {

	private IVisualCalculationService visualCalc;
    private IVisualizationService visualizationService;
	
    private String userIntakeLegendLabel = "User Intake";
    private String CFGRecommendationLegendLabel = "CFG Adequate Intake";
    private String advisedIntakeLegendLabel = "Advised Intake";
    
    private String avgIntakeTitle = "Average Daily Nutrient Intake: ";
    private String totalIntakeTitle = "Cumulative Nutrient Intake: ";
    private String nutrientAmtByDateTitle = "Nutrient Amount By Date: ";
    private String nutrientAmtPerMealTitle = "Nutrient Amount Per Meal: ";
    private String FoodGroupIntakeTitle = "Food Group Percentage Intake: ";


    public GraphService(IVisualCalculationService visualCalc, IVisualizationService visualizationService) {
        this.visualCalc = visualCalc;
        this.visualizationService = visualizationService;
    }
    
    protected boolean genderAgeInvalid(Gender gender, int age) {
        return (gender != Gender.MALE && gender != Gender.FEMALE) || age < 9;
    }
	
    public boolean isInvalidCFGRequest(GraphRequest request) {
        if (request == null || request.getProfile() == null) {
            return true; 
        }
        Gender gender = request.getProfile().getGender();
        int age = Period.between(request.getProfile().getDateOfBirth(), LocalDate.now()).getYears();

        return genderAgeInvalid(gender, age); 
    }
     
	public JFreeChart createGraph(AvgBarAdvisedGraphRequest request) {
	
		Map<Integer, Double> mealListNutrientAmounts = visualCalc.avgNutrients(request.getMealList());	
		Map<Integer, Double> swapMealListNutrientAmounts = visualCalc.avgNutrients(request.getSwapMealList());
		
	    LocalDate dateStart = request.getFilter().getStartDate();
	    LocalDate dateEnd = request.getFilter().getEndDate();
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		visualizationService.populateBarGraphDataset(dataset, mealListNutrientAmounts, swapMealListNutrientAmounts);		
		JFreeChart chart = visualizationService.formatBarGraph(dataset, avgIntakeTitle, dateStart, dateEnd );
	    
		return chart;
	
	}
	
	public JFreeChart createGraph(AvgBarCFGAdvisedGraphRequest request) {
		    
		    if (isInvalidCFGRequest(request)) { return visualizationService.createErrorGraph(); }

			Gender gender = request.getProfile().getGender();
			int age = Period.between(request.getProfile().getDateOfBirth(), LocalDate.now()).getYears();			
			Map<Integer, Double> mealListNutrientAmounts = visualCalc.avgNutrients(request.getMealList());	
			Map<Integer, Double> swapMealListNutrientAmounts = visualCalc.avgNutrients(request.getSwapMealList());
			Map<Integer, Double> CFGNutrientAvgMap = CFGNutrientRecService.getCFGNutrientAvgMap(gender, age);
			
		    LocalDate dateStart = request.getFilter().getStartDate();
		    LocalDate dateEnd = request.getFilter().getEndDate();
			
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			visualizationService.populateCFGBarGraphDataset(dataset, mealListNutrientAmounts, swapMealListNutrientAmounts, CFGNutrientAvgMap);
			
			JFreeChart chart = visualizationService.formatBarGraph(dataset, avgIntakeTitle,dateStart, dateEnd);
			return chart;    
		    }
	
	public JFreeChart createGraph(AvgBarCFGHistoricalGraphRequest request) {

	    if (isInvalidCFGRequest(request)) { return visualizationService.createErrorGraph(); }

	    Gender gender = request.getProfile().getGender();
	    int age = Period.between(request.getProfile().getDateOfBirth(), LocalDate.now()).getYears();
	    Map<Integer, Double> mealListNutrientAmounts = visualCalc.avgNutrients(request.getMealList());
	    Map<Integer, Double> CFGNutrientAvgMap = CFGNutrientRecService.getCFGNutrientAvgMap(gender, age);

	    LocalDate dateStart = request.getFilter().getStartDate();
	    LocalDate dateEnd = request.getFilter().getEndDate();
	    
	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	    visualizationService.populateCFGBarGraphDataset(dataset, mealListNutrientAmounts, CFGNutrientAvgMap);

	    JFreeChart chart = visualizationService.formatBarGraph(dataset, avgIntakeTitle,dateStart, dateEnd);
	    return chart;
	}
	
	public JFreeChart createGraph(AvgBarHistoricalGraphRequest request) {

	    Map<Integer, Double> mealListNutrientAmounts = visualCalc.avgNutrients(request.getMealList());

	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	    visualizationService.populateBarGraphDataset(dataset, mealListNutrientAmounts);

	    LocalDate dateStart = request.getFilter().getStartDate();
	    LocalDate dateEnd = request.getFilter().getEndDate();
	    
	    JFreeChart chart = visualizationService.formatBarGraph(dataset, avgIntakeTitle,dateStart, dateEnd);
	    return chart;
	}
	
	public JFreeChart createGraph(AvgPieAdvisedGraphRequest request) {

	    Map<Integer, Double> mealListNutrientAmounts = visualCalc.avgNutrients(request.getMealList());
	    Map<Integer, Double> swapMealListNutrientAmounts = visualCalc.avgNutrients(request.getSwapMealList());

	    LocalDate dateStart = request.getFilter().getStartDate();
	    LocalDate dateEnd = request.getFilter().getEndDate();
	    
	    DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
	    visualizationService.populatePieGraphDataset(dataset, mealListNutrientAmounts);

	    DefaultPieDataset<String> datasetAdvised = new DefaultPieDataset<>();
	    visualizationService.populatePieGraphDataset(datasetAdvised, swapMealListNutrientAmounts);

	    JFreeChart chart = visualizationService.formatDualPieChart(
	        dataset,
	        datasetAdvised,
	        avgIntakeTitle,
	        userIntakeLegendLabel,
	        advisedIntakeLegendLabel,dateStart, dateEnd
	    );

	    return chart;
	}
	
	public JFreeChart createGraph(AvgPieCFGAdvisedGraphRequest request) {

		
	    if (isInvalidCFGRequest(request)) { return visualizationService.createErrorGraph(); }
		
	    Gender gender = request.getProfile().getGender();
	    int age = Period.between(request.getProfile().getDateOfBirth(), LocalDate.now()).getYears();

	    Map<Integer, Double> mealListNutrientAmounts = visualCalc.avgNutrients(request.getMealList());
	    Map<Integer, Double> swapMealListNutrientAmounts = visualCalc.avgNutrients(request.getSwapMealList());
	    Map<Integer, Double> CFGNutrientAvgMap = CFGNutrientRecService.getCFGNutrientAvgMap(gender, age);

	    LocalDate dateStart = request.getFilter().getStartDate();
	    LocalDate dateEnd = request.getFilter().getEndDate();
	    
	    DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
	    visualizationService.populatePieGraphDataset(dataset, mealListNutrientAmounts);

	    DefaultPieDataset<String> datasetAdvised = new DefaultPieDataset<>();
	    visualizationService.populatePieGraphDataset(datasetAdvised, swapMealListNutrientAmounts);

	    DefaultPieDataset<String> datasetCFG = new DefaultPieDataset<>();
	    visualizationService.populatePieGraphDataset(datasetCFG, CFGNutrientAvgMap);

	    JFreeChart chart = visualizationService.formatTriplePieChart(
	        dataset,
	        datasetAdvised,
	        datasetCFG,
	        avgIntakeTitle,
	        userIntakeLegendLabel,
	        advisedIntakeLegendLabel,
	        CFGRecommendationLegendLabel,dateStart, dateEnd
	    );

	    return chart;
	}
	
	public JFreeChart createGraph(AvgPieCFGHistoricalGraphRequest request) {

	    if (isInvalidCFGRequest(request)) { return visualizationService.createErrorGraph(); }

	    Gender gender = request.getProfile().getGender();
	    int age = Period.between(request.getProfile().getDateOfBirth(), LocalDate.now()).getYears();
	    Map<Integer, Double> mealListNutrientAmounts = visualCalc.avgNutrients(request.getMealList());
	    Map<Integer, Double> CFGNutrientAvgMap = CFGNutrientRecService.getCFGNutrientAvgMap(gender, age);

	    LocalDate dateStart = request.getFilter().getStartDate();
	    LocalDate dateEnd = request.getFilter().getEndDate();
	    
	    DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
	    visualizationService.populatePieGraphDataset(dataset, mealListNutrientAmounts);

	    DefaultPieDataset<String> datasetCFG = new DefaultPieDataset<>();
	    visualizationService.populatePieGraphDataset(datasetCFG, CFGNutrientAvgMap);

	    JFreeChart chart = visualizationService.formatDualPieChart(
	        dataset,
	        datasetCFG,
	        avgIntakeTitle,
	        userIntakeLegendLabel,
	        CFGRecommendationLegendLabel,dateStart, dateEnd
	    );

	    return chart;
	}

	public JFreeChart createGraph(AvgPieHistoricalGraphRequest request) {

	    Map<Integer, Double> mealListNutrientAmounts = visualCalc.avgNutrients(request.getMealList());

	    LocalDate dateStart = request.getFilter().getStartDate();
	    LocalDate dateEnd = request.getFilter().getEndDate();
	    
	    DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
	    visualizationService.populatePieGraphDataset(dataset, mealListNutrientAmounts);

	    JFreeChart chart = visualizationService.formatPieGraph(dataset, avgIntakeTitle,dateStart, dateEnd);

	    return chart;
	}

	public JFreeChart createGraph(NutrientByDateLineAdvisedGraphRequest request) {

	    List<Map.Entry<String, Double>> nutrientByDateList = visualCalc.getNutrientByDateList(request.getMealList(), request.getNutrientChoice());

	    List<Map.Entry<String, Double>> swapNutrientByDateList = visualCalc.getNutrientByDateList(request.getSwapMealList(),request.getNutrientChoice());

	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	    visualizationService.populateLineGraphDataset(dataset, nutrientByDateList, swapNutrientByDateList);

	    JFreeChart chart = visualizationService.formatLineGraph(
	        dataset,
	        nutrientAmtByDateTitle,
	        request.getNutrientChoice().getID()
	    );

	    return chart;
	}
	
	public JFreeChart createGraph(NutrientByDateLineCFGAdvisedGraphRequest request) {

	    if (isInvalidCFGRequest(request)) { return visualizationService.createErrorGraph(); }

	    Gender gender = request.getProfile().getGender();
	    int age = Period.between(request.getProfile().getDateOfBirth(), LocalDate.now()).getYears();

	    Nutrient nutrientChoice = request.getNutrientChoice();
	    double cfgRecommendation = CFGNutrientRecService.getCFGNutrientRecommendation(gender, age, nutrientChoice);

	    List<Map.Entry<String, Double>> nutrientByDateList = visualCalc.getNutrientByDateList(request.getMealList(), nutrientChoice);

	    List<Map.Entry<String, Double>> swapNutrientByDateList = visualCalc.getNutrientByDateList(request.getSwapMealList(),nutrientChoice);

	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	    visualizationService.populateLineGraphDataset(dataset, nutrientByDateList, swapNutrientByDateList, cfgRecommendation);

	    JFreeChart chart = visualizationService.formatLineGraph(dataset, nutrientAmtByDateTitle, nutrientChoice.getID());
	    return chart;
	}
	
	public JFreeChart createGraph(NutrientByDateLineCFGHistoricalGraphRequest request) {
		
	    if (isInvalidCFGRequest(request)) { return visualizationService.createErrorGraph(); }
		
	    Gender gender = request.getProfile().getGender();
	    int age = Period.between(request.getProfile().getDateOfBirth(), LocalDate.now()).getYears();

	    Nutrient nutrientChoice = request.getNutrientChoice();
	    double CFGNUtrientRecommendation = CFGNutrientRecService.getCFGNutrientRecommendation(gender, age, nutrientChoice);

	    List<Map.Entry<String, Double>> nutrientByDateList = visualCalc.getNutrientByDateList(request.getMealList(),nutrientChoice);

	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	    visualizationService.populateLineGraphDataset(dataset, nutrientByDateList, CFGNUtrientRecommendation);

	    JFreeChart chart = visualizationService.formatLineGraph(dataset, nutrientAmtByDateTitle, nutrientChoice.getID());

	    return chart;
	}

	public JFreeChart createGraph(NutrientByDateLineHistoricalGraphRequest request) {

	    List<Map.Entry<String, Double>> nutrientByDateList =  visualCalc.getNutrientByDateList(request.getMealList(), request.getNutrientChoice());

	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

	    visualizationService.populateLineGraphDataset(dataset, nutrientByDateList);

	    JFreeChart chart = visualizationService.formatLineGraph(dataset, nutrientAmtByDateTitle, request.getNutrientChoice().getID() );

	    return chart;
	}
	
	public JFreeChart createGraph(NutrientPerMealLineAdvisedGraphRequest request) {
		
	    List<Map.Entry<String, Double>> nutrientPerMealList = visualCalc.getNutrientPerMealList(request.getMealList(), request.getNutrientChoice());
	    List<Map.Entry<String, Double>> swapNutrientPerMealList = visualCalc.getNutrientPerMealList(request.getSwapMealList(), request.getNutrientChoice());
	    
	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	    visualizationService.populateLineGraphDataset(dataset, nutrientPerMealList, swapNutrientPerMealList);
	    
	    JFreeChart chart = visualizationService.formatLineGraph(dataset, nutrientAmtPerMealTitle, request.getNutrientChoice().getID());
	    return chart;
	}
	
	public JFreeChart createGraph(NutrientPerMealLineHistoricalGraphRequest request) {

	    List<Map.Entry<String, Double>> nutrientPerMealList = visualCalc.getNutrientPerMealList(request.getMealList(), request.getNutrientChoice());
	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	    visualizationService.populateLineGraphDataset(dataset, nutrientPerMealList);
	    JFreeChart chart = visualizationService.formatLineGraph(dataset, nutrientAmtPerMealTitle, request.getNutrientChoice().getID());
	    
	    return chart;
	}
	
	public JFreeChart createGraph(TotalBarAdvisedGraphRequest request) {
	    Map<Integer, Double> mealListNutrientAmounts = visualCalc.totalNutrients(request.getMealList());
	    Map<Integer, Double> swapMealListNutrientAmounts = visualCalc.totalNutrients(request.getSwapMealList());
	    
	    LocalDate dateStart = request.getFilter().getStartDate();
	    LocalDate dateEnd = request.getFilter().getEndDate();
	    
	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	    visualizationService.populateBarGraphDataset(dataset, mealListNutrientAmounts, swapMealListNutrientAmounts);
	    JFreeChart chart = visualizationService.formatBarGraph(dataset, totalIntakeTitle,dateStart, dateEnd);
	    return chart;
	}
	
	public JFreeChart createGraph(TotalBarCFGAdvisedGraphRequest request) {

		if (isInvalidCFGRequest(request)) { return visualizationService.createErrorGraph(); }
		
	    Gender gender = request.getProfile().getGender();
	    int age = Period.between(request.getProfile().getDateOfBirth(), LocalDate.now()).getYears();
	    
	    Map<Integer, Double> mealListNutrientAmounts = visualCalc.totalNutrients(request.getMealList());
	    Map<Integer, Double> swapMealListNutrientAmounts = visualCalc.totalNutrients(request.getSwapMealList());
	    
	    LocalDate dateStart = request.getFilter().getStartDate();
	    LocalDate dateEnd = request.getFilter().getEndDate();
	    
	    int distinctDaysCount = visualCalc.countDistinctDays(request.getMealList());
	    
	    Map<Integer, Double> CFGNutrientTotalMap = CFGNutrientRecService.getCFGNutrientTotalMap(gender, age, distinctDaysCount);

	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	    visualizationService.populateCFGBarGraphDataset(dataset, mealListNutrientAmounts, swapMealListNutrientAmounts, CFGNutrientTotalMap);

	    JFreeChart chart = visualizationService.formatBarGraph(dataset, totalIntakeTitle,dateStart, dateEnd);

	    return chart;
	}
	
	public JFreeChart createGraph(TotalBarCFGHistoricalGraphRequest request) {

		if (isInvalidCFGRequest(request)) { return visualizationService.createErrorGraph(); }
		
	    Gender gender = request.getProfile().getGender();
	    int age = Period.between(request.getProfile().getDateOfBirth(), LocalDate.now()).getYears();

	    Map<Integer, Double> mealListNutrientAmounts = visualCalc.totalNutrients(request.getMealList());
	    int distinctDaysCount = visualCalc.countDistinctDays(request.getMealList());
	    Map<Integer, Double> CFGNutrientTotalMap = CFGNutrientRecService.getCFGNutrientTotalMap(gender, age, distinctDaysCount);

	    LocalDate dateStart = request.getFilter().getStartDate();
	    LocalDate dateEnd = request.getFilter().getEndDate();
	    
	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	    visualizationService.populateCFGBarGraphDataset(dataset, mealListNutrientAmounts, CFGNutrientTotalMap);

	    JFreeChart chart = visualizationService.formatBarGraph(dataset, totalIntakeTitle,dateStart, dateEnd);

	    return chart;
	}
	
	public JFreeChart createGraph(TotalBarHistoricalGraphRequest request) {
	    Map<Integer, Double> mealListNutrientAmounts = visualCalc.totalNutrients(request.getMealList());
	    
	    LocalDate dateStart = request.getFilter().getStartDate();
	    LocalDate dateEnd = request.getFilter().getEndDate();
	    
	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	    visualizationService.populateBarGraphDataset(dataset, mealListNutrientAmounts);
	    JFreeChart chart = visualizationService.formatBarGraph(dataset, totalIntakeTitle,dateStart, dateEnd);
	    return chart;
	}
	
	public JFreeChart createGraph(TotalPieAdvisedGraphRequest request) {
	    Map<Integer, Double> mealListNutrientAmounts = visualCalc.totalNutrients(request.getMealList());
	    Map<Integer, Double> swapMealListNutrientAmounts = visualCalc.totalNutrients(request.getSwapMealList());
	    
	    LocalDate dateStart = request.getFilter().getStartDate();
	    LocalDate dateEnd = request.getFilter().getEndDate();
	    
	    DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
	    visualizationService.populatePieGraphDataset(dataset, mealListNutrientAmounts);
	    DefaultPieDataset<String> datasetAdvised = new DefaultPieDataset<>();
	    visualizationService.populatePieGraphDataset(datasetAdvised, swapMealListNutrientAmounts);
	    
	    JFreeChart chart = visualizationService.formatDualPieChart(dataset, datasetAdvised, totalIntakeTitle, userIntakeLegendLabel, advisedIntakeLegendLabel,dateStart, dateEnd);
	    return chart;
	}
	
	public JFreeChart createGraph(TotalPieCFGAdvisedGraphRequest request) {
		
		if (isInvalidCFGRequest(request)) { return visualizationService.createErrorGraph(); }
	    Gender gender = request.getProfile().getGender();
	    int age = Period.between(request.getProfile().getDateOfBirth(), LocalDate.now()).getYears();
	   
	    Map<Integer, Double> mealListNutrientAmounts = visualCalc.totalNutrients(request.getMealList());
	    Map<Integer, Double> swapMealListNutrientAmounts = visualCalc.totalNutrients(request.getSwapMealList());
	    
	    LocalDate dateStart = request.getFilter().getStartDate();
	    LocalDate dateEnd = request.getFilter().getEndDate();
	    
	    DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
	    visualizationService.populatePieGraphDataset(dataset, mealListNutrientAmounts);
	    DefaultPieDataset<String> datasetAdvised = new DefaultPieDataset<>();
	    visualizationService.populatePieGraphDataset(datasetAdvised, swapMealListNutrientAmounts);
	    
	    int distinctDaysCount = visualCalc.countDistinctDays(request.getMealList());
	    Map<Integer, Double> CFGNutrientTotalMap = CFGNutrientRecService.getCFGNutrientTotalMap(gender, age, distinctDaysCount);
	    DefaultPieDataset<String> datasetCFG = new DefaultPieDataset<>();
	    
	    visualizationService.populatePieGraphDataset(datasetCFG, CFGNutrientTotalMap);
	    JFreeChart chart = visualizationService.formatTriplePieChart(dataset, datasetAdvised, datasetCFG, totalIntakeTitle, userIntakeLegendLabel,
	    		advisedIntakeLegendLabel, CFGRecommendationLegendLabel,dateStart, dateEnd);
	    
	    return chart;
	}
	
	public JFreeChart createGraph(TotalPieCFGHistoricalGraphRequest request) {
		
		if (isInvalidCFGRequest(request)) { return visualizationService.createErrorGraph(); }
		
	    Gender gender = request.getProfile().getGender();
	    int age = Period.between(request.getProfile().getDateOfBirth(), LocalDate.now()).getYears();
	    
	    Map<Integer, Double> mealListNutrientAmounts = visualCalc.avgNutrients(request.getMealList());
	    
	    int distinctDaysCount = visualCalc.countDistinctDays(request.getMealList());
	    Map<Integer, Double> CFGNutrientTotalMap = CFGNutrientRecService.getCFGNutrientTotalMap(gender, age, distinctDaysCount);
	   
	    LocalDate dateStart = request.getFilter().getStartDate();
	    LocalDate dateEnd = request.getFilter().getEndDate();
	    
	    DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
	    visualizationService.populatePieGraphDataset(dataset, mealListNutrientAmounts);
	    DefaultPieDataset<String> datasetCFG = new DefaultPieDataset<>();
	    visualizationService.populatePieGraphDataset(datasetCFG, CFGNutrientTotalMap);
	    
	    JFreeChart chart = visualizationService.formatDualPieChart(dataset, datasetCFG, totalIntakeTitle, userIntakeLegendLabel, CFGRecommendationLegendLabel,dateStart, dateEnd);
	    return chart;
	}
	
	public JFreeChart createGraph(TotalPieHistoricalGraphRequest request) {
	    Map<Integer, Double> mealListNutrientAmounts = visualCalc.totalNutrients(request.getMealList());
	    
	    LocalDate dateStart = request.getFilter().getStartDate();
	    LocalDate dateEnd = request.getFilter().getEndDate();
	    
	    DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
	    visualizationService.populatePieGraphDataset(dataset, mealListNutrientAmounts);
	    JFreeChart chart = visualizationService.formatPieGraph(dataset, totalIntakeTitle,dateStart, dateEnd);
	    
	    return chart;
	}

//	getFoodGroup(int ingredientID);
//	getFoodGroupName(int foodGroupID);

	
	public JFreeChart createGraph(FoodGroupPercentagePieCFGHistoricalGraphRequest request) {

	    Map<String, Double> foodGroupPercentages = visualCalc.getFoodGroupIntakePercentages(request.getMealList());
	    Map<String, Double> foodGroupPercentagesCFG = CFGNutrientRecService.getCFGFoodGroupRecommendationsMap();
	    
	    
	    DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
	    visualizationService.populateFoodGroupPieGraphDataset(dataset, foodGroupPercentages);

	    
	    DefaultPieDataset<String> datasetCFG = new DefaultPieDataset<>();
	    visualizationService.populateFoodGroupPieGraphDataset(datasetCFG, foodGroupPercentagesCFG);
	    
	    LocalDate dateStart = request.getFilter().getStartDate();
	    LocalDate dateEnd = request.getFilter().getEndDate();

	    JFreeChart chart = visualizationService.formatDualPieChart(dataset, datasetCFG, FoodGroupIntakeTitle, userIntakeLegendLabel, CFGRecommendationLegendLabel,dateStart, dateEnd);

	    return chart;
		
	}

	@Override
	public JFreeChart createGraph(FoodGroupPercentagePieCFGAdvisedGraphRequest request) {

	 	Map<String, Double> foodGroupPercentages = visualCalc.getFoodGroupIntakePercentages(request.getMealList());
	 	Map<String, Double> foodGroupPercentagesAdvised = visualCalc.getFoodGroupIntakePercentages(request.getSwapMealList());
	    Map<String, Double> foodGroupPercentagesCFG = CFGNutrientRecService.getCFGFoodGroupRecommendationsMap();
	    
	    DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
	    visualizationService.populateFoodGroupPieGraphDataset(dataset, foodGroupPercentages);

	    
	    DefaultPieDataset<String> datasetAdvised = new DefaultPieDataset<>();
	    visualizationService.populateFoodGroupPieGraphDataset(datasetAdvised, foodGroupPercentagesAdvised);
	    
	    DefaultPieDataset<String> datasetCFG = new DefaultPieDataset<>();
	    visualizationService.populateFoodGroupPieGraphDataset(datasetCFG, foodGroupPercentagesCFG);
	    
	    LocalDate dateStart = request.getFilter().getStartDate();
	    LocalDate dateEnd = request.getFilter().getEndDate();

	    
	    JFreeChart chart = visualizationService.formatTriplePieChart(dataset, datasetAdvised, datasetCFG, FoodGroupIntakeTitle, userIntakeLegendLabel,
	    		advisedIntakeLegendLabel, CFGRecommendationLegendLabel,dateStart, dateEnd);

	    return chart;
		
	}

}
