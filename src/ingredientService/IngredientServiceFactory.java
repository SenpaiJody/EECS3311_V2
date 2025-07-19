package ingredientService;


import database.CSVIngredientDB;

/**A flyweight factory for creating and storing objects that implement the IIngredientService interface
 * 
 * @see {@link IIngredientService}
 * */
public class IngredientServiceFactory {
	private static IIngredientService service;
	
	/**Returns an ingredient service. Multiple calls to this is guaranteed to return the same object.
	 * 
	 * @return an IIngredientService implementation
	 * */
	public static IIngredientService getService() {
		if (service == null)
			service = new IngredientService(new CSVIngredientDB());
		return service;
	}
}
