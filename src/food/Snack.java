package food;

import foodService.FoodServiceFactory;
import foodService.InvalidFoodTypeException;

//Snacks are a type of food separate from meals
public class Snack implements FoodType{

	@Override
	public void save(Food food) throws InvalidFoodTypeException {
		FoodServiceFactory.getService().saveSnack(food);
	}

	@Override
	public String getTypeName() {
		return "Snack";
	}
	

}
