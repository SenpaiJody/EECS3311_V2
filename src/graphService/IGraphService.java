package graphService;

import java.util.List;

import org.jfree.chart.JFreeChart;

public interface IGraphService {

	JFreeChart createGraph(List<IDataSet> data,  IGraphMode mode);
}
