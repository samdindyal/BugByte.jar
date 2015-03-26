public class Bug
{
	private UUID			ID;
	private BugStatus 		status;
	private String 			description;
	private BugPriority 	priority;

	public Bug(BugStatus status, BugPriority priority, String description, UUID ID)
	{
		this.status 		= status;
		this.priority 		= priority;
		this.description 	= description;
		this.ID 			= ID;
	}

	public BugStatus getStatus(){return status;}
	public String getDescription(){return description;}
	public BugPriority getPriority(){return priority;}

	public void setStatus(BugStatus newStatus){status = newStatus;}
	public void setPriority(BugPriority newPriority){priority = newPriority;}
	public void setDescription(String newDescription){description = newDescription;}
}