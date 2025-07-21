package graphService;

import java.util.List;

import org.jfree.chart.JFreeChart;

import food.Food;

public interface IGraphMode {

	public JFreeChart createChart(List<IDataSet> dataSets);
	
	public void populateDefaultDates(List<IDataSet> dataSets);

	public String getNutrientAmtTag (int nutrientID);
	
	public void addDates(List<Food> foodList);
}
