package GUI.profile;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import GUI.GBCUtility;
import GUI.UnitConverter;
import userService.IUserService;
import userService.Profile;
import userService.Profile.Unit;
import userService.ProfileDoesNotExistException;
import userService.UserServiceFactory;

public class ProfileSelectionButtonPanel extends ProfileButtonPanel{
	private Profile profile;
	private EditProfileButton editButton;
	private DeleteProfileButton deleteButton;
	
	EditProfileButton getEditButton() {return editButton;}
	DeleteProfileButton getDeleteButton() {return deleteButton;}
	
	Profile getProfile() {
		return profile;
	}
	
	ProfileSelectionButtonPanel(Profile profile){
		this.profile = profile;
		editButton = new EditProfileButton(profile);
		deleteButton = new DeleteProfileButton(profile);
		loadData();
	}
	
	
	@Override
	protected void buildSelf(){
		setBorder(BorderFactory.createRaisedBevelBorder());
		
	}
	
	
	
	
	void loadData() {
		GridBagConstraints editbuttonGBC =  GBCUtility.createGBC(0, 0, 2, 1);
		editbuttonGBC.anchor = GridBagConstraints.LINE_END;
		editbuttonGBC.insets = new Insets(0, 0, 0, 20);
		editButton.setPreferredSize(new Dimension(75, 20));
		add(editButton, editbuttonGBC);
		
		GridBagConstraints deletebuttonGBC =  GBCUtility.createGBC(0, 1, 2, 1);
		deletebuttonGBC.anchor = GridBagConstraints.LINE_END;
		deletebuttonGBC.insets = new Insets(0, 0, 0, 20);
		deleteButton.setPreferredSize(new Dimension(75, 20));
		add(deleteButton, deletebuttonGBC);
		
		addField("Name", profile.getName(), 0);
		addField("Gender", profile.getGender().toString(), 1);
		int age = LocalDate.now().getYear() - profile.getDateOfBirth().getYear() -
				(LocalDate.now().isAfter(profile.getDateOfBirth().withYear(LocalDate.now().getYear())) ? 0 : 1) ;
		
		addField("Date of Birth", profile.getDateOfBirth().toString() + String.format(" (%d years)",age), 2);
		addField("Weight", String.format("%s",getWeightString(profile)), 3);
		addField("Height", String.format("%s",getHeightString(profile)), 4);
	}
	
	private String getWeightString(Profile p) {
		if (p.getPreferredUnit() == Unit.METRIC) {
			return String.format("%.1f kg", p.getWeight());
		}
		else
			return String.format("%.1f lbs", p.getWeight() * 2.205);
	}
	
	private String getHeightString(Profile p) {
		if (p.getPreferredUnit() == Unit.METRIC) {
			return String.format("%d cm", (int)p.getHeight());
		}
		else {
			int[] imperial = UnitConverter.cmToFeetInches((int)p.getHeight());
			return String.format("%d' %d\"", imperial[0], imperial[1]);
		}
			
	}
	
	private void addField(String key, String value, int y) {
		JLabel keyLabel = new JLabel(key);
		keyLabel.setFont(keyLabel.getFont().deriveFont(Font.BOLD).deriveFont(18.f));
		GridBagConstraints keyGBC = GBCUtility.createGBC(0, 2*y);
		keyGBC.weightx = 0;
		keyGBC.anchor = GridBagConstraints.LINE_START;
		keyGBC.insets = new Insets(0,15,0,0);
		
		JLabel valueLabel = new JLabel(value);
		valueLabel.setFont(valueLabel.getFont().deriveFont(Font.ITALIC).deriveFont(15.f));
		GridBagConstraints valueGBC = GBCUtility.createGBC(0, 2*y+1);
		valueGBC.anchor = GridBagConstraints.LINE_START;
		valueGBC.weightx = 1;
		valueGBC.insets = new Insets(0,30,0,0);
		
		
		add(keyLabel, keyGBC);
		add(valueLabel, valueGBC);
	}

}
