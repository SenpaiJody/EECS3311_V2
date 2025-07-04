package ingredientService;

import java.util.List;

public interface IIngredientDB {

	//when given an ingredient ID, returns the name of that ingredient.
	public String getIngredientName(int id);
	
	//when given a list of ingredient IDs, returns a list of names of those ids
	public List<String> getIngredientNames(List<Integer> ids);
	
	//gets an iterator through ingredient objects
	public IIngredientIterator getIterator();
	
};
