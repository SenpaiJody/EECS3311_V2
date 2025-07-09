package nutritionRouting;

import org.jfree.chart.JFreeChart;

import nutritionRequests.AdvisedGraphRequest;
import nutritionRequests.HistoricalGraphRequest;

public interface INutritionRoutingService {

	//Advised requests get information from advising before proceeding
    JFreeChart createGraph(AdvisedGraphRequest request);

    JFreeChart createGraph(HistoricalGraphRequest request);
    
	
}
