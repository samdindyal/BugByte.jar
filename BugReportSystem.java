/**
 	Title: 			The "BugReportSystem" class
	Date Written: 	March 2015 - April 2015
	Author: 		Samuel Dindyal
	Description: 	A system containing bugs and users. It defines which users have access to which bugs and contains utilities in managing both of them.
*/

import java.io.Serializable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import java.util.UUID;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;

public class BugReportSystem implements Serializable
{
	//Instance variables
	private HashMap<String, User> 		users;
	private HashMap<String, Bug> 		bugs;

	private transient HashSet<String> 	currentUserIDs;
	private File 						file;

/**
	Create a new "BugReportSystem" object.
*/
	public BugReportSystem()
	{
		//Initialize instance variables
		users 			= new HashMap<String,User>();
		bugs 			= new HashMap<String,Bug>();
		currentUserIDs 	= new HashSet<String>();
	}

/**
	Create a new "BugReportSystem" object in a defined directory.
*/
	public BugReportSystem(String directory)
	{
		//Do everything in the default constructor
		this();

		//Load from a file at "directory"
		file = new File(directory);
		if (!loadFromDisk())
				writeToDisk();
	}

/**
	Add a user to the bug system.

	@param 	username 		A string representation of the username of the new user.
	@param 	password 		A string representation of the password of the new user.
	@param 	firstName 		A string representation of the new user's first name.
	@param 	lastName 		A string representation of the new user's last name.
	@param 	emailAddress 	A string representation of the email address of the new user.

	@return 	A flag indicating the success or failure of adding a new user to the system.
*/
	public boolean addUser(String username, String password, String firstName, String lastName, String emailAddress)
	{
		//If the user exists
		if(users.containsKey(username))
			return false;

		//Add a user to the system
		users.put(username, new User(username, password, firstName, lastName, emailAddress));
		return true;
	}

/**
	Add a bug to the system.

	@param 	status 			A BugStatus type representing the status of the bug (FIXED, NOT_FIXED).
	@param 	priority 		A BugPriority type representing the priority of the bug (BugPriority.LOW, BugPriority.MEDIUM, BugPriority.HIGH).
	@param 	description 	A string representation of the bug's description.
	@param 	name 			A string representation of the user-friendly name of the bug.
	@param 	id 				The ID of the bug.
	@param 	userID 			The ID of the user adding the bug.

	@return 	A flag indicating the success or failure of adding a new bug to the system.
*/
	public boolean addBug(BugStatus status, BugPriority priority, String description, String name, String id, String userID)
	{
		//If the user is not logged in
		if (!isLoggedIn(userID))
			return false;

		//Add the bug to the system
		bugs.put(id, new Bug(status, priority, description, id, name));

		//Give the user a key to the bug
		users.get(userID).addKey(id);
		return true;
	}

/**
	Write the BugReportSystem to a file.

	@return 	A flag indicating the terminating status of writing the system to a file.
*/
	public boolean writeToDisk()
	{
		try
		{
			//Attempt to write object to a file
			System.out.println((file.isFile() ? "Overwriting \"" : "Writing to \"" ) + file.getPath() + "\"...");
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(this);
			System.out.println("Successfully wrote to \"" + file.getPath() + "\"");

			//Close the object output stream
			oos.close();
			return true;
		}
		catch(Exception e)
		{
			System.err.println("Failed to write to \"" + file.getPath() + "\"");
			return false;
		}
	}
/**
	Loads a BugReportSystem from a file.

	@return 	A flag indicating the success or failure of reading from a file.
*/
	public boolean loadFromDisk()
	{
		try
		{
			//Attempt to load a BugReportSystem object from a file
			System.out.println("Loading \"" + file.getPath() + "\"...");
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

			BugReportSystem objectIn = (BugReportSystem)ois.readObject();

			//Close the object input stream
			ois.close();

			//Set the instance variables
			users = objectIn.users;
			bugs  = objectIn.bugs;

			currentUserIDs = new HashSet<String>();
			System.out.println("Successfully loaded \"" + file.getPath() + "\"");
			return true;
		}
		catch(Exception e)
		{
			System.err.println("Failed to load \"" + file.getPath() + "\"");
			return false;
		}
	}

/**
	Attempt to log a user in given his or her username and password.

	@param 	username 	The username of the user trying to log in.
	@param 	password 	The supposed password of the user trying to log in.

	@return 	A flag indicating whether the login was a success or not.
*/
	public boolean login(String username, String password)
	{
		//If the user does not exist
		if (!users.containsKey(username))
		{
			System.out.println("The user \"" + username + "\" does not exist.");
			return false;
		}

		//If the user is currently logged in
		else if (currentUserIDs.contains(username))
		{
			System.out.println("The user \"" + username + "\" is already logged in.");
			return false;
		}

		//If the user is successfully authenticated with the given passowrd
		else if (users.get(username).authenticate(password))
		{
			//Log the user in by adding his or her user ID to the list of currently logged in users
			currentUserIDs.add(username);
			return true;
		}
		else
			return false;
	}

/**
	Attempt to log a user out.

	@param 	username 	The username of the user requesting to log out.

	@return 	A flag indicating the success or failure of the logout.
*/
	public boolean logout(String username)
	{	
		//If the user is not logged in
		if (!currentUserIDs.contains(username))
			return false;
		currentUserIDs.remove(username);
		return true;
	}

/**
	Check if a user is currently logged in

	@param 	username 	The username to check for being logged in or not.

	@return  	A flag indicating if the user is logged in or not.
*/
	public boolean isLoggedIn(String username){return currentUserIDs.contains(username);}

/**
	Check if any users are currently logged into the system.

	@return 	A flag indicating if there are any users who are currently logged in.
*/
	public boolean hasActiveUsers(){return currentUserIDs.size() > 0;}

/**
	Get the object of a user account given the username and password.

	@param 	username 	The username of the account being tried for access.
	@param 	password	The password of the account being tried for access.

	@return 	An object corresponding to the user account being fetched.
*/
	public User getUserAccount(String username, String password)
	{
		//If there is no such user
		if (!users.containsKey(username))
		{
			System.out.println("Sorry, the user \"" + username + "\" does not exist.");
			return null;
		}

		//If the user is currently logged in and authentication is successful
		if (isLoggedIn(username) && users.get(username).authenticate(password))
			return users.get(username);
		return null;
	}

/**
	Get a bug given its ID.

	@param 	id 	The ID of the desired bug.

	@return 	An object corresponding to the desired bug.
*/
	public Bug getBug(String id)
	{
		//If the bug exists
		if (bugs.containsKey(id))
			return bugs.get(id);
		return null;
	}

/**
	Remove a bug from the system.

	@param 	id 	The ID of the bug to be removed.

	@return 	A flag indicating the success of the bug removal.
*/
	public boolean removeBug(String id)
	{
		//If the bug does not exist
		if (!bugs.containsKey(id))
			return false;

		//Remove the bug from the system
		bugs.remove(id);

		//Remove all users' reference to the bug
		for (String userID : users.keySet())
			if (users.get(userID).hasKey(id))
				users.get(userID).removeKey(id);
		return true;
	}

/**
	Get the frequency of bugs created within the past 5 days.
*/
	public ArrayList<Double> getStatistics()
	{
		double sts[] = new double[5];
		Date date = new Date();

		//Search all bugs for a date within the past 5 days
		for (String key : bugs.keySet())
		{
			//If a bug has been created within the past 5 days
			if (date.getDay() - bugs.get(key).getDateCreated().getDay() < 5)
				//Add it to the array in an index representing the date it was created in reference to today
				sts[date.getDay() - bugs.get(key).getDateCreated().getDay()]++;
		}

		//Trasnfer array to an arraylist
		ArrayList<Double> list = new ArrayList<Double>();

		for (Double s : sts)
			list.add(s);

		return list;
	}
}