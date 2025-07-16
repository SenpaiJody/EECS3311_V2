package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ingredientService.IIngredientDB;
import ingredientService.IIngredientIterator;

public class CSVIngredientDB implements IIngredientDB{
	
	private final String food_name_csv = "data/csv/FOOD_NAME.csv";
	private final String food_group_csv = "data/csv/FOOD_GROUP.csv";
	
	//implementation that returns a CSVIngredientIterator containing all of the ingredients
	public IIngredientIterator getIterator() {
		CSVIngredientIterator iterator = new CSVIngredientIterator();
		CSVDatabaseUtilities.readAndExecute(food_name_csv, (String line)->{
			String[] elements = CSVDatabaseUtilities.smartSplit(line);		
			iterator.addEntry(Integer.parseInt(elements[0]), elements[4]);
			return true;
		});
		return iterator;
	}
	
	@Override
	public String getIngredientName(int ingredientID) {
		StringBuilder sb = new StringBuilder();
		CSVDatabaseUtilities.readAndExecute(food_name_csv, (String line)->{
			String[] elements = CSVDatabaseUtilities.smartSplit(line);
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
		    	String[] elements = CSVDatabaseUtilities.smartSplit(line);
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

	@Override
	public int getFoodGroup(int ingredientID) {
		
		try (BufferedReader br = new BufferedReader(new FileReader(food_name_csv))) {
			String line;
		    while ((line = br.readLine()) != null) {
		    	String[] elements = CSVDatabaseUtilities.smartSplit(line);
		        if (ingredientID == Integer.parseInt(elements[0])) {
		        	br.close();
		        	return Integer.parseInt(elements[2]);
		        }        
		    }
		    br.close();
		}
		catch(IOException e) {
			throw new RuntimeException(food_name_csv + " not found.");
		}
		return -1;
	}

	@Override
	public String getFoodGroupName(int foodGroupID) {
		StringBuilder sb = new StringBuilder();
		CSVDatabaseUtilities.readAndExecute(food_group_csv, (String line)->{
			String[] elements = CSVDatabaseUtilities.smartSplit(line);
			if (elements[0].equals(String.format("%d", foodGroupID))) {
				sb.append(elements[2]);
				return false;
			}
			return true;
		});
		return sb.toString();
	}
}
