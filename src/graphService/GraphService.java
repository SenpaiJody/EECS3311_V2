package graphService;

import java.util.List;

import org.jfree.chart.JFreeChart;

/*Two main interfaces for different types of graphs: IGraphMode and IDataSet
 * This allows for distinction between how the data is represented (ie bar, pie, line)
 * and what the data consists of (historical input, advised data from app, or Canada Food Guide data)
 */

public class GraphService implements IGraphService {

	GraphService(){ }

	@Override
	public JFreeChart createGraph(List<IDataSet> data,  IGraphMode mode){
		return mode.createChart(data);
	}
}
