package graphService;

import java.awt.Font;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import food.Food;

public class AvgGraphMode extends GraphMode implements IGraphMode, BarGraph{


	@Override
	public JFreeChart createChart(List<IDataSet> dataSets) {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

	    for (IDataSet individualSet : dataSets) {
	            List<Food> foodList = individualSet.getFoodList();
	            addDates(foodList);
	        }


		for (IDataSet individualSet : dataSets) {
				Map<Integer, Double> mealListNutrientAmounts = individualSet.getAvgNutrientAmounts();
				String legendLabel = individualSet.getLegendLabel();

			for (Map.Entry<Integer, Double> entry : mealListNutrientAmounts.entrySet()) {
			    int nutrientID = entry.getKey();
			    double avgValue = entry.getValue();
			    String nutrientAmtTag = getNutrientAmtTag(nutrientID);
			    dataset.addValue(avgValue, legendLabel, nutrientAmtTag);
			}

		}

		LocalDate dateStart = uniqueDates.isEmpty() ? null : uniqueDates.first();
		LocalDate dateEnd = uniqueDates.isEmpty() ? null : uniqueDates.last();

		JFreeChart chart = formatBarGraph(dataset,avgIntakeTitle,dateStart,dateEnd);

		return chart;
	}

	@Override
	public JFreeChart formatBarGraph(DefaultCategoryDataset dataset, String title, LocalDate dateStart, LocalDate dateEnd) {

		JFreeChart chart = ChartFactory.createBarChart(
	    title + dateStart + " to " + dateEnd,
	    "Nutrient",
	    "Average Amount",
	    dataset,
	    PlotOrientation.VERTICAL,
	    true,	// include legend
	    true,	// include tooltips
	    false	// not URLs
			);

	     // Set font for category labels (nutrients)
        chart.getCategoryPlot()
             .getDomainAxis()
             .setTickLabelFont(new Font("SansSerif", Font.BOLD,6));  //

        // Optionally: set font for axis label (i.e., the word "Nutrient")
        chart.getCategoryPlot()
             .getDomainAxis()
             .setLabelFont(new Font("SansSerif", Font.BOLD, 20));

        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
        renderer.setSeriesPaint(0, COLOUR_HISTORICAL);  // "Meal"
        renderer.setSeriesPaint(1, COLOUR_ADVISED);
        renderer.setSeriesPaint(2, COLOUR_CFG);


	return chart;
}


}
