package visualization;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import food.Food;
import database.CSVDatabase;

import org.jfree.chart.JFreeChart;

public abstract class Graph {
    
    protected final database.CSVDatabase database;
	protected LocalDate dateStart;
	protected LocalDate dateEnd;
    

    public Graph(CSVDatabase database, LocalDate dateStart, LocalDate dateEnd) {
        this.database = database;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }
	
    public JFreeChart createGraph() {
		return null;
    }
    
	public  Map<String, Double> nutrientAmountsByName (Map<Integer, Double> nutrientAmounts){
	
	Map<String, Double> nutrientAmountsByName = new HashMap<>();
	 
    for (Map.Entry<Integer, Double> entry : nutrientAmounts.entrySet()) {
        Integer nutrientID = entry.getKey();
        Double value = entry.getValue();
        
        String name = database.getNutrientName(nutrientID);

        nutrientAmountsByName.put(name, value);
    	}
    
		return nutrientAmountsByName;
	}
}