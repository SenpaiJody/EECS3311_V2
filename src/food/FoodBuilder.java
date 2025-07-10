package food;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import foodService.FoodServiceFactory;

//a builder class for Foods. this class exists to ensure that when a food object is created, it is already "completed" with all the necessary information.
//This can be further ensured with an exception thrown when trying to use getResult() before all necessary fields are filled, but that will be left for later.
public class FoodBuilder {
	private String name;

	private Map<Integer, Double> ingredients = new HashMap<Integer, Double>();
	private LocalDate date;
	private FoodType foodType;
	private Integer id = null;
	
	public String getName() {return name;}
	public Map<Integer, Double> getIngredients(){return ingredients;}
	public LocalDate getDate() {return date;}
	public FoodType getType() {return foodType;}
	
	//add an ingredient to the food to be built
	public void addIngredient(int id, Double quantity) {
		ingredients.put(id, quantity);
	}
	//remove an ingredient
	public void removeIngredient(int id) {
		ingredients.remove(id);
	}
	//set the foods name
	public void setName(String n) {
		name = n;
	}
	//set the food's date
	public void setDate(LocalDate d) {
		date = d;
	}
	//set the food's type
	public void setFoodType(FoodType ft) {
		foodType = ft;
	}
	//set the food's id. this should not be done unless you can guarantee the food ID to be unique.
	public void setID(int id) {
		this.id = id;
	}
	
	//get the created food; throws an exception if there is not enough data to build the food
	public Food getResult() throws IncompleteFoodException {
		if (ingredients.size() == 0)
			throw new IncompleteFoodException("A Food cannot be created with zero ingredients");
		if (foodType == null)
			throw new IncompleteFoodException("A Food type must be specified");
		if (date == null)
			throw new IncompleteFoodException("The Food's Date must be specified");
		if (name == null || name.length() == 0)
			throw new IncompleteFoodException("The Food must have a name!");
		if (id == null)
			id = FoodServiceFactory.getService().generateFoodID();
		return new Food(id, name, ingredients, date, foodType);
	}
}
