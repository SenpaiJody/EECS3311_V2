package graphService;

import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public interface LineGraph {


	public JFreeChart formatLineGraph(DefaultCategoryDataset dataset, String title, int nutrientID);
}
