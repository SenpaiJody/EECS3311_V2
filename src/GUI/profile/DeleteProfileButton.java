package GUI.profile;

import javax.swing.JButton;

import userService.Profile;

//A button with the delete ActionCommand
public class DeleteProfileButton extends JButton {
	private Profile p;
	public Profile getProfile() {return p;}
	public DeleteProfileButton(Profile p) {
		super("Delete");
		this.p = p;
		setActionCommand("delete");
	}
}
