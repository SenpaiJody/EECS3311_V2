package graphService;

import java.awt.BasicStroke;
import java.awt.Color;
import java.time.LocalDate;
import java.util.List;
import java.util.TreeSet;

import food.Food;
import nutrientService.INutrientService;
import nutrientService.NutrientServiceFactory;

public abstract class GraphMode implements IGraphMode{


    protected String userIntakeLegendLabel = "User Intake";
    protected String CFGRecommendationLegendLabel = "CFG Adequate Intake";
    protected String advisedIntakeLegendLabel = "Advised Intake";

    protected String avgIntakeTitle = "Average Daily Nutrient Intake: ";
    protected String totalIntakeTitle = "Cumulative Nutrient Intake: ";
    protected String nutrientAmtByDateTitle = "Nutrient Amount By Date: ";
//    protected String nutrientAmtPerMealTitle = "Nutrient Amount Per Meal: ";
	protected String FoodGroupIntakeTitle = "Food Group Percentage Intake: ";

    protected TreeSet<LocalDate> uniqueDates;
    protected INutrientService nutrientService;

    // Note that name of colour is Canadian spelling, java object is American spelling
    protected static final Color COLOUR_HISTORICAL = Color.RED;
    protected static final Color COLOUR_ADVISED = Color.BLUE;
    protected static final Color COLOUR_CFG = Color.GREEN;

    protected static final BasicStroke STROKE_SOLID = new BasicStroke(2.0f);
    protected static final BasicStroke STROKE_DASHED = new BasicStroke(
        2.0f,
        BasicStroke.CAP_BUTT,
        BasicStroke.JOIN_BEVEL,
        0,
        new float[]{4.0f, 4.0f},
        0.0f
    );


    public GraphMode() {
    	this.nutrientService = NutrientServiceFactory.getService();
    	this.uniqueDates = new TreeSet<>();
    }

    protected void addDates(List<Food> foodList) {
            for (Food food : foodList) {
                if (food != null && food.getDate() != null) {
                    uniqueDates.add(food.getDate());
                }
            }
        }

    protected String getNutrientAmtTag (int nutrientID) {

        String name = nutrientService.getNutrientName(nutrientID);
        String unit = nutrientService.getNutrientUnit(nutrientID);
        String nutrientAmtTag = name + " (" + unit +")";

        return nutrientAmtTag;

    }

  }