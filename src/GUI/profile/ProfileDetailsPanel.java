package GUI.profile;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import javax.swing.SpinnerNumberModel;

import GUI.DateSpinner;
import GUI.GBCUtility;
import GUI.GUIPanelBase;
import userService.Profile;
import userService.Profile.Gender;
import userService.Profile.Unit;
import userService.ProfileData;

//The pane that displays profile data that can be edited.
public class ProfileDetailsPanel extends GUIPanelBase implements ItemListener{

	private JButton doneButton;
	private JButton cancelButton;
	
	
	boolean isMetric;
	
	JTextField nameField;
	JComboBox<String> genderField;
	DateSpinner dateOfBirthField;
	HeightSpinner heightField;
	JSpinner weightField;
	JLabel weightLabel;
	JComboBox<String> unitField;
	
	public ProfileDetailsPanel(int width, int height) {
		setPreferredSize(new Dimension(width, height));
	}

	public void loadProfileData(Profile p) {
		nameField.setText(p.getName());
		genderField.setSelectedItem(p.getGender().name());
		dateOfBirthField.setDate(p.getDateOfBirth());
		heightField.setHeight((int)p.getHeight());	
		weightField.setValue(p.getPreferredUnit() == Unit.METRIC ? p.getWeight() : p.getWeight() * 2.205);
		unitField.setSelectedItem(p.getPreferredUnit().name());
		isMetric = p.getPreferredUnit() == Unit.METRIC ? true : false;
		heightField.setIsMetric(isMetric);
	}
	

	@Override
	protected void buildSelf(){
		setBorder(BorderFactory.createRaisedBevelBorder());

	}
	
	private void addLabel(String content, int y){
		JLabel label = new JLabel(content);
		Font lblFont = label.getFont().deriveFont(Font.BOLD).deriveFont(24.f);
		label.setFont(lblFont);
		GridBagConstraints gbc = GBCUtility.createGBC(0, y);
		gbc.anchor = GridBagConstraints.LINE_START;
		add(label, gbc);
	}
	
	@Override
	protected void loadComponents() {
		JLabel title = new JLabel("Tell Us About Yourself");
		title.setFont(title.getFont().deriveFont(28.f).deriveFont(Font.ITALIC));
		add(title,GBCUtility.createGBC(0, 0, 3, 1));
		add(Box.createVerticalStrut(20), GBCUtility.createGBC(0, 1, 3, 1));
		
		
		addLabel("Name", 2);
		nameField = new JTextField(20);
		add(nameField, GBCUtility.createGBC(1,  2, 2, 1));
		
		addLabel("Gender", 3);
		genderField = new JComboBox<String>();
		genderField.addItem("MALE");
		genderField.addItem("FEMALE");
		add(genderField, GBCUtility.createGBC(1,  3, 2, 1));
		
		
		addLabel("Date of Birth", 4);
		dateOfBirthField = new DateSpinner(12);
		add(dateOfBirthField, GBCUtility.createGBC(1,4,2,1));		
		
		heightField = new HeightSpinner(true);
		addLabel("Height", 5);
		add(heightField, GBCUtility.createGBC(1, 5,2,1));
		
		addLabel("Weight", 6);
		JPanel weightPanel = new JPanel();
		weightField = new JSpinner(new SpinnerNumberModel(80,0.00f, 999.f,0.1));
		weightPanel.add(weightField, GBCUtility.createGBC(1, 6,2,1));
		weightLabel = new JLabel("kg");
		weightPanel.add(weightLabel);
		add(weightPanel, GBCUtility.createGBC(1, 6,2,1));
		
		addLabel("Preferred Units", 7);
		unitField = new JComboBox<String>();
		unitField.addItem("METRIC");
		unitField.addItem("IMPERIAL");
		unitField.addItemListener(this);
		add(unitField, GBCUtility.createGBC(1, 7,2,1));
		
		add(Box.createVerticalStrut(30), GBCUtility.createGBC(0, 8));
		
		
		cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("cancel");
		add(cancelButton, GBCUtility.createGBC(0, 9, 1, 1));
		doneButton = new JButton("Done");
		doneButton.setActionCommand("done");
		add(doneButton, GBCUtility.createGBC(2, 9, 1, 1));
		
	}

	private String getUserName() {
		return nameField.getText();
	}
	private Gender getUserGender() {
		return Gender.values()[genderField.getSelectedIndex()];
	}
	private LocalDate getUserDoB() {
		return dateOfBirthField.getDate();
	}
	private double getUserHeight() {
		return heightField.getHeightInMetric();
	}
	private double getUserWeight() {
		if (isMetric)
			return (Double)weightField.getValue();
		else
			return (Double)weightField.getValue()/2.205;
	}
	private Unit getUserPreferredUnit() {
		return isMetric ? Unit.METRIC : Unit.IMPERIAL; 
	}
	
	
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource().equals(unitField) && e.getStateChange() == ItemEvent.SELECTED) {
			if (((String)unitField.getSelectedItem()).equals("IMPERIAL")) {
				weightLabel.setText("lb");
				heightField.setIsMetric(false);
				if (isMetric) {
					weightField.setValue(((double)weightField.getValue()) * 2.205);
				}
				isMetric = false;
			}
			else {
				weightLabel.setText("kg");
				heightField.setIsMetric(true);
				if (!isMetric) {
					weightField.setValue(((double)weightField.getValue()) / 2.205);
				}
				isMetric = true;
			}
		}
		
	}
	
	private void validateForm() throws ProfileNoNameException, TooYoungException{
		if (getUserName().length() == 0)
			throw new ProfileNoNameException();
		int age = LocalDate.now().getYear() - getUserDoB().getYear() -
				(LocalDate.now().isAfter(getUserDoB().withYear(LocalDate.now().getYear())) ? 0 : 1) ;
		if (age < 9)
			throw new TooYoungException();
	}
	
	
	public Profile getNewProfile() throws ProfileNoNameException, TooYoungException {
		validateForm();
		
		ProfileData profileData = new ProfileData(getUserGender(),getUserDoB(), getUserHeight(), getUserWeight());
		
		return new Profile(getUserName(),profileData, getUserPreferredUnit());
	}
	//matches the provided profile to the data described in the ProfileDEtailsPanel
	public void editProfile(Profile p) throws ProfileNoNameException, TooYoungException {
		validateForm();
		p.setName(getUserName());
		p.setGender(getUserGender());
		p.setDateOfBirth(getUserDoB());
		p.setHeight(getUserHeight());
		p.setWeight(getUserWeight());
		p.setPreferredUnit(getUserPreferredUnit());
	}
	
	public JButton getDoneButton() {
		return doneButton;
	}
	public JButton getCancelButton() {
		return cancelButton;
	}
}
