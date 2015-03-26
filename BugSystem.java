import java.io.Serializable;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class BugSystem implements Serializable
{
	private HashMap<String, User> 	users;
	private HashMap<String, Bug> 		bugs;

	private String 					currentUserID;

	public BugSystem()
	{
		users 	= new HashMap<String,User>();
		bugs 	= new HashMap<String,Bug>();
	}

	public boolean addUser(String username, String password, String firstName, String lastName, String emailAddress)
	{
		if(users.containsKey(username))
			return false;

		users.put(username, new User(username, password, firstName, lastName, emailAddress));
		return true;
	}

	public void addBug(BugStatus status, BugPriority priority, String description)
	{
		String id = UUID.randomUUID().toString();
		bugs.put(id, new Bug(status, priority, description, id));
	}
	
}