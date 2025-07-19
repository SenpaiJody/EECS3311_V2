package ingredientService;

import java.util.List;


/** Interface describing the methods that an Ingredient Database should have
 * */
public interface IIngredientDB {

	/** gets the name of the provided ingredient
	 * @param id - ID of the ingredient to search
	 * @return the ingredient name
	 * */
	public String getIngredientName(int id);
	
	/** gets the names of the provided ingredients
	 * @param ids - a list of ID of the ingredients to search
	 * @return the ingredient names
	 * */
	public List<String> getIngredientNames(List<Integer> ids);
	
	/** gets an iterator that iterates through all ingredients in the database.
	 *  @return an {@link #IIngredientIterator} object that iterates through all ingredients in the database
	 * */
	public IIngredientIterator getIterator();
	
	/**gets the given ingredient's food group ID
	 * @param ingredientID - the ID of the food
	 * @returns the ID of the food group of that food, or -1 if the ingredient doesn't exist
	 * */
	public int getFoodGroup(int ingredientID);
	
	/**gets the given food group's name
	 * @param foodGroupID - the ID of the food
	 * @returns the food group's name, or the empty string if the food group does not exist
	 * */
	public String getFoodGroupName(int foodGroupID);
};
