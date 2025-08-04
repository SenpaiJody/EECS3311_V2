package userService;

/** an Implementation of {@link IUserService} that uses an {@link IUserDB} to store data. This class essentially provides logic to the underlying IUserDB object while implementing the {@link IUserService} interface.
 * <P> this allows the IUserDB to focus on CRUD operations while still being able to fulfil the IUserService interface
 * 
 * */
class UserService implements IUserService{
	
	//"contract" to a user database
	private IUserDB db;

	UserService(IUserDB implementation){
		db = implementation;
	}
	
	private User currentUser;
	private Profile currentProfile;
	
	@Override
	public User getCurrentUser() {
		return currentUser;
	}
	@Override
	public Profile getCurrentProfile() {
		return currentProfile;
	}
	
	@Override
	public void setCurrentProfile(Profile prof) throws ProfileDoesNotExistException
	{
		if(currentUser == null || !currentUser.getProfiles().contains(prof)) throw new ProfileDoesNotExistException();
		currentProfile = prof;
	}
	
	@Override
	public void attemptLogin(String UserID, String password) throws IncorrectLoginException {
		User u = db.getUser(UserID, password);
		if (u == null)
			throw new IncorrectLoginException();
		currentUser = u;
	}

	@Override
	public void updateUser(User u) throws UserDoesNotExistException{
		if (!db.doesUserExist(u.getUserID()))
			throw new UserDoesNotExistException();
		db.updateUser(u);
	}

	//registers a user then logs in as that user
	@Override
	public void registerUser(String userID, String password) throws UserAlreadyExistsException {
		if (db.doesUserExist(userID))
			throw new UserAlreadyExistsException();
		db.registerUser(userID, password);
		try {
			attemptLogin(userID,password);
		} catch (IncorrectLoginException e) { //VERY unlikely exception, only if somehow the database didnt save the recently registered user (probably permissions)
			throw new RuntimeException("Login incorrect for recently created user. This is likely an issue with the database.");
		}
	}
	@Override
	public int generateProfileID() {
		return db.generateProfileID();
	}


	
	
}
