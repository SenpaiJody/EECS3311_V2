package GUI.contentPages.statistics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.font.TextAttribute;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;

import org.jfree.chart.JFreeChart;

import GUI.DateSpinner;
import GUI.GBCUtility;
import GUI.MainWindow;
import GUI.contentPages.meal.MealHistoryPage;
import applySwap.ApplySwapFactory;
import applySwap.IApplySwap;
import food.Food;
import foodService.Filter;
import foodService.FoodServiceFactory;
import foodService.IFoodService;
import graphService.AvgGraphMode;
import graphService.CFGDataSet;
import graphService.FoodDataSet;
import graphService.FoodGroupMode;
import graphService.GraphServiceFactory;
import graphService.IDataSet;
import graphService.IGraphMode;
import graphService.NutrientByDateMode;
import graphService.TotalGraphMode;
import ingredientService.IIngredientService;
import ingredientService.IngredientServiceFactory;
import nutrientService.INutrientService;
import nutrientService.NutrientServiceFactory;
import userService.IUserService;
import userService.UserServiceFactory;

public class ChartQueryBuilder extends JPanel {

	IDataSetCreator userData;
	IDataSetCreator swappedData;
	IDataSetCreator CFGData;
	IGraphTypeSelector graphTypeSelector;
	
	Map<Integer,Integer> swaps;
	
	DateSpinner dateFrom;
	DateSpinner dateTo;
	
	ArrayList<ActionListener> actionListeners = new ArrayList<ActionListener>();
	
	JButton genGraphsBtn;

	public ChartQueryBuilder(Map<Integer,Integer> swaps) {
		this.swaps = swaps;
		setLayout(new GridBagLayout());
		
		GridBagConstraints dateRangeGBC = GBCUtility.createGBC(0, 0);
		dateRangeGBC.fill = GridBagConstraints.HORIZONTAL;
		dateRangeGBC.weightx = 1;
		add(createDateRangeSelector(), dateRangeGBC);
		
		GridBagConstraints dataSources = GBCUtility.createGBC(0, 1);
		dataSources.fill = GridBagConstraints.HORIZONTAL;
		dataSources.weightx = 1;
		add(createDataSourceSelector(), dataSources);
		
		GridBagConstraints graphTypeGBC = GBCUtility.createGBC(0, 2);
		graphTypeGBC.fill = GridBagConstraints.HORIZONTAL;
		graphTypeGBC.weightx = 1;
		add(createGraphTypeSelectionPanel(), graphTypeGBC);
		
		add(Box.createVerticalGlue(), GBCUtility.createFiller(0,3));
		
		genGraphsBtn = new JButton("Generate Graphs");
		genGraphsBtn.addActionListener(event->{
			notifyActionListeners();
		});
		GridBagConstraints generateGraphsBtnGBC = GBCUtility.createGBC(0, 4);
		generateGraphsBtnGBC.fill = GridBagConstraints.HORIZONTAL;
		generateGraphsBtnGBC.weightx = 1;
		add(genGraphsBtn, generateGraphsBtnGBC);
		
		validateGenerateGraphsBtn();
	}
	
	private void validateGenerateGraphsBtn() {
		boolean isValid = false;
		
		if (userData != null && userData.getDataSet() != null)
			isValid = true;
		if (isValid || (swappedData != null && swaps != null && swappedData.getDataSet() != null))
			isValid = true;
		if (isValid || (CFGData != null && CFGData.getDataSet() != null))
			isValid = true;
		
		genGraphsBtn.setEnabled(isValid);
	}

	
	private JPanel createDateRangeSelector() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createRaisedBevelBorder());
		panel.setLayout(new BorderLayout());
		panel.add(createTitle("Select Date Range"), BorderLayout.PAGE_START);
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new GridBagLayout());	
		innerPanel.add(createDateSelector(), GBCUtility.createGBC(0, 0));	
		panel.add(innerPanel, BorderLayout.CENTER);
		return panel;
	}
	
	private JPanel createDataSourceSelector() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createRaisedBevelBorder());
		panel.setLayout(new BorderLayout());
		panel.add(createTitle("Select Data Sources"), BorderLayout.PAGE_START);
		JPanel innerPanel = new JPanel();
		
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		innerPanel.add(createUserDataSourceField());
		innerPanel.add(createAdvisedDataSourceField());
		innerPanel.add(createCFGDataSourceField());
		
		panel.add(innerPanel, BorderLayout.CENTER);
		return panel;
	}
	
	
	private JPanel createDateSelector() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(4,0,4,0));
		panel.add(new JLabel("From"), GBCUtility.createGBC(0, 0));
		panel.add(new JLabel("To"), GBCUtility.createGBC(2, 0));
		dateFrom = new DateSpinner(16);
		dateFrom.setDate(LocalDate.of(1970, 1, 1));
		dateTo = new DateSpinner(16);
		dateTo.setDate(LocalDate.now());
		panel.add(dateFrom, GBCUtility.createGBC(0, 1));
		panel.add(dateTo, GBCUtility.createGBC(2, 1));
		
		panel.add(Box.createHorizontalStrut(20), GBCUtility.createGBC(1, 0));
		
		return panel;
	}
	
	
	private JPanel createUserDataSourceField() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		JCheckBox checkBox = new JCheckBox("Historical Data");
		panel.add(checkBox, GBCUtility.createGBC(0,0));
		panel.add(Box.createHorizontalGlue(), GBCUtility.createFiller(1,0));
		checkBox.addItemListener(item->{
			validateGenerateGraphsBtn();
		});
		userData = new IDataSetCreator() {	
			@Override
			public IDataSet getDataSet() {
				if (!checkBox.isSelected()) {
					return null;
				}
					
				
				Filter filter = new Filter();
				filter.setDateRange(dateFrom.getDate(), dateTo.getDate());
				IFoodService foodService = FoodServiceFactory.getService();
				List<Food> meals = foodService.getMeals(filter);
				List<Food> snacks = foodService.getSnacks(filter);
				
				meals.addAll(snacks);
				
				return new FoodDataSet("Historical Food", meals);
			}
		};
		return panel;
	}
	
	private JPanel createAdvisedDataSourceField() {
		final int MAX_INGREDIENT_NAME_LENGTH = 25;
		
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		JCheckBox checkBox = new JCheckBox("Advised Data");

		panel.add(checkBox, GBCUtility.createGBC(0,0));
		panel.add(Box.createHorizontalGlue(), GBCUtility.createFiller(1,0));
		
		JPanel swapDetailsContainer = new JPanel();
		if (swaps != null) {
			checkBox.setSelected(true);
			swapDetailsContainer.setLayout(new BoxLayout(swapDetailsContainer, BoxLayout.Y_AXIS));
			IIngredientService srv = IngredientServiceFactory.getService();
			
			swaps.forEach((_old, _new)->{
				JPanel swapDetail = new JPanel();
				
				String old_name = srv.getIngredientName(_old);
				JLabel oldIngredientLabel = new JLabel(old_name);
				//oldIngredientLabel.setFont(font);
				if (old_name.length() > MAX_INGREDIENT_NAME_LENGTH) {
					oldIngredientLabel.setText(old_name.substring(0,MAX_INGREDIENT_NAME_LENGTH) + "...");
					oldIngredientLabel.setToolTipText(old_name);
				}

			
				String new_name = srv.getIngredientName(_new);
				JLabel newIngredientLabel= new JLabel(new_name);
				//newIngredientLabel.setFont(font);
				if (new_name.length() > MAX_INGREDIENT_NAME_LENGTH) {
					newIngredientLabel.setText(new_name.substring(0,MAX_INGREDIENT_NAME_LENGTH) + "...");
					newIngredientLabel.setToolTipText(new_name);
				}
				
				swapDetail.add(oldIngredientLabel);
				swapDetail.add(new JLabel(" -> "));
				swapDetail.add(newIngredientLabel);
				swapDetailsContainer.add(swapDetail);
			});
			
			
			
			
			panel.add(swapDetailsContainer, GBCUtility.createGBC(0,1,2,1));
		}
		
		checkBox.addItemListener(event->{
			if (event.getStateChange() != ItemEvent.SELECTED)
				return;
			System.out.println("TEST");
			if (checkBox.isSelected() && swaps == null) {
				int choice = JOptionPane.showConfirmDialog(
						null, 
						"In order to show Advised Data, you must get recommendations from your Meal History. Would you like to go there now?", 
						"Go to Meal History?", 
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.QUESTION_MESSAGE);
				if (choice == JOptionPane.YES_OPTION) {
					MainWindow.getInstance().setPage(new MealHistoryPage());
				}
				else {
					checkBox.setSelected(false);
				}
			}
			swapDetailsContainer.setVisible(checkBox.isSelected());
		});
		checkBox.addItemListener(item->{
			validateGenerateGraphsBtn();
		});
		swappedData = new IDataSetCreator() {	
			@Override
			public IDataSet getDataSet() {
				if (!checkBox.isSelected())
					return null;
				
				Filter filter = new Filter();
				filter.setDateRange(dateFrom.getDate(), dateTo.getDate());
				IFoodService foodService = FoodServiceFactory.getService();
				List<Food> meals = foodService.getMeals(filter);
				List<Food> snacks = foodService.getSnacks(filter);
				meals.addAll(snacks);
				
				IApplySwap swapApplier = ApplySwapFactory.createApplySwap();
				List<Integer> oldIngredients = new ArrayList<Integer>(swaps.size());
				List<Integer> newIngredients = new ArrayList<Integer>(swaps.size());
				swaps.forEach((_old,_new)->{
					oldIngredients.add(_old);
					newIngredients.add(_new);
				});
				
				List<Food> afterSwapping = swapApplier.applySwaps(newIngredients, oldIngredients, meals);
				return new FoodDataSet("Swapped Food", afterSwapping);
			}
		};
		return panel;
	}
	
	private JPanel createCFGDataSourceField() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		JCheckBox checkBox = new JCheckBox("Canada Food Guide");
		panel.add(checkBox, GBCUtility.createGBC(0,0));
		panel.add(Box.createHorizontalGlue(), GBCUtility.createFiller(1,0));
		
		CFGData = new IDataSetCreator() {	
			@Override
			public IDataSet getDataSet() {
				if (!checkBox.isSelected()) {
					return null;
				}
				IUserService userSrv = UserServiceFactory.getService();
				return new CFGDataSet("Canada Food Guide",userSrv.getCurrentProfile());
			}
		};
		checkBox.addItemListener(item->{
			validateGenerateGraphsBtn();
		});
		return panel;
	}
	
	private JPanel createGraphTypeSelectionPanel() {
		
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createRaisedBevelBorder());
		panel.setLayout(new BorderLayout());
		panel.add(createTitle("Select a Graph Type"), BorderLayout.PAGE_START);
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		//AverageGraphMode,FoodGroupMode,NutrientByDateMode, TotalGraphMode
		JRadioButton averageMode = new JRadioButton("Average Graph");
		JRadioButton foodGroupMode = new JRadioButton("Food Groups");
		JRadioButton totalMode = new JRadioButton("Total Nutrients");
		JRadioButton nutrientMode = new JRadioButton("Specific Nutrient");
		
		JPanel nutrientSelection = new JPanel();
		nutrientSelection.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
		nutrientSelection.setLayout(new GridLayout(0,2));
		ButtonGroup nutrientSelectionGroup = new ButtonGroup();
		INutrientService nutrientService = NutrientServiceFactory.getService();
		
		Map<ButtonModel, Integer> nutrientButtons = new HashMap<ButtonModel,Integer>();
		nutrientService.getAllNutrientIDs().forEach(nutrientID->{
			JCheckBox checkbox = new JCheckBox(nutrientService.getNutrientName(nutrientID));
			nutrientButtons.put(checkbox.getModel(), nutrientID);
			nutrientSelection.add(checkbox);
			nutrientSelectionGroup.add(checkbox);
			checkbox.setEnabled(false);
		});
		
		JPanel radioButtons = new JPanel();
		radioButtons.setLayout(new GridLayout(0,1));
		radioButtons.add(averageMode);
		radioButtons.add(foodGroupMode);
		radioButtons.add(totalMode);
		radioButtons.add(nutrientMode);
		innerPanel.add(radioButtons);
		innerPanel.add(nutrientSelection);
		panel.add(innerPanel, BorderLayout.LINE_START);
		
		averageMode.addActionListener(event->{
			graphTypeSelector = new IGraphTypeSelector() {
				@Override
				public IGraphMode getGraphMode() {
					
					return new AvgGraphMode();
				}
			};
		});
		foodGroupMode.addActionListener(event->{
			graphTypeSelector = new IGraphTypeSelector() {	
				@Override
				public IGraphMode getGraphMode() {
					return new FoodGroupMode();
				}
			};
		});
		totalMode.addActionListener(event->{
			graphTypeSelector = new IGraphTypeSelector() {	
				@Override
				public IGraphMode getGraphMode() {
					return new TotalGraphMode();
				}
			};
		});
		nutrientMode.addItemListener(event->{
			if (event.getStateChange() == ItemEvent.SELECTED) {
				nutrientButtons.keySet().forEach(buttonModel->{
					buttonModel.setEnabled(true);
				});
				if(nutrientSelectionGroup.getSelection() == null)
					nutrientSelectionGroup.getElements().nextElement().setSelected(true);
				graphTypeSelector = new IGraphTypeSelector() {	
					@Override
					public IGraphMode getGraphMode() {
						return new NutrientByDateMode(nutrientButtons.get(nutrientSelectionGroup.getSelection()));
					}
				};
			}
			else {
				nutrientButtons.keySet().forEach(buttonModel->{
					buttonModel.setEnabled(false);
				});
			}
		});
		
		
		
		
		ButtonGroup modeGroup = new ButtonGroup();
		modeGroup.add(averageMode);
		modeGroup.add(foodGroupMode);
		modeGroup.add(totalMode);
		modeGroup.add(nutrientMode);
		averageMode.doClick(); //automatically select first.
		return panel;
	}
	
	
	
	private JLabel createTitle(String title) {
		JLabel label = new JLabel(title);
		Font font = new Font("Arial", Font.BOLD, 18);
		Map<TextAttribute, Object> attributes = new HashMap<TextAttribute,Object>(font.getAttributes());
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		label.setFont(font.deriveFont(attributes));

		return label;
	}
	
	/** interface for some class that can get a data source.
	 * In this class, it is used in tandem with capture groups and anonymous classes
	 * to prevent the need for "global" variables.
	 * */
	private interface IDataSetCreator {
		public IDataSet getDataSet();
	}
	
	private interface IGraphTypeSelector {
		public IGraphMode getGraphMode();
	}
	
	public JFreeChart createChart() {
		
		List<IDataSet> datasets = new ArrayList<IDataSet>();
		if (userData != null) {
			IDataSet data = userData.getDataSet();
			if (data != null)
				datasets.add(data);
		}
			
		if (swappedData != null) {
			IDataSet data = swappedData.getDataSet();
			if (data != null)
				datasets.add(data);
		}
		if (CFGData != null) {
			IDataSet data = CFGData.getDataSet();
			if (data != null)
				datasets.add(data);
		}
		return GraphServiceFactory.getService().createGraph(datasets, graphTypeSelector.getGraphMode());
	}
	

	public void addActionListener(ActionListener listener) {
		actionListeners.add(listener);
	}
	public void removeActionListener(ActionListener listener) {
		actionListeners.remove(listener);
	}
	
	private void notifyActionListeners() {
		actionListeners.forEach(listener->{
			listener.actionPerformed(new ActionEvent(this, 0, "GenerateGraphs"));
		});
	}
}
