package nutrientService;

import database.CSVNutrientDB;

public class NutrientServiceFactory {
	private static INutrientService service;
	
	public static INutrientService getService() {
		if (service == null)
			service = new FilteredNutrientServiceProxy(new CSVNutrientDB());
		return service;
	}
}
