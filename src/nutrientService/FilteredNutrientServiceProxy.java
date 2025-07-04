package nutrientService;

import java.util.List;
import java.util.Map;

//a proxy class for filtering, such that only specific nutrients are taken into account and the irrelevant nutrients are ignored
class FilteredNutrientServiceProxy implements INutrientService{

	//we are filtering the nutrients used to only Protein (203), Fats (204), Carbs (205), Calories (208), Cholesterol (601), Sodium (307), Potassium (306), Calcium(301), Iron (303), Vitamin C(401) and Vitamin D (324)
	private static final int[] ALLOWED_NUTRIENT_IDS = new int[] {203, 204, 205, 208, 601, 307, 306, 301, 303, 401, 324};
	
	INutrientService originalService;
	
	FilteredNutrientServiceProxy(INutrientService originalService){
		this.originalService = originalService;
	};
	
	@Override
	public Map<Integer, Double> getNutrientSumPer100g(List<Integer> ingredientIDs) {
		Map<Integer, Double> originalResult = originalService.getNutrientSumPer100g(ingredientIDs);
		return process(originalResult);
		
	}

	@Override
	public Map<Integer, Map<Integer, Double>> getNutrientsListPer100g(List<Integer> ingredientIDs) {
		Map<Integer, Map<Integer, Double>> originalResult = originalService.getNutrientsListPer100g(ingredientIDs);
		originalResult.forEach((key, value)->{
			originalResult.replace(key, process(value));
		});
		return originalResult;
	}

	@Override
	public Map<Integer, Double> getNutrientsPer100g(int ingredientID) {
		Map<Integer, Double> originalResult = originalService.getNutrientsPer100g(ingredientID);
		return process(originalResult);
	}

	@Override
	public String getNutrientName(int nutrientID) {
		return originalService.getNutrientName(nutrientID);
	}

	@Override
	public String getNutrientUnit(int nutrientID) {
		return originalService.getNutrientUnit(nutrientID);
	}

	//does all the processing required 
	private Map<Integer, Double> process (Map<Integer, Double> original){
		return addMissing(filter(original));
	}
	
	//removes unwanted nutrients
	private Map<Integer, Double> filter(Map<Integer, Double> original){
		original.entrySet().removeIf(entry->{
			for (int i = 0; i < ALLOWED_NUTRIENT_IDS.length; i++) {
				if (entry.getKey().equals(ALLOWED_NUTRIENT_IDS[i]))
				{
					return false;
				}	
			}
			return true;
		});
		return original;
	}
	//adds missing nutrients to the map (with a value of zero)
	private Map<Integer, Double> addMissing(Map<Integer, Double> original){
		for (int i = 0; i < ALLOWED_NUTRIENT_IDS.length; i++) {
			if (!original.containsKey(ALLOWED_NUTRIENT_IDS[i]))
				original.put(ALLOWED_NUTRIENT_IDS[i],0.d);
		}
		return original;
	}
}
