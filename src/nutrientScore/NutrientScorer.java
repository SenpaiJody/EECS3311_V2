package nutrientScore;

import java.util.Map;

public class NutrientScorer implements INutrientScorer{
	/**
	 * algo for scoring the similarity of two sets of nutrients
	 * @param target - the nutrient map to be used as a criteria
	 * @param trial - the nutrient map the user wishes to score
	 * 
	 * @returns - a score
	 * */
	@Override
	public double scoreLikeness(Map<Integer,Double> target, Map<Integer,Double> trial) {
		
		
		double finalScore = 0;
		for (Integer key : target.keySet()) {
			double targetValue = target.get(key);
			if (targetValue == 0) {
				continue;
			}
			double trialValue = trial.get(key);
			double percentageOff = Math.abs(targetValue-trialValue)/targetValue;
			finalScore += 1-percentageOff > 1 ? 1 : percentageOff;
		}
		
		finalScore /= target.size();
		
		return finalScore;
		
	}
}
