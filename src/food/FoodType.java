package food;

import foodService.InvalidFoodTypeException;


/**Something that represents the "type" of food a food object is. 
 * <br>Doing this via composition instead of inheritance allows us to separate the food state (which are the same for all foods and exist in the food class)
 * <br> and the food "behavior", which is determined by food type
 * 
 * <p> note that the Food Types (breakfast, lunch, dinner, snack) are NOT derived from the {@link Food} class, rather they are composed into it. 
 * */

public interface FoodType {
	/**Saves the food according to the FoodTypes implementation
	 * @param food The food to save. 
	 * @throws InvalidFoodTypeException if this foodtype cannot save
	 * */
	void save(Food food) throws InvalidFoodTypeException;
	/**Gets the name of this FoodType
	 * @return The name of this food type
	 * */
	public String getTypeName();
}
