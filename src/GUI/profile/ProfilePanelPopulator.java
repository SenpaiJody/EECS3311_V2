package GUI.profile;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JPanel;

import GUI.GBCUtility;
import userService.Profile;

//a class that populates a panel with clickable panels
public class ProfilePanelPopulator {
	
	private static final int MAX_ROWS = 2;
	private static final int MAX_COLUMNS = 5;
	
	int width;
	int height;
	
	JPanel panel;
	ProfilePanelPopulator(JPanel panel, int width, int height){
		this.panel = panel;
		this.width = width;
		this.height = height;
		}
	
	private List<EditProfileButton> editButtons;
	private List<DeleteProfileButton> deleteButtons;
	private List<ProfileSelectionButtonPanel> profileSelectButtons;	
	ProfileAdditionButtonPanel addProfileButton;
	
	List<EditProfileButton> getEditButtons() {
		return editButtons;
	}
	List<DeleteProfileButton> getDeleteButtons(){
		return deleteButtons;
	}
	List<ProfileSelectionButtonPanel> getProfileSelectButtons(){
		return profileSelectButtons;
	}
	
	public ProfileAdditionButtonPanel getAddProfileButton() {return addProfileButton;};
	
	public void loadProfiles(List<Profile> profiles, ProfilePage addButtonConnection) {
		panel.removeAll();
		editButtons = new ArrayList<EditProfileButton>();
		deleteButtons = new ArrayList<DeleteProfileButton>();
		profileSelectButtons = new ArrayList<ProfileSelectionButtonPanel>();
		int profilesToDisplay = profiles.size() < 10 ? profiles.size() : 10; //this display only supports up to 10 profiles.
		
		int i=0;
		for (i = 0; i < profilesToDisplay; i++) { //create profile buttons
			GridBagConstraints gbc = GBCUtility.createGBC(i%MAX_COLUMNS, i/MAX_COLUMNS);
			ProfileSelectionButtonPanel psbp = new ProfileSelectionButtonPanel(profiles.get(i));
			profileSelectButtons.add(psbp);
			gbc.insets = new Insets(10,10,10,10);
			psbp.setPreferredSize(new Dimension(width/MAX_COLUMNS,  height/MAX_ROWS));
			editButtons.add(psbp.getEditButton());
			deleteButtons.add(psbp.getDeleteButton());
			panel.add(psbp, gbc);
	
		}
		if (i < MAX_COLUMNS*MAX_ROWS) { //create a single 'add' button
			GridBagConstraints gbc = GBCUtility.createGBC(i%MAX_COLUMNS, i/MAX_COLUMNS);
			addProfileButton = new ProfileAdditionButtonPanel();
			gbc.insets = new Insets(10,10,10,10);
			addProfileButton.setPreferredSize(new Dimension(width/MAX_COLUMNS,  height/MAX_ROWS));
			panel.add(addProfileButton, gbc);

		}
		for (i = i+1; i < MAX_COLUMNS*MAX_ROWS; i++) { //fill the rest with blanks to keep the grid filled
			GridBagConstraints gbc = GBCUtility.createGBC(i%MAX_COLUMNS, i/MAX_COLUMNS);
			gbc.insets = new Insets(10,10,10,10);
			panel.add(Box.createRigidArea(new Dimension(width/MAX_COLUMNS, height/MAX_ROWS)), gbc);
	
		}
	
	}
	
	
}


