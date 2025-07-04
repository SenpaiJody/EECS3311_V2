package database;

import java.util.List;

import food.Food;
import foodService.Filter;
import foodService.IFoodDB;
import userService.IUserDB;
import userService.User;

public class SQLDatabase implements IFoodDB, IUserDB {

	@Override
	public boolean doesUserExist(String userID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public User getUser(String UserID, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateUser(User u) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerUser(String userID, String password) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Food> getMeals(int profileID, Filter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Food> getSnacks(int profileID, Filter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveSnack(int profileID, Food food) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveMeal(int profileID, Food food) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int generateProfileID() {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public int generateFoodID() {
		// TODO Auto-generated method stub
		return 0;
	}

}
