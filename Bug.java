/**
 	Title: 			The "Bug" class
	Date Written: 	March 2015 - April 2015
	Author: 		Samuel Dindyal
	Description: 	A serializable "Bug" object of which its name, ID, status, priority and description customizable.
*/

import java.util.Date;
import java.io.Serializable;

public class Bug implements Serializable
{
	//Instance variables and a constant
	private String			ID, name;
	private BugStatus 		status;
	private String 			description;
	private BugPriority 	priority;
	private Date 			dateModified;

	public static final Date DATE_CREATED = new Date();

/**
	Construct a "Bug" class based on the given arguments.

	@param 	status			An enumeration type representing the status of the bug.
	@param 	priority		An enumeration type representing the priority of the bug.
	@param 	description 	A string representation of the bug's desciption.	 	
	@param 	ID 				A string representation of the ID of the bug.
	@param 	name 			A String representation of the name of the bug.
*/
	public Bug(BugStatus status, BugPriority priority, String description, String ID, String name)
	{
		this.status 		= status;
		this.priority 		= priority;
		this.description 	= description;
		this.ID 			= ID;
		dateModified 		= new Date();
		this.name 			= name;
	}

/**
	The get method for "status" instance variable.

	@return		An enumeration type in correspondance of the current status of the bug (Ex. BugStatus.FIXED).
*/
	public BugStatus getStatus(){return status;}
	
	/**
	The get method for the "desciprtion" instance variable.

	@return		A string representation of the description of the bug.
*/
	public String getDescription(){return description;}
	
/**
	The get method for the "name" instance variable.

	@return		A string representation of the user defined named of the bug.
*/
	public String getName(){return name;}

/**
	The get method for the "ID" instance variable.

	@return		A string representation of the ID number of the bug.
*/
	public String getID(){return ID;}

/**
	The get method for the "priority" instance variable.

	@return		An enumeration type "BugPriority" representing priority of the bug (High, Medium, Low).
*/
	public BugPriority getPriority(){return priority;}

/**
	The get method for the "dateModified" instance variable.

	@return		A date object referring to the date the bug was last modified.
*/
	public Date getDateModifed(){return dateModified;}
	
/**
	The get method for the "DATE_CREATED" constant variable.

	@return		A date object referring to the date the bug object was initially instantiated.
*/	
	public Date getDateCreated(){return DATE_CREATED;}

/**
	The set method for the "status" instance variable.

	@param 	newStatus 		An enumeration type of correspondance to the new status for the bug.
*/
	public void setStatus(BugStatus newStatus)
	{
		status = newStatus;
		dateModified = new Date();
	}

/**
	The set method for the "priority" instance variable.

	@param	newPriority 	An enumeration type of correspondance to the desired priority (Ex. BugPriority.LOW).
*/
	public void setPriority(BugPriority newPriority)
	{
		priority = newPriority;
		dateModified = new Date();
	}

/**
	The set method for the "desciption" instance variable.

	@param 	newDescription 	A string representation of the new description.
*/
	public void setDescription(String newDescription)
	{
		description = newDescription;
		dateModified = new Date();
	}
}