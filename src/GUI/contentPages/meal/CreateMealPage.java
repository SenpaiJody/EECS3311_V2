package GUI.contentPages.meal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import GUI.DateSpinner;
import GUI.GBCUtility;
import GUI.MainWindow;
import GUI.contentPages.BasicPage;
import GUI.contentPages.homepage.HomePage;
import GUI.reusables.IngredientListItem;
import GUI.reusables.IngredientQuantityListItem;
import GUI.reusables.IngredientSearchBar;
import GUI.reusables.PanelList;
import food.Food;
import food.FoodBuilder;
import food.FoodType;
import food.IncompleteFoodException;
import food.Snack;
import foodService.FoodServiceFactory;
import foodService.IFoodService;
import foodService.InvalidFoodTypeException;
import nutriCalc.INutriCalc;
import nutriCalc.NutrientProfile;
import nutriCalc.NutritionFacade;
import nutrientService.INutrientService;
import nutrientService.NutrientServiceFactory;

//The page for creating meals
public class CreateMealPage extends BasicPage {

	
	public CreateMealPage(){
		setSubtitle("Record a New Meal");
		getNavBar().getaddMealButton().setEnabled(false);
		getInnerPanel().setLayout(new GridLayout());
		
		


		
		JPanel ingredientPanel = createIngredientPanel();
		JPanel nutrientPanel = addNutrientsPanel();
		JPanel mealInfoPanel = addMealInfoPanel();
		
		JSplitPane split2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, nutrientPanel, mealInfoPanel);
		split2.setResizeWeight(2.f/3);
		split2.setEnabled(false);
		split2.setDividerSize(0);
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, ingredientPanel, split2);
		split.setResizeWeight(1.f/3.f);
		split.setEnabled(false);
		split.setDividerSize(0);
		getInnerPanel().add(split);
	}
	
	private PanelList ingredientList;
	
	public Map<Integer, Double> getIngredientMap(){
		Map<Integer,Double> ingredients = new HashMap<Integer,Double>();
		ingredientList.getItems().forEach(panel->{
			IngredientQuantityListItem item = (IngredientQuantityListItem)panel;
			ingredients.put(item.getIngredientID(), item.getQuantity());
		});
		return ingredients;
	}
	
	private JPanel createIngredientPanel() {
		JPanel ingredientPanel = new JPanel();
		ingredientPanel.setLayout(new GridBagLayout());
		ingredientPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		ingredientPanel.setOpaque(false);

		
		JPanel topBarContainer = new JPanel();
		topBarContainer.setBorder(new EmptyBorder(20, 0, 20, 0));
		topBarContainer.setLayout(new GridBagLayout());
		
		GridBagConstraints topBarGBC = GBCUtility.createGBC(0, 0);
		topBarGBC.fill = GridBagConstraints.BOTH;
		topBarGBC.weightx = 1;
		ingredientPanel.add(topBarContainer,topBarGBC);
		
		JLabel addIngredientLabel = new JLabel("Add Ingredients ([Enter] to Search) ");
		addIngredientLabel.setFont(new Font("Arial", Font.BOLD, 16));
		topBarContainer.add(addIngredientLabel, GBCUtility.createGBC(0, 0,2,1));
		
		GridBagConstraints ingredientSearchbarGBC = GBCUtility.createGBC(0, 1);
		IngredientSearchBar searchBar = new IngredientSearchBar();
		topBarContainer.add(searchBar, ingredientSearchbarGBC);
		
		
		ingredientList = new PanelList(0, 0);
		
		GridBagConstraints ingredientListGBC = GBCUtility.createGBC(0, 1);
		ingredientListGBC.fill = GridBagConstraints.BOTH;
		ingredientListGBC.weightx = 1;
		ingredientListGBC.weighty = 1;
		ingredientPanel.add(ingredientList, ingredientListGBC);
		
		
		GridBagConstraints addBtnGBC = GBCUtility.createGBC(1, 1);
		JButton addButton = new JButton("+ Add");
		addButton.addActionListener(event->{
			Integer ingredient = searchBar.getIngredient();
			if (ingredient == null)
				return;
			ingredientList.addItem(new IngredientQuantityListItem(ingredientList, ingredient, true, 0.0f, true));
		});
		topBarContainer.add(addButton, addBtnGBC);
		
		return ingredientPanel;
	}
	
	private  JPanel addNutrientsPanel() {
		JPanel nutrientsPanel = new JPanel();
		nutrientsPanel.setLayout(new GridBagLayout());
		nutrientsPanel.setBorder(BorderFactory.createRaisedBevelBorder());
				
		JLabel areaTitle = new JLabel("Nutrient Overview");
		areaTitle.setFont(new Font("Arial", Font.BOLD, 16));
		areaTitle.setBorder(new EmptyBorder(10,0,10,0));
		nutrientsPanel.add(areaTitle,GBCUtility.createGBC(0, 0));
		JButton refreshButton = new JButton("Refresh");
		nutrientsPanel.add(refreshButton, GBCUtility.createGBC(0, 1));
		

		JSplitPane bottomSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		bottomSplit.setBorder(new EmptyBorder(10, 0, 0, 0));
		bottomSplit.setDividerSize(4);
		bottomSplit.setEnabled(false);
		bottomSplit.setResizeWeight(0.3f);

		var bottomSplitGBC = GBCUtility.createGBC(0,2);
		bottomSplitGBC.weighty =1;
		bottomSplitGBC.fill = GridBagConstraints.BOTH;
		bottomSplitGBC.weightx = 1;
		nutrientsPanel.add(bottomSplit, bottomSplitGBC);
		
		
		
		JPanel nutrientTextInfo = new JPanel();
		nutrientTextInfo.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		nutrientTextInfo.setLayout(new BoxLayout(nutrientTextInfo, BoxLayout.Y_AXIS));
		bottomSplit.setTopComponent(nutrientTextInfo);
		
		
		JPanel nutrientChart = new JPanel();
		nutrientChart.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		bottomSplit.setBottomComponent(nutrientChart);
		
		
		
		refreshButton.addActionListener(event->{

			Map<Integer,Double> data = getIngredientMap();
			updateNutrientTextDisplay(nutrientTextInfo, data);
			updateNutrientChart(nutrientChart, data);
		});

	

		
		
		
		
		
		return nutrientsPanel;
	}
	
	private void updateNutrientTextDisplay(JPanel nutrientTextInfo, Map<Integer,Double> data) {		
		INutriCalc nutritionCalculator = new NutritionFacade();
		NutrientProfile nutrients = nutritionCalculator.calculateNutritionProfiles(data);
		
		INutrientService nutrientService = NutrientServiceFactory.getService();
		
		
		nutrientTextInfo.removeAll();
		nutrients.getAllNutrients().forEach((id, amt)->{
			String nutrientName = nutrientService.getNutrientName(id);
			String nutrientUnit = nutrientService.getNutrientUnit(id);
			
			nutrientTextInfo.add(new JLabel(String.format("%s : %.2f %s", nutrientName, amt, nutrientUnit)));
		});
		
		
		nutrientTextInfo.revalidate();
		nutrientTextInfo.repaint();
	}
	
	private void updateNutrientChart(JPanel nutrientChartDisplay, Map<Integer,Double> data) {
		nutrientChartDisplay.removeAll();
		nutrientChartDisplay.add(new JLabel("Display nutrients chart here"));
		nutrientChartDisplay.revalidate();
		nutrientChartDisplay.repaint();
	}
	
	
	private  JPanel addMealInfoPanel() {
		JPanel mealInfoPanel = new JPanel();
		mealInfoPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		mealInfoPanel.setLayout(new GridBagLayout());
		
		Font labelFont = new Font("Arial", Font.BOLD, 24);
		JLabel nameLabel = new JLabel("Name: ");
		nameLabel.setFont(labelFont);
		JLabel dateLabel = new JLabel("Date: ");
		dateLabel.setFont(labelFont);
		JLabel typeLabel = new JLabel("Type: ");
		typeLabel.setFont(labelFont);

		mealInfoPanel.add(nameLabel, GBCUtility.createGBC(0, 0));
		mealInfoPanel.add(dateLabel, GBCUtility.createGBC(0, 1));
		mealInfoPanel.add(typeLabel, GBCUtility.createGBC(0, 2));
		
		Font fieldFont = new Font("Arial", Font.PLAIN, 24);
		JTextField nameField = new JTextField(20);
		nameField.setFont(fieldFont);
		DateSpinner dateField = new DateSpinner(24);
		JComboBox<String> typeField = new JComboBox<String>();
		
		var nameFieldGBC = GBCUtility.createGBC(1, 0);
		nameFieldGBC.anchor = GridBagConstraints.LINE_START;
		mealInfoPanel.add(nameField, nameFieldGBC);
		var dateFieldGBC = GBCUtility.createGBC(1, 1);
		dateFieldGBC.anchor = GridBagConstraints.LINE_START;
		mealInfoPanel.add(dateField, dateFieldGBC);
		var typeFieldGBC = GBCUtility.createGBC(1, 2);
		typeFieldGBC.anchor = GridBagConstraints.LINE_START;
		mealInfoPanel.add(typeField, typeFieldGBC);
		populateFoodTypeField(dateField.getDate(), typeField);

		
		dateField.addItemListener(event->{
			if (event.getStateChange() == ItemEvent.SELECTED) {
				populateFoodTypeField(dateField.getDate(), typeField);
			}
		});
			
		
		typeField.addPopupMenuListener(new PopupMenuListener() { //dynamically allows/disallows types based on what foods exist.
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				populateFoodTypeField(dateField.getDate(), typeField);
			}
			@Override public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
			@Override public void popupMenuCanceled(PopupMenuEvent e) {}
			
		});
		
		
		JButton submitBtn = new JButton("Submit");
		var submitGBC = GBCUtility.createGBC(3, 3);
		mealInfoPanel.add(submitBtn, submitGBC);
		
		submitBtn.addActionListener(event->{
			var ingredientData = getIngredientMap();
			if (ingredientData.size() == 0)	{
				JOptionPane.showMessageDialog(null, "A meal must have at least one ingredient!");
				return;
			}
			if (nameField.getText().strip().length() == 0) {
				JOptionPane.showMessageDialog(null, "A meal must have a name!");
				return;
			}	
			saveFood(nameField.getText(), dateField.getDate(), getIngredientMap(), (String)typeField.getSelectedItem());

		});
		
		
		return mealInfoPanel;
	}
	
	private void populateFoodTypeField(LocalDate date, JComboBox<String> typeField) {
		typeField.removeAllItems();
		for (FoodType f: FoodServiceFactory.getService().getValidFoodTypes(date)) {
			typeField.addItem(f.getTypeName());
		}
	}
	
	private void saveFood(String name, LocalDate date, Map<Integer, Double> ingredients, String foodTypeString){
		FoodBuilder foodBuilder = new FoodBuilder();
		foodBuilder.setName(name);
		foodBuilder.setDate(date);
		
		for (FoodType f: FoodServiceFactory.getService().getValidFoodTypes(date)) {
			if (f.getTypeName().equals(foodTypeString))
				foodBuilder.setFoodType(f);
		}
		
		ingredients.forEach((id, amt) -> {

			foodBuilder.addIngredient(id, amt);
		});
	
		
		try {
			Food completedFood = foodBuilder.getResult();
			completedFood.save();
			
			JOptionPane.showMessageDialog(null, "Food Created!");
			MainWindow.getInstance().setPage(new HomePage());

		} catch (IncompleteFoodException e) {
			JOptionPane.showMessageDialog(null, "Incomplete Food!"); //should not be possible to hit since input is already checked
		} catch (InvalidFoodTypeException e) {
			JOptionPane.showMessageDialog(null, "Invalid Food Type"); //should not be possible to hit since input is already checked
		}
		
	}
	
	//creates a "dummy" food object without fully completed data, just for nutrient calculation
//	private Food createDummyFood(Map<Integer, Double> ingredients) {
//		FoodBuilder foodBuilder = new FoodBuilder();
//		foodBuilder.setName("Dummy");
//		foodBuilder.setDate(LocalDate.now());
//		foodBuilder.setID(0);
//		foodBuilder.setFoodType(new Snack());
//		ingredients.forEach((id, amt) -> {
//
//			foodBuilder.addIngredient(id, amt);
//		});
//		try {
//			return foodBuilder.getResult();
//		} catch (IncompleteFoodException e) {
//			return null;
//		}
//	}
		

}
