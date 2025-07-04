package nutrientService;

import database.CSVDatabase;

public class NutrientServiceFactory {
	private static INutrientService service;
	
	public static INutrientService getService() {
		if (service == null)
			service = new FilteredNutrientServiceProxy(new CSVDatabase());
		return service;
	}
}
