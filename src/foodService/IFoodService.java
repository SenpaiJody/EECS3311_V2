package foodService;

import java.time.LocalDate;
import java.util.List;
import food.FoodType;
import food.Food;

public interface IFoodService {
	public List<Food> getMeals(Filter filter);
	public List<Food> getSnacks(Filter filter);
	public void saveSnack(Food food);
	public void saveMeal(Food food) throws InvalidFoodTypeException;
	public List<FoodType> getValidFoodTypes(LocalDate date);
	public int generateFoodID();
}
