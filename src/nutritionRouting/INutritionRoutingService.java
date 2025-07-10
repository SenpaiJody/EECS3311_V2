package nutritionRouting;

import org.jfree.chart.JFreeChart;

import nutritionRequests.AdvisedGraphRequest;
import nutritionRequests.HistoricalGraphRequest;


/*NutritionRoutingService is the class that receives the request from the GUI,
 * then passes along the request info to the various components before returning the JFreeChart to the GUI
 * It provides a List<Food> to Advising and receives a List<Food> in return containging all the swapped meals
 * 
 */

public interface INutritionRoutingService {

	//Advised requests get information from advising before proceeding
    JFreeChart createGraph(AdvisedGraphRequest request);

    JFreeChart createGraph(HistoricalGraphRequest request);
    
	
}
