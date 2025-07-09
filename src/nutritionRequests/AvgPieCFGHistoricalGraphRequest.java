package nutritionRequests;

import org.jfree.chart.JFreeChart;

import foodService.Filter;
import graphService.GraphRequestVisitor;

public class AvgPieCFGHistoricalGraphRequest extends HistoricalGraphRequest {

	public AvgPieCFGHistoricalGraphRequest(Filter filter) {
		super(filter);
	}
	
    @Override
    public JFreeChart accept(GraphRequestVisitor visitor) {
        return visitor.createGraph(this);
    }
}
