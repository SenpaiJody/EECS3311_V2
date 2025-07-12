package GUI.login;


import java.awt.Color;

import GUI.GUIPanelBase;

import javax.swing.*;

public class LoginPage extends GUIPanelBase {
	
	private GUIPanelBase currentPanel;
	
	@Override
	protected void buildSelf() {
		setBorder(BorderFactory.createRaisedBevelBorder());
		setBackground(Color.LIGHT_GRAY);
	}
	@Override
	protected void loadComponents() {
		setCurrentPanel(new LoginPanel(this));
	}
	
	void setCurrentPanel(GUIPanelBase panel) {
		if (currentPanel != null)
			remove(currentPanel);
		currentPanel = panel;
		add(currentPanel);
		revalidate();
		repaint();
	}
}

	
	
	
	
