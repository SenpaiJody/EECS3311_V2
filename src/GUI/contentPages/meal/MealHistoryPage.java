package GUI.contentPages.meal;

import java.awt.GridLayout;

import javax.swing.JSplitPane;

import GUI.contentPages.BasicPage;
import GUI.reusables.FilterBuilder;

public class MealHistoryPage extends BasicPage{

	
	public MealHistoryPage() {
		getNavBar().getHistoryButton().setEnabled(false);
		setSubtitle("Meal History");
		getInnerPanel().setLayout(new GridLayout());
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setTopComponent(new FilterBuilder());
		
		
		
		
		getInnerPanel().add(splitPane);
	}
}
