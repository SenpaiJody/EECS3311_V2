package nutritionRequests;

import org.jfree.chart.JFreeChart;

import foodService.Filter;
import graphService.GraphRequestVisitor;

public class AvgBarHistoricalGraphRequest extends HistoricalGraphRequest {

	public AvgBarHistoricalGraphRequest(Filter filter) {
		super(filter);
	}
	
    @Override
    public JFreeChart accept(GraphRequestVisitor visitor) {
        return visitor.createGraph(this);
    }
}