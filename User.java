import java.io.Serializable;

public class User implements Serializable
{
	private String username, password;

	public User(String username, String password)
	{
		this.username = username;
		this.password = password;
	}

	public User()
	{
		username = "";
		password = "";
	}
}
