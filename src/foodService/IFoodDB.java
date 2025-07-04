package foodService;

import java.util.List;

import food.Food;

public interface IFoodDB {
	public List<Food> getMeals(int profileID, Filter filter);
	public List<Food> getSnacks(int profileID, Filter filter);
	public void saveSnack(int profileID, Food food);
	public void saveMeal(int profileID, Food food);
	public int generateFoodID();
}
