package graphService;

import java.util.List;

import org.jfree.chart.JFreeChart;

public class GraphService implements IGraphService {

	GraphService(){ }

	@Override
	public JFreeChart createGraph(List<IDataSet> data,  IGraphMode mode){
		return mode.createChart(data);
	}

}
