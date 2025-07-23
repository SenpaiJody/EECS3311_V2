package ingredientService;
import java.util.List;
import recommendation.GoalType;
import java.util.Map;

/**An Interface describing all the services that has to do with ingredients (i.e.,the pieces that make up a meal or snack*/
public interface IIngredientService {
	
	
	/**Gets up to 'maxResults' best matching ingredients when provided a search term
	 * 
	 * @param searchTerm - the term to search for
	 * @param maxResults - the maximum results to return
	 * 
	 * @return List of Ingredient ID's that best match the search term. If nothing matches, an empty list is returned
	 * */
	public List<Integer> searchIngredientByName(String searchTerm, int maxResults);
	
	
	
	
	/** Gets the name of an Ingredient when provided an ingredient ID.
	 * @param ingredientID - the ingredient to get the name of
	 * 
	 * @return String denoting the name of the ingredient; if no ingredient exists, an empty string is returned.
	 * */
	public String getIngredientName(int ingredientID);
	
	
	/** Gets the names of all Ingredients when provided an ingredient ID.
	 * @param ingredientIDs - a list of the ingredients to get the names of
	 * 
	 * @return A list of string denoting the name of the ingredients, in the same order in which they were provided.
	 * If the ingredient does not exist, its name is returned as the empty string
	 * */
	public List<String> getIngredientNames(List<Integer> ingredientIDs);
	
	
	/**Gets up to 'maxResult' ingredients that best match the nutrient amounts provided, prioritizing some specific nutrientID and goal type. 
	 * <p> TODO: this function is going to be changed soon (deliverable 3)
	 * @param nutrients - A Map of IDs and Quantities of nutrients
	 * @param maxResults - the maximum number of results to return
	 * 
	 * @return A list of ingredientIDs that best match the nutrient amounts provided.
	 * */
	public List<Integer> getIngredientMatchingNutrients(Map<Integer, Double> target, int maxResults, Integer nutrientID, GoalType type);
	
	
	/**Gets up to 'maxResult' ingredients that best match the nutrient amounts provided, prioritizing some specific nutrientID and goal type. 
	 * <p> TODO: this function is going to be changed soon (deliverable 3)
	 * @param nutrients - A Map of IDs and Quantities of nutrients
	 * @param maxResults - the maximum number of results to return
	 * 
	 * @return A list of ingredientIDs that best match the nutrient amounts provided.
	 * */
	public List<Integer> getIngredientMatchingNutrients(Map<Integer, Double> target, int maxResults,List <Integer> nutrientID, List <GoalType> type);
	
	
	
	/**Gets the food group ID of the provided ingredient
	 * @param ingredientID - the ingredient to search for
	 * 
	 * @returns the food group ID, or -1 if the ingredient does not exist.
	 * */
	public int getFoodGroup(int ingredientID);
	
	/**Gets the name of the provided food group ID
	 * @param foodGroupID - the food group to search for
	 * 
	 * @returns the name of the food group. If the food group does not exist, the empty string is returned.
	 * */
	public String getFoodGroupName(int foodGroupID);
	
}
