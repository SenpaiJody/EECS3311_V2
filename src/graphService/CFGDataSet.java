package graphService;

import java.time.LocalDate;
import java.time.Period;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import cfgNutrientRecService.CFGNutrientRecServiceFactory;
import cfgNutrientRecService.ICFGNutrientRecService;
import food.Food;
import userService.Profile;
import userService.Profile.Gender;

public class CFGDataSet implements IDataSet {

	Profile profile;
	String legendLabel;
	Gender gender;
	int age;
	ICFGNutrientRecService CFGNutrientRecService;

	public CFGDataSet(String legendLabel, Profile profile) {
		this.legendLabel = legendLabel;
		this.profile = profile;
		this.gender = profile.getGender();
		this.age = Period.between(profile.getDateOfBirth(),LocalDate.now()).getYears();
		this.CFGNutrientRecService = CFGNutrientRecServiceFactory.getService();
	}

	@Override
	public String getLegendLabel() { return legendLabel; }

	// intentionally returning a null food
	@Override
	public List<Food> getFoodList() {
		Food nullFood = null;
		List<Food> foodList = new ArrayList<>();
		foodList.add(nullFood);

		return foodList; }

	@Override
	public List<Map.Entry<LocalDate, Double>> getNutrientByDateList (TreeSet<LocalDate> uniqueDates, int nutrientChoice) {

		double CFGNutrientRecommendation = CFGNutrientRecService.getCFGNutrientRecommendation(gender, age, nutrientChoice);
		List<Map.Entry<LocalDate, Double>> nutrientByDateList = new ArrayList<>();

	    for (LocalDate date : uniqueDates) {
	        nutrientByDateList.add(new AbstractMap.SimpleEntry<>(date, CFGNutrientRecommendation));
	    }

		return nutrientByDateList;
	}

	@Override
	public Map<String, Double> getFoodGroupPercentages() {

		Map<String, Double> foodGroupPercentages = CFGNutrientRecService.getCFGFoodGroupRecommendationsMap();

		return foodGroupPercentages;
	}

	@Override
	public Map<Integer, Double> getAvgNutrientAmounts() {

		Map<Integer, Double> CFGNutrientAvgMap = CFGNutrientRecService.getCFGNutrientAvgMap(gender,age);

		return CFGNutrientAvgMap;
	}

	@Override
	public Map<Integer, Double> getTotalNutrientAmounts(int distinctDaysCount) {

		Map<Integer, Double> CFGNutrientTotalMap = CFGNutrientRecService.getCFGNutrientTotalMap(gender,age,distinctDaysCount);

		return CFGNutrientTotalMap;
	}


}