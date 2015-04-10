/**
 	Title: 			The "BugStatus" enumeration type
	Date Written: 	March 2015 - April 2015
	Author: 		Samuel Dindyal
	Description: 	A serializable "BugStatus" enumeration type with the intent to represent the status of a bug.
*/

import java.io.Serializable;

public enum BugStatus implements Serializable
{
	//If the status of the bug implies it hasn't been fixed, it's status is "NOT_FIXED" with its numerical representation being 0.
	//If the status of the bug implies it has been fixed, it's status is "FIXED" with its numerical representation being 1.
	NOT_FIXED(0,"The bug has not been fixed."),
	FIXED(1, "The bug has been fixed.");

	//Instance variables
	public final int 		status;
	private final String 	statusDescription;

/**
	Construct a "BugStatus" enumeration type.

	@param 	status 	An integer representation of the status of a bug.
	@param  status 	A string representation of the description of the status.
*/
	BugStatus(int status, String statusDescription)
	{
		this.status 			= status;
		this.statusDescription 	= statusDescription;
	}
}