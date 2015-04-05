import java.io.Serializable;

import java.util.HashSet;

public class User implements Serializable
{
	private String 				username, password, firstName, lastName, emailAddress;
	private HashSet<String> 	keys;

	public User(String username, String password, String firstName, String lastName, String emailAddress)
	{
		this.username 		= username;
		this.password 		= password;
		this.firstName 		= firstName;
		this.lastName		= lastName;
		this.emailAddress 	= emailAddress;

		keys = new HashSet<String>();
	}

	public User()
	{
		username 		= "";
		password 		= "";
		firstName 		= "";
		lastName		= "";
		emailAddress	= "";
	}

	//Get and set values for instance variables
	public String getUsername(){return username;}
	public String getFirstName(){return firstName;}
	public String getLastName(){return lastName;}
	public String getEmailAddress(){return emailAddress;}

	public void setUsername(String username){this.username = username;}
	public void setPassword(String password){this.password = password;}
	public void setfirstName(String firstName){this.firstName = firstName;}
	public void setlastName(String lastName){this.lastName = lastName;}
	public void setEmailAddress(String emailAddress){this.emailAddress = emailAddress;}

	public boolean authenticate(String password){return this.password.equals(password);}

	public HashSet<String> getKeys(String password)
	{
		return authenticate(password) ? keys : null;
	}		
}
