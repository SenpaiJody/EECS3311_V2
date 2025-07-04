package food;

import foodService.FoodServiceFactory;

//Snacks are a type of food separate from meals
public class Snack implements FoodType{

	@Override
	public void save(Food food) {
		FoodServiceFactory.getService().saveSnack(food);
	}

	@Override
	public String getTypeName() {
		return "Snack";
	}
	

}
