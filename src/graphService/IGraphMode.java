package graphService;

import java.util.List;

import org.jfree.chart.JFreeChart;

public interface IGraphMode {

	JFreeChart createChart(List<IDataSet> dataSets);

}
