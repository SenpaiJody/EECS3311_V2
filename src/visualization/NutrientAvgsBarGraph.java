package visualization;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import database.CSVDatabase;
import food.Food;

// With the use of AI
public class NutrientAvgsBarGraph extends AvgsGraph {
	
	
	   public NutrientAvgsBarGraph(CSVDatabase database, LocalDate dateStart, LocalDate dateEnd, Map<Integer, Double> mealListNutrientAmounts, Map<Integer, Double> swapMealListNutrientAmounts) {
	        super(database, dateStart, dateEnd, mealListNutrientAmounts, swapMealListNutrientAmounts);
	    }
	
	/*
	   @Override
	public JFreeChart createGraph() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Map<String, Double> mealListNutrientAmountsByName = nutrientAmountsByName(mealListNutrientAmounts);        
        Map<String, Double> swapMealListNutrientAmountsByName = nutrientAmountsByName(swapMealListNutrientAmounts);
        

        // Add to dataset — each nutrient becomes a category, each pair of values becomes a bar pair
        for (String nutrient : mealListNutrientAmountsByName.keySet()) {
        	double mealValue = mealListNutrientAmountsByName.getOrDefault(nutrient, 0.0);
            double subMealValue = swapMealListNutrientAmountsByName.getOrDefault(nutrient, 0.0);

            dataset.addValue(mealValue, "Meal", nutrient);
            dataset.addValue(subMealValue, "SwapMeal", nutrient);
        }

        return ChartFactory.createBarChart(
                "TOP 10 Nutrients Comparison" + dateStart + " to " + dateEnd,
                "Nutrient",
                "Average Amount",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }*/
	   
	   @Override
	   public JFreeChart createGraph() {
	       DefaultCategoryDataset dataset = new DefaultCategoryDataset();

	       Map<String, Double> mealListNutrientAmountsByName = nutrientAmountsByName(mealListNutrientAmounts);        
	       Map<String, Double> swapMealListNutrientAmountsByName = nutrientAmountsByName(swapMealListNutrientAmounts);

	       // Step 1: Combine and sum both maps to get total consumption per nutrient
	       Map<String, Double> totalConsumption = new HashMap<>();
	       for (String nutrient : mealListNutrientAmountsByName.keySet()) {
	           double mealVal = mealListNutrientAmountsByName.getOrDefault(nutrient, 0.0);
	           double swapVal = swapMealListNutrientAmountsByName.getOrDefault(nutrient, 0.0);
	           totalConsumption.put(nutrient, mealVal + swapVal);
	       }

	       // Also include any nutrients that are only in swapMealList (not in mealList)
	       for (String nutrient : swapMealListNutrientAmountsByName.keySet()) {
	           if (!totalConsumption.containsKey(nutrient)) {
	               double mealVal = mealListNutrientAmountsByName.getOrDefault(nutrient, 0.0);
	               double swapVal = swapMealListNutrientAmountsByName.getOrDefault(nutrient, 0.0);
	               totalConsumption.put(nutrient, mealVal + swapVal);
	           }
	       }

	       // Step 2: Sort by total value descending and get top 10
	       List<Map.Entry<String, Double>> sorted = new ArrayList<>(totalConsumption.entrySet());
	       sorted.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue())); // descending

	       List<String> top10Nutrients = sorted.stream()
	           .limit(10)
	           .map(Map.Entry::getKey)
	           .toList();

	       // Step 3: Add top 10 to dataset
	       for (String nutrient : top10Nutrients) {
	           double mealValue = mealListNutrientAmountsByName.getOrDefault(nutrient, 0.0);
	           double swapValue = swapMealListNutrientAmountsByName.getOrDefault(nutrient, 0.0);

	           dataset.addValue(mealValue, "Meal", nutrient);
	           dataset.addValue(swapValue, "SwapMeal", nutrient);
	       }

	       return ChartFactory.createBarChart(
	           "Top 10 Nutrients: " + dateStart + " to " + dateEnd,
	           "Nutrient",
	           "Average Amount",
	           dataset,
	           PlotOrientation.VERTICAL,
	           true,
	           true,
	           false
	       );
	   }
}