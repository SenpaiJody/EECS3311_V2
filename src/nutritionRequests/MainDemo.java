package nutritionRequests;

import foodService.*;
import java.time.LocalDate;
import javax.swing.JFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import Advising.*;
import nutritionRouting.*;
import userService.IUserService;
import userService.IncorrectLoginException;
import userService.Profile;
import userService.ProfileDoesNotExistException;
import userService.User;
import userService.UserServiceFactory;
public class MainDemo {
	
	
	public static void main (String[] args) {
		
		IUserService singletonUserService = UserServiceFactory.getService();
		INutritionRequestService nutritionRequestService = NutritionRequestServiceFactory.getService();

		try {
		    singletonUserService.attemptLogin("bobloblaw3", "mypassword");
		    System.out.println("Login successful.");
		} catch (IncorrectLoginException e) {
		    System.err.println("Login failed: Incorrect credentials.");
		    e.printStackTrace();
		    return;
		}
		
	    User currentUser = singletonUserService.getCurrentUser();
	    
	    if (currentUser == null) {
	        System.err.println("Current user is null after login. Something went wrong.");
	        return;
	    } else {
	        System.out.println("Current user obtained: " + currentUser.getUserID());
	    }

	    boolean profileFound = false;

	    for (Profile p : currentUser.getProfiles()) {
	        System.out.println("Checking profile with ID: " + p.getID());
	        if (p.getID() == 11) {
	            System.out.println("Profile with ID 11 found.");
	            try {
	                singletonUserService.setCurrentProfile(p);
	                System.out.println("Current profile set to profile ID 11.");
	            } catch (ProfileDoesNotExistException e) {
	                System.err.println("ProfileDoesNotExistException thrown when setting profile ID 11.");
	                e.printStackTrace();
	            }
	            profileFound = true;
	            break;
	        }
	    }

	    if (!profileFound) {
	        System.err.println("Profile with ID 11 not found among current user's profiles.");
	    }

	    if (singletonUserService.getCurrentProfile() == null) {
	        System.err.println("Failed to set current profile. Check that Profile ID 11 exists for this user.");
	        return;
	    } else {
	        System.out.println("Current profile successfully set: " + singletonUserService.getCurrentProfile().getID());
	    }
	    
	    
		INutritionRoutingService routing = NutritionRoutingServiceFactory.getService();
		// Filter
		LocalDate dateStart = LocalDate.of(2025, 6, 1);
        LocalDate dateEnd = LocalDate.of(2025, 6, 6);
        Filter filter = new Filter();
        filter.setDateRange(dateStart,dateEnd);
		
		// GraphMode: AVG, TOTAL, NUTRIENTBYDATE, NUTRIENTPERMEAL, FOODGROUP
		GraphMode mode = GraphMode.NUTRIENTBYDATE;
        
		//GraphType: Bar, Pie or Line
        //NUTRIENTBYDATE, NUTRIENTPERMEAL only can do LINE
		//AVG and CUMUL can only do BAR/PIE
		//FOODGROUP can only do PIE
		GraphType type = GraphType.LINE;
		
        // Goal
		NutritionGoal goal = new NutritionGoal(101, 203,2,GoalType.INCREASE, null);
		
		// CFGComparison: checking to see if the user wants a reference point to the 
		// NutrientPerMealLine not possible with CFG recommendations (CFG only gives daily recommendations)
		Boolean CFGComparison = true;
				
		// Advised vs Historical
		Boolean AdvisedData = true;
		
		// Nutrient choice: Only for NutrientByDateGraphRequests / Line graph requests
		// Default could be PROTEIN if not chosen
		Nutrient nutrientChoice=Nutrient.SODIUM;
		
		if (nutrientChoice == null) {
			nutrientChoice = Nutrient.PROTEIN;
		}
		
		/*Important: when AdvisedGraphRequest and HistoricalGraphRequest both extend
		 * GraphRequest but have different methods. Their subclasses all have the same methods
		 * but when creating the request from the outset it is necessary to choose the right one
		 * for initialization, this is enforced with # of parameter so shouldn't be an issue
		 */
		
		JFreeChart chart;
		
		if	(AdvisedData) {
			
			//if requesting an Advised Graph, note it has the extra parameter of goal...
			AdvisedGraphRequest advisedRequest = nutritionRequestService.createAdvisedGraphRequest(filter, mode,type,CFGComparison, goal);
			advisedRequest.setNutrientChoice(nutrientChoice);
						
			System.out.println(advisedRequest.getProfile().getDateOfBirth());
			System.out.println(advisedRequest.getProfile().getGender());
			
			chart = routing.createGraph(advisedRequest);
			
		} else {
			
			//if requesting a Historical Graph...
			HistoricalGraphRequest historicalRequest = nutritionRequestService.createHistoricalGraphRequest(filter, mode,type,CFGComparison);
			historicalRequest.setNutrientChoice(nutrientChoice);
		
			System.out.println(historicalRequest.getProfile().getDateOfBirth());
			System.out.println(historicalRequest.getProfile().getGender());
			
			chart = routing.createGraph(historicalRequest);	
		}
		
    	DisplayChart(chart);	
	}
	
	public static void DisplayChart(JFreeChart chart) {
		
    	JFrame frame = new JFrame("Chart");
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.add(new ChartPanel(chart));
    	frame.pack();
    	frame.setLocationRelativeTo(null); // center it
    	frame.setVisible(true);
		
	}

}
