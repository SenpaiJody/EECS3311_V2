package view;

import ingredientService.IIngredientService;
import ingredientService.IngredientServiceFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MealLoggerUI extends JPanel {

    private final JTextField ingredientField;
    private final JTextField quantityField;
    private final JComboBox<String> mealTypeCombo;
    private final JTextField dateField;
    private final JTextArea logArea;

    private final IIngredientService ingredientService = IngredientServiceFactory.getService();

    public MealLoggerUI() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Log Meal"));

        ingredientField = new JTextField();
        quantityField = new JTextField();
        mealTypeCombo = new JComboBox<>(new String[]{"Breakfast", "Lunch", "Dinner", "Snack"});
        dateField = new JTextField();

        JButton logButton = new JButton("Log Meal");

        inputPanel.add(new JLabel("Ingredient name:")); inputPanel.add(ingredientField);
        inputPanel.add(new JLabel("Quantity (grams):")); inputPanel.add(quantityField);
        inputPanel.add(new JLabel("Meal Type:")); inputPanel.add(mealTypeCombo);
        inputPanel.add(new JLabel("Date (YYYY-MM-DD):")); inputPanel.add(dateField);
        inputPanel.add(new JLabel("")); inputPanel.add(logButton);

        add(inputPanel, BorderLayout.NORTH);

        logArea = new JTextArea(8, 30);
        logArea.setEditable(false);
        logArea.setBorder(BorderFactory.createTitledBorder("Meal Log Output"));
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        logButton.addActionListener(this::handleLogMeal);
    }

    private void handleLogMeal(ActionEvent e) {
        String ingredientName = ingredientField.getText().trim();
        String quantityStr = quantityField.getText().trim();
        String mealType = (String) mealTypeCombo.getSelectedItem();
        String date = dateField.getText().trim();

        if (ingredientName.isEmpty() || quantityStr.isEmpty() || date.isEmpty()) {
            logArea.setText("All fields are required.");
            return;
        }

        try {
            double quantity = Double.parseDouble(quantityStr);
            List<Integer> results = ingredientService.searchIngredientByName(ingredientName, 1);
            if (results.isEmpty()) {
                logArea.setText("Ingredient not found in database.");
                return;
            }
            int ingredientId = results.get(0);


            logArea.setText("Logged Meal:\n" +
                    "Ingredient: " + ingredientName + " (ID: " + ingredientId + ")\n" +
                    "Quantity: " + quantity + "g\n" +
                    "Meal Type: " + mealType + "\n" +
                    "Date: " + date);

            // Add to meal data store or pass to service here if needed

        } catch (NumberFormatException ex) {
            logArea.setText("Invalid number for quantity.");
        } catch (Exception ex) {
            logArea.setText("Error: " + ex.getMessage());
        }
    }
}
