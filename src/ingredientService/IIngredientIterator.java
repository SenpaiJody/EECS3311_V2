package ingredientService;

/** An iterator that iterates through ingredients
 * */
public interface IIngredientIterator {
	/**Gets the Name of the ingredient associated with the current iteration
	 * @return ingredient name of current iteration*/
	public String getName();
	
	/**Gets the ID of the ingredient associated with the current iteration
	 * @return ingredient ID of current iteration*/
	public int getID();
	
	
	/**Changes the state of the Iterator to the next iteration
	 * */
	public void next();
	
	/** Whether or not there is another iteration after this
	 * @return true if there is another iteration after the current iteration, else false
	 * */
	public boolean hasNext();
}
