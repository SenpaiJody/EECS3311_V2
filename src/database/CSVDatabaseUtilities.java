package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;

//TODO: consider moving this to a library for Deliverable 3.
/**A "Utility" class providing frequently used functions
 * */
class CSVDatabaseUtilities {
	
	/**Creates a new PrintWriter object that operates on the given filename
	 * @param filename that the PrintWriter will be created with
	 * @return the created PrintWriter object
	 * @throws RuntimeException if the file does not exist.
	 * */
	static PrintWriter createPrintWriter(String filename) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File(filename));
		}
		catch(FileNotFoundException e) {
			throw new RuntimeException(filename + " not found");
		}
		return pw;
	}
	
	
	/** A function that reads every line of the provided file and executes the predicate function on that line
	 * <p> If the predicate returns True, then the next line in the CSV will be read.
	 * <br>If the predicate returns False, then no more lines in the CSV will be read.
	 * 
	 * @param file - the file to read
	 * @param funcOnLine - the predicate with the function to execute each line
	 * */
	static void readAndExecute(String file, Predicate<String> funcOnLine) {
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
	
	
	
	/**An alternative to String.split(",") that smartly handles non-delimiting commas (commas within "") 
	 * @param String - string to split
	 * @return The string split by delimiting commas
	 * */
	static String[] smartSplit(String s) {
			CSVParser parser = new CSVParser();
			String[] result = null;
			try {
				result = parser.parseLine(s);
			} catch (IOException e) {
				throw new IllegalArgumentException("input string is not a comma-separated string");
			}
			return result;
		}
	
	/** Returns a StringBuilder that contains each line of the provided file, iff that line passes the provided predicate
	 * @param file - the file to copy
	 * @param predicate - the predicate to test each line on
	 * 
	 * @returns a StringBuilder that contains each line of the provided file iff that line passes the provided predicate
	 * */
	static StringBuilder copyContent(String file, Predicate<String> predicate){
		StringBuilder sb = new StringBuilder();		
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;

		    while ((line = br.readLine()) != null) {
		        if (predicate.test(line)){
					sb.append(line);
					sb.append('\n');
		        }
		    }
		    br.close();
		}
		catch(IOException e) {
			throw new RuntimeException(file + " not found.");
		}
		
		
	
		return sb;
		
	};
}
