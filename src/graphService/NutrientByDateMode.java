package graphService;

import java.awt.Font;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import food.Food;

/*
 *  getNutrientByDate uses  List<Map.Entry<LocalDate, Double>> because each tuple is Date, Amount (of nutrient)
 */

public class NutrientByDateMode extends GraphMode implements IGraphMode, LineGraph {

	int nutrientChoice;

	public NutrientByDateMode(int nutrientChoice) {
		this.nutrientChoice = nutrientChoice;
	}

	@Override
	public JFreeChart createChart(List<IDataSet> dataSets) {
	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

	    for (IDataSet inidvidualSet : dataSets) {
	            List<Food> foodList = inidvidualSet.getFoodList();
	            addDates(foodList);
	        }
	    
	    if (uniqueDates.isEmpty()) {populateDefaultDates(dataSets);	}
	    
	    for (IDataSet individualSet : dataSets) {

	    	List<Map.Entry<LocalDate, Double>> nutrientByDateList = individualSet.getNutrientByDateList(uniqueDates, nutrientChoice);
            String legendLabel = individualSet.getLegendLabel();

	    	for (LocalDate individualDate : uniqueDates) {

	            double value = 0.0;
	            // Find the matching date in nutrientByDateList
	            for (Map.Entry<LocalDate, Double> entry : nutrientByDateList) {
	                if (entry.getKey().equals(individualDate)) {
	                    value = entry.getValue();
	                    break;
	                }
	            }
	            dataset.addValue(value, legendLabel, individualDate.toString());
	        }
	    }

	    JFreeChart chart = formatLineGraph(dataset, nutrientAmtByDateTitle, nutrientChoice);

	    return chart;
	}

	@Override
	public JFreeChart formatLineGraph(DefaultCategoryDataset dataset, String title, int nutrientID) {
		JFreeChart chart = ChartFactory.createLineChart(
		    	title  + getNutrientAmtTag(nutrientID),
		        "Meal",            // X-axis label
		        "Amount",          // Y-axis label
		        dataset,
		        PlotOrientation.VERTICAL,
		        true,             // include legend
		        true,              // tooltips
		        false              // URLs
		    );

		    // Enable shapes (points) on each data item to show distinct points
		    CategoryPlot plot = chart.getCategoryPlot();
		    LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
		    renderer.setDefaultShapesVisible(true);   // show points
		    renderer.setDefaultShapesFilled(true);    // fill points

		    renderer.setSeriesPaint(0, COLOUR_HISTORICAL);
		    renderer.setSeriesPaint(1, COLOUR_CFG);
		    renderer.setSeriesPaint(2, COLOUR_ADVISED);

		    renderer.setSeriesStroke(0, STROKE_SOLID);
		    renderer.setSeriesStroke(1, STROKE_DASHED);
		    renderer.setSeriesStroke(2, STROKE_SOLID);

		    // Optional: Adjust fonts, colors, etc.
		    plot.getDomainAxis().setTickLabelFont(new Font("SansSerif", Font.BOLD, 8));
		    plot.getDomainAxis().setLabelFont(new Font("SansSerif", Font.BOLD, 12));
		    plot.getRangeAxis().setLabelFont(new Font("SansSerif", Font.BOLD, 12));

		    return chart;
		}

}
