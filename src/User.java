/**
 	Title: 			The "User" class
	Date Written: 	March 2015 - April 2015
	Author: 		Samuel Dindyal
	Description: 	A serializable "User" object of which its username, password, first name, last name and email address are customizable.
*/

import java.io.Serializable;

import java.util.LinkedList;

public class User implements Serializable
{
	//Instance variables
	private String 				username, password, firstName, lastName, emailAddress;
	private LinkedList<String> 	keyRing;

/**
	Construct a "User" object.

	@param 	username 	The username of the new user.
	@param 	password 	The password of the new user.
	@param  firstName 	The first name of the new user.
	@param 	lastName 	The last name of the user to be created.
*/
	public User(String username, String password, String firstName, String lastName, String emailAddress)
	{
		//Set instance variables
		this.username 		= username;
		this.password 		= password;
		this.firstName 		= firstName;
		this.lastName		= lastName;
		this.emailAddress 	= emailAddress;

		keyRing = new LinkedList<String>();
	}

	public User()
	{
		//Initialize instance variables
		username 		= "";
		password 		= "";
		firstName 		= "";
		lastName		= "";
		emailAddress	= "";
	}

/**
	The get method for the "username" instance variable.

	@return 	The username of the user.
*/
	public String getUsername(){return username;}

/**
	The get method for the "firstName" instance variable.

	@return 	The first name of the user.
*/
	public String getFirstName(){return firstName;}
	
/**
	The get method for the "lastName" instance variable.

	@return 	The last name of the user.
*/
	public String getLastName(){return lastName;}
	
/**
	The get method for the "emailAddress" instance variable.

	@return 	The email address of the user.
*/
	public String getEmailAddress(){return emailAddress;}

/**
	The set method for the "username" instance variable.

	@param 	username 	The new username.
*/
	public void setUsername(String username){this.username = username;}
	
/**
	The set method for the "password" instance variable.

	@param 	password 	The new password.
*/
	public void setPassword(String password){this.password = password;}
	
/**
	The set method for the "firstName" instance variable.

	@param 	firstName 	The new first name.
*/
	public void setfirstName(String firstName){this.firstName = firstName;}
	
/**
	The set method for the "lastName" instance variable.

	@param 	lastName 	The new last name.
*/
	public void setlastName(String lastName){this.lastName = lastName;}
	
/**
	The set method for the "emailAddress" instance variable.

	@param 	The new email address.
*/
	public void setEmailAddress(String emailAddress){this.emailAddress = emailAddress;}

/**
	Authenticate an entered password with this user account.

	@param 	password 	The password being used in an attempt for authentication.
*/
	public boolean authenticate(String password){return this.password.equals(password);}

/**
	Get method for the "keyRing" instance variable.

	@param 	password 	Password for protection of keyRing.
*/
	public LinkedList<String> getKeys(String password){return authenticate(password) ? keyRing : null;}		

/**
	Add a key to the user's keyRing.

	@param 	key 	The new key.
*/
	public boolean addKey(String key)
	{
		//If the user already has this key
		if (keyRing.contains(key))
			return false;

		//Add the key to the user's key ring
		keyRing.add(key);
		return true;
	}

/**
	Remove a key from the user's set of keyRing.

	@param 	key 	The key to remove.
*/
	public boolean removeKey(String key)
	{
		//If the user doesn't have that key
		if (!keyRing.contains(key))
			return false;

		//Remove the key from the user's key ring
		keyRing.remove(key);
		return true;
	}
	
/**
	Check if a user has a key.

	@param 	key 	The key to search for.
*/
	public boolean hasKey(String key){return keyRing.contains(key);}
}
