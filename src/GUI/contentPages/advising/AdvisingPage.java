package GUI.contentPages.advising;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;

import GUI.GBCUtility;
import GUI.MainWindow;
import GUI.contentPages.BasicPage;
import GUI.contentPages.statistics.StatisticsPage;
import GUI.reusables.IngredientListItem;
import GUI.reusables.IngredientQuantityListItem;
import GUI.reusables.PanelList;
import GUI.reusables.SelectablePanelList;
import applySwap.ApplySwapFactory;
import applySwap.IApplySwap;
import food.Food;
import ingredientService.IIngredientService;
import ingredientService.IngredientServiceFactory;
import nutriCalc.INutriCalc;
import nutriCalc.NutrientProfile;
import nutriCalc.NutritionFacade;
import nutrientService.INutrientService;
import nutrientService.NutrientServiceFactory;
import recommendation.FoodRecommendation;
import recommendation.IFoodRecommendation;
import recommendation.INutritionGoalManager;
import recommendation.NutritionGoalManager;
import userService.UserServiceFactory;

//the page in which meal replacements are recommended based on a user's goal
public class AdvisingPage extends BasicPage{

	private static final Font sectionNumberFont = new Font("Arial", Font.BOLD, 28);
	private static final Font sectionLabelFont = new Font("Arial", Font.PLAIN, 16);
	
	private Food originalFood;
	

	//number of active goals
	private int nGoals = 0;
	//list of all goal forms
	private List<GoalForm> goalForms = new ArrayList<GoalForm>();
	
	//listeners that activate whenever the selected swap is changed
	private List<ActionListener> swapListeners = new ArrayList<ActionListener>();
	//a map of old ingredients to new ingredients
	private Map<Integer, Integer> replacements = new HashMap<Integer, Integer>();
	
	

	
	//constructor
	public AdvisingPage(Food originalFood){
		setSubtitle("Advising");
		
		this.originalFood = originalFood;
		

		getInnerPanel().setLayout(new GridBagLayout());
		
		JSplitPane outerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JSplitPane innerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		outerSplitPane.setTopComponent(createGoalCreationPanel());
		innerSplitPane.setTopComponent(createSwapRecommendationPanel());
		innerSplitPane.setBottomComponent(createDifferenceViewerPanel());
		outerSplitPane.setBottomComponent(innerSplitPane);
		
		outerSplitPane.setEnabled(false);
		outerSplitPane.setResizeWeight(0);
		innerSplitPane.setEnabled(false);
		innerSplitPane.setResizeWeight(0.5);
		
		getInnerPanel().add(outerSplitPane, GBCUtility.createFiller(0, 0));
	}
	

	//creates a numbered title of a section of the page
	private JPanel createSectionTitle(int number, String title) {
		JPanel panel = new JPanel();
		JLabel numberLabel = new JLabel(String.format("%d) ", number));
		numberLabel.setFont(sectionNumberFont);
		panel.add(numberLabel);
		
		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(sectionLabelFont);
		panel.add(titleLabel);
		return panel;
	}
	
	
	//The section of the page that prompts the user for goals
	private JPanel createGoalCreationPanel() {
		
		JPanel panel = new JPanel();
		//panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panel.setLayout(new GridBagLayout());
		panel.add(createSectionTitle(1, "Define your goals"), GBCUtility.createGBC(0, 0));
		
		
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new GridBagLayout());
		nGoals = 1;
		GoalForm goalForm1 = new GoalForm(originalFood);
		goalForms.add(goalForm1);
		goalForm1.setBorder(BorderFactory.createLoweredBevelBorder());
		innerPanel.add(goalForm1, GBCUtility.createGBC(0, 1));
		
		JPanel buttonWrapper = new JPanel();
		JToggleButton andBtn = new JToggleButton("And...");
		buttonWrapper.add(andBtn);
		innerPanel.add(buttonWrapper, GBCUtility.createGBC(0, 2));;
		
		GoalForm goalForm2 = new GoalForm(originalFood);		
		goalForms.add(goalForm2);
		goalForm2.setBorder(BorderFactory.createLoweredBevelBorder());
		innerPanel.add(goalForm2, GBCUtility.createGBC(0, 3));
		goalForm2.setVisible(false);
		
		andBtn.addActionListener(event->{
			goalForm2.setVisible(andBtn.isSelected());
			nGoals = andBtn.isSelected() ? 2 : 1;	
			ensureNoGoalDuplicates(goalForm1, goalForm2, nGoals > 1);
		});
		
		
		goalForm1.getNutrientSelectionBox().addItemListener(event->{
			if (event.getStateChange() == ItemEvent.SELECTED) {
				ensureNoGoalDuplicates(goalForm1, goalForm2, nGoals > 1);
			}
		});
		goalForm2.getNutrientSelectionBox().addItemListener(event->{
			if (event.getStateChange() == ItemEvent.SELECTED) {
				ensureNoGoalDuplicates(goalForm1, goalForm2, nGoals > 1);
			}
		});
		
		panel.add(innerPanel, GBCUtility.createFiller(0, 1));
		
		return panel;
	}
	
	//ensure that two goalForms do not have a duplicated Nutrient
	private void ensureNoGoalDuplicates(GoalForm goalForm1, GoalForm goalForm2, boolean twoForms){
		if (twoForms) { //then, make sure that there are no duplicates.
			goalForm2.reloadNutrientNameField();
			goalForm2.getNutrientSelectionBox().removeItem(goalForm1.getNutrientSelectionBox().getSelectedItem());
			goalForm1.reloadNutrientNameField();
			goalForm1.getNutrientSelectionBox().removeItem(goalForm2.getNutrientSelectionBox().getSelectedItem());
		}
		else //otherwise, ensure that the first form is not being hindered at all.
		{
			goalForm1.reloadNutrientNameField();
		}
	}

	//section of the page that displays swaps and has the user pick one
	private JPanel createSwapRecommendationPanel() {
		
		JPanel panel = new JPanel();
		
		//panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panel.setLayout(new GridBagLayout());
		panel.add(createSectionTitle(2,"Select a Swap"), GBCUtility.createGBC(0, 0));
		JButton loadRecommendationsBtn = new JButton("Load Recommendations");
		panel.add(loadRecommendationsBtn, GBCUtility.createGBC(0, 1));
		
		JPanel container = new JPanel();
		container.setLayout(new GridBagLayout());
		container.add(Box.createHorizontalStrut(225),GBCUtility.createGBC(0,0));
		loadRecommendationsBtn.addActionListener(event->{		
			loadSwaps(container);
		});
		panel.add(container, GBCUtility.createFiller(0, 2));
		return panel;
	}
	
	//populate the swapRecommendation panel with lists of ingredients
	private void loadSwaps(JPanel container) {
		IFoodRecommendation recommendationManager = new FoodRecommendation();
		INutritionGoalManager goalManager = new NutritionGoalManager();
		goalManager.addGoalChangeListener(recommendationManager);
		
		container.removeAll();
		container.revalidate();
		container.repaint();
		int profileID = UserServiceFactory.getService().getCurrentProfile().getID();
		IIngredientService ingredientService = IngredientServiceFactory.getService();
		
		goalManager.getActiveGoals(profileID).forEach(goal->{
			goalManager.removeGoal(profileID, goal.getgoalId());
		});
		for (int i =0; i < nGoals; i++) {
			goalManager.addGoal(profileID, goalForms.get(i).getGoal());
		}
	
		//this method returns a list of lists of recommendations, returning two elements if the goals are on different ingredients, or one if it is the same ingredient
		List<List<Integer>> recommendations = recommendationManager.getLatestRecommendations(profileID);
		

		replacements = new HashMap<Integer,Integer>();
		for (int i =0; i < recommendations.size(); i++) {
			SelectablePanelList swapList = new SelectablePanelList(225,0);
			int goalFormIndex = i;
			swapList.addActionListener(event->{ //whenever one of the swaps are edited...
				replacements.put(goalForms.get(goalFormIndex).getSelectedIngredient(), ((IngredientListItem)event.getSource()).getIngredientID());
				swapListeners.forEach(listener->listener.actionPerformed(event));
				//also inform the other swap listeners
			});
			
			JLabel info = new JLabel(String.format("Swap \"%s\" for...", ingredientService.getIngredientName(goalForms.get(i).getSelectedIngredient())));
			info.setBorder(BorderFactory.createEmptyBorder(8,0,0,0));
			info.setFont(new Font("Arial", Font.BOLD, 16));
			container.add(info, GBCUtility.createGBC(0, 2*i));
			recommendations.get(i).forEach(id ->{
				IngredientListItem item = new IngredientListItem(swapList, id, false);
				swapList.addItem(item);
				if (swapList.getItems().size() == 1) { //if this is the first one...
					swapList.select(item);//select it.
				}
			});
			container.add(swapList, GBCUtility.createFiller(0, (2*i)+1));
		}
		container.revalidate();
		container.repaint();
	}


	//panel responsible for showing the difference in ingredients and nutrients (in text format)
	private JPanel createDifferenceViewerPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(createSectionTitle(3, "See the changes"), GBCUtility.createGBC(0,0,2,1));
		
		PanelList previousIngredientsPanelList = new PanelList(250, 300);
		PanelList newIngredientsPanelList = new PanelList(250,300);
		
		originalFood.getIngredients().forEach((id,amt)->{
			previousIngredientsPanelList.addItem(new IngredientQuantityListItem(previousIngredientsPanelList, id, false, amt, false));
		});
		
		JPanel textDifferencesPanelContainer = new JPanel();
		
		JButton graphsButton = new JButton("Graphs");
		graphsButton.setVisible(false);;
		
		swapListeners.add(event->{
			IApplySwap swapApplier = ApplySwapFactory.createApplySwap();
			
			List<Integer> oldIngredientsList = new ArrayList<Integer>();
			List<Integer> newIngredientsList = new ArrayList<Integer>();
			replacements.forEach((_old, _new)->{
				oldIngredientsList.add(_old);
				newIngredientsList.add(_new);
			});
			
			List<Food> oldFoods = new ArrayList<Food>();
			oldFoods.add(originalFood);
			Map<Integer,Double> newIngredientsMap = swapApplier.applySwaps(newIngredientsList, oldIngredientsList, oldFoods).getFirst().getIngredients();
			
			populateNewIngredientsList(newIngredientsPanelList, newIngredientsMap);
			highlightChangedIngredients(previousIngredientsPanelList);
			
			textDifferencesPanelContainer.removeAll();
			textDifferencesPanelContainer.add(getTextDifferencesPanel(newIngredientsMap), newIngredientsPanelList);
			
			graphsButton.setVisible(true);
			graphsButton.addActionListener(btnEvent->{
				MainWindow.getInstance().setPage(new StatisticsPage(replacements));
			});
			
			revalidate();
			repaint();
		});
		
		JPanel graphsBtnContainer = new JPanel();
		graphsBtnContainer.setLayout(new GridBagLayout());
		GridBagConstraints horizontalFiller = GBCUtility.createGBC(0, 0);
		horizontalFiller.fill = GridBagConstraints.HORIZONTAL;
		horizontalFiller.weightx = 1;
		graphsBtnContainer.add(Box.createHorizontalStrut(1), horizontalFiller);
		graphsBtnContainer.add(graphsButton, GBCUtility.createGBC(1,0));
		
	
		panel.add(new JLabel("Before Swap"), GBCUtility.createGBC(0, 1));
		panel.add(new JLabel("After Swap"), GBCUtility.createGBC(1, 1));
		panel.add(previousIngredientsPanelList, GBCUtility.createFiller(0, 2));
		panel.add(newIngredientsPanelList,GBCUtility.createFiller(1, 2));
		panel.add(textDifferencesPanelContainer, GBCUtility.createGBC(0, 3,2,1));
		GridBagConstraints graphsBtnContainerGBC = GBCUtility.createGBC(0,4, 2, 1);
		graphsBtnContainerGBC.fill = GridBagConstraints.HORIZONTAL;
		graphsBtnContainerGBC.weightx = 1;
		panel.add(graphsBtnContainer, graphsBtnContainerGBC);
		return panel;
	}
	
	//highlights an ingredient that was changed by the swap with a RED border
	private void highlightChangedIngredients(PanelList list) {
		list.getItems().forEach(item->{
			item.setBorder(
					!replacements.containsKey(((IngredientListItem) item).getIngredientID()) ?
							BorderFactory.createRaisedBevelBorder() : BorderFactory.createLineBorder(Color.RED, 3)
					);
		});
	}
	
	//populates a PanelList with ingredients, new ingredients are given a GREEN border
	private void populateNewIngredientsList(PanelList list, Map<Integer, Double> newIngredientMap) {
		list.removeAllItems();
		newIngredientMap.forEach((id,amt)->{
			var item = new IngredientQuantityListItem(list, id, false, amt, false);
			list.addItem(item);
		});
		list.getItems().forEach(item->{
			item.setBorder(
					originalFood.getIngredients().containsKey(((IngredientListItem) item).getIngredientID()) ?
							BorderFactory.createRaisedBevelBorder() : BorderFactory.createLineBorder(Color.GREEN, 3)
					);
		});
		list.revalidate();
		list.repaint();
	}
	
	//sub-panel for displaying specifically the differences in nutrients, with arrows for changes
	private JPanel getTextDifferencesPanel(Map<Integer,Double> newIngredientMap) {
		
		INutrientService nutrientSrv = NutrientServiceFactory.getService();
		List<Integer> allNutrients = nutrientSrv.getAllNutrientIDs();
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		INutriCalc nutriCalc = new NutritionFacade();
		NutrientProfile original = nutriCalc.calculateNutritionProfiles(originalFood.getIngredients());
		NutrientProfile modified = nutriCalc.calculateNutritionProfiles(newIngredientMap);
		
		for (int i =0; i < allNutrients.size(); i++){
			int id = allNutrients.get(i);
			JPanel line = new JPanel();
			JLabel label = new JLabel(nutrientSrv.getNutrientName(id) + ": ");
			label.setFont(label.getFont().deriveFont(Font.BOLD));
			line.add(label);
			JLabel originalAmt = new JLabel(String.format("%.2f%s", original.getNutrient(id), nutrientSrv.getNutrientUnit(id)));
			line.add(originalAmt);
			if (original.getNutrient(id) != modified.getNutrient(id)) {
				JLabel changeLog = new JLabel(String.format(" -> %.2f%s", modified.getNutrient(id), nutrientSrv.getNutrientUnit(id)));
				changeLog.setForeground(original.getNutrient(id) > modified.getNutrient(id) ? new Color(200, 0, 0) : new Color(0, 175, 0));
				line.add(changeLog);
			}
			panel.add(line, GBCUtility.createGBC(0, i));
		}
		
		
		return panel;
	}
}
