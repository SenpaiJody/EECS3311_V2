package database;

import java.util.ArrayList;
import java.util.List;

import ingredientService.IIngredientIterator;

//an iterator that goes through all Ingredient entries of the CSV Ingredient Database. Since we are reading CSV values.
//this iterator works by storing everything (uses more memory but prevents needing to read over and over). This behaviour is hidden from the user.
//left as package scope on purpose
//this could be an inner class of the CSVDatabase class tbh
class CSVIngredientIterator implements IIngredientIterator {

	int currentIteration = 0;
	List<Integer> ids = new ArrayList<Integer>();
	List<String> names = new ArrayList<String>();
	
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
