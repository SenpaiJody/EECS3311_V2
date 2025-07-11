package visualization;

import java.util.HashMap;
import java.util.Map;

public class NutrientProfile {
	
    private Map<Integer, Double> nutrients;

    public NutrientProfile() {
        this.nutrients = new HashMap<>();
    }

    public double getNutrient(int nutrientId) {
        return nutrients.getOrDefault(nutrientId, 0.0);
    }

    public Map<Integer, Double> getAllNutrients() {
        return nutrients;
    }

    public void setNutrient(int nutrientId, double calculatedValue) {
        nutrients.put(nutrientId, calculatedValue);
    }
}