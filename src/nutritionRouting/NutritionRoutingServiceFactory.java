package nutritionRouting;

public class NutritionRoutingServiceFactory {

private static NutritionRoutingService obj;
	

	public static INutritionRoutingService getService() {
		if (obj == null)
			obj = new NutritionRoutingService(foodService.FoodServiceFactory.getService(), graphService.GraphServiceFactory.getService());
		
		return obj;
	}
	
}
