package userService;

//interface for everything that has to deal with users and profiles
public interface IUserService {
	//returns the "current" user. The "current" user is the user either most recently created or most recently logged into with a successful call of attemptLogin(...)
	public User getCurrentUser();
	
	//returns the "current" profile. The "current" profile is the profile either most recently created or most recently selected by setCurrentProfile()
	public Profile getCurrentProfile();
	
	//sets the current user; throws an error if the profile does not exist.
	public void setCurrentProfile(Profile prof) throws ProfileDoesNotExistException;
	
	//"logs in" and retrieves a user when provided a UserID and password; throws an exception if the login details are incorrect.
	public void attemptLogin(String UserID, String password) throws IncorrectLoginException;
	
	//when provided a User, updates the user in the database. throws an exception if the user does not exist.
	public void updateUser(User u) throws UserDoesNotExistException;
	
	//register a new User with this userID and password. throws an exception if the user already exists.
	public void registerUser(String userID, String password) throws UserAlreadyExistsException;
	
	//generates a unique profile ID;
	public int generateProfileID();
}
