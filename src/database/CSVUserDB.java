package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import userService.IUserDB;
import userService.Profile;
import userService.User;
import userService.Profile.Gender;
import userService.Profile.Unit;

public class CSVUserDB implements IUserDB{
	private final String user_pwd_csv = "data/csv/user_pwd.csv";
	private final String user_profile_csv = "data/csv/user_profiles.csv";
	private final String profile_data_csv = "data/csv/profile_data.csv";
	private final String unique_profileID_csv = "data/csv/unique_profileID.csv";
	
	private final Profile.Gender[] gender_lookup = {Gender.MALE,Gender.FEMALE};
	private final Profile.Unit[] unit_lookup = {Unit.METRIC, Unit.IMPERIAL};
	
	private String formatProfile(Profile p) {
		return String.format("%d,%d,%s,%s,%.2f,%.2f,%d", 
				p.getID(), 
				p.getGender().ordinal(), 
				p.getName(),
				String.format("%d-%d-%d", p.getDateOfBirth().getYear(),p.getDateOfBirth().getMonthValue(), p.getDateOfBirth().getDayOfMonth()),
				p.getHeight(),
				p.getWeight(),
				p.getPreferredUnit() == Unit.METRIC ? 0 : 1
				);
	}
	
	private void updateProfiles(List<Profile> profiles) {
		List<Profile> temp = new ArrayList<Profile>(profiles.size());
		
		for (int i =0 ; i <profiles.size(); i++) {
			temp.add(profiles.get(i));
		}
		
		StringBuilder sb = new StringBuilder();		
		
		for (Profile p : temp) {
			sb.append(formatProfile(p) + "\n");
		}
		
		CSVDatabaseUtilities.readAndExecute(profile_data_csv, (String line)->{
			boolean foundMatch = false;
			for (int i = 0; i < temp.size(); i++) {
				if (line.split(",")[0].equals(String.format("%d",temp.get(i).getID()))) {
					foundMatch = true;
					temp.remove(i);
					break;
				}
			}
			if (!foundMatch) {
				sb.append(line);
				sb.append('\n');
			}
			return true;
		});
		

		
		

		
		PrintWriter pw = CSVDatabaseUtilities.createPrintWriter(profile_data_csv);
		pw.print(sb.toString());
		pw.close();
		
	}
	

	private List<Profile> getProfiles(String userID){
		List<String> ids = new ArrayList<String>();
		CSVDatabaseUtilities.readAndExecute(user_profile_csv, (String line)->{
			String[] elements = line.split(",");
			if (elements[0].equals(userID))
				ids.add(elements[1]);
			return true;
		});

		List<Profile> profiles = new ArrayList<Profile>();
		CSVDatabaseUtilities.readAndExecute(profile_data_csv, (String line)->{
			String[] elements = line.split(",");
			for (int i = 0; i < ids.size(); i++) {
				if (elements[0].equals(ids.get(i))) {
					
					
					String[] dateString = elements[3].split("-");
					
					int profileID = Integer.parseInt(elements[0]);		
					Profile.Gender gender = gender_lookup[Integer.parseInt(elements[1])];
					String name = elements[2];
					double height = Double.parseDouble(elements[4]);
					double weight = Double.parseDouble(elements[5]);
					Profile.Unit unit = unit_lookup[Integer.parseInt(elements[6])];
					
					LocalDate dateOfBirth = LocalDate.of(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]), Integer.parseInt(dateString[2]));
					
 					profiles.add(new Profile(profileID, name, gender ,dateOfBirth,height,weight, unit));
 					
 					ids.remove(i);
					break;
				}
			}
			return ids.size()>0;
		});
		
		return profiles;
	};
	
	@Override
	public boolean doesUserExist(String userID) {
		try (BufferedReader br = new BufferedReader(new FileReader(user_pwd_csv))) {
			String line;
		    while ((line = br.readLine()) != null) {
		        if (userID.equals(line.split(",")[0])) {
		        	br.close();
		        	return true;
		        }        
		    }
		    br.close();
		}
		catch(IOException e) {
			throw new RuntimeException(user_pwd_csv + " not found.");
		}
		return false;
	}

	@Override
	public User getUser(String userID, String password) {
		
		try (BufferedReader br = new BufferedReader(new FileReader(user_pwd_csv))) {
			String line;
		    while ((line = br.readLine()) != null) {
		    	String[] elements = line.split(",");
				if (elements[0].equals(userID) && elements[1].equals(password)) {
					return new User(userID, getProfiles(userID));
				}
		    }
		    br.close();
		}
		catch(IOException e) {
			throw new RuntimeException(user_pwd_csv + " not found.");
		}
		return null;
	}

	@Override
	public void updateUser(User u) {
		updateProfiles(u.getProfiles());
		StringBuilder sb = CSVDatabaseUtilities.copyContent(user_profile_csv, (String line)->{
			String[] elements = line.split(",");
			return !elements[0].equals(u.getUserID());
		});
		for (Profile p : u.getProfiles()) {
			sb.append(String.format("%s,%d\n",u.getUserID(),p.getID()));
		}
		
		PrintWriter pw = CSVDatabaseUtilities.createPrintWriter(user_profile_csv);
		pw.print(sb.toString());
		pw.close();
		
	}
	@Override
	public void registerUser(String userID, String password) {	
		StringBuilder sb = CSVDatabaseUtilities.copyContent(user_pwd_csv, (String s)->true);
		PrintWriter pw = CSVDatabaseUtilities.createPrintWriter(user_pwd_csv);
		sb.append(userID + "," + password);
		pw.print(sb.toString());
		pw.close();
	}

	@Override
	public int generateProfileID() {
		StringBuilder sb = CSVDatabaseUtilities.copyContent(unique_profileID_csv, (String s)->true);
		PrintWriter pw = CSVDatabaseUtilities.createPrintWriter(unique_profileID_csv);
		int generated = Integer.parseInt(sb.toString().strip())+1;
		pw.print(generated);
		pw.close();
		return generated;
	}

}
