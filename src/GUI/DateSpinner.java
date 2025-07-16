package GUI;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.time.YearMonth;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A "Spinner" for dates (actually uses JComboBoxes)
 * */
public class DateSpinner extends JPanel implements ItemListener{
	
	private JComboBox<Integer> yearComboBox;
	private JComboBox<String> monthComboBox;
	private JComboBox<Integer> dayComboBox;
	
	private static final String[] MONTHS = new String[]{"January","February","March","April","May","June","July","August","September","October","November","December"};
	

	public void setDate(LocalDate d) {
		yearComboBox.setSelectedItem(d.getYear());
		monthComboBox.setSelectedIndex(d.getMonth().getValue()-1);
		dayComboBox.setSelectedItem(d.getDayOfMonth());
	}
	
	public DateSpinner(int fontSize){
		setLayout(new GridBagLayout());
		
	
		JLabel yearLabel = new JLabel("Year");
		JLabel monthLabel = new JLabel("Month");
		JLabel dayLabel = new JLabel("Day");
		Font font = yearLabel.getFont().deriveFont(fontSize).deriveFont(Font.ITALIC);
		yearLabel.setFont(font);
		monthLabel.setFont(font);;
		dayLabel.setFont(font);
		
		
		Font comboFont = font.deriveFont(Font.BOLD);
		
		yearComboBox = new JComboBox<Integer>();
		yearComboBox.setFont(comboFont);
		for (int i = 0; i < 100; i++)
			yearComboBox.addItem(LocalDate.now().getYear()-i);
		yearComboBox.addItemListener(this);
		
		monthComboBox = new JComboBox<String>();
		monthComboBox.setFont(comboFont);
		for (int i =0; i < MONTHS.length; i++)
			monthComboBox.addItem(MONTHS[i]);
		monthComboBox.addItemListener(this);
		
		dayComboBox = new JComboBox<Integer>();
		dayComboBox.setFont(comboFont);
		dayComboBox.removeAllItems();
		for (int i =1; i <= 31; i++)
			dayComboBox.addItem(i);
		
		add(yearLabel, GBCUtility.createGBC(0, 0));
		add(yearComboBox, GBCUtility.createGBC(0, 1));
		add(monthLabel, GBCUtility.createGBC(1, 0));
		add(monthComboBox, GBCUtility.createGBC(1, 1));
		add(dayLabel, GBCUtility.createGBC(2, 0));
		add(dayComboBox, GBCUtility.createGBC(2, 1));
	}
	

	
	public LocalDate getDate() {
		return LocalDate.of(
				(int)yearComboBox.getSelectedItem(),
				monthComboBox.getSelectedIndex()+1,
				(int)dayComboBox.getSelectedItem()
				);
	}



	@Override
	public void itemStateChanged(ItemEvent e) {
        if (e.getSource().equals(monthComboBox) || e.getSource().equals(yearComboBox)){
        	if (e.getStateChange() == ItemEvent.SELECTED) {
        		int days = YearMonth.of((int)yearComboBox.getSelectedItem(), monthComboBox.getSelectedIndex()+1).lengthOfMonth();
        		int previousSelection = (int)dayComboBox.getSelectedItem();

        		dayComboBox.removeAllItems();
        		for (int j = 1; j <= days; j++) {
        			dayComboBox.addItem(j);
        		}
        		dayComboBox.setSelectedItem(previousSelection);
        		dayComboBox.revalidate();
        		dayComboBox.repaint();
        		
        		
                revalidate();
                repaint();
        	}
 
        }
	};
	
	public void addItemListener(ItemListener listener) {
		yearComboBox.addItemListener(listener);
		monthComboBox.addItemListener(listener);
		dayComboBox.addItemListener(listener);
		
	}	
	public void removeItemListener(ItemListener listener) {
		yearComboBox.removeItemListener(listener);
		monthComboBox.removeItemListener(listener);
		dayComboBox.removeItemListener(listener);
	}
	
}
