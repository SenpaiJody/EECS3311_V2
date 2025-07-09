package nutritionRequests;

/*Protein (203) g, Fats (204) g, Carbohydrates (205) g, Calories (208),kcal 
 * Cholesterol (601), Sodium (307), Potassium (306), Calcium(301), 
 * Iron (303), Vitamin C(401) and Vitamin D (324)
 */

public enum Nutrient {


    PROTEIN(203),
    FAT(204),
    CARBOHYDRATES (205),
    CALORIES(208),
    CHOLESTEROL(601),
    SODIUM(307),
    POTASSIUM(306),
    CALCIUM(301),
    IRON(303),
    VITAMIN_C(401),
    VITAMIN_D(324);

    private final int id;

    Nutrient(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }
	
}
