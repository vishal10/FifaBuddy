package Data;

/**
 * This class represents a user and stores all information about the user such
 * as user-id, first name and last name.
 * 
 * @author Vishal
 * 
 */
public class User {
	private String name;
	private String email;
	private String password;

	public User(String n, String e, String p) {
		name = n;
		email = e;
		password = p;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

}
