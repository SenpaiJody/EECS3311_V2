package graphService;

import java.awt.Color;
import java.awt.Font;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.general.DefaultPieDataset;
import food.Food;
import org.jfree.chart.labels.StandardPieToolTipGenerator;

public class FoodGroupMode extends GraphMode implements IGraphMode, PieGraph{


    public FoodGroupMode() {
    }

	@SuppressWarnings({ "rawtypes" })
	@Override
	public JFreeChart createChart(List<IDataSet> dataSets) {

		 List<AbstractMap.SimpleEntry<PiePlot, String>> plotsWithTitles = new ArrayList<>();
		 for (IDataSet individualSet : dataSets) {
			//add dates is for putting start end dates in graph
            List<Food> foodList = individualSet.getFoodList();
            addDates(foodList);

            Map<String, Double> foodGroupPercentages = individualSet.getFoodGroupPercentages();
			DefaultPieDataset dataset = new DefaultPieDataset<>();
			String legendLabel = individualSet.getLegendLabel();
			populateFoodGroupPieGraphDataset(dataset, foodGroupPercentages);
			PiePlot plot = formatPiePlot(dataset);
			plotsWithTitles.add(new AbstractMap.SimpleEntry<>(plot, legendLabel));
		 }

			MultiPiePlot multiPlot = new MultiPiePlot(plotsWithTitles);

			LocalDate dateStart;
			LocalDate dateEnd;	
			
			if(!uniqueDates.isEmpty()) {
				 dateStart = uniqueDates.first();
				 dateEnd = uniqueDates.last();
			} else {
				IDataSet defaultDataSet =  dataSets.get(0);			
				List<LocalDate> defaultDateList = new ArrayList<>();
				defaultDateList = defaultDataSet.getDefaultDateList();
				dateStart = defaultDateList.get(0);
				dateEnd = defaultDateList.get(defaultDateList.size()-1);
			}
			
		JFreeChart chart = new JFreeChart(
				FoodGroupIntakeTitle + " " + dateStart.toString() + " to " + dateEnd.toString(),
			    new Font("SansSerif", Font.BOLD, 18),
			    multiPlot,
			    false
			);
			return chart;

		 }

	@Override
	public void populateFoodGroupPieGraphDataset(DefaultPieDataset dataset, Map<String, Double> foodGroupPercentages) {
	    for (Map.Entry<String, Double> entry : foodGroupPercentages.entrySet()) {
	        String foodGroupName = entry.getKey();
	        Double percentage = entry.getValue();
	        dataset.setValue(foodGroupName, percentage);
	    }
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PiePlot formatPiePlot(DefaultPieDataset dataset) {
	    Font labelFont = new Font("SansSerif", Font.PLAIN, 10);
    
	    PiePlot plot = new PiePlot(dataset);
	    plot.setLabelFont(labelFont);
	    plot.setInteriorGap(0.04);
	    plot.setBackgroundPaint(Color.WHITE);
	    
	    plot.setToolTipGenerator(new StandardPieToolTipGenerator("{0}: {1} ({2})"));
	    
	    // Display percentages
	    
	    
	    plot.setLabelGenerator(new FirstWordPercentageLabelGenerator());
	    plot.setSimpleLabels(true);
	    plot.setInteriorGap(0.1);
	    plot.setLabelGap(0.05);
	    plot.setSimpleLabelOffset(new RectangleInsets(5, 15, 5, 15));
	    plot.setLabelLinkMargin(0.2);
	    plot.setLabelLinksVisible(true);
	    plot.setLabelLinkPaint(Color.GRAY);
	    plot.setLabelFont(new Font("SansSerif", Font.BOLD, 8));
	    
	    // setting the gap btw label and edge of pie graph
	    plot.setLabelGap(0.00);
	    plot.setSimpleLabelOffset(new RectangleInsets(2, 10, 2, 10)); 
	    plot.setLabelLinkMargin(0.1);

	    return plot;
	}

}
