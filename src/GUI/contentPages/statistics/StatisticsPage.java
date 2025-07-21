package GUI.contentPages.statistics;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import GUI.GBCUtility;
import GUI.contentPages.BasicPage;

public class StatisticsPage extends BasicPage{
	
	Map<Integer,Integer> swaps;
	IChartDisplay chartDisplay;
	
	public StatisticsPage(Map<Integer,Integer> swaps) {
		this.swaps = swaps;
		getNavBar().getStatsButton().setEnabled(false);
		setSubtitle("Graphs & Comparisons");
		
		
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setResizeWeight(0);
		
		JPanel graphDisplay = getGraphDisplay();
		
		
		ChartQueryBuilder chartQueryBuilder = new ChartQueryBuilder(swaps);
		chartQueryBuilder.addActionListener(event->{
			chartDisplay.displayChart(chartQueryBuilder.createChart());
		});
		splitPane.setLeftComponent(chartQueryBuilder);
		splitPane.setRightComponent(graphDisplay);
		
		getInnerPanel().setLayout(new GridBagLayout());
		getInnerPanel().add(splitPane, GBCUtility.createFiller(0,0));
	}

	public StatisticsPage(){
		this(null);
	}
	
	private JPanel getGraphDisplay() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		JLabel sectionLabel = new JLabel("Generated Graph");
		sectionLabel.setFont(new Font("Arial", Font.BOLD, 24));
		JPanel topBar = new JPanel();
		topBar.setLayout(new GridBagLayout());
		topBar.add(sectionLabel, GBCUtility.createGBC(0, 0, 2, 1));
		GridBagConstraints fillGBC = GBCUtility.createGBC(1,0);
		fillGBC.fill = GridBagConstraints.HORIZONTAL;
		fillGBC.weightx = 1;
		topBar.add(Box.createHorizontalGlue(), fillGBC);

		GridBagConstraints topBarGBC = GBCUtility.createGBC(0,0);
		topBarGBC.fill = GridBagConstraints.HORIZONTAL;
		topBarGBC.weightx = 1;
		panel.add(topBar, topBarGBC);
		//topBar.setBorder(BorderFactory.createLineBorder(Color.RED,2));
		
		
		JPanel chartContainer = new JPanel();
		panel.add(chartContainer, GBCUtility.createFiller(0, 1));
		
//		ChartPanel chartPanel = new ChartPanel(null);
//		chartPanel.setPreferredSize(chartContainer.getSize());
//		chartContainer.add(chartPanel);
		
		
		
		chartDisplay = new IChartDisplay() {
			@Override
			public void displayChart(JFreeChart chart) {
				chartContainer.removeAll();
				ChartPanel chartPanel = new ChartPanel(chart);
				//System.out.println(chartContainer.getSize());
				chartPanel.setPreferredSize(chartContainer.getSize());
				chartContainer.add(chartPanel);
				chartPanel.revalidate();
				chartPanel.repaint();
			}
		};
		
		return panel;
	}
	
	private interface IChartDisplay {
		public void displayChart(JFreeChart chart);
	}
}
