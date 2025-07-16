package nutrientService;

import database.CSVNutrientDB;

/**A flyweight factory for creating and storing INutrientService objects
 * */
public class NutrientServiceFactory {
	private static INutrientService service;
	
	public static INutrientService getService() {
		if (service == null)
			service = new FilteredNutrientServiceProxy(new CSVNutrientDB());
		return service;
	}
}
