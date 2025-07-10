package visualCalculationService;

import java.time.LocalDate;
import nutrientService.*;
import nutritionRequests.Nutrient;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import food.Food;
import ingredientService.IIngredientService;


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


public class VisualCalculationService implements IVisualCalculationService {
	
    private INutrientService nutrientService;
    private IIngredientService ingredientService;
    public VisualCalculationService(INutrientService nutrientService, IIngredientService ingredientService) {
        this.nutrientService = nutrientService;
        this.ingredientService = ingredientService;
    }
	
    public String getMealTag (Food meal) {
    	
    	String mealType = meal.getType().getTypeName();    
        String date = meal.getDate().toString();
        String mealTag = mealType + " (" + date + ")";
        
        return mealTag;
    }
	
	public Map<Integer, Double> avgNutrients(List<Food> foodList) {
		
	Map<Integer, Double> nutrientAverages = new HashMap<Integer, Double>();
	
    if (foodList == null || foodList.isEmpty()) return nutrientAverages;

	    // Step 1: calculate number of days
	    int distinctDaysCount = countDistinctDays(foodList);
	    if (distinctDaysCount == 0) return nutrientAverages;
	    
	    // Step 2: sum all the nutrients
	    Map<Integer, Double> TotalNutrients = new HashMap<Integer, Double>();
	    
	    
	    for (Food eachMeal : foodList) {
	    	
	    	Map<Integer, Double> ingredients = eachMeal.getIngredients();
	    	
	    	  if (ingredients == null || ingredients.isEmpty()) {
	    	        continue;
	    	    }
	    	
	    	// IngredietnID, ingredientAmount
	    	for (Map.Entry<Integer, Double> eachIngredient : ingredients.entrySet()) {
	    		
	    		
	            Integer ingredientID = eachIngredient.getKey();
	            Double ingredientAmount = eachIngredient.getValue();
	            
	            // nutrientID, nutrientAmount
	            Map<Integer, Double> nutrients = nutrientService.getNutrientsPer100g(ingredientID);
	            if (nutrients == null ) {
	            	continue;
	            }
	                      
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

	    
	    //  Average across all daily totals using distinctDaysCount
	    for (Map.Entry<Integer, Double> eachNutrientTotal : TotalNutrients.entrySet()) {
	    	Integer nutrientID = eachNutrientTotal.getKey();
            Double totalNutrientAmt = eachNutrientTotal.getValue();	        
            Double avgNutrientAmt = totalNutrientAmt / distinctDaysCount;        
            nutrientAverages.put(nutrientID, avgNutrientAmt); 
	    }
	   
	    
	    return nutrientAverages;
	}

	
	public Map<Integer, Double> totalNutrients(List<Food> foodList) {
	    Map<Integer, Double> totalNutrients = new HashMap<>();

	    if (foodList == null) {
	        System.out.println("foodList is null.");
	        return totalNutrients;
	    }

	    for (Food eachMeal : foodList) {
	        Map<Integer, Double> ingredients = eachMeal.getIngredients();
	        if (ingredients == null) continue;

	        for (Map.Entry<Integer, Double> eachIngredient : ingredients.entrySet()) {
	            Integer ingredientID = eachIngredient.getKey();
	            Double ingredientAmount = eachIngredient.getValue();

	            Map<Integer, Double> nutrients = nutrientService.getNutrientsPer100g(ingredientID);

	            for (Map.Entry<Integer, Double> eachNutrient : nutrients.entrySet()) {
	                Integer nutrientID = eachNutrient.getKey();
	                Double nutrientAmountPer100g = eachNutrient.getValue();

	                Double amountOfNutrientInG = (nutrientAmountPer100g / 100.0) * ingredientAmount;
	                totalNutrients.merge(nutrientID, amountOfNutrientInG, Double::sum);
	            }
	        }
	    }

	    return totalNutrients;
	}
	
	public int countDistinctDays(List<Food> foodList) {
	    
		int distinctDaysCount = 0;
		
		if (foodList == null) return distinctDaysCount;
	    
	    Set<LocalDate> uniqueDates = new HashSet<>();

	    for (Food food : foodList) {
	        if (food == null) continue;
	        uniqueDates.add(food.getDate());
	    }

	    if (uniqueDates != null) {
	    	distinctDaysCount = uniqueDates.size();
	    }
	    return distinctDaysCount;
	}
	
	
	public List<Map.Entry<String, Double>> getNutrientPerMealList (List<Food> foodList, Nutrient nutrientChoice) {
		
	    List<Map.Entry<String, Double>> nutrientByDateList = new ArrayList<>();
	    //int nutrientID = nutrientChoice.getId();

	    if (foodList == null) return nutrientByDateList;

	    for (Food eachMeal : foodList) {
	    	
	    	double perMealNutrientTotal = 0.0;
	    	
	        Map<Integer, Double> ingredients = eachMeal.getIngredients();
	        if (ingredients == null) continue;

	        for (Map.Entry<Integer, Double> eachIngredient : ingredients.entrySet()) {
	            Integer ingredientID = eachIngredient.getKey();
	            Double ingredientAmount = eachIngredient.getValue();

	            Map<Integer, Double> nutrients = nutrientService.getNutrientsPer100g(ingredientID);

	            Double nutrientAmountPer100g = nutrients.get(nutrientChoice.getID());
	                
	                if (nutrientAmountPer100g != null) {
		               perMealNutrientTotal += (nutrientAmountPer100g / 100.0) * ingredientAmount;
		            }
	                
	            }
	        
	        nutrientByDateList.add(new AbstractMap.SimpleEntry<>(getMealTag(eachMeal), perMealNutrientTotal));
	     
	        }
	       
	    return nutrientByDateList;
	}

	public List<Map.Entry<String, Double>> getNutrientByDateList (List<Food> foodList, Nutrient nutrientChoice) {

	    Map<LocalDate, Double> nutrientTotalsByDate = new HashMap<>();

	    if (foodList == null) return new ArrayList<>();

	    for (Food eachMeal : foodList) {

	        LocalDate date = eachMeal.getDate();
	        double perMealNutrientTotal = 0.0;

	        Map<Integer, Double> ingredients = eachMeal.getIngredients();
	        if (ingredients == null) continue;

	        for (Map.Entry<Integer, Double> eachIngredient : ingredients.entrySet()) {
	            Integer ingredientID = eachIngredient.getKey();
	            Double ingredientAmount = eachIngredient.getValue();

	            Map<Integer, Double> nutrients = nutrientService.getNutrientsPer100g(ingredientID);

	            Double nutrientAmountPer100g = nutrients.get(nutrientChoice.getID());

	            if (nutrientAmountPer100g != null) {
	                perMealNutrientTotal += (nutrientAmountPer100g / 100.0) * ingredientAmount;
	            }
	        }

	        nutrientTotalsByDate.merge(date, perMealNutrientTotal, Double::sum);
	    }

	    // Convert to List<Map.Entry<String, Double>> using date.toString() as the key
	    List<Map.Entry<String, Double>> result = new ArrayList<>();
	    for (Map.Entry<LocalDate, Double> entry : nutrientTotalsByDate.entrySet()) {
	        result.add(new AbstractMap.SimpleEntry<>(entry.getKey().toString(), entry.getValue()));
	    }

	    // Optional: sort by date
	    result.sort(Map.Entry.comparingByKey());

	    return result;
	}
	
	public Map<String, Double> getFoodGroupIntakePercentages(List<Food> foodList) {
		Map<Integer, Double> foodGroupTotals = new HashMap<>();
	    double totalAmount = 0.0;

	    for (Food food : foodList) {
	        Map<Integer, Double> ingredients = food.getIngredients();
	        
	        for (Map.Entry<Integer, Double> ingredientEntry : ingredients.entrySet()) {
	            int ingredientID = ingredientEntry.getKey();
	            double quantity = ingredientEntry.getValue();

	            int foodGroupID = ingredientService.getFoodGroup(ingredientID);
	            foodGroupTotals.put(foodGroupID, foodGroupTotals.getOrDefault(foodGroupID, 0.0) + quantity);
	            totalAmount += quantity;
	        }
	    }

	    Map<String, Double> foodGroupPercentages = new HashMap<>();
	    for (Map.Entry<Integer, Double> entry : foodGroupTotals.entrySet()) {
	        String groupName = ingredientService.getFoodGroupName(entry.getKey());
	        double percentage = (entry.getValue() / totalAmount) * 100.0;
	        foodGroupPercentages.put(groupName, percentage);
	    }

	    return foodGroupPercentages;
	}
	
	
}
