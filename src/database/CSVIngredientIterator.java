package database;

import java.util.ArrayList;
import java.util.List;

import ingredientService.IIngredientIterator;


/**An iterator that goes through all Ingredient entries of the CSV Ingredient Database. Since we are reading CSV values.
 *<p> This iterator works by storing everything (uses more memory but prevents needing to read the file over and over). */
class CSVIngredientIterator implements IIngredientIterator {

	int currentIteration = 0;
	List<Integer> ids = new ArrayList<Integer>();
	List<String> names = new ArrayList<String>();
	
	/**adds an iteration to the iterator; typically used during the population of this iterator
	 * @param id - Ingredient ID
	 * @param name - Ingredient Name
	 * */
	public void addEntry(int id, String name) {
		ids.add(id);
		names.add(name);
	};
	
	@Override
	public String getName() {
		return names.get(currentIteration);
	}

	@Override
	public int getID() {
		return ids.get(currentIteration);
	}

	@Override
	public void next() {
		currentIteration++;
	}

	@Override
	public boolean hasNext() {
		return currentIteration < ids.size()-1;
	}

}
