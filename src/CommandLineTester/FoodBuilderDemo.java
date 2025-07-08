package CommandLineTester;
import java.time.LocalDate;
import java.util.Map;

import food.Breakfast;
import food.Food;
import food.FoodBuilder;
import food.IncompleteFoodException;
import ingredientService.IIngredientService;
import ingredientService.IngredientServiceFactory;

public class FoodBuilderDemo {
	//DEMO class showing the process of building a food object
	
	
	public static void main(String[] args) {
		FoodBuilder builder = new FoodBuilder();
		
		builder.setName("Strange Breakfast"); //food name
		builder.setDate(LocalDate.of(2025, 7, 8)); //food date of 2025, July 8th
		builder.addIngredient(18, 24.3); //24.3 grams of Blue Cheese
		builder.addIngredient(20, 10.4); //10.4 grams of brie cheese
		builder.addIngredient(1971, 100.d); //100 grams of Pork, shoulder, butt, blade (boneless), lean and fat, raw
		
		builder.setFoodType(new Breakfast());
		
		try {
			Food food = builder.getResult();
			System.out.println(String.format("ID:%s\nName: %s\nDate :%s\nIngredients:", food.getID(), food.getName(), food.getDate()));
			
			
			IIngredientService service = IngredientServiceFactory.getService();
			
			food.getIngredients().forEach((id, quantity)->{
				System.out.println(String.format("  %s (%.2f grams)", service.getIngredientName(id), quantity));
			});
			for (Map.Entry<Integer, Double> entry : food.getIngredients().entrySet()) {
				
			}
		} catch (IncompleteFoodException e) {
			System.out.println(e.getMessage());
		}
	}
}
