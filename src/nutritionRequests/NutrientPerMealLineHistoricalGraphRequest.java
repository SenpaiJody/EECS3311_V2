package nutritionRequests;

import org.jfree.chart.JFreeChart;

import foodService.Filter;
import graphService.GraphRequestVisitor;

public class NutrientPerMealLineHistoricalGraphRequest extends HistoricalGraphRequest {

	public NutrientPerMealLineHistoricalGraphRequest(Filter filter) {
		super(filter);
	}

    @Override
    public JFreeChart accept(GraphRequestVisitor visitor) {
        return visitor.createGraph(this);
    }
}