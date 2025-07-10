package nutritionRequests;

import org.jfree.chart.JFreeChart;

import foodService.Filter;
import graphService.GraphRequestVisitor;

public class NutrientByDateLineHistoricalGraphRequest extends HistoricalGraphRequest {

	public NutrientByDateLineHistoricalGraphRequest(Filter filter) {
		super(filter);
	}

    @Override
    public JFreeChart accept(GraphRequestVisitor visitor) {
        return visitor.createGraph(this);
    }
	
	
}
