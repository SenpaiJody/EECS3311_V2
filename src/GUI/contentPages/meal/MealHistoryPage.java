package GUI.contentPages.meal;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import GUI.GBCUtility;
import GUI.MainWindow;
import GUI.contentPages.BasicPage;
import GUI.contentPages.homepage.HomePage;
import GUI.reusables.FilterBuilder;
import GUI.reusables.PanelList;
import GUI.reusables.PanelListItem;
import food.Breakfast;
import food.Dinner;
import food.Food;
import food.FoodType;
import food.Lunch;
import food.Snack;
import foodService.Filter;
import foodService.FoodServiceFactory;
import foodService.IFoodService;

public class MealHistoryPage extends BasicPage{

	MealListItem currentlyExpandedItem;
	
	public MealHistoryPage() {
		getNavBar().getHistoryButton().setEnabled(false);
		setSubtitle("Meal History");
		getInnerPanel().setLayout(new GridLayout());
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setEnabled(false);
		FilterBuilder filterBuilder = new FilterBuilder();
		splitPane.setTopComponent(filterBuilder);
		
		JPanel mealHistoryPanel = new JPanel();
		mealHistoryPanel.setLayout(new GridBagLayout());
		
		JLabel mealHistoryLabel = new JLabel("Results");
		mealHistoryLabel.setFont(new Font("Arial", Font.BOLD,24));
		mealHistoryPanel.add(mealHistoryLabel, GBCUtility.createGBC(0, 0));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		JButton loadFromFilterBtn = new JButton("Load From Filter");
		GridBagConstraints fill = GBCUtility.createGBC(1, 0);
		fill.weightx = 1;
		fill.fill = GridBagConstraints.BOTH;
		JButton newMealBtn = new JButton("+ New Meal");
		buttonPanel.add(loadFromFilterBtn, GBCUtility.createGBC(0, 0));
		buttonPanel.add(Box.createHorizontalStrut(10), fill);
		buttonPanel.add(newMealBtn, GBCUtility.createGBC(2, 0));
		
		var buttonPanelGBC = GBCUtility.createGBC(0, 1);
		buttonPanelGBC.weightx = 1;
		buttonPanelGBC.fill = GridBagConstraints.HORIZONTAL;
		mealHistoryPanel.add(buttonPanel, buttonPanelGBC);
		
		var mealListGBC = GBCUtility.createGBC(0, 2);
		mealListGBC.weighty = 1;
		mealListGBC.weightx = 1;
		mealListGBC.fill = GridBagConstraints.BOTH;
		
		PanelList mealList = new PanelList(0,0);
		mealHistoryPanel.add(mealList, mealListGBC);
		
		
		
		splitPane.setBottomComponent(mealHistoryPanel);
		getInnerPanel().add(splitPane);
		
		
		newMealBtn.addActionListener(event->{
			MainWindow.getInstance().setPage(new CreateMealPage());
		});
		
		loadFromFilterBtn.addActionListener(event->{
			mealList.removeAllItems();
			
			IFoodService foodService = FoodServiceFactory.getService();
			Filter filter = filterBuilder.createFilter();
			
			List<Food> meals = foodService.getMeals(filter);
			List<Food> snacks = foodService.getSnacks(filter);
			
			meals.addAll(snacks);
			
			meals.sort((Food foodA, Food foodB)->{
				int comparison = foodA.getDate().compareTo(foodB.getDate());
				if (comparison != 0)
					return comparison;
				
				if (foodA.getType().getClass() == foodB.getType().getClass())
					return 0;
				
				for (FoodType type : new FoodType[] {new Breakfast(), new Lunch(), new Dinner(), new Snack()}) {
					if (foodA.getType().getClass() == type.getClass()) {
						return -1;
					}
					else if (foodB.getType().getClass() == type.getClass()) {
						return 1;
					}
				}
				return 0;
				
			});
			
			for (Food food : meals) {
				MealListItem item = new MealListItem(food);
				item.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						if (currentlyExpandedItem != null && currentlyExpandedItem != item)
							currentlyExpandedItem.setExpanded(false);
						
						
						item.setExpanded(!item.getExpanded());
						currentlyExpandedItem = item.getExpanded() ? item : null;
					}
				});
				mealList.addItem(item);
			}
			
			
		});
	}
}
