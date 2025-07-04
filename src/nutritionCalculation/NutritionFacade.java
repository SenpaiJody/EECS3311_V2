/**
 * 
 */
/**
 * @author kunjalarora
 *
 */
package nutritionCalculation;

import java.util.*;
import nutrientService.INutrientService;
import nutrientService.NutrientServiceFactory;


/**
 * Service responsible for performing nutrition calculations
 */
class CalculationService {
    
    /**
     * Calculates nutrition profiles for a list of ingredients with their quantities
     * @param ingredients List of [ingredientId, quantity] pairs
     * @param nutritionDataPer100g Map of ingredient ID to nutrition data per 100g
     * @return NutrientProfile containing calculated values
     */
    public NutrientProfile calculateNutrientProfiles(List<List<Object>> ingredients, 
                                                   Map<Integer, Map<Integer, Double>> nutritionDataPer100g) {
        Map<Integer, Double> totalNutrients = new HashMap<>();
        
        for (List<Object> ingredient : ingredients) {
            int ingredientId = (Integer) ingredient.get(0);
            double quantity = (Double) ingredient.get(1);
            
            Map<Integer, Double> ingredientNutrients = nutritionDataPer100g.get(ingredientId);
            if (ingredientNutrients != null) {
                for (Map.Entry<Integer, Double> nutrientEntry : ingredientNutrients.entrySet()) {
                    int nutrientId = nutrientEntry.getKey();
                    double nutrientPer100g = nutrientEntry.getValue();
                    double calculatedAmount = applyQuantityFormula(nutrientPer100g, quantity);
                    
                    totalNutrients.merge(nutrientId, calculatedAmount, Double::sum);
                }
            }
        }
        
        return new NutrientProfile(totalNutrients);
    }
    
    /**
     * Combines multiple nutrition profiles into one
     * @param profiles List of NutrientProfile objects to combine
     * @return Combined NutrientProfile
     */
    public NutrientProfile combineNutritionProfiles(List<NutrientProfile> profiles) {
        Map<Integer, Double> combinedNutrients = new HashMap<>();
        
        for (NutrientProfile profile : profiles) {
            for (Map.Entry<Integer, Double> entry : profile.getAllNutrients().entrySet()) {
                combinedNutrients.merge(entry.getKey(), entry.getValue(), Double::sum);
            }
        }
        
        return new NutrientProfile(combinedNutrients);
    }
    
    /**
     * Applies the quantity formula: (nutrientPer100g * quantity) / 100
     * @param nutrientPer100g The nutrient amount per 100g
     * @param quantity The actual quantity of the ingredient
     * @return Calculated nutrient amount
     */
    private double applyQuantityFormula(double nutrientPer100g, double quantity) {
        return (nutrientPer100g * quantity) / 100.0;
    }
    
    /**
     * Sums nutrients from individual profiles
     * @param individualProfiles List of maps containing nutrient data
     * @return Map of summed nutrients
     */
    public Map<Integer, Double> sumNutrients(List<Map<Integer, Double>> individualProfiles) {
        Map<Integer, Double> summedNutrients = new HashMap<>();
        
        for (Map<Integer, Double> profile : individualProfiles) {
            for (Map.Entry<Integer, Double> entry : profile.entrySet()) {
                summedNutrients.merge(entry.getKey(), entry.getValue(), Double::sum);
            }
        }
        
        return summedNutrients;
    }
}

/**
 * Facade class that simplifies complex nutrition calculations
 * Coordinates between data retrieval and calculation subsystems
 */
public class NutritionFacade {
    private INutrientService nutrientService;
    private CalculationService calculationService;
    
    public NutritionFacade() {
        this.nutrientService = NutrientServiceFactory.getService();
        this.calculationService = new CalculationService();
    }
    
    /**
     * Main method that takes ingredients with quantities and provides complete nutrition profile
     * Takes ingredients + per100g data, applies formula: (nutrientPer100g x quantity) ÷ 100
     * Creates one NutrientProfile for the list
     * 
     * @param ingredients List of [ingredientId, quantity] pairs
     * @return NutrientProfile containing calculated nutrition values
     */
    public NutrientProfile calculateNutritionProfiles(List<List<Object>> ingredients) {
        // Extract ingredient IDs for data retrieval
        List<Integer> ingredientIds = new ArrayList<>();
        for (List<Object> ingredient : ingredients) {
            ingredientIds.add((Integer) ingredient.get(0));
        }
        
        // Get nutrition data per 100g for all ingredients
        Map<Integer, Map<Integer, Double>> nutritionDataPer100g = new HashMap<>();
        for (Integer ingredientId : ingredientIds) {
            List<Integer> singleIngredient = Arrays.asList(ingredientId);
            Map<Integer, Double> nutrientsFor100g = nutrientService.getNutrientsPer100g(singleIngredient);
            nutritionDataPer100g.put(ingredientId, nutrientsFor100g);
        }
        
        // Calculate the nutrition profile
        return calculationService.calculateNutrientProfiles(ingredients, nutritionDataPer100g);
    }
    
    /**
     * Combines multiple nutrition profiles into one
     * @param profiles List of NutrientProfile objects to combine
     * @return Combined NutrientProfile
     */
    public NutrientProfile combineNutritionProfiles(List<NutrientProfile> profiles) {
        return calculationService.combineNutritionProfiles(profiles);
    }
}
    