package nutritionRequests;

import org.jfree.chart.JFreeChart;

import foodService.Filter;
import graphService.GraphRequestVisitor;

public abstract class HistoricalGraphRequest extends GraphRequest {


	public HistoricalGraphRequest(Filter filter) {
		super(filter);
	}

	
	
}
