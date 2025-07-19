package GUI.contentPages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import GUI.GBCUtility;

//base class for whole pages in the GUI
public abstract class BasicPage extends JPanel {

	private NavBar navBar;
	public NavBar getNavBar() {return navBar;}
	
	private JPanel innerPanel;
	public JPanel getInnerPanel() {return innerPanel;};
	
	private JLabel subtitleLabel;
	public void setSubtitle(String s) {
		subtitleLabel.setText(s);
	}
	public BasicPage(){
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(5, 5, 5, 5));
		
		navBar = new NavBar();
		add(navBar, BorderLayout.PAGE_START);
		JPanel body = new JPanel();
		body.setLayout(new GridBagLayout());
		add(body);
		body.setBorder(new EmptyBorder(10,10,10,10));
		
		GridBagConstraints subtitleGBC = GBCUtility.createGBC(0, 0);
		subtitleGBC.weighty = 0;
		subtitleGBC.weightx = 1;
		subtitleLabel = new JLabel("Subtitle");
		subtitleLabel.setFont(new Font("Arial", Font.BOLD, 32));
		subtitleLabel.setBorder(new EmptyBorder(5, 0, 15, 0));
		
		body.add(subtitleLabel, subtitleGBC);

		innerPanel = new JPanel();
		innerPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		innerPanel.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints innerPanelGBC = GBCUtility.createGBC(0, 1);
		innerPanelGBC.weighty = 1;
		innerPanelGBC.fill = GridBagConstraints.BOTH;
		body.add(innerPanel, innerPanelGBC);
		
	}


	
}
