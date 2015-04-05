import java.io.Serializable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import java.util.UUID;
import java.util.HashMap;
import java.util.Set;

public class BugReportSystem implements Serializable
{
	private HashMap<String, User> 		users;
	private HashMap<String, Bug> 		bugs;

	private transient String 			currentUserID;
	private File 						file;

	public BugReportSystem()
	{
		users 			= new HashMap<String,User>();
		bugs 			= new HashMap<String,Bug>();
		currentUserID 	= "";
	}

	public BugReportSystem(String directory)
	{
		this();
		file = new File(directory);
		if (!loadFromDisk())
				writeToDisk();
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

	public boolean writeToDisk()
	{
		try
		{
			if (!new File("res").isDirectory())
				new File("res").mkdir();
			System.out.println((file.isFile() ? "Overwriting \"" : "Writing to \"" ) + file.getPath() + "\"...");
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(this);
			System.out.println("Successfully wrote to \"" + file.getPath() + "\"");
			return true;
		}
		catch(Exception e)
		{
			System.err.println("Failed to write to \"" + file.getPath() + "\"");
			return false;
		}
	}

	public boolean loadFromDisk()
	{
		try
		{
			System.out.println("Loading \"" + file.getPath() + "\"...");
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

			BugReportSystem objectIn = (BugReportSystem)ois.readObject();
			users = objectIn.users;
			bugs = objectIn.bugs;

			currentUserID = "";
			System.out.println("Successfully loaded \"" + file.getPath() + "\"");
			return true;
		}
		catch(Exception e)
		{
			System.err.println("Failed to load \"" + file.getPath() + "\"");
			return false;
		}
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

	public boolean isLoggedIn(){return !currentUserID.equals("");}

	public User getUserAccount(String password)
	{
		if (users.get(currentUserID).authenticate(password))
			return users.get(currentUserID);
		return null;
	}
}