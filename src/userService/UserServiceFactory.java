package userService;

import database.CSVUserDB;


/** A flyweight factory responsible for creating and storing IUserService objects.
 * */
public class UserServiceFactory {
	private static IUserService obj;
	
	/** returns a user service. Multiple calls are guaranteed to return the same Object.
	 * 
	 * @return IUserService
	 * */
	public static IUserService getService() {
		if (obj == null)
			obj = new UserService(new CSVUserDB()); //right now, this is hardcoded to use a UserService and a CSVUserDB
		return obj;
	}
}
