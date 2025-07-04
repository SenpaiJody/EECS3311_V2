package userService;

//interface implemented by any class that serves as a "user database". Used by UserService
public interface IUserDB {
	
	//returns whether or not a user exists in the database
	public boolean doesUserExist(String userID);
	
	//creates a User from the database, returns null if no user matches the arguments.
	public User getUser(String UserID, String password);
	
	//when provided a User, updates the user in the database. Assume the provided user exists in the database already.
	public void updateUser(User u);
	
	//register a new User with this userID and password. Assume the userID is already unique.
	public void registerUser(String userID, String password);
	
	//generate a new, unique ProfileID
	public int generateProfileID();
}
