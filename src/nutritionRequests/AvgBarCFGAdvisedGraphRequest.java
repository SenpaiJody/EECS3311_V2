package nutritionRequests;

import java.util.Map;

import org.jfree.chart.JFreeChart;

import advisingTestForVisualization.NutritionGoal;
import foodService.Filter;
import graphService.GraphRequestVisitor;

public class AvgBarCFGAdvisedGraphRequest extends AdvisedGraphRequest {
	
	

	public AvgBarCFGAdvisedGraphRequest(Filter filter, NutritionGoal goal) {
		super(filter, goal);
	}

    @Override
    public JFreeChart accept(GraphRequestVisitor visitor) {
        return visitor.createGraph(this);
    }

	
}