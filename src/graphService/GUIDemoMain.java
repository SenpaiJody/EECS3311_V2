package graphService;

import applySwap.*;
import applySwap.ApplySwapFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import food.Food;
import foodService.Filter;
import foodService.FoodServiceFactory;
import foodService.IFoodService;
import userService.IUserService;
import userService.IncorrectLoginException;
import userService.Profile;
import userService.ProfileDoesNotExistException;
import userService.User;
import userService.UserServiceFactory;



public class GUIDemoMain {

	public static void main (String[] args) {

	IUserService userService = UserServiceFactory.getService();
	IFoodService foodService = FoodServiceFactory.getService();
	IApplySwap applySwap = ApplySwapFactory.createApplySwap();
	IGraphService graphService = GraphServiceFactory.getService();

	
	// Test user login for Demo
	try {
	    userService.attemptLogin("bobtest3", "mypassword");
	    System.out.println("Login successful.");
	} catch (IncorrectLoginException e) {
	    System.err.println("Login failed: Incorrect credentials.");
	    e.printStackTrace();
	    return;
	}

    User currentUser = userService.getCurrentUser();

    if (currentUser == null) {
        System.err.println("Current user is null after login. Something went wrong.");
        return;
    } else {
        System.out.println("Current user obtained: " + currentUser.getUserID());
    }

    for (Profile p : currentUser.getProfiles()) {
        System.out.println("Checking profile with ID: " + p.getID());
        if (p.getID() == 11) {
            System.out.println("Profile with ID 11 found.");
            try {
                userService.setCurrentProfile(p);
                System.out.println("Current profile set to profile ID 11.");
            } catch (ProfileDoesNotExistException e) {
                System.err.println("ProfileDoesNotExistException thrown when setting profile ID 11.");
                e.printStackTrace();
            }
            break;
        }
    }

    
    /*Parameters that would be needed from GUI/User input
     * 
     * 
     */
	LocalDate dateStart = LocalDate.of(2025, 6, 1);
    LocalDate dateEnd = LocalDate.of(2025, 6, 7);
    Filter filter = new Filter();
    filter.setDateRange(dateStart,dateEnd);
    List<Food> foodList = foodService.getMeals(filter);
   
    String userIntakeLegendLabel = "User Intake";
	FoodDataSet foodDataSet = new FoodDataSet(userIntakeLegendLabel, foodList);	
	
	// Old ingredients set by user during recommendation
	List<Integer> oldIngredients = Arrays.asList(567, 16, 501841); 
	
	//newIngredients returned by recommendation
	List<Integer> newIngredients = Arrays.asList(1509, 2146, 501799); 
	
	// SwapFoodList from applySwap
	List<Food> swapFoodList = applySwap.applySwaps(newIngredients, oldIngredients, foodList);
	
	String advisedIntakeLegendLabel = "Advised Intake";
	FoodDataSet swapFoodDataSet = new FoodDataSet(advisedIntakeLegendLabel, swapFoodList);

	String CFGRecommendationLegendLabel = "CFG Adequate Intake";
	CFGDataSet cfgDataSet = new CFGDataSet(CFGRecommendationLegendLabel, userService.getCurrentProfile(),dateStart,dateEnd);

	List<IDataSet> data = new ArrayList<>();
//	data.add(foodDataSet);
//	data.add(swapFoodDataSet);
	data.add(cfgDataSet);
	

	
	/*
	 * Choosing one type of chart at a time for the Demo
	 */
	

//	FoodGroupMode foodGroupMode = new FoodGroupMode();
//	JFreeChart foodGroupChart = graphService.createGraph(data,foodGroupMode);
//	DisplayChart(foodGroupChart);

	/*Protein (203) g, Fats (204) g, Carbohydrates (205) g, Calories (208),kcal
	 * Cholesterol (601), Sodium (307), Potassium (306), Calcium(301),
	 * Iron (303), Vitamin C(401) and Vitamin D (324)
	 */
	
//	int nutrientChoice=203;
//	NutrientByDateMode nutrientByDateMode = new NutrientByDateMode(nutrientChoice);
//	JFreeChart nutrientByDateChart =  graphService.createGraph(data, nutrientByDateMode);
//	DisplayChart(nutrientByDateChart);

	AvgGraphMode avgGraphMode = new AvgGraphMode();
	JFreeChart avgGraphChart =  graphService.createGraph(data, avgGraphMode);
	DisplayChart(avgGraphChart);

//	TotalGraphMode totalGraphMode = new TotalGraphMode();
//	JFreeChart totalGraphChart =  graphService.createGraph(data, totalGraphMode);
//	DisplayChart(totalGraphChart);

	}

	public static void DisplayChart(JFreeChart chart) {

    	JFrame frame = new JFrame("Chart");
    	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    	frame.add(new ChartPanel(chart));
    	frame.pack();
    	frame.setLocationRelativeTo(null); // center it
    	frame.setVisible(true);

	}
}
