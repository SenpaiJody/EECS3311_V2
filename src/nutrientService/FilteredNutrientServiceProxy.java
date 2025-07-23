package nutrientService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import database.CSVNutrientDB;

//

/**A proxy class that filters the nutrients retrieved, such that only a handful of nutrients are retrieved and the rest are ignored.
 * 
 *<p>Since a reduced list of Nutrients is being used, specific names are also be specified that are more human-readable.
 *<p> The list of nutrients that are used are:
 *<ul>
 *	<li>Protein (203)
 *	<li>Fats (204)
 *	<li>Carbs (205)
 *	<li>Calories (208)
 *	<li>Cholesterol (601)
 *	<li>Sodium (307)
 *	<li>Potassium (306)
 *	<li>Calcium (301)
 *	<li>Iron (303)
 *	<li>Vitamin C (401)
 *	<li>Vitamin D (324)
 *<ul>
 * 
 * */
class FilteredNutrientServiceProxy implements INutrientService{

	//we are filtering the nutrients used to only Protein (203), Fats (204), Carbs (205), Calories (208), Cholesterol (601), Sodium (307), Potassium (306), Calcium(301), Iron (303), Vitamin C(401) and Vitamin D (324)
	private static final int[] ALLOWED_NUTRIENT_IDS = new int[] {203, 204, 205, 208, 601, 307, 306, 301, 303, 401, 324};
	private static final String[] NUTRIENT_NAME_LOOKUP = new String[] {"Protein", "Fats", "Carbohydrates", "Calories", "Cholesterol", "Sodium", "Potassium", "Calcium", "Iron", "Vitamin C", "Vitamin D"};
	
	private CSVNutrientDB originalService;
	
	FilteredNutrientServiceProxy(CSVNutrientDB originalService){
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
		for (int i =0; i < ALLOWED_NUTRIENT_IDS.length; i++) {
			if (ALLOWED_NUTRIENT_IDS[i] == nutrientID)
				return NUTRIENT_NAME_LOOKUP[i];
		}
		
		return originalService.getNutrientName(nutrientID);
	}

	@Override
	public String getNutrientUnit(int nutrientID) {
		return originalService.getNutrientUnit(nutrientID);
	}

	//does all the processing required (as of right now, it is filtering out unwanted ingredients and adding missing ingredients (with an amount of zero)
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
	
	@Override
	public List<Integer> getAllNutrientIDs(){
		List<Integer> retVal = new ArrayList<Integer>(ALLOWED_NUTRIENT_IDS.length);
		for (int i =0 ; i < ALLOWED_NUTRIENT_IDS.length; i++) {
			retVal.add(ALLOWED_NUTRIENT_IDS[i]);
		}
		return retVal;
	}

	@Override
	public INutrientIterator getIterator() {
		return new FilteredIteratorProxy(originalService.getIterator());
	}
	
	private class FilteredIteratorProxy implements INutrientIterator{
		INutrientIterator originalIterator;
		
		private FilteredIteratorProxy(INutrientIterator originalIterator){
			this.originalIterator = originalIterator;
		}
		@Override
		public int getIngredientID() {
			return originalIterator.getIngredientID();
		}

		/** Removes all nutrients not in the ALLOWED_NUTRIENT_LIST, and adds in values that are supposed to be there
		 * */
		@Override
		public Map<Integer, Double> getNutrientMap() {
			return process(originalIterator.getNutrientMap());
		}

		@Override
		public void next() {
			originalIterator.next();
			
		}

		@Override
		public boolean hasNext() {
			return originalIterator.hasNext();
		}
		
	}
}
