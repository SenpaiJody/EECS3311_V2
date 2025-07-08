package foodService;

import database.CSVFoodDB;

//sort of flyweight pattern (also may count as the factory patterN0
public class FoodServiceFactory {
	private static FoodService obj;
	
	public static IFoodService getService() {
		if (obj == null)
			obj = new FoodService(new CSVFoodDB()); //defaulting to using an CSVDatabase temporarily as the implementation.
		return obj;
	}
	
	
	
}
