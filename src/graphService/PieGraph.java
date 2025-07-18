package graphService;

import java.util.Map;

import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

public interface PieGraph {


	public void populateFoodGroupPieGraphDataset(DefaultPieDataset<String> dataset, Map<String, Double> foodGroupPercentages);

	@SuppressWarnings("rawtypes")
	public PiePlot formatPiePlot(DefaultPieDataset<String> dataset);
}
