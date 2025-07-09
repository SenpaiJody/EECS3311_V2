package nutritionRequests;

public class NutritionRequestServiceFactory {

	private static NutritionRequestService obj;
	
	public static INutritionRequestService getService() {
		
		if (obj == null)
			obj = new  NutritionRequestService();
		
		return obj;
	}
	
}
