package foodService;

import database.CSVFoodDB;

/**A flyweight factory for creating objects that implement the IFoodService interface
 * 
 * @see {@link IFoodService}
 * */
public class FoodServiceFactory {
	private static IFoodService obj;
	
	/** gets an IFoodService implementation
	 * 
	 * @return an IFoodService implementation; multiple calls are guaranteed to return the same object.
	 * */
	public static IFoodService getService() {
		if (obj == null)
			obj = new FoodService(new CSVFoodDB()); //defaulting to using an CSVDatabase temporarily as the implementation.
		return obj;
	}
	
	
	
}
