package ingredientService;


import database.CSVIngredientDB;

//Factory for instantiating IIngredientService objects. A flyweight factory.
public class IngredientServiceFactory {
	private static IIngredientService service;
	
	public static IIngredientService getService() {
		if (service == null)
			service = new IngredientService(new CSVIngredientDB());
		return service;
	}
}
