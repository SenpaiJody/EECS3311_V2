package cfgNutrientRecService;

public class CFGNutrientRecServiceFactory {

    private static CFGNutrientRecService instance;

    public static ICFGNutrientRecService getService() {
        if (instance == null) {
            instance = new CFGNutrientRecService();
        }
        return instance;
    }
}
