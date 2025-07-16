package foodService;

import java.util.List;

import food.Food;


/**Interface that describes all the methods that a food database should have
 * */
public interface IFoodDB {
	
	/**Get the provided profile's meals that match the given filter.
	 * 	@param profileID - the profile ID whose meals should be searched for
	 *  @param filter  - The Filter Object that describes which meals should be obtained
	 *  
	 *  @return The list of Foods that match the filter. The returned list is empty if no meals matches the filter
	 * */
	public List<Food> getMeals(int profileID, Filter filter);
	
	
	/**Get the provided profile's snacks that match the given filter.
	 *  @param profileID - the profile whose meals should be searched for
	 * 	@param filter  - The Filter Object that describes which snacks should be obtained
	 *  
	 *  @return The list of Foods that match the filter. The returned list is empty if no meals matches the filter
	 * */
	public List<Food> getSnacks(int profileID, Filter filter);
	
	
	/** Save the provided food object as a Snack.
	 * 
	 * @param profileID - the profile ID to save the food under
	 * @param Food - the food object to save
	 * */
	public void saveSnack(int profileID, Food food);
	
	/** Save the provided food object as a Meal.
	 * 
	 * @param profileID - the profile ID to save the food under
	 * @param Food - the food object to save
	 * */
	public void saveMeal(int profileID, Food food);
	
	
	/** Generates a food ID that is guaranteed to be unique
	 *	@return a unique food ID that does not correspond to any existing food item
	 * */
	public int generateFoodID();
}
