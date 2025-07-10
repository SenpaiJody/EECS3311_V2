package nutritionRequests;


import org.jfree.chart.JFreeChart;

import foodService.Filter;
import graphService.GraphRequestVisitor;

public class FoodGroupPercentagePieCFGHistoricalGraphRequest extends HistoricalGraphRequest {

	public FoodGroupPercentagePieCFGHistoricalGraphRequest(Filter filter) {
		super(filter);
	}
	
    @Override
    public JFreeChart accept(GraphRequestVisitor visitor) {
        return visitor.createGraph(this);
    }
}