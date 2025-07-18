package graphService;

import java.time.LocalDate;

import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public interface BarGraph {

	public JFreeChart formatBarGraph(DefaultCategoryDataset dataset, String title, LocalDate dateStart, LocalDate dateEnd);
}
