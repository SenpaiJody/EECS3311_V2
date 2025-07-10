package graphService;

import org.jfree.chart.JFreeChart;

import nutritionRequests.*;

public interface GraphRequestVisitor {

	JFreeChart createGraph(AvgBarAdvisedGraphRequest avgBarAdvisedGraphRequest);
    JFreeChart createGraph(AvgBarCFGAdvisedGraphRequest request);
    JFreeChart createGraph(AvgBarCFGHistoricalGraphRequest request);
    JFreeChart createGraph(AvgBarHistoricalGraphRequest request);

    JFreeChart createGraph(AvgPieAdvisedGraphRequest request);
    JFreeChart createGraph(AvgPieCFGAdvisedGraphRequest request);
    JFreeChart createGraph(AvgPieCFGHistoricalGraphRequest request);
    JFreeChart createGraph(AvgPieHistoricalGraphRequest request);

    JFreeChart createGraph(NutrientByDateLineAdvisedGraphRequest request);
    JFreeChart createGraph(NutrientByDateLineCFGAdvisedGraphRequest request);
    JFreeChart createGraph(NutrientByDateLineCFGHistoricalGraphRequest request);
    JFreeChart createGraph(NutrientByDateLineHistoricalGraphRequest request);

    JFreeChart createGraph(NutrientPerMealLineAdvisedGraphRequest request);
    JFreeChart createGraph(NutrientPerMealLineHistoricalGraphRequest request);

    JFreeChart createGraph(TotalBarAdvisedGraphRequest request);
    JFreeChart createGraph(TotalBarCFGAdvisedGraphRequest request);
    JFreeChart createGraph(TotalBarCFGHistoricalGraphRequest request);
    JFreeChart createGraph(TotalBarHistoricalGraphRequest request);

    JFreeChart createGraph(TotalPieAdvisedGraphRequest request);
    JFreeChart createGraph(TotalPieCFGAdvisedGraphRequest request);
    JFreeChart createGraph(TotalPieCFGHistoricalGraphRequest request);
    JFreeChart createGraph(TotalPieHistoricalGraphRequest request);

    JFreeChart createGraph(FoodGroupPercentagePieCFGHistoricalGraphRequest request);
	JFreeChart createGraph(FoodGroupPercentagePieCFGAdvisedGraphRequest foodGroupPercentagePieCFGAdvisedGraphRequest);

}