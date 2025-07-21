package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import nutrientService.INutrientIterator;

/** An Implementation of CSVNutrientIterator that is backed by a CSVDatabase.
 *  This class uses a BufferedReader and assumes that the CSV file entries are sorted by ingredientID
 * */
public class CSVNutrientIterator implements INutrientIterator {
	private final int READ_AHEAD_LIMIT = 150;
	private BufferedReader reader;
	private int ingredientID = 0;
	private Map<Integer, Double> nutrientMap;
	
	private String file;
	
	CSVNutrientIterator(String nutrient_amountCSVFile){
		this.file = nutrient_amountCSVFile;
		try{
			reader = new BufferedReader(new FileReader(file));
		}
		catch(IOException e) {
			throw new RuntimeException(file + " not found.");
		};
		
		if(hasNext())
			next();
	}

	@Override
	public int getIngredientID() {
		return ingredientID;
	}

	@Override
	public Map<Integer, Double> getNutrientMap() {
		return nutrientMap;
	}

	@Override
	public void next() {
		String line;
		Map<Integer, Double> nutrientMap = new HashMap<Integer,Double>();
		Integer ingredientID = null;
		try {
		    while (true) {
				reader.mark(READ_AHEAD_LIMIT); //set a mark here, in case the entry is finished.
		    	line = reader.readLine();
		    	if (line == null) {
		    		reader.reset();
		    		break;
		    	}
		 
		    	
		    	String[] elements = line.split(",");		
				if (ingredientID == null) {
					ingredientID = Integer.parseInt(elements[0]);
				}
				else if (ingredientID != Integer.parseInt(elements[0])) {
					reader.reset(); //reset the reader
					break;
				}
				int nutrient_id = Integer.parseInt(elements[1]);
				double nutrient_amt = Double.parseDouble(elements[2]);
				nutrientMap.put(nutrient_id,nutrient_amt);
		    }
		    
		}catch (IOException e) {
			throw new RuntimeException("An IO Exception occured while iterating through " + file);
		}
		this.ingredientID = ingredientID;
		this.nutrientMap = nutrientMap;
	}

	@Override
	public boolean hasNext() {
		try {
			reader.mark(READ_AHEAD_LIMIT); //set a mark here, so that data is not skipped by checking (BufferedReader has no peek method)
			String line = reader.readLine();
	    	reader.reset(); //reset to before reading
	    	return line != null;
		    
		} catch (IOException e) {
			throw new RuntimeException("An IO Exception occured while iterating through " + file);
		}
	}

}
