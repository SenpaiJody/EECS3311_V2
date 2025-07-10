package nutritionRequests;

import org.jfree.chart.JFreeChart;

import foodService.Filter;
import graphService.GraphRequestVisitor;

public class TotalBarHistoricalGraphRequest extends HistoricalGraphRequest {


	public TotalBarHistoricalGraphRequest(Filter filter) {
		super(filter);
	}

    @Override
    public JFreeChart accept(GraphRequestVisitor visitor) {
        return visitor.createGraph(this);
    }
	
}
