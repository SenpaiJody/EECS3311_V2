package food;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import foodService.FoodServiceFactory;

/**a builder class for Foods. this class exists to ensure that when a food object is created, it is already "completed" with all the necessary information.*
**/
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

	/**add an ingredient to the food to be built
	 * @param id - ingredient ID
	 * @param quantity - ingredient quantity
	 * */
	public void addIngredient(int id, Double quantity) {
		ingredients.put(id, quantity);
	}
	/**removes an ingredient
	 * @param id - ingredient ID
	 * */
	public void removeIngredient(int id) {
		ingredients.remove(id);
	}
	
	/**sets the food's name
	 * @param name - food name
	 * */
	public void setName(String name) {
		this.name = name;
	}
	/**sets the food's date
	 * @param date - food date
	 * */
	public void setDate(LocalDate date) {
		this.date = date;
	}
	/**sets the food's type
	 * @param type - foodtype
	 * */
	public void setFoodType(FoodType type) {
		foodType = type;
	}

	/**set the food's id. this should not be done unless you can guarantee the food ID to be unique or will not save the food
	 * @param id - food id
	 * */
	public void setID(int id) {
		this.id = id;
	}
	
	//
	/**get the created food; throws an exception if there is not enough data to build the food
	 * @throws IncompleteFoodException if there is not enough data to build the food
	 * */
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
