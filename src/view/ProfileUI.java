package view;

import userService.Profile;
import userService.User;
import userService.IUserService;
import userService.UserServiceFactory;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class ProfileUI extends JPanel {

    private final JTextField nameField;
    private final JTextField dobField;
    private final JComboBox<Profile.Gender> genderCombo;
    private final JTextField heightField;
    private final JTextField weightField;
    private final IUserService userService;

    public ProfileUI() {
        setLayout(new GridLayout(7, 2, 10, 10));
        setBorder(BorderFactory.createTitledBorder("Create Profile"));

        userService = UserServiceFactory.create();

        nameField = new JTextField();
        dobField = new JTextField();
        genderCombo = new JComboBox<>(Profile.Gender.values());
        heightField = new JTextField();
        weightField = new JTextField();

        JButton saveButton = new JButton("Save Profile");

        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        add(dobField);
        add(new JLabel("Gender:"));
        add(genderCombo);
        add(new JLabel("Height (cm):"));
        add(heightField);
        add(new JLabel("Weight (kg):"));
        add(weightField);
        add(new JLabel(""));
        add(saveButton);

        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                LocalDate dob = LocalDate.parse(dobField.getText());
                Profile.Gender gender = (Profile.Gender) genderCombo.getSelectedItem();
                double height = Double.parseDouble(heightField.getText());
                double weight = Double.parseDouble(weightField.getText());

                Profile profile = new Profile(1, name, gender, dob, height, weight);
                userService.saveProfile(profile);

                JOptionPane.showMessageDialog(this, "Profile Saved!");
                nameField.setText("");
                dobField.setText("");
                heightField.setText("");
                weightField.setText("");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loadProfileIfExists();
    }

    private void loadProfileIfExists() {
        User user = userService.getUserProfile();
        if (user != null && !user.getProfiles().isEmpty()) {
            Profile profile = user.getProfiles().get(0);
            nameField.setText(profile.getName());
            dobField.setText(profile.getDateOfBirth().toString());
            genderCombo.setSelectedItem(profile.getGender());
            heightField.setText(String.valueOf(profile.getHeight()));
            weightField.setText(String.valueOf(profile.getWeight()));
        }
    }
}
