package GUI.contentPages.homepage;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GUI.GBCUtility;
import GUI.contentPages.BasicPage;

//The home page / landing page 
public class HomePage extends BasicPage {
	
	public HomePage(){
		
		getNavBar().getHomeButton().setEnabled(false);
		setSubtitle("Home Page");
		
		getInnerPanel().setLayout(new GridBagLayout());
		
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createRaisedBevelBorder());
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(createLabel("NutriSci App", 50, true));
		panel.add(createLabel("A project for EECS-3311 Summer 2025", 26, false));
		panel.add(Box.createVerticalStrut(30));
		panel.add(createNamedList("To Log a New Meal:", Arrays.asList("Click the 'Add Meal' button in the Navigation Bar", "OR, Click the 'Meal History' button in the Navigation Bar, then click the '+ New Meal' button"), 20));
		panel.add(createNamedList("To Make Food Replacements:", Arrays.asList(
				"Click the 'Meal History' button in the Navigation Bar", 
				"Define a Search term using the Filter Builder on the left",
				"Press 'Load from Filter' to view your meals",
				"Select a meal",
				"Click the 'Replacements' button"
				), 20));
		panel.add(createNamedList("To Visualize your Food Replacement:", Arrays.asList(
				"First, make a food replacement (follow the above instructions)",
				"Then select a desired eeplacement from the generated options",
				"Then click the 'Graphs' Button to visualize the changes"
				), 20));
		panel.add(createNamedList("To Visualize your Nutrient Intake or Compare it to the Canada Food Guide:", Arrays.asList(
				"Click the 'Graphs & Comparisons' Button on the Navigation Bar"
				), 20));
		
		getInnerPanel().add(panel, GBCUtility.createFiller(0, 0));
		
	}
	
	private JLabel createLabel(String content, int fontSize, boolean isBold) {
		JLabel label = new JLabel(content);
		label.setFont(new Font("Arial", isBold ? Font.BOLD : Font.PLAIN, fontSize));
		return label;
	}
	
	private JPanel createNamedList(String title, List<String> lines, int fontSize) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(createLabel(title, fontSize, true));
		lines.forEach(line->{
			panel.add(createLabel("    > " + line, fontSize, false));
		});
		
		
		return panel;
		
	}
}
