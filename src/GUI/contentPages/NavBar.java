package GUI.contentPages;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import GUI.GBCUtility;
import GUI.MainWindow;
import GUI.contentPages.homepage.HomePage;
import GUI.contentPages.meal.CreateMealPage;
import GUI.contentPages.meal.MealHistoryPage;
import GUI.contentPages.statistics.StatisticsPage;
import GUI.profile.ProfilePage;

//The navigation bar that exists on any page of the GUI
public class NavBar extends JPanel {
	
	private JButton homeBtn;
	public JButton getHomeButton() { return homeBtn;}
	
	private JButton historyBtn;
	public JButton getHistoryButton() {return historyBtn;}
	
	private JButton addMealBtn;
	public JButton getaddMealButton() {return addMealBtn;}
	
	private JButton statsBtn;
	public JButton getStatsButton() {return statsBtn;}
	
	
	private JButton profileBtn;
	public JButton getProfileButton() {return profileBtn;}
	
	private int buttonCount = 0;
	public NavBar() {
		setLayout(new GridBagLayout());
		setBorder(new EmptyBorder(10,10,0,10));
		homeBtn = addBtn("Home");
		homeBtn.addActionListener(event->{
			MainWindow.getInstance().setPage(new HomePage());
		});
		
		
		historyBtn = addBtn("History");
		historyBtn.addActionListener(event->{
			MainWindow.getInstance().setPage(new MealHistoryPage());
		});
		addMealBtn = addBtn("Add Meal");
		addMealBtn.addActionListener(event->{
			MainWindow.getInstance().setPage(new CreateMealPage());
		});
		statsBtn = addBtn("Statistics");
		statsBtn.addActionListener(event->{
			MainWindow.getInstance().setPage(new StatisticsPage());
		});
		
		
		
	
		GridBagConstraints fillGBC = GBCUtility.createGBC(buttonCount++, 0);
		fillGBC.weightx = 1;
		add(Box.createHorizontalGlue(), fillGBC);
		
		Font font = new Font("Arial", Font.BOLD,28);
		profileBtn = new JButton("Profile");
		profileBtn.setFont(font);
		GridBagConstraints profileGBC = GBCUtility.createGBC(buttonCount++, 0);
		profileGBC.weightx = 0;
		add(profileBtn, profileGBC);
		profileBtn.addActionListener(event->{
			MainWindow.getInstance().setPage(new ProfilePage());
		});

		
	}
	
	private JButton addBtn(String name) {
		JButton btn = new JButton(name);
		btn.setFont(new Font("Arial", Font.BOLD,28));
		GridBagConstraints GBC = GBCUtility.createGBC(buttonCount++, 0);
		GBC.weightx = 0;
		add(btn, GBC);
		return btn;
	}
}
