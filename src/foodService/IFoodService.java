package foodService;

import java.time.LocalDate;
import java.util.List;
import food.FoodType;
import food.Food;

/** Interface that defines the methods that a service that deals with Foods has
 * This Interface assumes that a user has already logged in.
 * These should be instantiated from the FoodServiceFactory class
 * 
 * @see {@link FoodServiceFactory#getService()}
 * */
public interface IFoodService {
	
	/**
	 * Get all meals that match the given filter.
	 * 	@param filter  - The Filter Object that describes which meals should be obtained
	 * 
	 *  
	 *  @return The list of Foods that match the filter. The returned list is empty if no meals matches the filter
	 * */
	public List<Food> getMeals(Filter filter);
	
	
	
	/**
	 * Get the all Snacks that match the given filter.
	 * 	@param filter  - The Filter Object that describes which snacks should be obtained
	 *  
	 *  @return The list of Foods that match the filter. The returned list is empty if no meals matches the filter
	 * */
	public List<Food> getSnacks(Filter filter);
	
	
	
	
	/**
	 *  Saves a Food Object in the database as a Snack.
	 * 	@param filter  - The Filter Object that describes which snacks should be obtained
	 * 
	 *  @return The list of Foods that match the filter. The returned list is empty if no meals matches the filter
	 *  
	 *  @throws InvalidFoodTypeException if the food is not a Snack.
	 * */
	public void saveSnack(Food food)  throws InvalidFoodTypeException;
	
	/**
	 *  Saves a Food Object in the database as a Meal.
	 * 	@param filter  - The Filter Object that describes which snacks should be obtained
	 * 
	 *  @return The list of Foods that match the filter. The returned list is empty if no meals matches the filter
	 *  
	 *  @throws InvalidFoodTypeException if the food is not a meal or is not a food type that is allowed to be saved on that day.
	 * */
	public void saveMeal(Food food) throws InvalidFoodTypeException;
	
	
	/** Gets all valid food types for the provided date. There can only be one of each Breakfast, Lunch and Dinner  per day but Snacks are always allowed.
	 * <p> For example, if the provided date already has a Breakfast stored, the Breakfast foodtype will not be in the included list.
	 * 
	 * @param date - The date for which the valid food types are retrieved
	 * @return A List of FoodType objects that are allowed to be saved on that day. a Snack object will always exist in this list.
	 * */
	public List<FoodType> getValidFoodTypes(LocalDate date);
	
	
	/** Generates a Food ID that is guaranteed to be unique
	 * 
	 * @returns a unique food ID that does not correspond to any other food.
	 * */
	public int generateFoodID();
}
