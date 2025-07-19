package GUI;

import javax.swing.JWindow;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.util.TableOrder;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.CategoryToPieDataset;

public class Tester {

	public static void main(String[] args) {
		System.out.println("Start");
		DefaultCategoryDataset defCatData = new DefaultCategoryDataset();
		defCatData.addValue((Number)100, "Something", 3);
		defCatData.addValue((Number)300, "Something", 3);
		defCatData.addValue((Number)200, "Else", 3);
		defCatData.addValue((Number)400, "Else", 3);
		defCatData.addValue((Number)150, "Another", 3);
		defCatData.addValue((Number)100, "Another", 3);
		
		
		CategoryDataset data = defCatData;
		
		CategoryToPieDataset ctp = new CategoryToPieDataset(data, TableOrder.BY_COLUMN, 0);
		//total = 1250
		//something = 400/1250 = ~1/3 
		//Else = 600/1250 = ~1/2
		//Another = 250/1250 = 1/5
		
		
		JFreeChart chart;
		//chart = ChartFactory.createLineChart("Title", "CategoryAxis", "ValueAxis", data);
		//chart = ChartFactory.createBarChart("Bar Chart", "Axis Lable", "Value Label", data);
		chart = ChartFactory.createPieChart("Pie Chart", ctp);
		ChartPanel panel = new ChartPanel(chart);

		JWindow window = new JWindow();
		window.add(panel);	
		window.pack();
		window.setVisible(true);
		System.out.println("Done");
	}
}
