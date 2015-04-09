/**
 	Title: 			The "BugPriority" enumeration type
	Date Written: 	March 2015 - April 2015
	Author: 		Samuel Dindyal
	Description: 	A serializable "BugPriority" enumeration type which is intended to represent the priority of a bug.
*/

import java.io.Serializable;

public enum BugPriority implements Serializable
{
	//Low is represented by 0
	//Medium is represented by 1
	//High is represented by 2
	LOW(0,"Low"),		
	MEDIUM(1,"Medium"),
	HIGH(2,"High");

	//Instance variables
	public final int		priority;
	public final String	description;

/**
	Constructs a "BugPriority" enumeration type.

	@param 	priority 		An integer representation of the priority of a bug.
	@param  description 	A string representation of the description of the priority (Low, Medium, High).
*/
	BugPriority(int priority, String description)
	{
		this.priority 		= priority;
		this.description 	= description;
	}

}