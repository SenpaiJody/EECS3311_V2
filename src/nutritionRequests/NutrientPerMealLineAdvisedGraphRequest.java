package nutritionRequests;

import org.jfree.chart.JFreeChart;

import Advising.NutritionGoal;
import foodService.Filter;
import graphService.GraphRequestVisitor;

public class NutrientPerMealLineAdvisedGraphRequest extends AdvisedGraphRequest {

	public NutrientPerMealLineAdvisedGraphRequest(Filter filter, NutritionGoal goal) {
		super(filter, goal);
	}
	
    @Override
    public JFreeChart accept(GraphRequestVisitor visitor) {
        return visitor.createGraph(this);
    }
}