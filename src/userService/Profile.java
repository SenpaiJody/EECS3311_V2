	package userService;

import java.time.LocalDate;

//a profile represents a set of data that describes a person. A user can have multiple profiles.
public class Profile {
	
	private Integer profileID;
	
	private Gender gender = Gender.MALE;
	private String name = "";
	private LocalDate dateOfBirth = null;
	private double weight;
	private double height;
	private Unit preferredUnit = Unit.METRIC;
	
	public Profile(String name, Gender gender, LocalDate DoB, double height, double weight, Unit preferredUnit) {
		this(UserServiceFactory.getService().generateProfileID(), name, gender, DoB, height, weight, preferredUnit);
	};
	
	public Profile(int id, String name, Gender gender, LocalDate DoB, double height, double weight, Unit preferredUnit) {
		this.profileID = id;
		this.name = name;
		this.gender = gender;
		this.dateOfBirth = DoB;
		this.height = height;
		this.weight = weight;
		this.preferredUnit = preferredUnit;
	};
	
	//setters
	public void setGender(Gender g) {gender = g;}
	public void setName(String n) {name = n;}
	public void setDateOfBirth(LocalDate d) {dateOfBirth = d;}
	public void setHeight(double h) {height = h;}
	public void setWeight(double w) {weight = w;}
	public void setPreferredUnit(Unit preferredUnit) {this.preferredUnit = preferredUnit;}
	

	//getters
	public int getID() {return profileID;}
	public Gender getGender() {return gender;}
	public String getName() {return name;}
	public LocalDate getDateOfBirth() {return dateOfBirth;}
	public double getHeight() {return height;}
	public double getWeight() {return weight;}
	public Unit getPreferredUnit() {return preferredUnit;}
	
	public enum Gender{MALE, FEMALE}
	public enum Unit{METRIC, IMPERIAL}
	
	public String toString() {
		return String.format("ID: %d\nGender: %s\nName: %s\nDoB: %s\nHeight: %.2fcm\nWeight: %.2fkg", getID(), getGender().toString(),getName(), getDateOfBirth().toString(), getHeight(),getWeight());
	}
	
}
