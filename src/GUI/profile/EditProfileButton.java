package GUI.profile;

import javax.swing.JButton;

import userService.Profile;

public class EditProfileButton extends JButton {
	private Profile p;
	public Profile getProfile() {return p;}
	public EditProfileButton(Profile p) {
		super("Edit");
		this.p = p;
		setActionCommand("edit");
	}
}
