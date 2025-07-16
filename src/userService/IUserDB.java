package userService;

/* interface implemented by any class that serves as a "user database". Used by UserService 
 * */
public interface IUserDB {
	

	/** returns whether or not a user exists in the database
	 *  @param userID - user to check for
	 *  @returns true if the user exists in the DB, false otherwise
	 * */
	public boolean doesUserExist(String userID);
	

	/** creates a User from the database
	 *  @param userID - user to check for
	 *  @param password- password
	 *  
	 *  @returns the User matching the userID and password, or null if this user does not exist
	 * */
	public User getUser(String UserID, String password);
		
	/** updates a User in the database. Assume the provided user exists in the database already.
	 *  @param user - a user that exists in the database.
	 * */
	public void updateUser(User user);
	
	/** registers a User in the database. Assume the provided user ID is unique
	 *  @param userID - a unique user ID
	 *  @param password - a password
	 * */
	public void registerUser(String userID, String password);
	
	/**generate a new, unique ProfileID
	 * @return
	 * a unique profile ID not associated with any existing profiles*/
	public int generateProfileID();
}
