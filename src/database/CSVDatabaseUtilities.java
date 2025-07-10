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

class CSVDatabaseUtilities {
	public static PrintWriter createPrintWriter(String filename) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File(filename));
		}
		catch(FileNotFoundException e) {
			System.out.println(filename + " not found");
		}
		return pw;
	}
	
	//funcOnLine is a function executed for each line of CSV, if this function returns false, the execution will end there.
	public static void readAndExecute(String file, Predicate<String> funcOnLine) {
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
	
	//splits a line, taking into account the quotations ("") that cause a regular .split(",") to not work. Not yet rigorously tested.
	public static String[] smartSplit(String s) {
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
	
	public static StringBuilder copyContent(String file, Predicate<String> predicate){
		StringBuilder sb = new StringBuilder();		
		readAndExecute(file, (String line)->{
			sb.append(line);
			sb.append('\n');
			return true;
		});
		return sb;
		
	};
}
