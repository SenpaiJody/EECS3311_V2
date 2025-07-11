package visualization;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset; 

import database.CSVDatabase;
import food.Food;

public class NutrientAvgsPieGraph extends AvgsGraph {

	// With the use of AI
	 public NutrientAvgsPieGraph(CSVDatabase database, LocalDate dateStart, LocalDate dateEnd, Map<Integer, Double> mealListNutrientAmounts, Map<Integer, Double> swapMealListNutrientAmounts) {
	        super(database, dateStart, dateEnd, mealListNutrientAmounts, swapMealListNutrientAmounts);
	    }
	
	 
     @Override
     public JFreeChart createGraph() {
         Map<String, Double> mealData = nutrientAmountsByName(mealListNutrientAmounts);

         // Sort and get top 10 nutrients by amount
         List<Map.Entry<String, Double>> sorted = mealData.entrySet().stream()
             .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
             .limit(10)
             .toList();

         // Build pie dataset
         DefaultPieDataset dataset = new DefaultPieDataset();
         for (Map.Entry<String, Double> entry : sorted) {
             dataset.setValue(entry.getKey(), entry.getValue());
         }

         return ChartFactory.createPieChart(
             "Meal Nutrient Averages: " + dateStart + " to " + dateEnd,
             dataset,
             true,  
             true,  
             false
         );
     }
       
}
