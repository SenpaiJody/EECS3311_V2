// src/view/NutritionGoalUI.java
package view;

import Advising.GoalType;
import Advising.NutritionGoal;
import Advising.NutritionGoalManager;
import nutrientService.INutrientService;
import nutrientService.NutrientServiceFactory;
import userService.IUserService;
import userService.Profile;
import userService.UserServiceFactory;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class NutritionGoalUI extends JPanel {

    private final JComboBox<String> nutrientCombo;
    private final JComboBox<GoalType> goalTypeCombo;
    private final JSpinner intensitySpinner;
    private final INutrientService nutrientService;
    private final NutritionGoalManager goalManager;
    private final IUserService userService;

    public NutritionGoalUI() {
        setLayout(new GridLayout(5, 2, 10, 10));
        setBorder(BorderFactory.createTitledBorder("Set Nutrition Goal"));
        //
        nutrientService = NutrientServiceFactory.getService();
        goalManager = new NutritionGoalManager();
        userService = UserServiceFactory.create();

        nutrientCombo = new JComboBox<>();
        goalTypeCombo = new JComboBox<>(GoalType.values());
        intensitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        JButton setGoalButton = new JButton("Set Goal");

        add(new JLabel("Nutrient:"));
        add(nutrientCombo);
        add(new JLabel("Goal Type:"));
        add(goalTypeCombo);
        add(new JLabel("Intensity (1-10):"));
        add(intensitySpinner);
        add(new JLabel(""));
        add(setGoalButton);

        // Load nutrients
        List<String> nutrients = nutrientService.getSupportedNutrients();
        for (String nutrient : nutrients) {
            nutrientCombo.addItem(nutrient);
        }

        setGoalButton.addActionListener(e -> {
            try {
                String nutrientName = (String) nutrientCombo.getSelectedItem();
                GoalType goalType = (GoalType) goalTypeCombo.getSelectedItem();
                int intensity = (Integer) intensitySpinner.getValue();

                int nutrientId = nutrientService.getNutrientIdByName(nutrientName);
                Profile profile = userService.getUserProfile().getProfiles().get(0); // Assume single profile
                int profileId = profile.getID();

                NutritionGoal goal = new NutritionGoal(profileId, nutrientId, intensity, goalType, null);
                goalManager.setGoal(goal);

                JOptionPane.showMessageDialog(this, "Nutrition goal set successfully!");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to set goal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
