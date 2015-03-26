import java.io.Serializable;

import java.util.HashSet;
import java.util.UUID;

public class User implements Serializable
{
	private String 			username, password, firstName, lastName, emailAddress;
	private HashSet<UUID> 	keys;

	public User(String username, String password, String firstName, String lastName, String emailAddress)
	{
		this.username 		= username;
		this.password 		= password;
		this.firstName 		= firstName;
		this.lastName		= lastName;
		this.emailAddress 	= emailAddress;

		keys = new HashSet<UUID>();
	}

	public User()
	{
		username 		= "";
		password 		= "";
		firstName 		= "";
		lastName		= "";
		emailaddress	= "";
	}
}
