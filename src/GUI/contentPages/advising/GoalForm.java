package GUI.contentPages.advising;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import GUI.GBCUtility;
import food.Food;
import ingredientService.IIngredientService;
import ingredientService.IngredientServiceFactory;
import nutrientService.INutrientService;
import nutrientService.NutrientServiceFactory;
import recommendation.GoalType;
import recommendation.NutritionGoal;
import userService.UserServiceFactory;

class GoalForm extends JPanel{
	private JComboBox<String> nutrientNameField;
	private JSlider intensitySlider;
	private JComboBox<String> ingredientNameField;
	
	private Map<Integer,String> nutrients = new HashMap<Integer, String>();
	//cache of ingredients in the food with their name
	private Map<Integer,String> ingredients = new HashMap<Integer, String>();
	
	
	private static final Font goalTextFont = new Font("Arial", Font.PLAIN, 14);
	private static final Font goalTextFont_mono = new Font("Monospaced", Font.PLAIN, 14);
	private static final Color increaseColor = new Color(0, 150, 0);
	private static final Color decreaseColor = new Color(225, 0, 0);
	

	public int getSelectedNutrient() {
		for (Map.Entry<Integer, String> entry : nutrients.entrySet()) {
			if (entry.getValue().equals((String)nutrientNameField.getSelectedItem())) {
				return entry.getKey();
			}
		}
		return -1;
	}
	
	public int getSelectedIngredient() {
		for (Map.Entry<Integer, String> entry : ingredients.entrySet()) {
			if (entry.getValue().equals((String)ingredientNameField.getSelectedItem())) {
				return entry.getKey();
			}
		}
		return -1;
	}
	
	//loads values into the nutrientNameField
	public void reloadNutrientNameField() {
		Object previous = nutrientNameField.getSelectedItem();
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		model.addAll(nutrients.values());
		if (previous != null)
			model.setSelectedItem(previous);
		else
			model.setSelectedItem(model.getElementAt(0));
		
		nutrientNameField.setModel(model);
	}

	
	//loads values into the ingredientField
	public void reloadIngredientField() {
		Object previous = ingredientNameField.getSelectedItem();
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		model.addAll(ingredients.values());
		if (previous != null)
			model.setSelectedItem(previous);
		else
			model.setSelectedItem(model.getElementAt(0));
		
		ingredientNameField.setModel(model);
	}
	
	public GoalForm(Food food){
		INutrientService service = NutrientServiceFactory.getService();
		service.getAllNutrientIDs().forEach(id->{
			nutrients.put(id, service.getNutrientName(id));
		});
		IIngredientService ingredientService = IngredientServiceFactory.getService();
		food.getIngredients().keySet().forEach(id->{
			ingredients.put(id, ingredientService.getIngredientName(id));
			});
		
		nutrientNameField = new JComboBox<String>();
		reloadNutrientNameField();
		
		intensitySlider = new JSlider(JSlider.HORIZONTAL, -100, 100, 1);
		intensitySlider.setLabelTable(intensitySlider.createStandardLabels(50));
		intensitySlider.setPaintLabels(true);
		intensitySlider.setPaintTicks(true);
		intensitySlider.setMajorTickSpacing(10);

		
		ingredientNameField = new JComboBox<String>();
		reloadIngredientField();
		
		
		
		setLayout(new GridBagLayout());
		
		
		JPanel line1 = new JPanel();
		line1.setLayout(new FlowLayout());
		line1.add(createGoalText("I want to replace"));
		
		reloadIngredientField();
		
		
		JPanel line2 = new JPanel();
		line2.add(createGoalText("to "));
		JLabel increaseDecreaseLabel = new JLabel("Increase");
		increaseDecreaseLabel.setFont(goalTextFont_mono);
		increaseDecreaseLabel.setBorder(BorderFactory.createLoweredBevelBorder());
		increaseDecreaseLabel.setForeground(increaseColor);
		line2.add(increaseDecreaseLabel);
		
		line2.add(createGoalText("my intake of "));
		line2.add(nutrientNameField);
		
		
		
		JPanel line3 = new JPanel();
		
		line3.add(createGoalText("by"));
		JLabel intensityPreciseLabel = new JLabel("  1%");
		intensityPreciseLabel.setFont(goalTextFont);
		intensityPreciseLabel.setFont(goalTextFont_mono);
		intensityPreciseLabel.setBorder(BorderFactory.createLoweredBevelBorder());
		line3.add(intensityPreciseLabel);
		line3.add(createGoalText("(a"));
		JLabel intensityLabel = new JLabel(" Small  ");
		intensityLabel.setFont(goalTextFont_mono);
		intensityLabel.setBorder(BorderFactory.createLoweredBevelBorder());
		line3.add(intensityLabel);
		line3.add(createGoalText("amount)."));

		intensitySlider.addChangeListener(event->{
			if (intensitySlider.getValue() == 0)
				intensitySlider.setValue(1);
			
			if (intensitySlider.getValue() > 0) {
				increaseDecreaseLabel.setText("Increase");
				increaseDecreaseLabel.setForeground(increaseColor);
				intensityPreciseLabel.setForeground(increaseColor);
			}
			else {
				increaseDecreaseLabel.setText("Decrease");
				increaseDecreaseLabel.setForeground(decreaseColor);
				intensityPreciseLabel.setForeground(decreaseColor);
			}
			
			intensityPreciseLabel.setText(String.format("%3s",Math.abs(intensitySlider.getValue())) + "%");
			
			if (Math.abs(intensitySlider.getValue()) > 50)
				intensityLabel.setText(" Large  ");
			else if (Math.abs(intensitySlider.getValue()) > 20)
				intensityLabel.setText("Moderate");
			else
				intensityLabel.setText(" Small  ");
			
			
			
			revalidate();
			repaint();
		});
		
		add(line1, GBCUtility.createGBC(0, 0));
		add(ingredientNameField, GBCUtility.createGBC(0, 1));
		add(line2, GBCUtility.createGBC(0, 2));
		add(line3, GBCUtility.createGBC(0, 3));
		add(new JLabel("Intensity (%)"), GBCUtility.createGBC(0, 4));
		add(intensitySlider, GBCUtility.createGBC(0, 5));
		
		nutrientNameField.addItemListener(event->{
			if (event.getStateChange() == ItemEvent.SELECTED);
		});
		
	}
	
	//builds a label with the proper font
	private JLabel createGoalText(String s) {
		JLabel label = new JLabel(s);
		label.setFont(goalTextFont);
		return label;
	}
	
	/***creates a goal from the GoalForm's data; throws an IllegalStateException if there is not enough data to do so*/
	public NutritionGoal getGoal() {
		int ingredientID = getSelectedIngredient();
		
		int nutrient = getSelectedNutrient();
		
		int intensity = Math.abs(intensitySlider.getValue());
		
		GoalType type = intensitySlider.getValue() > 0 ? GoalType.INCREASE : GoalType.DECREASE;

		System.out.println("Creating Goal with: ");
		System.out.println("  Nutrient: " + nutrient);
		System.out.println("  intensity: " + intensity);
		System.out.println("  type: " + type.toString());
		System.out.println("  ingredient: " + ingredientID);
		
		return new NutritionGoal(
				UserServiceFactory.getService().getCurrentProfile().getID(),
				nutrient,
				intensity,
				type,
				ingredientID
				);
	}
	
	public JSlider getIntensitySlider() {
		return intensitySlider;
	}
	public JComboBox<String> getNutrientSelectionBox(){
		return nutrientNameField;
	}
	
}
