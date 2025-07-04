package database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;


import food.*;
import foodService.Filter;
import foodService.IFoodDB;
import ingredientService.IIngredientDB;
import ingredientService.IIngredientIterator;
import nutrientService.INutrientService;
import userService.IUserDB;
import userService.User;
import userService.Profile;
import userService.Profile.Gender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

/**
 * concrete implementation of a "database".
 * 
 * an absolute monster of a class, but it is just the implementation of the below interfaces;
 * shouldn't be an issue. It fulfils the contracts of the interfaces it implements, so it shouldnt matter that this class itself is kind of gargantuan 
 * 
 * */
public class CSVDatabase implements IFoodDB, IUserDB, IIngredientDB, INutrientService{

	private final String user_pwd_csv = "data/csv/user_pwd.csv";
	private final String user_profile_csv = "data/csv/user_profiles.csv";
	private final String profile_data_csv = "data/csv/profile_data.csv";
	private final String unique_profileID_csv = "data/csv/unique_profileID.csv";
	private final String food_name_csv = "data/csv/FOOD_NAME.csv";
	private final String unique_foodID_csv = "data/csv/unique_foodID.csv";
	private final String food_data_csv = "data/csv/food_data.csv";
	private final String profile_food_csv = "data/csv/profile_food.csv";
	private final String nutrient_name_csv = "data/csv/NUTRIENT_NAME.csv";
	private final String nutrient_amount_csv = "data/csv/NUTRIENT_AMOUNT.csv";
	
	private final Profile.Gender[] gender_lookup = {Gender.MALE,Gender.FEMALE,Gender.OTHER,Gender.UNSPECIFIED};

	
	public CSVDatabase() {
	}
	
	private PrintWriter createPrintWriter(String filename) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File(filename));
		}
		catch(FileNotFoundException e) {
			System.out.println(filename + " not found");
		}
		return pw;
	}
	
	//predicates are NOT the correct class here, but for now this will do. This is just an arbitrary implementation anyways
	private void readAndExecute(String file, Predicate<String> funcOnLine) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    boolean cont = true;
		    while (cont && (line = br.readLine()) != null) {
		        cont = funcOnLine.test(line);
		    }
		    br.close();
		}
		catch(IOException e) {
			throw new RuntimeException(file + " not found.");
		}
	}
	
	
	private StringBuilder copyContent(String file, Predicate<String> predicate){
		StringBuilder sb = new StringBuilder();		
		readAndExecute(file, (String line)->{
			sb.append(line);
			sb.append('\n');
			return true;
		});
		return sb;
		
	};
	
	private List<Profile> getProfiles(String userID){
		List<String> ids = new ArrayList<String>();
		readAndExecute(user_profile_csv, (String line)->{
			String[] elements = line.split(",");
			if (elements[0].equals(userID))
				ids.add(elements[1]);
			return true;
		});

		List<Profile> profiles = new ArrayList<Profile>();
		readAndExecute(profile_data_csv, (String line)->{
			String[] elements = line.split(",");
			for (int i = 0; i < ids.size(); i++) {
				if (elements[0].equals(ids.get(i))) {
					
					
					String[] dateString = elements[3].split("-");
					
					int profileID = Integer.parseInt(elements[0]);		
					Profile.Gender gender = gender_lookup[Integer.parseInt(elements[1])];
					String name = elements[2];
					double height = Double.parseDouble(elements[4]);
					double weight = Double.parseDouble(elements[5]);
					
					
					LocalDate dateOfBirth = LocalDate.of(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]), Integer.parseInt(dateString[2]));
					
 					profiles.add(new Profile(profileID, name, gender ,dateOfBirth,height,weight));
 					
 					ids.remove(i);
					break;
				}
			}
			return ids.size()>0;
		});
		
		return profiles;
	};
	
	@Override
	public boolean doesUserExist(String userID) {
		try (BufferedReader br = new BufferedReader(new FileReader(user_pwd_csv))) {
			String line;
		    while ((line = br.readLine()) != null) {
		        if (userID.equals(line.split(",")[0])) {
		        	br.close();
		        	return true;
		        }        
		    }
		    br.close();
		}
		catch(IOException e) {
			throw new RuntimeException(user_pwd_csv + " not found.");
		}
		return false;
	}

	@Override
	public User getUser(String userID, String password) {
		
		try (BufferedReader br = new BufferedReader(new FileReader(user_pwd_csv))) {
			String line;
		    while ((line = br.readLine()) != null) {
		    	String[] elements = line.split(",");
				if (elements[0].equals(userID) && elements[1].equals(password)) {
					return new User(userID, getProfiles(userID));
				}
		    }
		    br.close();
		}
		catch(IOException e) {
			throw new RuntimeException(user_pwd_csv + " not found.");
		}
		return null;
	}

	@Override
	public void updateUser(User u) {
		updateProfiles(u.getProfiles());
		StringBuilder sb = copyContent(user_profile_csv, (String line)->{
			String[] elements = line.split(",");
			return elements[0] != u.getUserID();
		});
		for (Profile p : u.getProfiles()) {
			sb.append(String.format("%s,%d",u.getUserID(),p.getID()));
		}
		
		PrintWriter pw = createPrintWriter(user_profile_csv);
		pw.print(sb.toString());
		pw.close();
		
		//TODO: update user settings too; ignored for this deliverable.
	}

	private String formatProfile(Profile p) {
		return String.format("%d,%d,%s,%s,%.2f,%.2f", 
				p.getID(), 
				p.getGender().ordinal(), 
				p.getName(),
				String.format("%d-%d-%d", p.getDateOfBirth().getYear(),p.getDateOfBirth().getMonthValue(), p.getDateOfBirth().getDayOfMonth()),
				p.getHeight(),
				p.getWeight()
				);
	}
	
	private void updateProfiles(List<Profile> profiles) {
		StringBuilder sb = new StringBuilder();		
		
		for (Profile p : profiles) {
			sb.append(formatProfile(p) + "\n");
		}
		
		readAndExecute(profile_data_csv, (String line)->{
			boolean foundMatch = false;
			for (int i = 0; i < profiles.size(); i++) {
				if (line.split(",")[0].equals(String.format("%d",profiles.get(i).getID()))) {
					foundMatch = true;
					profiles.remove(i);
					break;
				}
			}
			if (!foundMatch) {
				sb.append(line);
				sb.append('\n');
			}
			return true;
		});
		
		

		
		PrintWriter pw = createPrintWriter(profile_data_csv);
		pw.print(sb.toString());
		pw.close();
		
	}
	
	
	@Override
	public void registerUser(String userID, String password) {	
		StringBuilder sb = copyContent(user_pwd_csv, (String s)->true);
		PrintWriter pw = createPrintWriter(user_pwd_csv);
		sb.append(userID + "," + password);
		pw.print(sb.toString());
		pw.close();
	}

	@Override
	public int generateProfileID() {
		StringBuilder sb = copyContent(unique_profileID_csv, (String s)->true);
		PrintWriter pw = createPrintWriter(unique_profileID_csv);
		int generated = Integer.parseInt(sb.toString().strip())+1;
		pw.print(generated);
		pw.close();
		return generated;
	}


	private List<Food> getFood(int profileID, Filter filter, boolean isSnack){		
		List<String> profileIDs = new ArrayList<String>();
		readAndExecute(profile_food_csv, (String line)->{
			String[] elements = line.split(",");
			if (elements[0].equals(String.format("%d", profileID)))
				profileIDs.add(elements[1]);
			return true;
		});
		
		List<Food> foods = new ArrayList<Food>();
		readAndExecute(food_data_csv, (String line)->{
			String[] elements = line.split(",");
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
				default ->throw new RuntimeException();
			});
			for (int i=4; i < elements.length; i++) {
				String[] elems = elements[i].split(":");
				fb.addIngredient(Integer.parseInt(elems[0]), Double.parseDouble(elems[1]));
			}
			Food f = fb.getResult();
			
			boolean good = true;
			if (!isSnack && f.getType() instanceof Snack) 
				good = false;
			if (good && isSnack && !(f.getType() instanceof Snack))
				good = false;
			if (good && (filter == null || filter.test(f)))
				foods.add(f);
			return true;
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
		StringBuilder sb = copyContent(food_data_csv, (String s)->true);
		sb.append(String.format("%d,%s,%s,%s", 
				food.getID(),
				food.getName(),
				String.format("%d-%d-%d", food.getDate().getYear(),food.getDate().getMonthValue(), food.getDate().getDayOfMonth()),
				food.getType().getTypeName()
				));
		food.getIngredients().forEach((Integer ingredientID,Double quantity)->{
			sb.append(String.format(",%d:%.2f",ingredientID,quantity));
		});
		
		PrintWriter pw = createPrintWriter(food_data_csv);
		pw.print(sb.toString());
		pw.close();
		
		
		StringBuilder sb2 = copyContent(profile_food_csv, (String s)->true);
		sb2.append(String.format("%d,%d", profileID, food.getID()));
		pw = createPrintWriter(profile_food_csv);
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
		StringBuilder sb = copyContent(unique_foodID_csv, (String s)->true);
		PrintWriter pw = createPrintWriter(unique_foodID_csv);
		int generated = Integer.parseInt(sb.toString().strip())+1;
		pw.print(generated);
		pw.close();
		return generated;
	}
	
	@Override
	public String getIngredientName(int ingredientID) {
		StringBuilder sb = new StringBuilder();
		readAndExecute(food_name_csv, (String line)->{
			String[] elements = smartSplit(line);
			if (elements[0].equals(String.format("%d", ingredientID))) {
				sb.append(elements[4]);
				return false;
			}
			return true;
		});
		return sb.toString();
	}
	
	@Override
	public List<String> getIngredientNames(List<Integer> ids) {
		String[] names = new String[ids.size()];
		int left = ids.size();
		
		try (BufferedReader br = new BufferedReader(new FileReader(food_name_csv))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	String[] elements = smartSplit(line);
				for (int i =0; i<ids.size(); i++) {
					if (elements[0].equals(String.format("%d", ids.get(i)))) {
						names[i] = (elements[4]);
						left--;
						break;
					}
				}
				if (left ==0)
					break;
		    }
		    br.close();
		}
		catch(IOException e) {
			throw new RuntimeException(food_name_csv + " not found.");
		}
	

		return Arrays.asList(names);
	}
	

	
	//splits a line, taking into account the quotations ("") that cause a regular .split(",") to not work. Not yet rigorously tested.
	private String[] smartSplit(String s) {
		List<String> result = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		boolean inQuotes = false;
		boolean justReadQuote = false;
		
		for (char c : s.toCharArray()) {
			if (c == '"')
			{
				if (justReadQuote) {	
					inQuotes = true;
					justReadQuote = false;			
					sb.append('"');
				}
				else if (!inQuotes) {
					inQuotes = true;
					justReadQuote = true;
				}
				else {
					justReadQuote = true;
					inQuotes = false;
				}
			}
			else if (justReadQuote) {
				justReadQuote = false;
			}
			
			
			if (c == ',' && !inQuotes) {
				result.add(sb.toString());
				sb = new StringBuilder();
				
			}
			if ((c != ',' || inQuotes) && c !='"')
				sb.append(c);
		}
		String[] casted = new String[result.size()];
		return result.toArray(casted);
	}

	
	
	@Override
	public Map<Integer,Map<Integer, Double>> getNutrientsListPer100g(List<Integer> ingredientIDs) {
		
		Map<Integer,Map<Integer,Double>> retval = new HashMap<Integer,Map<Integer,Double>>(ingredientIDs.size());
		for (Integer i : ingredientIDs) {
			retval.put(i, new HashMap<Integer,Double>());
		}
		
		
		try (BufferedReader br = new BufferedReader(new FileReader(nutrient_amount_csv))) {
		    String line;
		    while ((line = br.readLine()) != null) {
				String[] elements = line.split(",");		
				int ingredient_id = Integer.parseInt(elements[0]);
				if (ingredientIDs.contains(ingredient_id)) {
					Integer nutrient_id = Integer.parseInt(elements[1]);
					Double nutrient_quantity = Double.parseDouble(elements[2]);
					if(nutrient_quantity > 0) {
						if (retval.get(ingredient_id).containsKey(nutrient_id))
							retval.get(ingredient_id).replace(nutrient_id, retval.get(ingredient_id).get(nutrient_id) + nutrient_quantity);
						else
							retval.get(ingredient_id).put(nutrient_id, nutrient_quantity);
					}
				}
		    }
		    br.close();
		}
		catch(IOException e) {
			throw new RuntimeException(nutrient_amount_csv + " not found.");
		}
		
		return retval;
	}

	
	@Override
	public Map<Integer, Double> getNutrientSumPer100g(List<Integer> ingredientIDs) {
		Map<Integer,Double> map = new HashMap<Integer,Double>();
		try (BufferedReader br = new BufferedReader(new FileReader(nutrient_amount_csv))) {
		    String line;
		    while ((line = br.readLine()) != null) {
				String[] elements = line.split(",");
				for (int i =0; i < ingredientIDs.size(); i++) {
					if (ingredientIDs.contains(Integer.parseInt(elements[0]))) {
						Integer id = Integer.parseInt(elements[1]);
						Double quantity = Double.parseDouble(elements[2]);
						if(quantity > 0) {
							if (map.containsKey(id))
								map.replace(id, map.get(id) + quantity);
							else
								map.put(id, quantity);
						}
					}
				}
				if (ingredientIDs.size() == 0)
					break;
		    }
		    br.close();
		}
		catch(IOException e) {
			throw new RuntimeException(nutrient_amount_csv + " not found.");
		}
		
		return map;
	}


	@Override
	public Map<Integer, Double> getNutrientsPer100g(int ingredientID) {
		Map<Integer,Double> map = new HashMap<Integer,Double>();
		try (BufferedReader br = new BufferedReader(new FileReader(nutrient_amount_csv))) {
		    String line;
		    while ((line = br.readLine()) != null) {
				String[] elements = line.split(",");
				if (elements[0].equals(String.format("%d", ingredientID))) {
					Integer n_id = Integer.parseInt(elements[1]);
					Double n_quantity = Double.parseDouble(elements[2]);
					if(n_quantity > 0) {
						if (map.containsKey(n_id))
							map.replace(n_id, map.get(n_id) + n_quantity);
						else
							map.put(n_id, n_quantity);
					}
				}
		    }
		    br.close();
		}
		catch(IOException e) {
			throw new RuntimeException(nutrient_amount_csv + " not found.");
		}
		
		return map;
	}
	
	@Override
	public String getNutrientName(int nutrientID) {
		StringBuilder sb = new StringBuilder();
		readAndExecute(nutrient_name_csv, (String line)->{
			String[] elements = smartSplit(line);
			if (elements.length<5)
				return true;
			
			if (Integer.parseInt(elements[0])==nutrientID) {
				sb.append(elements[4]);
				return false;
			}
			return true;
		});
		return sb.toString();
	}

	@Override
	public String getNutrientUnit(int nutrientID) {
		StringBuilder sb = new StringBuilder();
		readAndExecute(nutrient_name_csv, (String line)->{
			String[] elements = smartSplit(line);
			if (elements.length<5)
				return true;
			
			if (Integer.parseInt(elements[0])==nutrientID) {
				sb.append(elements[3]);
				return false;
			}
			return true;
		});
		return sb.toString();
	}

	@Override
	//creates an iterator
	public IIngredientIterator getIterator() {
		CSVIngredientIterator iterator = new CSVIngredientIterator();
		readAndExecute(food_name_csv, (String line)->{
			String[] elements = smartSplit(line);		
			iterator.addEntry(Integer.parseInt(elements[0]), elements[4]);
			return true;
		});
		return iterator;
	}

	@Override
	public List<Integer> getAllNutrientIDs() {
		List<Integer> ids = new ArrayList<Integer>();
		readAndExecute(nutrient_name_csv, (String line)->{
			String[] elements = smartSplit(line);		
			ids.add(Integer.parseInt(elements[0]));
			return true;
		});
		return ids;
	}


}
