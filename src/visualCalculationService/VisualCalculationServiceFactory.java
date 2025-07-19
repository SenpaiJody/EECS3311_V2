package visualCalculationService;

public class VisualCalculationServiceFactory {

private static VisualCalculationService obj;

	public static IVisualCalculationService getService() {
		if (obj == null) {
			obj = new VisualCalculationService(nutrientService.NutrientServiceFactory.getService(),ingredientService.IngredientServiceFactory.getService());
		}

		return obj;
	}
}
