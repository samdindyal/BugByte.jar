import java.io.Serializable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import java.util.UUID;
import java.util.HashMap;
import java.util.Set;

public class BugSystem implements Serializable
{
	private HashMap<String, User> 		users;
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

	public void writeToDisk(String dir) throws Exception
	{

		File file = new File(dir);
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
		oos.writeObject(this);
	}

	public void loadFromDisk(String dir) throws Exception
	{
		File file = new File(dir);
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

		BugSystem objectIn = (BugSystem)ois.readObject();
		users = objectIn.users;
		bugs = objectIn.bugs;

		currentUserID = objectIn.currentUserID;
	}

	public boolean login(String username, String password)
	{
		if (!users.containsKey(username))
			return false;

		else if (users.get(username).authenticate(password))
		{
			currentUserID = username;
			return true;
		}
		else
			return false;
	}

	public boolean logout()
	{	
		if (currentUserID.equals(""))
			return false;
		
		currentUserID = "";
		return true;
	}
	
}