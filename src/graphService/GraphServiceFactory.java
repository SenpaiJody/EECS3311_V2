package graphService;

public class GraphServiceFactory {

	private static GraphService instance;

	    public static IGraphService getService() {
	        if (instance == null) {
	            instance = new GraphService();
	        }
	        return instance;
	    }
	}