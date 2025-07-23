package nutrientScore;

import java.util.Map;

public interface INutrientScorer {
	/**
	 * algo for scoring the similarity of two sets of nutrients
	 * @param target - the nutrient map to be used as a criteria
	 * @param trial - the nutrient map the user wishes to score
	 * 
	 * @returns - a score
	 * */
	public double scoreLikeness(Map<Integer,Double> target, Map<Integer,Double> trial);
}
