package Advising;


import java.time.LocalDate;
import java.util.*;
import foodService.Filter;
import food.Food;
import food.Lunch;


//hard coded for demo purposes

public class AdvisingService {
	//private NutriCalc calc;
	
	

	public AdvisingService() {

	}
	
	public List<Food> produceSwapMeals(Filter filter){
		List<Food> swapMealList = new ArrayList<>();
		
    	Lunch lunch = new Lunch();
    	int foodIdCounter = 1;
    	
		for (int day = 1; day <= 6; day++) {
    	    LocalDate date = LocalDate.of(2025, 6, day);

    	    // Vary tofu amount: 180g + (day * 12)
    	    double tofuAmount = 180.0 + (day * 12);
    	    HashMap<Integer, Double> tofuMap = new HashMap<>();
    	    tofuMap.put(4909, tofuAmount);

    	    Food tofuMeal = new Food(foodIdCounter++,"Tofu Lunch " + day, tofuMap, date, lunch);
    	    swapMealList.add(tofuMeal);
    	}
		
		return swapMealList;
	}
	
}
