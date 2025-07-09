package nutritionRequests;

import Advising.NutritionGoal;
import foodService.Filter;

public interface INutritionRequestService {
	
	public AdvisedGraphRequest createAdvisedGraphRequest(Filter filter, GraphMode mode, GraphType type,Boolean CFGComparison, NutritionGoal goal);
	
	public HistoricalGraphRequest createHistoricalGraphRequest(Filter filter, GraphMode mode, GraphType type,Boolean CFGComparison);
	
	public int encodeGraphRequest (GraphMode mode, GraphType type,Boolean CFGComparison);

}
