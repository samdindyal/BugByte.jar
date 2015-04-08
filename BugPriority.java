import java.io.Serializable;

public enum BugPriority implements Serializable
{
	LOW(1,"Low"),
	MEDIUM(2,"Medium"),
	HIGH(3,"High");

	public final int		priority;
	public final String	description;

	BugPriority(int priority, String description)
	{
		this.priority 		= priority;
		this.description 	= description;
	}

}