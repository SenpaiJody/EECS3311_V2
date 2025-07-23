package foodService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import food.*;
import userService.UserServiceFactory;

/**An implementation of the IFoodService interface that uses an IFoodDB object to provide services.
 * 
 * */
class FoodService implements IFoodService{
	
	private IFoodDB db;
	FoodService(IFoodDB implementation){
		this.db = implementation;
	}
	
	/**{@inheritDoc} Only returns meals that belong to the current selected profile
	 * */
	@Override
	public List<Food> getMeals(Filter filter) {
		return db.getMeals(UserServiceFactory.getService().getCurrentProfile().getID(), filter);
	}

	/**{@inheritDoc} Only returns snacks that belong to the current selected profile
	 * */
	@Override
	public List<Food> getSnacks(Filter filter) {
		return db.getSnacks(UserServiceFactory.getService().getCurrentProfile().getID(), filter);
	}
	
	/**{@inheritDoc} Saves to the Current Selected profile.
	 * */
	@Override
	public void saveSnack(Food food) throws InvalidFoodTypeException {
		if (!(food.getType() instanceof Snack))
			throw new InvalidFoodTypeException();
		db.saveSnack(UserServiceFactory.getService().getCurrentProfile().getID(), food);
	}

	/**{@inheritDoc} Saves to the Current Selected profile.
	 * */
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
		if (!found || !(food.getType() instanceof Meal)) {
			throw new InvalidFoodTypeException();
		}
		
		db.saveMeal(UserServiceFactory.getService().getCurrentProfile().getID(), food);
	}
	
	/**{@inheritDoc}*/
	@Override
	public int generateFoodID() {
		return db.generateFoodID();
	}

	/**{@inheritDoc}*/
	@Override
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
