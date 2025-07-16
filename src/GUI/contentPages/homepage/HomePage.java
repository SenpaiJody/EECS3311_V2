package GUI.contentPages.homepage;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import GUI.contentPages.BasicPage;

//The home page / landing page 
public class HomePage extends BasicPage {
	
	public HomePage(){
		
		getNavBar().getHomeButton().setEnabled(false);
		setSubtitle("Home Page");
		
		getInnerPanel().setLayout(new GridBagLayout());
		JLabel welcomeMsg = new JLabel("Welcome! This page is still being built...");
		getInnerPanel().add(welcomeMsg);
	}
}
