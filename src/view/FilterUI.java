package view;

import foodService.Filter;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class FilterUI extends JPanel {

    private final JTextField startDateField;
    private final JTextField endDateField;
    private final JButton applyButton;

    public interface FilterListener {
        void onFilterApplied(Filter filter);
    }

    public FilterUI(FilterListener listener) {
        setLayout(new GridLayout(3, 2, 10, 10));
        setBorder(BorderFactory.createTitledBorder("Filter by Date"));

        startDateField = new JTextField();
        endDateField = new JTextField();
        applyButton = new JButton("Apply Filter");

        add(new JLabel("Start Date (YYYY-MM-DD):"));
        add(startDateField);
        add(new JLabel("End Date (YYYY-MM-DD):"));
        add(endDateField);
        add(new JLabel(""));
        add(applyButton);

        applyButton.addActionListener(e -> {
            try {
                LocalDate start = LocalDate.parse(startDateField.getText().trim());
                LocalDate end = LocalDate.parse(endDateField.getText().trim());

                Filter filter = new Filter();
                filter.setDateRange(start, end);
                listener.onFilterApplied(filter);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid date format. Please use YYYY-MM-DD.",
                        "Date Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
