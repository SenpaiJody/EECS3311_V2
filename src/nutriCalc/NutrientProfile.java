/**
 * 
 */
/**
 * @author kunjalarora
 *
 */
package nutriCalc;

import java.util.*;
import nutrientService.INutrientService;
import nutrientService.NutrientServiceFactory;

/**
 * Represents a nutrition profile containing calculated nutrient values
 */
public class NutrientProfile {
    private Map<Integer, Double> nutrients;
    
    public NutrientProfile() {
        this.nutrients = new HashMap<>();
    }
    
    public NutrientProfile(Map<Integer, Double> nutrients) {
        this.nutrients = new HashMap<>(nutrients);
    }
    
    public double getNutrient(int nutrientId) {
        return nutrients.getOrDefault(nutrientId, 0.0);
    }
    
    public Map<Integer, Double> getAllNutrients() {
        return new HashMap<>(nutrients);
    }
    
    public void setNutrient(int nutrientId, double calculatedValue) {
        nutrients.put(nutrientId, calculatedValue);
    }
    
    public Set<Integer> getNutrientIds() {
        return new HashSet<>(nutrients.keySet());
    }
}
