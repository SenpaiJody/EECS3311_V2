package view;

import foodService.IFoodService;
import foodService.FoodServiceFactory;
import food.Meal;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class MealViewerUI extends JPanel {

    private final JTextArea mealArea;
    private final JButton refreshButton;
    private final IFoodService mealService;

    public MealViewerUI() {
        this.mealService = FoodServiceFactory.create();

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Meal Viewer"));

        mealArea = new JTextArea(15, 40);
        mealArea.setEditable(false);
        refreshButton = new JButton("Refresh Meals");

        add(new JScrollPane(mealArea), BorderLayout.CENTER);
        add(refreshButton, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> loadMeals());
        loadMeals();
    }

    private void loadMeals() {
        try {
            List<Meal> meals = mealService.getAllMeals();
            StringBuilder sb = new StringBuilder();
            for (Meal meal : meals) {
                sb.append("Meal ID: ").append(meal.getId()).append("\n");
                sb.append("Date: ").append(meal.getDate()).append("\n");
                sb.append("Type: ").append(meal.getMealType()).append("\n");
                sb.append("Ingredients: ").append(meal.getIngredients()).append("\n\n");
            }
            mealArea.setText(sb.toString());
        } catch (Exception ex) {
            mealArea.setText("Failed to load meals: " + ex.getMessage());
        }
    }
}
