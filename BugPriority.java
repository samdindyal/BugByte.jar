import java.io.Serializable;

public enum BugPriority implements Serializable
{
	LOW(0,"Low"),
	MEDIUM(1,"Medium"),
	HIGH(2,"High");

	public final int		priority;
	public final String	description;

	BugPriority(int priority, String description)
	{
		this.priority 		= priority;
		this.description 	= description;
	}

}