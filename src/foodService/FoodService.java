package foodService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import food.*;
import userService.UserServiceFactory;

class FoodService implements IFoodService{
	
	IFoodDB db;
	FoodService(IFoodDB implementation){
		this.db = implementation;
	}
	
	@Override
	public List<Food> getMeals(Filter filter) {
		return db.getMeals(UserServiceFactory.getService().getCurrentProfile().getID(), filter);
	}

	@Override
	public List<Food> getSnacks(Filter filter) {
		return db.getSnacks(UserServiceFactory.getService().getCurrentProfile().getID(), filter);
	}
	
	@Override
	public void saveSnack(Food food) {
		db.saveSnack(UserServiceFactory.getService().getCurrentProfile().getID(), food);
	}

	@Override
	public void saveMeal(Food food) throws InvalidFoodTypeException {
		List<FoodType> validTypes = getValidFoodTypes(food.getDate());
		boolean found = false;
		for (FoodType ft : validTypes) {
			if (ft.getClass().equals(food.getType().getClass())){
				found = true;
				break;
			}
		}
		if (!found) {
			throw new InvalidFoodTypeException();
		}
		
		db.saveMeal(UserServiceFactory.getService().getCurrentProfile().getID(), food);
	}
	@Override
	public int generateFoodID() {
		return db.generateFoodID();
	}

	@Override
	/**
	 * searches the database and returns the foodTypes that can be saved to that date; 
	 * (if a breakfast exists on that day, the returned value will not include the Breakfast FoodType)
	 * Since Snacks are always allowed, the Snack FoodType will always be included in the result*
	 * */	
	public List<FoodType> getValidFoodTypes(LocalDate date) {
		Filter f = new Filter();
		f.setDateRange(date, date);
		List<Food> meals = getMeals(f);
		ArrayList<FoodType> types = new ArrayList<FoodType>();
		
		
		types.add(new Breakfast());
		types.add(new Lunch());
		types.add(new Dinner());	
		for (Food food : meals) {
			types.removeIf(ft -> ft.getClass()==food.getType().getClass());
		}
		types.add(new Snack());
		return types;
	} 

}
