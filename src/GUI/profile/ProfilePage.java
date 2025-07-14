package GUI.profile;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import GUI.GBCUtility;
import GUI.MainWindow;
import GUI.contentPages.BasicPage;
import GUI.contentPages.homepage.HomePage;
import userService.IUserService;
import userService.Profile;
import userService.ProfileDoesNotExistException;
import userService.UserDoesNotExistException;
import userService.UserServiceFactory;

public class ProfilePage extends GUI.GUIPanelBase {
	
	JLabel pageTitle;
	private int getPanelWidth() {return (int) (MainWindow.SIZE_X * 0.85);}
	private int getPanelHeight() {return (int) (MainWindow.SIZE_Y * 0.7);}
	
	JPanel container;
	
	public ProfilePage() {
		showProfiles();
	}
	
	@Override
	protected void loadComponents() {
		pageTitle = new JLabel();
		pageTitle.setFont(pageTitle.getFont().deriveFont(32.f).deriveFont(Font.BOLD));
		add(pageTitle, GBCUtility.createGBC(0, 0));
		add(Box.createVerticalStrut(20), GBCUtility.createGBC(0, 1, 1, 1));

		add(Box.createHorizontalStrut(getPanelWidth()), GBCUtility.createGBC(0, 2));
		add(Box.createVerticalStrut(getPanelHeight()), GBCUtility.createGBC(0, 2));
	
		
		
		container = new JPanel();
		GridBagConstraints containerGBC = GBCUtility.createGBC(0, 2);
		container.setLayout(new GridBagLayout());
		containerGBC.fill = GridBagConstraints.BOTH;	
		container.setBorder(BorderFactory.createLoweredBevelBorder());
		container.setBackground(Color.LIGHT_GRAY);
		add(container, containerGBC);

	}
	
	void showProfiles() {
		pageTitle.setText("Select a Profile");
		container.removeAll();
		IUserService userService = UserServiceFactory.getService();
		ProfilePanelPopulator psp = new ProfilePanelPopulator(container, getPanelWidth(), getPanelHeight());
		psp.loadProfiles(userService.getCurrentUser().getProfiles(), this);
		psp.getEditButtons().forEach(btn->{
			btn.addActionListener(event -> {
				if (event.getActionCommand().equals("edit")) {
					EditProfileButton b = (EditProfileButton) event.getSource();
					showProfileEditor(b.getProfile());
				}		
			});
		});
		
		var addProfilebtn = psp.getAddProfileButton();
		if (addProfilebtn != null) {
			addProfilebtn.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					showProfileCreator();
				}
			});
		}
		
		psp.getProfileSelectButtons().forEach(btn->{
			btn.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					try {
						IUserService srv = UserServiceFactory.getService();
						srv.setCurrentProfile(btn.getProfile());
						MainWindow.getInstance().setPage(new HomePage());
					}
					catch(ProfileDoesNotExistException e1) {
						JOptionPane.showMessageDialog(null, "Uh oh, this profile doesn't exist on this user...something went wrong!");
					}
				}
			});
		});
		
		psp.getDeleteButtons().forEach(btn->{
			btn.addActionListener(event ->{
				if (event.getActionCommand().equals("delete")) {
					userService.getCurrentUser().getProfiles().remove(btn.getProfile());
					try {
						userService.updateUser(userService.getCurrentUser());
					} catch (UserDoesNotExistException e) {
						JOptionPane.showMessageDialog(null, "Uh oh, something went wrong!");
					}
					showProfiles();
				}
			});
		});
		update();
	}
	
	void showProfileCreator() {
		pageTitle.setText("Build a Profile");;
		container.removeAll();
		ProfileDetailsPanel pdp = new ProfileDetailsPanel(getPanelHeight() - 20, getPanelHeight() - 20);
		container.add(pdp);
		pdp.getDoneButton().addActionListener(event ->{
			if (event.getActionCommand().equals("done"))
			{
				
				try {
					IUserService userSrv = UserServiceFactory.getService();
					userSrv.getCurrentUser().getProfiles().add(pdp.getNewProfile());
					showProfiles();
					userSrv.updateUser(userSrv.getCurrentUser());
					
				} catch (ProfileNoNameException e) {
					JOptionPane.showMessageDialog(null, "A Profile must have a name!");
				} catch (TooYoungException e) {
					JOptionPane.showMessageDialog(null, "You are too young! (9+ Only)");
				} catch (UserDoesNotExistException e) {
					JOptionPane.showMessageDialog(null, "Something went wrong!");
				}
				
			}
		});;
		
		pdp.getCancelButton().addActionListener(event->{
			if (event.getActionCommand().equals("cancel")) {
				showProfiles();
			}
		});
		update();
	}
	void showProfileEditor(Profile p) {
		pageTitle.setText("Edit a Profile");;
		container.removeAll();
		ProfileDetailsPanel pdp = new ProfileDetailsPanel(getPanelHeight() - 20, getPanelHeight() - 20);
		container.add(pdp);
		pdp.loadProfileData(p);
		pdp.getDoneButton().addActionListener(event -> {
			if (event.getActionCommand().equals("done"))
				try {
					pdp.editProfile(p);
					IUserService userService = UserServiceFactory.getService();
					try {
						userService.updateUser(userService.getCurrentUser());
					} catch (UserDoesNotExistException e) {
						JOptionPane.showMessageDialog(null,"Something went wrong!"); //should not be possible, unless the database was changed mid-action.
					}
					showProfiles();
				} catch (ProfileNoNameException e) {
					JOptionPane.showMessageDialog(null, "A Profile must have a name!");
				} catch (TooYoungException e) {
					JOptionPane.showMessageDialog(null, "You are too young! (9+ Only)");
				}

			});
		
		pdp.getCancelButton().addActionListener(event->{
			if (event.getActionCommand().equals("cancel"))
				showProfiles();
		});
		
		update();
	}
	

	
	
}
