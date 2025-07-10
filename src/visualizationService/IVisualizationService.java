package visualizationService;

import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


/*VisualizationService actually performs the formatting of the graphService collected data into a JFreeChart
 * 
 */

public interface IVisualizationService {

    //    LocalDate dateStart, LocalDate dateEnd 
    // graph formatters
    JFreeChart formatBarGraph(DefaultCategoryDataset dataset, String title, LocalDate dateStart, LocalDate dateEnd);
    JFreeChart formatLineGraph(DefaultCategoryDataset dataset, String title, int nutrientID);
    JFreeChart formatPieGraph(DefaultPieDataset<String> dataset, String title, LocalDate dateStart, LocalDate dateEnd );

    JFreeChart formatDualPieChart(
        DefaultPieDataset<String> dataset1,
        DefaultPieDataset<String> dataset2,
        String mainTitle,
        String plot1title,
        String plot2title,
        LocalDate dateStart, LocalDate dateEnd 
    );

    JFreeChart formatTriplePieChart(
        DefaultPieDataset<String> dataset1,
        DefaultPieDataset<String> dataset2,
        DefaultPieDataset<String> dataset3,
        String mainTitle,
        String plot1title,
        String plot2title,
        String plot3title,
        LocalDate dateStart, LocalDate dateEnd 
    );
    


	// works for both avg and total, one map function for historical, no CFG
    void populateBarGraphDataset(DefaultCategoryDataset dataset, Map<Integer, Double> mealListNutrientAmounts);
    
    // works for both avg and total, two map function for advised, no CFG
    void populateBarGraphDataset(DefaultCategoryDataset dataset, Map<Integer, Double> mealListNutrientAmounts, Map<Integer, Double> swapMealListNutrientAmounts);
    
	// works for both avg and total, two map function for historical, with CFG
    void populateCFGBarGraphDataset(DefaultCategoryDataset dataset, Map<Integer, Double> mealListNutrientAmounts, Map<Integer, Double> cfgNutrientsMap);
   
	// works for both avg and total, three map function for advised, with CFG
    void populateCFGBarGraphDataset(DefaultCategoryDataset dataset, Map<Integer, Double> mealListNutrientAmounts, Map<Integer, Double> swapMealListNutrientAmounts, Map<Integer, Double> cfgNutrientsMap);

	// works for both avg and total, one map function for historical, no CFG
    void populatePieGraphDataset(DefaultPieDataset<String> dataset, Map<Integer, Double> mealListNutrientAmounts);

    void populateFoodGroupPieGraphDataset(DefaultPieDataset<String> dataset, Map<String, Double> foodGroupPercentages);
    
	// one map function for historical, no CFG
    void populateLineGraphDataset(DefaultCategoryDataset dataset, List<Map.Entry<String, Double>> nutrientList);
    
	// one map function for historical, with CFG
    void populateLineGraphDataset(DefaultCategoryDataset dataset, List<Map.Entry<String, Double>> nutrientList, double CFGNutrientRecommendation);
    
	// two map function for advised, no CFG
    void populateLineGraphDataset(DefaultCategoryDataset dataset, List<Map.Entry<String, Double>> nutrientList, List<Map.Entry<String, Double>> swapNutrientByDateList);
    
	// two map function for advised, with CFG
    void populateLineGraphDataset(DefaultCategoryDataset dataset, List<Map.Entry<String, Double>> nutrientList, List<Map.Entry<String, Double>> swapNutrientByDateList, double CFGNutrientRecommendation);
	
    // message for invalid input exceptions allows user to change selections
    JFreeChart createErrorGraph();
}
