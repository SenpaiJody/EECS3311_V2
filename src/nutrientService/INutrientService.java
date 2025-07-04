package nutrientService;

import java.util.List;
import java.util.Map;

//Interface for anything that has to do with nutrients, such 
public interface INutrientService {

	//returns the sum of nutrients in the listed ingredients
	public Map<Integer, Double> getNutrientSumPer100g(List<Integer> ingredientIDs);
	
	//returns the list of nutrient maps matching the list of ingredient IDs provided such that the i'th ingredient ID corresponds with the i'th nutrient map
	public Map<Integer,Map<Integer,Double>> getNutrientsListPer100g(List<Integer> ingredientIDs); 
	
	//same as above but for a single ingredient
	public Map<Integer, Double> getNutrientsPer100g(int ingredientID);
	//returns a nutrients name when given an ID
	public String getNutrientName(int nutrientID);
	//erturns a nutrient's units when given an ID
	public String getNutrientUnit(int nutrientID);
}
