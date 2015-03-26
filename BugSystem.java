import java.io.Serializable;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class BugSystem implements Serializable
{
	private HashMap<String, User> 	users;
	private HashMap<UUID, Bug> 		bugs;

	public BugSystem()
	{
		users 	= new HashMap<String,User>();
		bugs 	= new HashMap<UUID, Bug>();
	}

	public boolean addUser(String username, String password)
	{
		if(users.containsKey(username))
			return false;

		users.put(username, new User(username, password));
		return true;
	}

	public void addBug(BugStatus status, BugPriority priority, String description)
	{
		UUID id = UUID.randomUUID();
		bugs.put(id, new Bug(status, priority, description, id));
	}
	
}