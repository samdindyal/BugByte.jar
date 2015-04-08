import java.io.Serializable;

public enum BugStatus implements Serializable
{
	NOT_FIXED(0,"The bug has not been fixed."),
	FIXED(1, "The bug has been fixed.");

	public final int 		status;
	private final String 	statusDescription;

	BugStatus(int status, String statusDescription)
	{
		this.status 			= status;
		this.statusDescription 	= statusDescription;
	}
}