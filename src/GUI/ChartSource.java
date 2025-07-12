package GUI;
import org.jfree.data.general.PieDataset;

public abstract class ChartSource {
	protected abstract PieDataset<Integer> getPieChartData();
	
}
