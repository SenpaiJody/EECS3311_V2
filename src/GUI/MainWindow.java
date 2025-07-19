package GUI;


import javax.swing.*;

import GUI.filter.FilterBuilderPanel;
import GUI.login.LoginRegistrationPage;
import GUI.profile.ProfilePage;
import userService.IncorrectLoginException;
import userService.UserServiceFactory;


/** The main window of the program.
 * */
public class MainWindow extends JFrame {

	private static MainWindow instance;
	

	public static final int SIZE_X = 1422; //defaulting to a 16:9 ratio of 1422:800
	public static final int SIZE_Y = 800;
	private static final String title = "FOOD NUTRIENT APP";
	
	
	private JPanel currentPage;
	
	
	public static MainWindow getInstance() {
		return instance;
	}
	
	
	public MainWindow() {		
		if (instance == null)
			instance = this;
		
		init();
		
		
		try {
			UserServiceFactory.getService().attemptLogin("bobtest3", "mypassword");
		} catch (IncorrectLoginException e) {
			e.printStackTrace();
		}
		setPage(new ProfilePage());
		
		//setPage(new LoginRegistrationPage());
		
		//setPage(new FilterBuilderPanel());
	}
	
	private void init() {
		setTitle(title);
		setSize(SIZE_X,SIZE_Y);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	
	public void setPage(JPanel page) {
		if (currentPage != null)
			remove(currentPage);
		currentPage = page;
		add(currentPage);
		revalidate();
		repaint();
	}

	
	

}
