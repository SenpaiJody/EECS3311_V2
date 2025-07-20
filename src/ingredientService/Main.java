package ingredientService;

import java.util.List;
import ingredientService.IIngredientService;
import ingredientService.IngredientServiceFactory;

public class Main {
    public static void main(String[] args) {
    	IIngredientService ingredientService = IngredientServiceFactory.getService();// Replace with your actual DB implementation
        
        List<Integer> ids = ingredientService.searchIngredientByName("Chicken, broiler, meat only, roasted", 1);
        System.out.println("ID: " + ids.get(0));
        
        
        List<String> id1 = ingredientService.getIngredientNames(List.of(733));
        System.out.println("string: " + id1.get(0));
    }
}