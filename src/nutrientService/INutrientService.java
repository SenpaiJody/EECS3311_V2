package nutrientService;

import java.util.List;
import java.util.Map;

/**The interface describing the methods that have to do with nutrients
 * */
public interface INutrientService {

	/**gets the sum of nutrients in the listed ingredients
	 * @param ingredientIDs - the ingredients to search for
	 * 
	 * @return a Map of Nutrient ID's and amounts corresponding to the ingredients provided.
	 * */
	public Map<Integer, Double> getNutrientSumPer100g(List<Integer> ingredientIDs);
	
	
	/**gets the nutrients of a provided list of ingredients, separated for each ingredient.
	 * @param ingredientIDs - the ingredients to search for
	 * 
	 * @return a Map of Ingredient IDs mapped to a map of Nutrient IDs mapped to their amounts
	 * */
	public Map<Integer,Map<Integer,Double>> getNutrientsListPer100g(List<Integer> ingredientIDs); 
	
	
	/**gets the nutrients of a provided ingredient
	 * @param ingredientID - the ingredient to search for
	 * 
	 * @return a map of Nutrient IDs mapped to their amounts
	 * */
	public Map<Integer, Double> getNutrientsPer100g(int ingredientID);
	
	
	/** Gets a nutrient's name when provided a nutrient ID
	 * @param nutrientID - ID of the nutrient to search for.
	 * @returns the nutrient's name
	 * */
	public String getNutrientName(int nutrientID);
	/** Gets a nutrient's unit when provided a nutrient ID
	 * @param nutrientID - ID of the nutrient to search for.
	 * @returns the nutrient's unit
	 * */
	public String getNutrientUnit(int nutrientID);
	
	
	/** Gets a list of ALL nutrient IDs
	 * @returns a list of all nutrient IDs
	 * */
	public List<Integer> getAllNutrientIDs();
	
	/** Get an iterator that lets the user loop through all nutrient profiles
	 * @return an {@link INutrientIterator}
	 * */
	public INutrientIterator getIterator();
}
