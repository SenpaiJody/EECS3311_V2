package graphService;

import java.util.List;

import org.jfree.chart.JFreeChart;

import food.Food;

/*Different classes that implement GraphMode:
 * AvgGraphMode (Bar), TotalGraph Mode (Bar),
 * FoodGroupMode (Pie), NutrientByDateMode (Line)
 * 
 * 
 */

public interface IGraphMode {

	public JFreeChart createChart(List<IDataSet> dataSets);
	
	
	/* This is done for two reasons: in the case of some kind of exception where there are no recognizable dates
	 * available, and also for visualizations of CFG data which do not contain dates within the data (in this
	 * case it relies on the dateStart and dateEnd parameters passed to it
	 */
	public void populateDefaultDates(List<IDataSet> dataSets);

	public String getNutrientAmtTag (int nutrientID);
	
	public void addDates(List<Food> foodList);
}
