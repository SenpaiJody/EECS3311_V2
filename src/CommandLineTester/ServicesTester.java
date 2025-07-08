package CommandLineTester;

import ingredientService.IIngredientService;
import ingredientService.IngredientServiceFactory;
import nutrientService.INutrientService;
import nutrientService.NutrientServiceFactory;

public class ServicesTester {

	public static void main (String[] args) {
		IIngredientService ingService = IngredientServiceFactory.getService();
		
		System.out.println(ingService.getFoodGroupName(ingService.getFoodGroup(3000)));
		
		INutrientService nutService = NutrientServiceFactory.getService();
		
		System.out.println(nutService.getAllNutrientIDs());
		
		
		
	}	
}
