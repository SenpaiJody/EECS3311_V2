package foodService;

import java.util.ArrayList;
import java.time.LocalDate;

import food.Food;
import food.FoodType;

/** A class describes a food search filter.
 * 
 * @see {@link IFoodService#getMeals(Filter)}
 * @see {@link IFoodService#getSnacks(Filter)}
 * */
public class Filter{
	
	//start date
	private LocalDate dateStart;
	//end date
	private LocalDate dateEnd;
	private ArrayList<Integer> includedIngredients = new ArrayList<Integer>();
	private ArrayList<Integer> excludedIngredients = new ArrayList<Integer>();
	private ArrayList<FoodType> excludedTypes = new ArrayList<FoodType>();
	
	/**Sets the date range of this filter. Only foods with Dates in this range (inclusive) will pass the test.
	 * 
	 * @param start - starting date (inclusive)
	 * @param end	- ending date (inclusive)
	 * 
	 * @see {@link #test(Food)}
	 * */
	public void setDateRange(LocalDate start, LocalDate end) {
		dateStart = start;
		dateEnd = end;
	};
	
	
	/** Gets the starting date of this filter (inclusive)
	 * 
	 * @return filter starting date
	 * */
	public LocalDate getStartDate() {return dateStart;}
	
	/** Gets the ending date of this filter (inclusive)
	 * 
	 * @return filter ending date
	 * */
	public LocalDate getEndDate() {return dateEnd;}
	
	
	/** Adds an ingredient to the filter's list of included ingredients. Only foods that include ALL of the included ingredients will pass the test
	 * <br> If the provided id already exists in the list of included ingredients, this function does nothing.
	 * @param id - Ingredient ID to include
	 * */
	public void addToIncludedIngredients(int id) {
		if (!includedIngredients.contains(id))
			includedIngredients.add(id);
	}
	
	
	/** Removes an ingredient from the list of included ingredients, if it exists.
	 * @param id - the Ingredient ID to remove
	 * */
	public void removeFromIncludedIngredients(int id) {
		includedIngredients.remove(id);
	}
	
	
	/** Adds an ingredient to the filter's list of excluded ingredients. Only foods that have NONE of these excluded ingredients will pass the test
	 * <br> If the provided id already exists in the list of excluded ingredients, this function does nothing.
	 * @param id - Ingredient ID to exclude
	 * */
	public void addToExcludedIngredients(int id) {
		excludedIngredients.add(id);
	}
	/** Removes an ingredient from the list of excluded ingredients, if it exists.
	 * @param id - the Ingredient ID to remove
	 * */
	public void removeFromExcludedIngredients(int id) {
		excludedIngredients.remove(id);
	}
	
	
	/** adds a FoodType to the list of excluded FoodTypes. Only foods that are NOT this type will pass the test.
	 * @param FoodType - the foodType to add.
	 * 
	 * @see {@link FoodType}
	 * */
	public void addToExcludedTypes(FoodType type) {
		excludedTypes.add(type);
	}
	
	/**Tests a food object to see if it passes this filter.
	 * <p> Only foods that include ALL of the ingredients in the Included Ingredients List will pass
	 * <br> Only foods that include NONE of the ingredients in the Excluded Ingredients List will pass
	 * <br> Only foods with FoodTypes that are NOT INSTANCES of the FoodTypes in the Excluded Ingredients List will pass
	 * 
	 * @param food - The Food object to test.
	 * @see {@link FoodType}
	 * */
	public boolean test(Food food) {
		boolean good = true;
		if (dateStart != null && food.getDate().isBefore(dateStart))	//if there is dateStart set and the food is dated strictly before the dateStart, false
			return false;
		if (dateEnd != null && food.getDate().isAfter(dateEnd))	//if there is a dateEnd set and the food is dated strictly before the dateStart, false
			return false;
		
		for (FoodType ft : excludedTypes) {							//if the food type matches any excluded food type, false
			if (ft.getClass().equals(food.getType().getClass()))
				return false;
		}	
		
		if (!food.getIngredients().keySet().containsAll(includedIngredients)) //if it does not contain ALL included ingredients, false
			return false;
		
		for (int ingredientID : excludedIngredients) {						// if it contains ANY excluded ingredients, false;
			if (food.getIngredients().keySet().contains(ingredientID))
				return false;
		}
		
		return good;
	}
	 
}
