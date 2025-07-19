package userService;

/** An interface that describes the all services that have to do with Users (and profiles)
 * */
public interface IUserService {
	/**returns the "current" user.
	* @return
	* The user either most recently created or most recently logged into with a sucessful call of {@link #attemptLogin(String, String)}
	*/
	public User getCurrentUser();
	
	/**returns the current profile
	 * @return
	 * the profile most recently selected by setCurrentProfile
	 * */
	public Profile getCurrentProfile();
	
	/**Sets the current profile to the provided profile object.
	 * 
	 * @throws ProfileDoesNotExistException if the profile does not exist
	 * */
	public void setCurrentProfile(Profile prof) throws ProfileDoesNotExistException;
	
	/**sets the current user to the user with the associated UserID and Password iff the UserID and password are correct for some user.
	 * @param UserID - user ID to log in with
	 * @param password - password to log in with
	 * @throws IncorrectLoginException if the UserID and Password do not match any user
	 * */
	public void attemptLogin(String UserID, String password) throws IncorrectLoginException;
	
	/** Updates the provided user in the database.
	 * 
	 * @throws UserDoesNotExistException if the user does not exist.
	 * */
	public void updateUser(User u) throws UserDoesNotExistException;
	
	/** Creates a new user in the database and sets the current user to it.
	 * @throws UserAlreadyExistsException if the user already exists
	 * */
	public void registerUser(String userID, String password) throws UserAlreadyExistsException;
	
	/** generates a profile ID that is guaranteed to be unique
	 * @returns a profile ID that is not associated with any existing user.
	 * */
	public int generateProfileID();
}
