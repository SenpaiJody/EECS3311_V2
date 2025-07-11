package visualization;

import database.*;

import java.util.*;

import org.jfree.chart.JFreeChart;

import foodService.Filter;
import food.Food;
import food.FoodType;
import food.Lunch;

import org.jfree.chart.ChartPanel;
import javax.swing.*;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;

import static visualization.GraphType.*;

// if needed, this was deleted: module Visualization {}

/*
 * 
 * 567	Nutrient ID 203 Protein	28.93 (in grams)
 * 
 * Where quantity is in grams:
 * 
 * actualAmount = (28.93 grams / 100 grams) * (quantity)
 * division by 100 makes the rate become amount of nutrient per 1 gram,
 * than you multiply by grams
 * 
 * 		Filter filter = new Filter();
		filter.setDateRange(startDate, endDate);
 * 
 * 
 * 		// Exclude:  Cream cheese = 28, milk = 61, spaghetti pasta = 4464
        ArrayList<Integer> excludedIngredients = new ArrayList<Integer>(Arrays.asList(28, 61, 4464));
        
		// Include: roast chicken meat = 567, ground beef = 2698
        ArrayList<Integer> includedIngredients = new ArrayList<Integer>(Arrays.asList(567, 2698));
 * 
 */


// With the use of AI
public class GUIclient {
	
	public static void main(String[] args) {

		
        LocalDate dateStart = LocalDate.of(2025, 6, 1);
        LocalDate dateEnd = LocalDate.of(2025, 6, 6);
			
		CSVDatabase csvDatabase = new CSVDatabase();
		
		/* Chooses current implementation of GraphFactory, this could be swapped out later if you wanted to test different
		 * GraphFactory implementations
		 */
	    GraphFactory graphFactory = new GraphFactory();
	
	    // Inject factory into service
	    GraphService graphService = new GraphService(graphFactory,csvDatabase);
		
	    
	    // For Testing: Creates two meal list
	    
	    List<Food> mealList = new ArrayList<>();
    	List<Food> swapMealList = new ArrayList<>();

    	Lunch lunch = new Lunch();
    	int foodIdCounter = 1;

    	for (int day = 1; day <= 6; day++) {
    	    LocalDate date = LocalDate.of(2025, 6, day);

    	    // Vary chicken amount: 200g + (day * 10)
    	    double chickenAmount = 200.0 + (day * 10);
    	    HashMap<Integer, Double> chickenMap = new HashMap<>();
    	    chickenMap.put(567, chickenAmount);
  
    	    Food chickenMeal = new Food(foodIdCounter++, "Chicken Lunch " + day, chickenMap , date, lunch);
    	    mealList.add(chickenMeal);

    	    // Vary tofu amount: 180g + (day * 12)
    	    double tofuAmount = 180.0 + (day * 12);
    	    HashMap<Integer, Double> tofuMap = new HashMap<>();
    	    tofuMap.put(3404, tofuAmount);

    	    Food tofuMeal = new Food(foodIdCounter++,"Tofu Lunch " + day, tofuMap, date, lunch);
    	    swapMealList.add(tofuMeal);
    	}
        
    	printMealDetails(mealList);
    	printMealDetails(swapMealList);
	    
    	
    	// First Request: Display Averages
    	// Send meal lists to NutriCalc to get required calculations
    	NutriCalc calc = new NutriCalc(csvDatabase);
    
    	Map<Integer, Double> mealListNutrientAmounts = calc.avgNutrients(mealList);
    	Map<Integer, Double> swapMealListNutrientAmounts = calc.avgNutrients(swapMealList);	
	    
    	// All for testing purposes only
    	JFreeChart barChart = graphService.generateGraph(NUTRIENT_AVGS_BAR_GRAPH, csvDatabase, dateStart, dateEnd, mealListNutrientAmounts, swapMealListNutrientAmounts);
    	JFreeChart pieChartMeal = graphService.generateGraph(NUTRIENT_AVGS_PIE_GRAPH, csvDatabase, dateStart, dateEnd, mealListNutrientAmounts, swapMealListNutrientAmounts);
    	JFreeChart pieChartSwap = graphService.generateGraph(NUTRIENT_AVGS_PIE_GRAPH, csvDatabase, dateStart, dateEnd, swapMealListNutrientAmounts, swapMealListNutrientAmounts);  	


    	Scanner scanner = new Scanner(System.in);
    	int choice = -1;

    	while (true) {
    	    System.out.println("Select chart to display:");
    	    System.out.println("1 - Averages Bar Chart");
    	    System.out.println("2 - Averages Pie Charts Side by Side");
    	    System.out.print("Enter choice (1 or 2): ");

    	    if (scanner.hasNextInt()) {
    	        choice = scanner.nextInt();

    	        if (choice == 1) {
    	        	DisplayBarChartTest(barChart);
    	            break; 
    	        } else if (choice == 2) {
    	            DisplayPieChartTest(pieChartMeal, pieChartSwap);
    	            break;
    	        } else {
    	            System.out.println("Invalid choice, please try again.");
    	        }
    	    } else {
    	
    	        System.out.println("Invalid input, please enter a number.");
    	        scanner.next();
    	    }
    	}

    	scanner.close();
	    
	}
	
	
	
	public static void DisplayBarChartTest(JFreeChart chart) {
		
    	JFrame frame = new JFrame("Nutrient Averages Chart");
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.add(new ChartPanel(chart));
    	frame.pack();
    	frame.setLocationRelativeTo(null); // center it
    	frame.setVisible(true);
		
	}

	
	public static void DisplayPieChartTest(JFreeChart pieChartMeal, JFreeChart pieChartSwap) {
		
    	ChartPanel mealPanel = new ChartPanel(pieChartMeal);
    	ChartPanel swapPanel = new ChartPanel(pieChartSwap);
    	
    	JPanel chartsPanel = new JPanel(new GridLayout(1, 2)); // Use this!
    	chartsPanel.add(mealPanel);
    	chartsPanel.add(swapPanel);

    	JFrame frame = new JFrame("Nutrient Averages");
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setContentPane(chartsPanel);
    	frame.pack();
    	frame.setLocationRelativeTo(null);
    	frame.setVisible(true);
	}
	

	public static void printMealDetails(List<Food> meals) {
	    for (Food food : meals) {
	        System.out.println("Meal: " + food.getName());
	        System.out.println("Date: " + food.getDate());

	        Map<Integer, Double> ingredients = food.getIngredients();

	        if (ingredients == null || ingredients.isEmpty()) {
	            System.out.println("  No ingredients listed.");
	        } else {
	            for (Map.Entry<Integer, Double> entry : ingredients.entrySet()) {
	                Integer ingredientID = entry.getKey();
	                Double quantity = entry.getValue();
	                System.out.println("  Ingredient ID: " + ingredientID + " , Quantity: " + quantity + "g");
	            }
	        }

	        System.out.println(); // for spacing
	    }
	}


}
