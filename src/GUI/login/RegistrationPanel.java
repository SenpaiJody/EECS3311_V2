package GUI.login;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import GUI.GBCUtility;
import GUI.GUIPanelBase;
import GUI.MainWindow;
import GUI.profile.ProfilePage;
import userService.IUserService;
import userService.User;
import userService.UserAlreadyExistsException;
import userService.UserServiceFactory;

class RegistrationPanel extends GUIPanelBase implements ActionListener{
	
	JTextField usernameField;
	JPasswordField passwordField;
	LoginPage parentPage;

	RegistrationPanel(LoginPage parentPage){
		this.parentPage = parentPage;
	}

	@Override
	protected void buildSelf() {
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	}
	
	@Override 
	protected void loadComponents() {
		
		/////////////////
		JLabel loginLabel = new JLabel("REGISTER");
		loginLabel.setFont(getFont().deriveFont(25.f).deriveFont(Font.BOLD));
		add(loginLabel, GBCUtility.createGBC(0, 0, 3, 1));
		
		add(Box.createVerticalStrut(15), GBCUtility.createGBC(0, 1, 3, 1));
		
		//////////////////
		
		add(new JLabel("Username: "),GBCUtility.createGBC(0, 2, 1, 1));
		usernameField = new JTextField(20);
		add(usernameField, GBCUtility.createGBC(1, 2, 2, 1));
	
		//////////////////
		
		passwordField = new JPasswordField(20);
		add(new JLabel("Password: "), GBCUtility.createGBC(0, 3, 1, 1));
		add(passwordField, GBCUtility.createGBC(1, 3, 2, 1));
			
		add(Box.createVerticalStrut(15), GBCUtility.createGBC(0, 4, 3, 1));
		///////////////////
		
		GridBagConstraints btn_register_constraints = GBCUtility.createGBC(2,5);
		btn_register_constraints.anchor = GridBagConstraints.LINE_END;
		JButton btn_register = new JButton("Register >");
		add(btn_register, btn_register_constraints);
		btn_register.setActionCommand("attempt_register");
		btn_register.addActionListener(this);
		
		
		
		JLabel label_returningUser = new JLabel("Returning user?");
		label_returningUser.setFont(getFont().deriveFont(10.f).deriveFont(Font.ITALIC));
		add(label_returningUser,GBCUtility.createGBC(0, 4, 1, 1));
		
		JButton btn_loginInstead = new JButton("Login");
		add(btn_loginInstead,GBCUtility.createGBC(0, 5, 1, 1));
		btn_loginInstead.setActionCommand("login");
		btn_loginInstead.addActionListener(this);
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("attempt_register")) {
			attemptRegister();
		}
		else if (e.getActionCommand().equals("login")){
			parentPage.setCurrentPanel(new LoginPanel(parentPage));
		}
	}
	
	private void attemptRegister() {
		try {
			IUserService userService = UserServiceFactory.getService();
			String password = new String(passwordField.getPassword());
					
			if (usernameField.getText().length() == 0 || password.length() == 0)
				throw new IllegalArgumentException();
			
			userService.registerUser(usernameField.getText(), password);
			
			MainWindow.getInstance().setPage(new ProfilePage());
		}
		catch(UserAlreadyExistsException e) {
			JOptionPane.showMessageDialog(null, "User Already Exists. Try Logging in.");
		}
		catch(IllegalArgumentException e) {
			JOptionPane.showMessageDialog(null, "Please enter a username and password");
		}
	}
}