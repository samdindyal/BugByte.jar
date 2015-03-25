import java.util.UUID;

public class Bug
{
	private final UUID		ID = UUID.randomUUID();
	private BugStatus 		status;
	private String 			description;
	private BugPriority 	priority;

	public Bug(BugStatus status, BugPriority priority, String description)
	{
		this.status 		= status;
		this.priority 		= priority;
		this.description 	= description;
	}

	public BugStatus getStatus(){return status;}
	public String getDescription(){return description;}
	public BugPriority getPriority(){return priority;}

	public void setStatus(BugStatus newStatus){status = newStatus;}
	public void setPriority(BugPriority newPriority){priority = newPriority;}
	public void setDescription(String newDescription){description = newDescription;}
}