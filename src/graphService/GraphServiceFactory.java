package graphService;


public class GraphServiceFactory {

	private static GraphService obj;
	
	public static IGraphService getService() {
		
		if (obj == null) 
			obj = new GraphService(visualCalculationService.VisualCalculationServiceFactory.getService(), visualizationService.VisualizationServiceFactory.getService());
		
		return obj;
	}
	
}
