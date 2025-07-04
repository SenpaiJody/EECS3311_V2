package food;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import foodService.InvalidFoodTypeException;

public class Food {
	private String name;
	private int id;
	private Map<Integer, Double> ingredients = new HashMap<Integer, Double>();
	private LocalDate date;
	private FoodType foodType;
	
	
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
	
	public void addIngredient(int id, Double quantity) {
		ingredients.put(id, quantity);
	}
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
	
	public void save() throws InvalidFoodTypeException { //does delegating break SRP? the actual save functionality is on the foodType class, but this just delegates
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
