package database;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import food.Breakfast;
import food.Dinner;
import food.Food;
import food.FoodBuilder;
import food.IncompleteFoodException;
import food.Lunch;
import food.Snack;
import foodService.Filter;
import foodService.IFoodDB;

public class CSVFoodDB implements IFoodDB {


	private final String unique_foodID_csv = "data/csv/unique_foodID.csv";
	private final String food_data_csv = "data/csv/food_data.csv";
	private final String profile_food_csv = "data/csv/profile_food.csv";



	private List<Food> getFood(int profileID, Filter filter, boolean isSnack){		
		List<String> foodIDs = new ArrayList<String>();
		CSVDatabaseUtilities.readAndExecute(profile_food_csv, (String line)->{
			String[] elements = line.split(",");
			if (elements[0].equals(String.format("%d", profileID)))
				foodIDs.add(elements[1]);
			return true;
		});
		
		List<Food> foods = new ArrayList<Food>();
		CSVDatabaseUtilities.readAndExecute(food_data_csv, (String line)->{
			String[] elements = CSVDatabaseUtilities.smartSplit(line);
			
			if (!foodIDs.contains(elements[0])) {
				return true;
			}
			foodIDs.remove((Object) Integer.parseInt(elements[0]));
			
			FoodBuilder fb = new FoodBuilder();
			fb.setID(Integer.parseInt(elements[0]));
			fb.setName(elements[1]);
			String[] dateString = elements[2].split("-");
			fb.setDate(LocalDate.of(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]), Integer.parseInt(dateString[2])));
			fb.setFoodType(switch(elements[3]) {
			case "Breakfast" -> new Breakfast();
			case "Lunch" -> new Lunch();
			case "Dinner" -> new Dinner();
			case "Snack" -> new Snack();
				default ->throw new RuntimeException("Food failed to be built; likely due to a corrupted csv file");
			});
			for (int i=4; i < elements.length; i++) {
				String[] elems = elements[i].split(":");
				fb.addIngredient(Integer.parseInt(elems[0]), Double.parseDouble(elems[1]));
			}
			Food f;
			try {
				f = fb.getResult();
			} catch (IncompleteFoodException e) {
				throw new RuntimeException("Food failed to be built; likely due to a corrupted csv file");
			}
			
			boolean good = true;
			if (!isSnack && f.getType() instanceof Snack) 
				good = false;
			if (good && isSnack && !(f.getType() instanceof Snack))
				good = false;
			if (good && (filter == null || filter.test(f)))
				foods.add(f);
			
			
			return (foodIDs.size() > 0);
		});

		
		return foods;
	}
	
	@Override
	public List<Food> getMeals(int profileID, Filter filter) {
		return getFood(profileID, filter, false);
	}

	@Override
	public List<Food> getSnacks(int profileID, Filter filter) {	
		return getFood(profileID, filter, true);
	}

	
	private void saveFood(int profileID, Food food) {
		StringBuilder sb = CSVDatabaseUtilities.copyContent(food_data_csv, (String s)->true);
		sb.append(String.format("%d,%s,%s,%s", 
				food.getID(),
				food.getName(),
				String.format("%d-%d-%d", food.getDate().getYear(),food.getDate().getMonthValue(), food.getDate().getDayOfMonth()),
				food.getType().getTypeName()
				));
		food.getIngredients().forEach((Integer ingredientID,Double quantity)->{
			sb.append(String.format(",%d:%.2f",ingredientID,quantity));
		});
		
		PrintWriter pw = CSVDatabaseUtilities.createPrintWriter(food_data_csv);
		pw.print(sb.toString());
		pw.close();
		
		
		StringBuilder sb2 = CSVDatabaseUtilities.copyContent(profile_food_csv, (String s)->true);
		sb2.append(String.format("%d,%d", profileID, food.getID()));
		pw = CSVDatabaseUtilities.createPrintWriter(profile_food_csv);
		pw.print(sb2.toString());
		pw.close();	
	}
	
	@Override
	public void saveSnack(int profileID, Food food) {
		saveFood(profileID, food);	
	}

	@Override
	public void saveMeal(int profileID, Food food) {
		saveFood(profileID, food);
	}


	@Override
	public int generateFoodID() {
		StringBuilder sb = CSVDatabaseUtilities.copyContent(unique_foodID_csv, (String s)->true);
		PrintWriter pw = CSVDatabaseUtilities.createPrintWriter(unique_foodID_csv);
		int generated = Integer.parseInt(sb.toString().strip())+1;
		pw.print(generated);
		pw.close();
		return generated;
	}
		
}
