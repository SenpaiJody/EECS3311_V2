package ingredientService;

public interface IIngredientIterator {
	//gets the name of the ingredient associated with the current iteration
	public String getName();
	//gets the id of the ignredient associated with the current iteration
	public int getID();
	//changes the state of the object to the next ingredient iteration
	public void next();
	//whether or not there is a next object in the iteration;
	public boolean hasNext();
}
