package visualization;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;

public class NutritionGoalPanel extends JPanel {

    private final JComboBox<String> nutrientDropdown;
    private final JTextField targetValueField;
    private final JTextArea outputArea;

    private static final LinkedHashMap<String, Integer> nutrientMap = new LinkedHashMap<>() {{
        put("Protein", 203);
        put("Fat", 204);
        put("Carbohydrates", 205);
        put("Calories", 208);
        put("Cholesterol", 601);
        put("Sodium", 307);
        put("Potassium", 306);
        put("Calcium", 301);
        put("Iron", 303);
        put("Vitamin C", 401);
        put("Vitamin D", 324);
    }};

    public NutritionGoalPanel() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Set Nutrition Goal"));

        inputPanel.add(new JLabel("Nutrient:"));
        nutrientDropdown = new JComboBox<>(nutrientMap.keySet().toArray(new String[0]));
        inputPanel.add(nutrientDropdown);

        inputPanel.add(new JLabel("Target Value (g or mg):"));
        targetValueField = new JTextField();
        inputPanel.add(targetValueField);

        JButton submitButton = new JButton("Add Goal");
        inputPanel.add(submitButton);

        add(inputPanel, BorderLayout.NORTH);

        outputArea = new JTextArea(5, 30);
        outputArea.setEditable(false);
        outputArea.setBorder(BorderFactory.createTitledBorder("Result"));
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleGoalSubmission();
            }
        });
    }

    private void handleGoalSubmission() {
        String selectedNutrient = (String) nutrientDropdown.getSelectedItem();
        String valueText = targetValueField.getText().trim();

        try {
            double value = Double.parseDouble(valueText);
            int nutrientId = nutrientMap.get(selectedNutrient);
            outputArea.setText("Created Goal:\nNutrient: " + selectedNutrient +
                    " (ID: " + nutrientId + ")\nTarget: " + value);

            // You could store or pass this goal to another module here.

        } catch (NumberFormatException ex) {
            outputArea.setText(" Invalid number entered. Please enter a valid numeric value.");
        }
    }
}
