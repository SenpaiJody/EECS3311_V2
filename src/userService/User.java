package userService;

import java.util.List;
import java.util.TimeZone;

//class representing a "user". This is separate from a "profile". A user can have many profiles; each profile can only be assigned to one user.
public class User {
	
	private String userID = new String();
	private List<Profile> profiles;
	private Settings settings = new Settings();
	
	public User(String userID, List<Profile> profiles) {
		this.userID = userID;
		this.profiles = profiles;
	}
	
	//gets the user's ID
	public String getUserID() { return userID;}
	
	//gets a list of the user's profiles
	public List<Profile> getProfiles() {return profiles;}
	
	//gets the user's settings
	public Settings getSettings() {return settings;}
	
	
	//currently unused for deliverable 1. TODO: finish implementing this
	class Settings{
		private Unit unit = Unit.METRIC;
		private TimeZone timeZone = TimeZone.getTimeZone("CST");
	
		//I skipped "Language" since right now we don't have support for that -Jody
		
		public Unit getPreferredUnit() {return unit;}
		public TimeZone getTimeZone() {return timeZone;}
		
		public void setPreferredUnit(Unit u) {unit = u;}
		public void setTimeZone(TimeZone tz) {timeZone = tz;}
		
		
		public enum Unit{METRIC, IMPERIAL}
	}
}
