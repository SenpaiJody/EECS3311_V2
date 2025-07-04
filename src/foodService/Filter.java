package foodService;

import java.util.ArrayList;
import java.time.LocalDate;

import food.Food;
import food.FoodType;

//a "Filter" that describes a search term by which food objects can be filtered out.
public class Filter{
	private LocalDate dateStart;
	private LocalDate dateEnd;
	private ArrayList<Integer> includedIngredients = new ArrayList<Integer>();
	private ArrayList<Integer> excludedIngredients = new ArrayList<Integer>();
	private ArrayList<FoodType> excludedTypes = new ArrayList<FoodType>();
	
	public void setDateRange(LocalDate start, LocalDate end) {
		dateStart = start;
		dateEnd = end;
	};
	
	
	public LocalDate getStartDate() {return dateStart;}
	public LocalDate getEndDate() {return dateEnd;}
	
	public void addToIncludedIngredients(int id) {
		includedIngredients.add(id);
	}
	public void removeFromIncludedIngredients(int id) {
		includedIngredients.remove(id);
	}
	public void addToExcludedIngredients(int id) {
		excludedIngredients.add(id);
	}
	public void removeFromExcludedIngredients(int id) {
		excludedIngredients.remove(id);
	}
	
	public void addToExcludedTypes(FoodType type) {
		excludedTypes.add(type);
	}
	
	//tests a food object to see if it passes the filter
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
