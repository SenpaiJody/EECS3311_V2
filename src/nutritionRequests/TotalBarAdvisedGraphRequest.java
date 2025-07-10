package nutritionRequests;

import org.jfree.chart.JFreeChart;

import advisingTestForVisualization.NutritionGoal;
import foodService.Filter;
import graphService.GraphRequestVisitor;

public class TotalBarAdvisedGraphRequest extends AdvisedGraphRequest {

	public TotalBarAdvisedGraphRequest(Filter filter, NutritionGoal goal) {
		super(filter, goal);
	}
	
    @Override
    public JFreeChart accept(GraphRequestVisitor visitor) {
        return visitor.createGraph(this);
    }
	
}