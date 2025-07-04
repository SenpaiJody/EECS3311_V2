package food;

import foodService.FoodServiceFactory;
import foodService.InvalidFoodTypeException;


//The abstract Meal class serves as distinction between "Snacks" and the other food types. The other food types (breakfast, lunch, dinner, etc...) will inherit from this.
public abstract class Meal implements FoodType {
	
	//Since there is not much difference between different types of meals, the saving functionality is shared. Of course, this can be overridden.
	public void save(Food food) throws InvalidFoodTypeException {
		FoodServiceFactory.getService().saveMeal(food);
		
	}
}
