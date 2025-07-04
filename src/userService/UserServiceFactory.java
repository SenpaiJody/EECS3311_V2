package userService;

import database.CSVDatabase;


//Flyweight Factory class that creates IUserService to be used by clients.
//Implements the singleton pattern; it is also responsible for deciding which implementation should be used
public class UserServiceFactory {
	private static UserService obj;
	
	public static IUserService getService() {
		if (obj == null)
			obj = new UserService(new CSVDatabase());
		return obj;
	}
}
