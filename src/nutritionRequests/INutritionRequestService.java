package nutritionRequests;

import advisingTestForVisualization.NutritionGoal;
import foodService.Filter;

/*Nutrition Request service currently acts kind of like a factory in that it takes the input from the GUI
 * and creates a request which is passed to nutritionRouting
 */

public interface INutritionRequestService {
	
	public AdvisedGraphRequest createAdvisedGraphRequest(Filter filter, GraphMode mode, GraphType type,Boolean CFGComparison, NutritionGoal goal);
	
	public HistoricalGraphRequest createHistoricalGraphRequest(Filter filter, GraphMode mode, GraphType type,Boolean CFGComparison);
	
	public int encodeGraphRequest (GraphMode mode, GraphType type,Boolean CFGComparison);

}
