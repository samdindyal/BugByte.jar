import java.util.Date;
import java.io.Serializable;

public class Bug implements Serializable
{
	private String			ID, name;
	private BugStatus 		status;
	private String 			description;
	private BugPriority 	priority;
	private Date 			dateModified;

	public static final Date DATE_CREATED = new Date();


	public Bug(BugStatus status, BugPriority priority, String description, String ID, String name)
	{
		this.status 		= status;
		this.priority 		= priority;
		this.description 	= description;
		this.ID 			= ID;
		dateModified 		= new Date();
		this.name 			= name;
	}

	public BugStatus getStatus(){return status;}
	public String getDescription(){return description;}
	public String getName(){return name;}
	public String getID(){return ID;}
	public BugPriority getPriority(){return priority;}
	public Date getDateModifed(){return dateModified;}
	public Date getDateCreated(){return DATE_CREATED;}

	public void setStatus(BugStatus newStatus)
	{
		status = newStatus;
		dateModified = new Date();
	}
	public void setPriority(BugPriority newPriority)
	{
		priority = newPriority;
		dateModified = new Date();
	}
	public void setDescription(String newDescription)
	{
		description = newDescription;
		dateModified = new Date();
	}
}