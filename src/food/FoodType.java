package food;

import foodService.InvalidFoodTypeException;

//represents the "type" of food a food object is. 
//Doing this via composition instead of inheritance allows us to separate the food state (which are the same for all foods and exist in the food class)
//and the food "behavior", which is determined by food type
public interface FoodType {
	//calls the appropriate save method for this food type.
	public void save(Food food) throws InvalidFoodTypeException;
	//returns a string representation of this food type
	public String getTypeName();
}
