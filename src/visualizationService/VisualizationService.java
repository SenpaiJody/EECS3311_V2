package visualizationService;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import userService.Profile.Gender;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;



public class VisualizationService implements IVisualizationService {
   
	    private nutrientService.INutrientService nutrientService;
	    
	    private String userIntakeLegendLabel = "User Intake";
	    private String CFGRecommendationLegendLabel = "CFG Adequate Intake";
	    private String advisedIntakeLegendLabel = "Advised Intake";
	    
//	    private String avgIntakeTitle = "Average Daily Nutrient Intake: ";
//	    private String totalIntakeTitle = "Cumulative Nutrient Intake: ";
//	    private String nutrientAmtByDateTitle = "Nutrient Amount By Date: ";
//	    private String nutrientAmtPerMealTitle = "Nutrient Amount Per Meal: ";
	    
	    // Note that name of colour is Canadian spelling, java object is American spelling
	    private static final Color COLOUR_HISTORICAL = Color.RED;
	    private static final Color COLOUR_ADVISED = Color.BLUE;
	    private static final Color COLOUR_CFG = Color.GREEN;

	    private static final BasicStroke STROKE_SOLID = new BasicStroke(2.0f);
	    private static final BasicStroke STROKE_DASHED = new BasicStroke(
	        2.0f,
	        BasicStroke.CAP_BUTT,
	        BasicStroke.JOIN_BEVEL,
	        0,
	        new float[]{4.0f, 4.0f},
	        0.0f
	    );
	    
	    // could change but get nutrient name depends on database so may not be worth it....
	    private static final Map<String, Color> NUTRIENT_COLORS = Map.ofEntries(
	    	    Map.entry("PROTEIN (g)",                      new Color(255, 99, 132)),
	    	    Map.entry("FAT (TOTAL LIPIDS) (g)",           new Color(255, 206, 86)),
	    	    Map.entry("CARBOHYDRATE, TOTAL (BY DIFFERENCE) (g)", new Color(54, 162, 235)),
	    	    Map.entry("ENERGY (KILOCALORIES) (kCal)",     new Color(153, 102, 255)),
	    	    Map.entry("CHOLESTEROL (mg)",                  new Color(255, 159, 64)),
	    	    Map.entry("SODIUM (mg)",                       new Color(0, 191, 255)),
	    	    Map.entry("POTASSIUM (mg)",                    new Color(124, 252, 0)),
	    	    Map.entry("CALCIUM (mg)",                      new Color(75, 192, 192)),
	    	    Map.entry("IRON (mg)",                         new Color(255, 105, 180)),
	    	    Map.entry("VITAMIN C (mg)",                    new Color(255, 215, 0)),
	    	    Map.entry("VITAMIN D (INTERNATIONAL UNITS) (IU)", new Color(106, 90, 205))
	    	);
	    
	    public VisualizationService(nutrientService.INutrientService nutrientService) {
	        this.nutrientService = nutrientService;
	    }
		
	    protected JFreeChart createGraph() {
			return null;
	    }
	    
	    public boolean isInvalidCFGRequest(Gender gender, int age) {
	        return (gender != Gender.MALE && gender != Gender.FEMALE) || age < 9;
	    }
	    
	    public String getNutrientAmtTag (int nutrientID) {
	    	
	        String name = nutrientService.getNutrientName(nutrientID);    
	        String unit = nutrientService.getNutrientUnit(nutrientID);
	        String nutrientAmtTag = name + " (" + unit +")";
	        
	        return nutrientAmtTag;
	    	
	    }
	        
	  
		public JFreeChart createErrorGraph() {
			
			String errorMessage = "Canada Food Guide recommendations are currently only available for profiles with gender set to male or female, "
					+ "and age 9 years or older. We apologize for the inconvenience. Please adjust selection for further visualizations";
			 // Create dummy axes with no visible lines or labels
	        CategoryAxis domainAxis = new CategoryAxis();
	        domainAxis.setVisible(false);
	        NumberAxis rangeAxis = new NumberAxis();
	        rangeAxis.setVisible(false);

	        // Renderer that draws nothing
	        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
	        renderer.setDefaultShapesVisible(false);
	        renderer.setDefaultLinesVisible(false);

	        // Create plot with no dataset
	        CategoryPlot plot = new CategoryPlot(null, domainAxis, rangeAxis, renderer);
	        plot.setOutlineVisible(false);
	        plot.setBackgroundPaint(Color.WHITE);
	        plot.setDomainGridlinesVisible(false);
	        plot.setRangeGridlinesVisible(false);

	        // Create chart with empty title (we'll add error message as a separate title)
	        JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, false);
	        chart.setBackgroundPaint(Color.WHITE);
	        chart.setAntiAlias(true);

	        // Add centered error message as chart title
	        TextTitle message = new TextTitle(errorMessage);
	        message.setFont(new Font("SansSerif", Font.BOLD, 18));
	        message.setPaint(Color.BLACK);
	        message.setHorizontalAlignment(TextTitle.DEFAULT_HORIZONTAL_ALIGNMENT); // CENTER by default
	        message.setMargin(300, 50, 100, 50);  // Adjust margins to center vertically

	        chart.setTitle(message);

	        return chart;
	    }
	    
	    // for a whole map
		public  Map<String, Double> nutrientAmountsByName (Map<Integer, Double> nutrientAmounts){
		
		Map<String, Double> nutrientAmountsByName = new HashMap<>();
		 
	    for (Map.Entry<Integer, Double> entry : nutrientAmounts.entrySet()) {
	        Integer nutrientID = entry.getKey();
	        Double value = entry.getValue();

	        nutrientAmountsByName.put(getNutrientAmtTag (nutrientID), value);
	    	}
	    
			return nutrientAmountsByName;
		}

		protected void printAvgNutrients(Map<Integer, Double> avgNutrients) {
			    if (avgNutrients == null || avgNutrients.isEmpty()) {
			        System.out.println("No nutrient data to display.");
			        return;
			    }
			    System.out.println("Average Nutrients:");
			    for (Map.Entry<Integer, Double> entry : avgNutrients.entrySet()) {
			        Integer nutrientID = entry.getKey();
			        Double avgAmount = entry.getValue();
			        System.out.printf("Nutrient ID %d: %.2f grams%n", nutrientID, avgAmount);
			    }
			}	   
		
		public JFreeChart formatBarGraph(DefaultCategoryDataset dataset, String title, LocalDate dateStart, LocalDate dateEnd) {
		
				JFreeChart chart = ChartFactory.createBarChart(
			    title + dateStart + " to " + dateEnd,
			    "Nutrient",
			    "Average Amount",
			    dataset,
			    PlotOrientation.VERTICAL,
			    true,	// include legend
			    true,	// include tooltips
			    false	// not URLs
					);

			     // Set font for category labels (nutrients)
		        chart.getCategoryPlot()
		             .getDomainAxis()
		             .setTickLabelFont(new Font("SansSerif", Font.BOLD,6));  // 

		        // Optionally: set font for axis label (i.e., the word "Nutrient")
		        chart.getCategoryPlot()
		             .getDomainAxis()
		             .setLabelFont(new Font("SansSerif", Font.BOLD, 20));
				
		        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
		        renderer.setSeriesPaint(0, COLOUR_HISTORICAL);  // "Meal"
		        renderer.setSeriesPaint(1, COLOUR_ADVISED);
		        renderer.setSeriesPaint(2, COLOUR_CFG); 
		        
		        
			return chart;
		}
		
		public JFreeChart formatPieGraph(DefaultPieDataset<String> dataset, String title, LocalDate dateStart, LocalDate dateEnd) {
			// Create pie chart
		    JFreeChart chart = ChartFactory.createPieChart(
		    	title + dateStart + " to " + dateEnd,
		        dataset,
		        true,   // include legend
		        true,   // include tooltips
		        false   // no URLs
		    );

		    // Optional: customize pie plot font sizes
		    PiePlot plot = (PiePlot) chart.getPlot();
		    plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 10));
		    chart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 18));
		    chart.getLegend().setItemFont(new Font("SansSerif", Font.PLAIN, 12));
		    
		    return chart;
			
		}
		
		public JFreeChart formatDualPieChart(DefaultPieDataset<String> dataset1, DefaultPieDataset<String> dataset2, String mainTitle, String plot1title, String plot2title, LocalDate dateStart, LocalDate dateEnd) {
			PiePlot plot1 = new PiePlot(dataset1);
		    PiePlot plot2 = new PiePlot(dataset2);

		    // Customize the plots
		    Font labelFont = new Font("SansSerif", Font.PLAIN, 10);
		    plot1.setLabelFont(labelFont);
		    plot2.setLabelFont(labelFont);

		    plot1.setInteriorGap(0.04);
		    plot2.setInteriorGap(0.04);

		    plot1.setBackgroundPaint(Color.WHITE);
		    plot2.setBackgroundPaint(Color.WHITE);

		    // === Enforce colors here ===
		    for (Map.Entry<String, Color> entry : NUTRIENT_COLORS.entrySet()) {
		        String nutrient = entry.getKey();
		        Color color = entry.getValue();
		        plot1.setSectionPaint(nutrient, color);
		        plot2.setSectionPaint(nutrient, color);
		    }
		    
		    // Create a custom Plot that draws both PiePlots side by side
		    DualPiePlot combinedPlot = new DualPiePlot(plot1,plot2,plot1title,plot2title);

		    // Build the chart using the custom plot
		    JFreeChart chart = new JFreeChart(mainTitle + dateStart + " to " + dateEnd,
		            new Font("SansSerif", Font.BOLD, 18),
		            combinedPlot,
		            false); // no auto legend — each PiePlot handles its own

		    return chart;
			
		}
		
		public JFreeChart formatTriplePieChart(
		        DefaultPieDataset<String> dataset1,
		        DefaultPieDataset<String> dataset2,
		        DefaultPieDataset<String> dataset3,
		        String mainTitle,
		        String plot1title,
		        String plot2title,
		        String plot3title, LocalDate dateStart, LocalDate dateEnd) {
		    
		    PiePlot plot1 = new PiePlot(dataset1);
		    PiePlot plot2 = new PiePlot(dataset2);
		    PiePlot plot3 = new PiePlot(dataset3);

		    // Customize the plots
		    Font labelFont = new Font("SansSerif", Font.PLAIN, 10);
		    plot1.setLabelFont(labelFont);
		    plot2.setLabelFont(labelFont);
		    plot3.setLabelFont(labelFont);

		    plot1.setInteriorGap(0.04);
		    plot2.setInteriorGap(0.04);
		    plot3.setInteriorGap(0.04);

		    plot1.setBackgroundPaint(Color.WHITE);
		    plot2.setBackgroundPaint(Color.WHITE);
		    plot3.setBackgroundPaint(Color.WHITE);

		    // === Enforce colors ===
		    for (Map.Entry<String, Color> entry : NUTRIENT_COLORS.entrySet()) {
		        String nutrient = entry.getKey();
		        Color color = entry.getValue();
		        plot1.setSectionPaint(nutrient, color);
		        plot2.setSectionPaint(nutrient, color);
		        plot3.setSectionPaint(nutrient, color);
		    }

		    // Create the combined plot
		    TriplePiePlot combinedPlot = new TriplePiePlot(
		            plot1, plot2, plot3,
		            plot1title, plot2title, plot3title
		    );

		    // Build the chart using the custom plot
		    JFreeChart chart = new JFreeChart(
		            mainTitle + dateStart + " to " + dateEnd,
		            new Font("SansSerif", Font.BOLD, 18),
		            combinedPlot,
		            false // No legend — pies handle their own
		    );

		    return chart;
		}
		
		public JFreeChart formatLineGraph(DefaultCategoryDataset dataset, String title, int nutrientID) {
			JFreeChart chart = ChartFactory.createLineChart(
			    	title  + getNutrientAmtTag(nutrientID),
			        "Meal",            // X-axis label
			        "Amount",          // Y-axis label
			        dataset,           
			        PlotOrientation.VERTICAL,
			        true,             // include legend
			        true,              // tooltips
			        false              // URLs
			    );

			    // Enable shapes (points) on each data item to show distinct points
			    CategoryPlot plot = chart.getCategoryPlot();
			    LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
			    renderer.setDefaultShapesVisible(true);   // show points
			    renderer.setDefaultShapesFilled(true);    // fill points

			    renderer.setSeriesPaint(0, COLOUR_HISTORICAL);    
			    renderer.setSeriesPaint(1, COLOUR_CFG);
			    renderer.setSeriesPaint(2, COLOUR_ADVISED);
			    
			    renderer.setSeriesStroke(0, STROKE_SOLID);
			    renderer.setSeriesStroke(1, STROKE_DASHED);
			    renderer.setSeriesStroke(2, STROKE_SOLID);
			    
			    // Optional: Adjust fonts, colors, etc.
			    plot.getDomainAxis().setTickLabelFont(new Font("SansSerif", Font.BOLD, 8));
			    plot.getDomainAxis().setLabelFont(new Font("SansSerif", Font.BOLD, 12));
			    plot.getRangeAxis().setLabelFont(new Font("SansSerif", Font.BOLD, 12));

			    return chart;
			}

		// works for both avg and total, one map function for historical, no CFG
		public void populateBarGraphDataset(DefaultCategoryDataset dataset, Map<Integer, Double> mealListNutrientAmounts) {
			for (Map.Entry<Integer, Double> entry : mealListNutrientAmounts.entrySet()) {
			    int nutrientID = entry.getKey();
			    double avgValue = entry.getValue();
			    	    
			    // Create label from nutrient ID
			    String nutrientAmtTag = getNutrientAmtTag(nutrientID);
			    
			    // Add both data points to the dataset
			    dataset.addValue(avgValue, userIntakeLegendLabel, nutrientAmtTag);
			}
			
		}
		
		// works for both avg and total, two map function for advised, no CFG
		public void populateBarGraphDataset(DefaultCategoryDataset dataset, Map<Integer, Double> mealListNutrientAmounts, Map<Integer, Double> swapMealListNutrientAmounts) {
			

			for (Map.Entry<Integer, Double> entry : mealListNutrientAmounts.entrySet()) {
			    int nutrientID = entry.getKey();
			    double avgValue = entry.getValue();
			    
		        double advisedValue = swapMealListNutrientAmounts.getOrDefault(nutrientID, 0.0);
		        
			    String nutrientAmtTag = getNutrientAmtTag(nutrientID);
			    
			    dataset.addValue(avgValue, userIntakeLegendLabel, nutrientAmtTag);
			    dataset.addValue(advisedValue, advisedIntakeLegendLabel, nutrientAmtTag);
			}
			
		}
		
		// works for both avg and total, two map function for historical, with CFG
		public void populateCFGBarGraphDataset(DefaultCategoryDataset dataset, Map<Integer, Double> mealListNutrientAmounts, Map<Integer, Double> cfgNutrientsMap) {
			for (Map.Entry<Integer, Double> entry : mealListNutrientAmounts.entrySet()) {
			    int nutrientID = entry.getKey();
			    double avgValue = entry.getValue();
			    
			    // Get corresponding CFG value
			    double cfgValue = cfgNutrientsMap.getOrDefault(nutrientID, 0.0);
			    
			    // Create label from nutrient ID
			    String nutrientAmtTag = getNutrientAmtTag(nutrientID);
			    
			    // Add both data points to the dataset
			    dataset.addValue(avgValue, userIntakeLegendLabel, nutrientAmtTag);
			    dataset.addValue(cfgValue, CFGRecommendationLegendLabel, nutrientAmtTag);
			}
			
		}
		
		// works for both avg and total, three map function for advised, with CFG
		public void populateCFGBarGraphDataset(DefaultCategoryDataset dataset, Map<Integer, Double> mealListNutrientAmounts,Map<Integer, Double> swapMealListNutrientAmounts, Map<Integer, Double> cfgNutrientsMap) {
			for (Map.Entry<Integer, Double> entry : mealListNutrientAmounts.entrySet()) {
			    int nutrientID = entry.getKey();
			    double avgValue = entry.getValue();
			    
			    // Get corresponding CFG value
			    double advisedValue = mealListNutrientAmounts.getOrDefault(nutrientID, 0.0);
			    double cfgValue = cfgNutrientsMap.getOrDefault(nutrientID, 0.0);
			    
			    // Create label from nutrient ID
			    String nutrientAmtTag = getNutrientAmtTag(nutrientID);
			    
			    // Add both data points to the dataset
			    dataset.addValue(avgValue, userIntakeLegendLabel, nutrientAmtTag);
			    dataset.addValue(advisedValue, advisedIntakeLegendLabel, nutrientAmtTag);
			    dataset.addValue(cfgValue, CFGRecommendationLegendLabel, nutrientAmtTag);
			}
			
		}
		
		// works for both avg and total, one map function for historical, no CFG
		public void populatePieGraphDataset(DefaultPieDataset<String> dataset, Map<Integer, Double> mealListNutrientAmounts) {
			Map<String, Double> nutrientAmounts = nutrientAmountsByName(mealListNutrientAmounts);
		    for (Map.Entry<String, Double> entry : nutrientAmounts.entrySet()) {
		        String nutrient = entry.getKey();
		        double value = entry.getValue();    
		        dataset.setValue(nutrient, value);
		    }
		}
		
		// just for food group graphs
		public void populateFoodGroupPieGraphDataset(DefaultPieDataset<String> dataset, Map<String, Double> foodGroupPercentages) {
		    for (Map.Entry<String, Double> entry : foodGroupPercentages.entrySet()) {
		        String foodGroupName = entry.getKey();
		        Double percentage = entry.getValue();
		        dataset.setValue(foodGroupName, percentage);
		    }
		}
		
		// one map function for historical, no CFG
		public void populateLineGraphDataset(DefaultCategoryDataset dataset,List<Map.Entry<String, Double>> nutrientList) {
		
			for (Map.Entry<String, Double> entry : nutrientList) {
		        String date = entry.getKey();
		        double nutrientValue = entry.getValue();
		        dataset.addValue(nutrientValue, userIntakeLegendLabel, date);
		    }
		}
		
		// one map function for historical, with CFG
		public void populateLineGraphDataset(DefaultCategoryDataset dataset,List<Map.Entry<String, Double>> nutrientList, double CFGNutrientRecommendation) {
		
			for (Map.Entry<String, Double> entry : nutrientList) {
		        String date = entry.getKey();
		        double nutrientValue = entry.getValue();
		        dataset.addValue(nutrientValue, userIntakeLegendLabel, date);
		        dataset.addValue(CFGNutrientRecommendation, CFGRecommendationLegendLabel, date);
		    }
		}
		
		// two map function for advised, no CFG
		public void populateLineGraphDataset(DefaultCategoryDataset dataset,List<Map.Entry<String, Double>> nutrientList, List<Map.Entry<String, Double>> swapNutrientByDateList) {
			
			for (int i = 0; i < nutrientList.size(); i++) {
		        String date = nutrientList.get(i).getKey();
		        double originalValue = nutrientList.get(i).getValue();
		        double swapValue = swapNutrientByDateList.get(i).getValue();

		        dataset.addValue(originalValue, userIntakeLegendLabel, date);
		        dataset.addValue(swapValue, advisedIntakeLegendLabel, date);
		    }
		}
		
		// two map function for advised, with CFG
		public void populateLineGraphDataset(DefaultCategoryDataset dataset,List<Map.Entry<String, Double>> nutrientList, List<Map.Entry<String, Double>> swapNutrientByDateList, double CFGNutrientRecommendation) {
			
			for (int i = 0; i < nutrientList.size(); i++) {
		        String date = nutrientList.get(i).getKey();
		        double originalValue = nutrientList.get(i).getValue();
		        double swapValue = swapNutrientByDateList.get(i).getValue();

		        dataset.addValue(originalValue, userIntakeLegendLabel, date);
		        dataset.addValue(swapValue, advisedIntakeLegendLabel, date);
		        dataset.addValue(CFGNutrientRecommendation,CFGRecommendationLegendLabel, date);
		    }
		}
	}
