package ingredientService;

import database.CSVDatabase;

//Factory for instantiating IIngredientService objects. A flyweight factory.
public class IngredientServiceFactory {
	private static IIngredientService service;
	
	public static IIngredientService getService() {
		if (service == null)
			service = new IngredientService(new CSVDatabase());
		return service;
	}
}
