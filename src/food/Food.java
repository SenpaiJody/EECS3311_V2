package food;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import foodService.InvalidFoodTypeException;

/** The main data structure that defines a "Food"
 * <p> A food object is defined by its name, id, ingredients, date and type
 * <p> Note that the specific food type (breakfast, lunch, dinner, snack) is NOT a class derived from Food. Rather, it is composed into this class via the {@link FoodType} class
 * */
public class Food {
	private String name;
	private int id;
	private Map<Integer, Double> ingredients = new HashMap<Integer, Double>();
	private LocalDate date;
	private FoodType foodType;
	
	//getters
	public String getName() {return name;}
	public int getID() {return id;}
	public Map<Integer, Double> getIngredients(){return ingredients;}
	public LocalDate getDate() {return date;}
	public FoodType getType() {return foodType;}
	
	
	public Food(int id, String name, Map<Integer, Double> ingredients, LocalDate date, FoodType type){
		this.id = id;
		this.name = name;
		this.ingredients = ingredients;
		this.date = date;
		this.foodType = type;
	}
	
	/**Adds an ingredient to the food.
	 * */
	public void addIngredient(int id, Double quantity) {
		ingredients.put(id, quantity);
	}
	/**Removes an ingredient from the food
	 * */
	public void removeIngredient(int id) {
		ingredients.remove(id);
	}
	public void setName(String n) {
		name = n;
	}
	public void setDate(LocalDate d) {
		date = d;
	}
	public void setFoodType(FoodType ft) {
		foodType = ft;
	}
	
	/**Saves this food based on the implementation of its FoodType
	 * @throws InvalidFoodTypeException if the food cannot be saved with this foodtype
	 * */
	public void save() throws InvalidFoodTypeException {
		foodType.save(this);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		ingredients.forEach((Integer ing, Double q)->{
			sb.append(String.format("\n  %d : %.2f", ing, q));
		});
		
		return String.format("id: %d\n  name: %s\n  ingredients: %s\n  date: %s\n  type: %s", getID(), getName(), sb.toString(), date.toString(), foodType.getTypeName() );
	}
	
}
