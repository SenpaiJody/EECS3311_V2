package userService;

//represents a concrete implementation of IUserService implementing the common logic that has to do with the Users and Profiles
class UserService implements IUserService{
	
	//"contract" to a user database
	private IUserDB db;

	UserService(IUserDB implementation){
		db = implementation;
	}
	
	User currentUser;
	Profile currentProfile;
	
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

	@Override
	public void registerUser(String userID, String password) throws UserAlreadyExistsException {
		if (db.doesUserExist(userID))
			throw new UserAlreadyExistsException();
		db.registerUser(userID, password);
	}
	@Override
	public int generateProfileID() {
		return db.generateProfileID();
	}


	
	
}
