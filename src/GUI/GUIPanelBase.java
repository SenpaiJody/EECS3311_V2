package GUI;


import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;

//base class for a GUI component; uses GridBagLayout
/*Implements the Template Pattern
 * */
 
public abstract class GUIPanelBase extends JPanel {
	

	private GridBagConstraints constraints;
	
	
	public GUIPanelBase(){
		initialize();
	}
	
	protected void initialize() {
		buildSelf();
		setLayout(new GridBagLayout());
		constraints = new GridBagConstraints();
		configureConstraints(constraints);
		loadComponents();
		update();
	}
	
	protected void buildSelf() {};
	protected void loadComponents() {}; 
	protected void configureConstraints(GridBagConstraints c) {};
	
	protected void update() {
		revalidate();
		repaint();
	}
	
	
}
