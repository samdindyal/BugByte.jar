public enum BugPriority
{
	LOW(1,"Low"),
	MEDIUM(2,"Medium"),
	HIGH(3,"High");

	private final int		priority;
	private final String	description;

	BugPriority(int priority, String description)
	{
		this.priority 		= priority;
		this.description 	= description;
	}

}