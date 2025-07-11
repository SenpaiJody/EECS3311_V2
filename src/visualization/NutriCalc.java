package visualization;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import database.CSVDatabase;
import food.Food;


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
 *     567 = roast chicken meat 
    3404	= Tofu, regular, firm or extra firm, raw (prepared with calcium sulphate and magnesium chloride)
    
    203	PROT	g
    204	FAT	g
    205	CARB	g
 */


// Map<Integer, Double> returns the nutrients as NutrientID and averaged amt per day (based on that period)


public class NutriCalc {
		

    private final CSVDatabase database;

    public NutriCalc(CSVDatabase database) {
        this.database = database;
    }
	
	
	Map<Integer, Double> avgNutrients(List<Food> foodList) {
		
		int numberOfDifferentDays = 0;
		
	    Map<Integer, Double> nutrientAverages = new HashMap<>();
	    if (foodList == null || foodList.isEmpty()) return nutrientAverages;

	    // Step 1: calculate number of days
	    Set<LocalDate> uniqueDates = new HashSet<>();

	    for (Food food : foodList) {
	        if (food == null) continue;
	        uniqueDates.add(food.getDate());
	    }

	    if (uniqueDates != null) {
	    	numberOfDifferentDays = uniqueDates.size();
	    }
 
	    // Step 2: sum all the nutrients
	    Map<Integer, Double> TotalNutrients = new HashMap<Integer, Double>();
	

	    	    
	    
	    for (Food eachMeal : foodList) {
	    	
	    	Map<Integer, Double> ingredients = eachMeal.getIngredients();
	    	
	    	if (ingredients == null) continue;
	    	
	    	// IngredietnID, ingredientAmount
	    	for (Map.Entry<Integer, Double> eachIngredient : ingredients.entrySet()) {
	            Integer ingredientID = eachIngredient.getKey();
	            Double ingredientAmount = eachIngredient.getValue();
	              	      
	            
	          /* The method in the database takes in lists of ingredientIDs:
	           * 
	           * public Map<Integer, Double> getNutrientsPer100g(List<Integer> ingredientIDs)
	           * 
	           * Thus we make a single list: */
	           
	            List<Integer> listOfcurrIngredient = List.of(ingredientID);
	            // nutrientID, nutrientAmount
	            Map<Integer, Double> nutrients = database.getNutrientSumPer100g(listOfcurrIngredient);
	            
	            for (Map.Entry<Integer, Double> eachNutrient: nutrients.entrySet()) {
	            	Integer nutrientID = eachNutrient.getKey();
		            Double nutrientAmountper100g = eachNutrient.getValue();
	            	
		            Double amtOfNutrientsinG = (nutrientAmountper100g / 100.0) * (ingredientAmount);
		            
		            //if the map already contains nutrientID, add amtOfNutrientsinG to the existing total.
		            //If not, just insert amtOfNutrientsinG as the starting value.
		            TotalNutrients.merge(nutrientID, amtOfNutrientsinG, Double::sum);
	            }
	        } 	
	    }

	    // Step 3: Average across all daily totals using numberOfDifferentDays
	    Map<Integer, Double> NutrientAvgs = new HashMap<Integer, Double>();

	    for (Map.Entry<Integer, Double> eachNutrientTotal : TotalNutrients.entrySet()) {
	    	Integer nutrientID = eachNutrientTotal.getKey();
            Double totalNutrientAmt = eachNutrientTotal.getValue();	        
            Double avgNutrientAmt = totalNutrientAmt / numberOfDifferentDays;        
            NutrientAvgs.put(nutrientID, avgNutrientAmt);   
	    }
	    return NutrientAvgs;
	}

	
}
