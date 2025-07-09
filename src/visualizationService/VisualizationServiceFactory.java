package visualizationService;

public class VisualizationServiceFactory {

private static VisualizationService obj;
	
	public static IVisualizationService getService() {
		if (obj == null)
			obj = new VisualizationService(nutrientService.NutrientServiceFactory.getService());
		return obj;
	}	
}
