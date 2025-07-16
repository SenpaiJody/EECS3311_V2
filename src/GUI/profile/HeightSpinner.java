package GUI.profile;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import GUI.UnitConverter;

//A Spinner that displays height and can switch between imperial and metric
public class HeightSpinner extends JPanel implements ChangeListener{

	double height = 170;
	
	private boolean isDisplayMetric;
	public void setIsMetric(boolean isDisplayMetric) {
		if (this.isDisplayMetric != isDisplayMetric) {
			this.isDisplayMetric = isDisplayMetric;
			load();
		}
			
		
	}
	
	public void setHeight(double height) {
		this.height = height;
		load();
	}
	public boolean getIsDisplayMetric() {return isDisplayMetric;}
	
	
	JSpinner metricSpinner;
	JSpinner footSpinner;
	JSpinner inchSpinner;
	
	
	
	public HeightSpinner(boolean isMetric){
		setIsMetric(isMetric);
		
		load();
	}
	
	private void load() {
		removeAll();
		metricSpinner = null;
		footSpinner = null;
		inchSpinner = null;
		
		if (getIsDisplayMetric())
			loadMetricDisplay();
		else
			loadImperialDisplay();
	}
	private void loadMetricDisplay() {
		metricSpinner = new JSpinner(new SpinnerNumberModel(height,0,999,1));
		metricSpinner.addChangeListener(this);
		JLabel metricLabel = new JLabel("cm");
		add(metricSpinner);
		add(metricLabel);
	}
	
	private void loadImperialDisplay() {
		
		int[] currentHeight = UnitConverter.cmToFeetInches(height);
		footSpinner = new JSpinner(new SpinnerNumberModel(currentHeight[0],0,20,1));
		add(footSpinner);
		footSpinner.addChangeListener(this);
		JLabel footLabel = new JLabel("ft");
		add(footLabel);
		
		inchSpinner = new JSpinner(new SpinnerNumberModel(currentHeight[1],0,11,1));
		add(inchSpinner);
		inchSpinner.addChangeListener(this);
		JLabel inchLabel = new JLabel("inches");
		add(inchLabel);
	}
	
	public double getHeightInMetric() {
		return height;
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		if (getIsDisplayMetric() && e.getSource().equals(metricSpinner)) {
			height = (double)metricSpinner.getValue();
		}
		else if (!getIsDisplayMetric() && (e.getSource().equals(footSpinner) || e.getSource().equals(inchSpinner))) {
			System.out.println(String.format("%d'%d\"",(int)footSpinner.getValue(), (int)inchSpinner.getValue()));
			height = UnitConverter.feetInchesToCm((int)footSpinner.getValue(), (int)inchSpinner.getValue());
		}

	}
}
