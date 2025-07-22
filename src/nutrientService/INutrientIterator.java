package nutrientService;

import java.util.Map;

public interface INutrientIterator {
	
	/**Gets the ingredientID associated with these nutrients
	 * @return ingredient ID*/
	public int getIngredientID();
	
	/**Gets the nutrient Map of this iteration
	 * @return nutrient Map of NutrientID's to Amounts*/
	public Map<Integer, Double> getNutrientMap();
	
	
	/**Changes the state of the Iterator to the next iteration
	 * */
	public void next();
	
	/** Whether or not there is another iteration after this
	 * @return true if there is another iteration after the current iteration, else false
	 * */
	public boolean hasNext();
}
