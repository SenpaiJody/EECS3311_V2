package unitTests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import userService.IUserService;
import userService.IncorrectLoginException;
import userService.Profile;
import userService.Profile.Gender;
import userService.Profile.Unit;
import userService.ProfileData;
import userService.ProfileDoesNotExistException;
import userService.UserAlreadyExistsException;
import userService.UserDoesNotExistException;
import userService.UserServiceFactory;

class Test_UserService {

	/** Testing the IUserService interface
	 * 
	 * */
	@Test
	void wrongLogin() {	
		IUserService service = UserServiceFactory.getService();
		assertNotNull(service);

		try { //wrong login; expected exception
			service.attemptLogin("wrong", "login");
			fail("Wrong login did not throw exception");
		}
		catch(IncorrectLoginException e) {}
	}
	
	@Test
	void correctLogin() {
		IUserService service = UserServiceFactory.getService();
		assertNotNull(service);
		try { //correct login
			service.attemptLogin("bobtest3", "mypassword");
		}
		catch(IncorrectLoginException e) {
			fail("Correct login threw exception");
		}
		
//		assertNull(service.getCurrentProfile()); //a profile has not been selected yet
//		assertNotNull(service.getCurrentUser()); //however, a user should have been selected since it was logged in
		assertTrue(service.getCurrentUser().getUserID().equals("bobtest3")); //and the user that was selected should be bobtest3 since we logged in as him
		
		
	}
	
	@Test
	void registerKnownUser() {
		IUserService service = UserServiceFactory.getService();
		assertNotNull(service);
		
		try { //correct login
			service.registerUser("bobtest3", "mypassword");
			fail("trying to register as an existing user didnt throw an exception");
		}
		catch(UserAlreadyExistsException e) {
		}
	}
	
	
	@Test
	void selectProfile(){
		IUserService service = UserServiceFactory.getService();
		assertNotNull(service);
		try { //correct login
			service.attemptLogin("bobtest3", "mypassword");
		}
		catch(IncorrectLoginException e) {
			fail("Correct login threw exception");
		}
		
		assertNull(service.getCurrentProfile()); //a profile has not been selected yet
		assertNotNull(service.getCurrentUser()); //however, a user should have been selected since it was logged in
		assertTrue(service.getCurrentUser().getUserID().equals("bobtest3")); //and the user that was selected should be bobtest3 since we logged in as him
		
		
		var profiles = service.getCurrentUser().getProfiles();
		assertNotNull(profiles);
		if (profiles.size() > 0) {
			
			var profileToLoginWith = profiles.getFirst();
			
			if (!service.getCurrentUser().getProfiles().contains(profileToLoginWith))
				fail("Profile does not exist in user's list");
			
			try {
				service.setCurrentProfile(profiles.getFirst());
			} catch (ProfileDoesNotExistException e) {
				fail("Profile does not exist in user's list");
			}
			
			assertNotNull(service.getCurrentProfile());
			assertEquals(profileToLoginWith, service.getCurrentProfile());
		}
		else
			assertNull(service.getCurrentProfile());
	}
	
	@Test 
	void createNewProfile(){
		IUserService service = UserServiceFactory.getService();
		assertNotNull(service);
		try { //correct login
			service.attemptLogin("bobtest3", "mypassword");
		}
		catch(IncorrectLoginException e) {
			fail("Correct login threw exception");
		}
		
		int oldAmountOfProfiles = service.getCurrentUser().getProfiles().size();
		
		ProfileData bobsWifeStats = new ProfileData(Gender.FEMALE, LocalDate.of(1980, 2, 12), 150, 70);
		Profile toBeAdded = new Profile(service.generateProfileID(), "Bob's Wife", bobsWifeStats, Unit.METRIC);
		
		var BobsProfiles = service.getCurrentUser().getProfiles();
		BobsProfiles.add(toBeAdded);
		try {
			service.updateUser(service.getCurrentUser());
		} catch (UserDoesNotExistException e) {
			fail("somehow, bob does not exist (database issue)");
		}
		
		
		int newAmountOfProfiles = service.getCurrentUser().getProfiles().size();
		assertTrue(newAmountOfProfiles == oldAmountOfProfiles + 1);
	}
}
