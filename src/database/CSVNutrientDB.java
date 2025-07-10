package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nutrientService.INutrientService;



public class CSVNutrientDB implements INutrientService {
	
	private final String nutrient_name_csv = "data/csv/NUTRIENT_NAME.csv";
	private final String nutrient_amount_csv = "data/csv/NUTRIENT_AMOUNT.csv";
	
	
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
		CSVDatabaseUtilities.readAndExecute(nutrient_name_csv, (String line)->{
			String[] elements = CSVDatabaseUtilities.smartSplit(line);
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
		CSVDatabaseUtilities.readAndExecute(nutrient_name_csv, (String line)->{
			String[] elements = CSVDatabaseUtilities.smartSplit(line);
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
	public List<Integer> getAllNutrientIDs() {
		List<Integer> ids = new ArrayList<Integer>();
		CSVDatabaseUtilities.readAndExecute(nutrient_name_csv, (String line)->{
			String[] elements = CSVDatabaseUtilities.smartSplit(line);		
			ids.add(Integer.parseInt(elements[0]));
			return true;
		});
		return ids;
	}

}
