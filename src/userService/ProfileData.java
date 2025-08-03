package userService;

import java.time.LocalDate;

/** A Parameter Object describing non-identifying data of a Profile; 
 * */
public class ProfileData {
	
	private LocalDate dateOfBirth;
	private Profile.Gender gender;
	private double height;
	private double weight;
	
	public ProfileData(Profile.Gender gender, LocalDate dateOfBirth, double height, double weight) {
		this.gender=gender;
		this.dateOfBirth=dateOfBirth;
		this.height= height;
		this.weight=weight;
	}
	
	
	//getters
	public LocalDate getDateOfBirth() { return dateOfBirth;}
	public Profile.Gender getGender() {return gender;}
	public double getHeight() {return height;}
	public double getWeight() {return weight;}
}
