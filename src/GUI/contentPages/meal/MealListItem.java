package GUI.contentPages.meal;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GUI.GBCUtility;
import GUI.MainWindow;
import GUI.contentPages.advising.AdvisingPage;
import GUI.reusables.IngredientQuantityListItem;
import GUI.reusables.PanelList;
import GUI.reusables.PanelListItem;
import food.Food;
import ingredientService.IIngredientService;
import nutriCalc.INutriCalc;
import nutriCalc.NutritionFacade;
import nutrientService.INutrientService;
import nutrientService.NutrientServiceFactory;


//A PanelListItem that displays a Meal/Food
public class MealListItem extends PanelListItem implements MouseListener{

	boolean expanded = false;
	
	JPanel detailsPanel;
	JLabel expansionLabel;
	Food food;
	
	public Food getFood() {return food;}
	public JPanel getDetailsPanel() {
		if (detailsPanel == null)
			generateDetailsPanel();
		return detailsPanel;
	}
	
	public MealListItem(Food food) {
		
		this.food = food;
		addMouseListener(this);
		
		
		setLayout(new GridBagLayout());
		
		JLabel foodName = new JLabel(food.getName() + " | ");
		foodName.setFont(new Font("Arial", Font.BOLD, 16));
		add(foodName, GBCUtility.createGBC(0, 0, GridBagConstraints.LINE_START));
		
		JLabel foodType = new JLabel("[" + food.getType().getTypeName() + "] ");
		foodType.setFont(new Font("Arial", Font.ITALIC, 14));
		add(foodType, GBCUtility.createGBC(0, 1,  GridBagConstraints.LINE_START));
	
		
		JLabel dateLabel = new JLabel(String.format("%d/%s/%d", food.getDate().getDayOfMonth(), food.getDate().getMonth(),food.getDate().getYear()));
		dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		add(dateLabel, GBCUtility.createGBC(1, 0, 1, 1, GridBagConstraints.LINE_START));
		
		add(Box.createHorizontalGlue(),GBCUtility.createFiller(2, 0));
		
		expansionLabel = new JLabel("[Click to Expand]  ");
		add(expansionLabel, GBCUtility.createGBC(3,0,1,2));
		
		

	}
	
	public boolean getExpanded() {return expanded;}
	public void setExpanded(boolean expanded) {
		if (this.expanded == expanded) 
			return;
		
		if (expanded == true && detailsPanel == null) {
			generateDetailsPanel();
		}
		detailsPanel.setVisible(expanded);
		expansionLabel.setText(!expanded ? "[Click to Expand]  " : "[Click to Retract]  ");
		this.expanded = expanded;
	}

	
	private void generateDetailsPanel() {
		detailsPanel = new JPanel();
		detailsPanel.setOpaque(false);
		detailsPanel.setVisible(false);
		
		detailsPanel.setLayout(new GridBagLayout());
		
		detailsPanel.add(new JLabel("Ingredients"), GBCUtility.createGBC(0, 0));
		
		PanelList ingredientsList = new PanelList(300, 300);
		detailsPanel.add(ingredientsList, GBCUtility.createFiller(0, 1));
		
		detailsPanel.add(new JLabel("Nutrients"), GBCUtility.createGBC(1, 0));

		
		detailsPanel.add(createNutrientsPanel(), GBCUtility.createFiller(1, 1));
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridBagLayout());
		buttonsPanel.setOpaque(false);
		buttonsPanel.add(Box.createVerticalGlue(), GBCUtility.createFiller(0, 0));
		JButton replaceButton = new JButton("Replacements");
		JButton graphsButton =  new JButton("Graphs");
		
		var replaceButtonGBC = GBCUtility.createGBC(0, 1);
		replaceButtonGBC.fill = GridBagConstraints.HORIZONTAL;
		replaceButtonGBC.weightx = 1;
		
		var graphsButtonGBC = GBCUtility.createGBC(0, 2);
		graphsButtonGBC.fill = GridBagConstraints.HORIZONTAL;
		graphsButtonGBC.weightx = 1;
		
		buttonsPanel.add(replaceButton,replaceButtonGBC);
		//buttonsPanel.add(graphsButton,graphsButtonGBC);
		
		
		var buttonsPanelGBC = GBCUtility.createGBC(2, 0,1,2);
		buttonsPanelGBC.fill = GridBagConstraints.VERTICAL;
		buttonsPanelGBC.weighty = 1;
		detailsPanel.add(buttonsPanel, buttonsPanelGBC);
		
		var detailsPanelGBC = GBCUtility.createGBC(0, 2, 4, 1);
		detailsPanelGBC.fill = GridBagConstraints.BOTH;
		detailsPanelGBC.weightx = 1;
		add(detailsPanel,detailsPanelGBC);
		
		
		food.getIngredients().forEach((id, amt)->{
			ingredientsList.addItem(new IngredientQuantityListItem(ingredientsList, id, false, amt, false));
		});
		
		
		replaceButton.addActionListener(event->{
			MainWindow.getInstance().setPage(new AdvisingPage(food));
		});
	}
	
	
	private JPanel createNutrientsPanel() {
		JPanel nutrientsPanel = new JPanel();
		nutrientsPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		nutrientsPanel.setLayout(new GridBagLayout());
		INutrientService srv = NutrientServiceFactory.getService();
		INutriCalc calculator = new NutritionFacade();
		
		var nutrients = calculator.calculateNutritionProfiles(food.getIngredients()).getAllNutrients();
		var allNutrientIDs = srv.getAllNutrientIDs();
		for (int i =0 ; i <allNutrientIDs.size(); i++) {
			int nutrientID = allNutrientIDs.get(i);
			JLabel nutrientTitle = new JLabel(String.format("%s :", srv.getNutrientName(nutrientID)));
			JLabel nutrientAmt = new JLabel(String.format(" %.2f%s", nutrients.get(nutrientID), srv.getNutrientUnit(nutrientID)));
			nutrientsPanel.add(nutrientTitle,GBCUtility.createGBC(0, i));
			nutrientsPanel.add(nutrientAmt,GBCUtility.createGBC(1, i));
		}
		return nutrientsPanel;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {
		setBackground(getBackground().darker());
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		setBackground(getBackground().brighter());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}
}
