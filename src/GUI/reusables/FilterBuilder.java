package GUI.reusables;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.time.LocalDate;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;

import GUI.DateSpinner;
import GUI.GBCUtility;
import foodService.Filter;

public class FilterBuilder extends JPanel {

	private DateSpinner dateFrom;
	private DateSpinner dateTo;
	
	private JToggleButton select_Breakfast;
	private JToggleButton select_Lunch;
	private JToggleButton select_Dinner;
	private JToggleButton select_Snack;
	
	private IngredientSearchBar searchBar;
	private PanelList includePanelList;
	private PanelList excludePanelList;
	
	
	public Filter getFilter() {
		Filter filter = new Filter();
		return null;
	}
	public FilterBuilder(){
		
		setLayout(new GridBagLayout());
		
		
		JSplitPane togglesNDate = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		togglesNDate.setEnabled(false);
		togglesNDate.setDividerSize(4);
		JPanel datePanel = new JPanel();
		datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.Y_AXIS));
		datePanel.add(createWrappedLabel("From ", new Font("Arial", Font.ITALIC, 12)));
		dateFrom = new DateSpinner(16);
		dateFrom.setDate(LocalDate.of(1970,1, 1));
		datePanel.add(dateFrom);
		datePanel.add(createWrappedLabel("To ", new Font("Arial", Font.ITALIC, 12)));
		dateTo = new DateSpinner(16);
		dateTo.setDate(LocalDate.now());
		datePanel.add(dateTo);
		togglesNDate.setTopComponent(datePanel);
		
		JPanel foodTypePanel = new JPanel();
		foodTypePanel.setLayout(new BorderLayout());
		
		JPanel foodTypeLabelPanel = new JPanel();
		foodTypeLabelPanel.add(new JLabel("Food Types"));
		foodTypePanel.add(foodTypeLabelPanel, BorderLayout.PAGE_START);
		JPanel togglesPanel = new JPanel();
		togglesPanel.setLayout(new GridLayout(2,2));
		select_Breakfast = new JToggleButton("Breakfast", false);
		select_Lunch = new JToggleButton("Lunch", false);
		select_Dinner = new JToggleButton("Dinner", false);
		select_Snack = new JToggleButton("Snack", false);
		togglesPanel.add(select_Breakfast);
		togglesPanel.add(select_Lunch);
		togglesPanel.add(select_Dinner);
		togglesPanel.add(select_Snack);
		foodTypePanel.add(togglesPanel);
		
		togglesNDate.setBottomComponent(foodTypePanel);
		
		add(togglesNDate, GBCUtility.createGBC(0, 0));
		
		add(new JLabel("Select Ingredients ([Enter] to search)"), GBCUtility.createGBC(0, 1));
		
		
		JPanel ingredientSearchAddPanel = new JPanel();
		ingredientSearchAddPanel.setLayout(new GridBagLayout());
		searchBar = new IngredientSearchBar();
		ingredientSearchAddPanel.add(searchBar);
		
		JButton includeBtn = new JButton("Include");
		JButton excludeBtn = new JButton("Exclude");
		ingredientSearchAddPanel.add(Box.createHorizontalStrut(20));
		ingredientSearchAddPanel.add(includeBtn);
		ingredientSearchAddPanel.add(excludeBtn);
		add(ingredientSearchAddPanel, GBCUtility.createGBC(0, 2));
		
		add(new JLabel("Included Ingredients"), GBCUtility.createGBC(0, 3));
		includePanelList = new PanelList(0, 0);
		var includePanelGBC = GBCUtility.createGBC(0, 4);
		includePanelGBC.fill = GridBagConstraints.BOTH;
		includePanelGBC.weightx = 1;
		includePanelGBC.weighty = 1;
		add(includePanelList, includePanelGBC);
		
		add(new JLabel("Excluded Ingredients"), GBCUtility.createGBC(0, 5));
		excludePanelList = new PanelList(0, 0);
		var excludePanelGBC = GBCUtility.createGBC(0, 6);
		excludePanelGBC.fill = GridBagConstraints.BOTH;
		excludePanelGBC.weightx = 1;
		excludePanelGBC.weighty = 1;
		add(excludePanelList, excludePanelGBC);
		
		
		includeBtn.addActionListener(event->{
			Integer result = searchBar.getIngredient();
			if (result == null)
				return;
			if (excludePanelList.contains(new IngredientListItem(includePanelList, result, true)))
				return;
			includePanelList.addItem(new IngredientListItem(includePanelList, result, true));
		});
		excludeBtn.addActionListener(event->{
			Integer result = searchBar.getIngredient();
			if (result == null)
				return;
			if (includePanelList.contains(new IngredientListItem(excludePanelList, result, true)))
				return;
			excludePanelList.addItem(new IngredientListItem(excludePanelList, result, true));
		});
		
	}
	private JPanel createWrappedLabel(String s, Font f) {
		JPanel panel = new JPanel();
		JLabel label = new JLabel(s);
		label.setFont(f);
		panel.add(label);
		return panel;
	}
}
