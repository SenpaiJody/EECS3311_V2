package nutritionRequests;

import Advising.NutritionGoal;
import foodService.Filter;

public class NutritionRequestService implements INutritionRequestService {

	
	public NutritionRequestService() {
		
	}
	
	
	public AdvisedGraphRequest createAdvisedGraphRequest(Filter filter, GraphMode mode, GraphType type,Boolean CFGComparison, NutritionGoal goal) {
		
		int graphRequestCode = encodeGraphRequest (mode, type, CFGComparison);
		
		    return switch (graphRequestCode) {
		        case 0   -> new AvgBarAdvisedGraphRequest(filter, goal);
		        case 1   -> new TotalBarAdvisedGraphRequest(filter, goal);
		        case 10  -> new AvgPieAdvisedGraphRequest(filter, goal);
		        case 11  -> new TotalPieAdvisedGraphRequest(filter, goal);
		        case 22  -> new NutrientByDateLineAdvisedGraphRequest(filter, goal);
		        case 23  -> new NutrientPerMealLineAdvisedGraphRequest(filter, goal);

		        case 100 -> new AvgBarCFGAdvisedGraphRequest(filter, goal);
		        case 101 -> new TotalBarCFGAdvisedGraphRequest(filter, goal);
		        case 110 -> new AvgPieCFGAdvisedGraphRequest(filter, goal);
		        case 111 -> new TotalPieCFGAdvisedGraphRequest(filter, goal);
		        case 114 -> new FoodGroupPercentagePieCFGAdvisedGraphRequest(filter, goal);
		        case 122 -> new NutrientByDateLineCFGAdvisedGraphRequest(filter, goal);

		        
		        default -> throw new IllegalArgumentException("Invalid graphRequestCode: " + graphRequestCode);
		    };
		}
	
	public HistoricalGraphRequest createHistoricalGraphRequest(Filter filter, GraphMode mode, GraphType type,Boolean CFGComparison) {

		int graphRequestCode = encodeGraphRequest (mode, type, CFGComparison);
		
	    return switch (graphRequestCode) {
	        case 0   -> new AvgBarHistoricalGraphRequest(filter);
	        case 1   -> new TotalBarHistoricalGraphRequest(filter);
	        case 10  -> new AvgPieHistoricalGraphRequest(filter);
	        case 11  -> new TotalPieHistoricalGraphRequest(filter);
	        case 22  -> new NutrientByDateLineHistoricalGraphRequest(filter);
	        case 23  -> new NutrientPerMealLineHistoricalGraphRequest(filter);

	        case 100 -> new AvgBarCFGHistoricalGraphRequest(filter);
	        case 101 -> new TotalBarCFGHistoricalGraphRequest(filter);
	        case 110 -> new AvgPieCFGHistoricalGraphRequest(filter);
	        case 111 -> new TotalPieCFGHistoricalGraphRequest(filter);
	        case 114 -> new FoodGroupPercentagePieCFGHistoricalGraphRequest(filter);
	        case 122 -> new NutrientByDateLineCFGHistoricalGraphRequest(filter);


	        default -> throw new IllegalArgumentException("Invalid graphRequestCode: " + graphRequestCode);
	    };
	}
	
	/*parseRequest is a way to encode the request into a serial number essentially,
	 * then the implementation can be selected from a "catalog" of implementations
	 * as there are many different types of combinations for the graphs
	 * and the implementations are all different
	 * 
	 * applies to both advised and historical
	 */
	public int encodeGraphRequest (GraphMode mode, GraphType type,Boolean CFGComparison) {
		
	    int modeVal = mode.ordinal();					//	AVG, CUMUL, NUTRIENTBYDATE
	    int typeVal = type.ordinal();					// 	BAR, PIE, LINE
	    int CFGComparisonVal = CFGComparison ? 1 : 0;	// True 1 ; False 0

	    int graphRequestCode = (CFGComparisonVal * 100) + (typeVal * 10) + modeVal;
			    
		return graphRequestCode;
	}
	
}
