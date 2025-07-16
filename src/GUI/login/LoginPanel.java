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
import userService.IncorrectLoginException;
import userService.UserAlreadyExistsException;
import userService.UserServiceFactory;


//the "login" part of the login page
class LoginPanel extends GUIPanelBase implements ActionListener{		
	private JTextField usernameField;
	private JPasswordField passwordField;
	
	private LoginRegistrationPage parentPage;

	LoginPanel(LoginRegistrationPage parentPage){
		this.parentPage = parentPage;
	}
	
	
	@Override
	protected void buildSelf(){
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	}
	
	@Override
	protected void loadComponents(){
		///////////
		JLabel loginLabel = new JLabel("LOGIN");
		loginLabel.setFont(getFont().deriveFont(25.f).deriveFont(Font.BOLD));
		add(loginLabel, GBCUtility.createGBC(0, 0, 3, 1));
		
		add(Box.createVerticalStrut(15), GBCUtility.createGBC(0, 1, 3, 1));
		
		////////////
		add(new JLabel("Username: "),GBCUtility.createGBC(0, 2, 1, 1));
		usernameField = new JTextField(20);
		add(usernameField, GBCUtility.createGBC(1, 2, 2, 1));
		
		//////////////
		
		passwordField = new JPasswordField(20);
		add(new JLabel("Password: "), GBCUtility.createGBC(0, 3, 1, 1));
		add(passwordField, GBCUtility.createGBC(1, 3, 2, 1));
			
		add(Box.createVerticalStrut(15), GBCUtility.createGBC(0,4, 3, 1));
		
		/////////////////
		
		GridBagConstraints btn_login_constraints = GBCUtility.createGBC(2,5);
		btn_login_constraints.anchor = GridBagConstraints.LINE_END;
		JButton btn_login = new JButton("Login >");
		add(btn_login, btn_login_constraints);
		btn_login.setActionCommand("attempt_login");
		btn_login.addActionListener(this);
		
		
		JLabel label_newUser = new JLabel("New user?");
		label_newUser.setFont(getFont().deriveFont(10.f).deriveFont(Font.ITALIC));
		add(label_newUser,GBCUtility.createGBC(0, 4, 1, 1));
		
		
		
		JButton btn_registerInstead = new JButton("Register");
		add(btn_registerInstead,GBCUtility.createGBC(0, 5, 1, 1));
		btn_registerInstead.setActionCommand("register");
		btn_registerInstead.addActionListener(this);
	}

	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("attempt_login")) {
			attemptLogin();
		}
		else if (e.getActionCommand().equals("register")){
			parentPage.setCurrentPanel(new RegistrationPanel(parentPage));
		}
	}
	
	
	private void attemptLogin() {
		try {
			IUserService srv = UserServiceFactory.getService();
			String password = new String(passwordField.getPassword());
			
			if (usernameField.getText().length() == 0 || password.length() == 0) 
				throw new IllegalArgumentException();
			
			srv.attemptLogin(usernameField.getText(), password);
			
			MainWindow.getInstance().setPage(new ProfilePage());
		}
		catch(IncorrectLoginException e) {
			JOptionPane.showMessageDialog(null,"Login Incorrect, please try again!");
		}
		catch(IllegalArgumentException e) {
			JOptionPane.showMessageDialog(null,"Please enter username and password!");
		}
		
	}
}


