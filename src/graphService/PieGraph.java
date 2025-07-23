package graphService;

import java.util.Map;

import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

public interface PieGraph {


	public void populateFoodGroupPieGraphDataset(DefaultPieDataset dataset, Map<String, Double> foodGroupPercentages);


	public PiePlot formatPiePlot(DefaultPieDataset dataset);
}
