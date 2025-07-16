package GUI.profile;

import java.awt.Color;

import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import userService.Profile;

//A clickable panel that has a "+" on it. Exists on the profile page
public class ProfileAdditionButtonPanel extends ProfileButtonPanel{
	Profile profile;
	
	ProfileAdditionButtonPanel(){
	}
	@Override
	protected void loadComponents() {
		JLabel label = new JLabel("+");
		label.setFont(label.getFont().deriveFont(200.f));
		label.setForeground(Color.LIGHT_GRAY);
		add(label);
	}
}
