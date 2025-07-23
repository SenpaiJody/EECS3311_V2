package userService;

import java.util.List;
import java.util.TimeZone;

//class representing a "user". This is separate from a "profile". A user can have many profiles; each profile can only be assigned to one user.
public class User {
	
	private String userID = new String();
	private List<Profile> profiles;

	public User(String userID, List<Profile> profiles) {
		this.userID = userID;
		this.profiles = profiles;
	}
	
	//gets the user's ID
	public String getUserID() { return userID;}
	
	//gets a list of the user's profiles
	public List<Profile> getProfiles() {return profiles;}
	
}
