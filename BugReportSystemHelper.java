import java.lang.StringBuffer;
import java.lang.Math;

import java.util.ArrayList;

/**
 * This class acts as a helper for the tester, containing the various cases that
 * will be tested including logging-in, seeing what users are logged in, adding
 * users, adding bugs, changing bugs, removing bugs, and logging out.
 *
 * @author Rishabh Kalra
 */
public class BugReportSystemHelper {

	private ArrayList<String> usrnmList, pwdList, bidList;
	private ArrayList<User> usrList;

	private BugReportSystem bsr;
	private String usr, pwd, fname, lname, email;
	
	private BugStatus bs = BugStatus.NOT_FIXED;
	private BugPriority bp = BugPriority.LOW;
	private String des, name, id, usrid;

	/*
	 * Constructor
	 * initializes all instance variables
	 *
	 * @param	none
	 */
	public BugReportSystemHelper() {
		bsr = new BugReportSystem();
		usrnmList = new ArrayList<String>();
		pwdList = new ArrayList<String>();
		bidList = new ArrayList<String>();
		usrList = new ArrayList<User>();
		usr = "";
		pwd = "";
		fname = "";
		lname = "";
		email = "";
		des = "";
		name = "";
		id = "";
		usrid = "";
	}

	/*
	 * Adds dummy users to test addUser method
	 *
	 * @param	none
	 */
	public void testAddUser() {
		boolean b = true;
		int failCount = 0;

		for(int i=0;i<10;i++) {
			setUserData();
			b = bsr.addUser(usr, pwd, fname, lname, email);
			usrnmList.add(usr);
			pwdList.add(pwd);
			usrList.add(new User(usr, pwd, fname, lname, email));
			System.out.println("");
			if(!b) {
				System.out.println("User: " + usr + ", with password: " + pwd + ", with first name: " + fname + ", with last name: " + ", and email: " + email + " was not added.");
				failCount++;
			}
			System.out.println("User: " + usr + ", with password: " + pwd + ", with first name: " + fname + ", with last name: " + ", and email: " + email + " was added.");
		}
		if(failCount != 0) {
			System.out.println("User Creation Test Finished with errors.");
		} else {
			System.out.println("User Creation Test Succeeded.");

			System.out.println("\n\nTesting alternate case.");
			b = bsr.addUser(usr, pwd, fname, lname, email);
			if(!b) {
				System.out.println("User: " + usr + ", with password: " + pwd + ", with first name: " + fname + ", with last name: " + ", and email: " + email + " was not added.");
			}
		}
	}

	/*
	 * Adds dummy bugs to test addBug method
	 *
	 * @param	none
	 */
	public void testAddBug() {
		boolean b = true;
		int failCount = 0;

		for(int i=0;i<10;i++) {
			setBugData();
			b = bsr.addBug(bs, bp, des, name, id, usrnmList.get(i));
			bidList.add(id);
			System.out.println("");
			if(!b) {
				System.out.println("Bug with status: NOT_FIXED, priority: LOW, name: " + name + ", id: " + id + ", and user: " + usrnmList.get(i) + " was not added.");
				failCount++;
			}
			System.out.println("Bug with status: NOT_FIXED, priority: LOW, name: " + name + ", id: " + id + ", and user: " + usrnmList.get(i) + " was added.");
		}
		if(failCount != 0) {
			System.out.println("Adding Bug Test Completed with errors.");
		} else {
			System.out.println("Adding Bug Test Succeeded.");

			System.out.println("\n\nTesting alternate case.");
			b = bsr.addBug(bs, bp, des, name, id, usrnmList.get(9));
			if(!b) {
				System.out.println("Bug with status: NOT_FIXED, priority: LOW, name: " + name + ", id: " + id + ", and user: " + usrnmList.get(9) + " was not added.");
			}
		}
	}

	/*
	 * Logs dummy users in to test login method
	 *
	 * @param	none
	 */
	public void testLogin() {
		String temp_u = "", temp_p = "";
		boolean b = false;
		int failCount = 0;

		for(int i=0;i<usrnmList.size();i++) {
			temp_u = usrnmList.get(i);
			temp_p = pwdList.get(i);
			b = bsr.login(temp_u, temp_p);
			System.out.println("");
			if(!b) { 
				System.out.println("User: " + temp_u + ", with password: " + temp_p + " was not logged in.");
				failCount++;
			}
			System.out.println("User: " + temp_u + ", with password: " + temp_p + " was logged in.");
		}

		if(failCount != 0) {
			System.out.println("User Login Test Finished with errors.");
		} else {
			System.out.println("User Login Test Succeeded.");

			System.out.println("\n\nTesting alternate case.");
			b = bsr.login(temp_u, temp_p);
			if(!b) { 
				System.out.println("User: " + temp_u + ", with password: " + temp_p + " was not logged in.");
			}
		}
	}

	/*
	 * Checks which dummy users logged in to test isLoggedIn method
	 *
	 * @param	none
	 */
	public void testLoggedIn() {
		boolean b = false;
		int failCount = 0;
		String temp = "";

		for(int i=0;i<usrList.size();i++) {
			b = bsr.isLoggedIn(usrnmList.get(i));
			System.out.println("");
			if(!b) {
				System.out.println("User: " + usrnmList.get(i) + " is not logged in.");
				failCount++;
			}
			System.out.println("User: " + usrnmList.get(i) + " is logged in.");
		}
		if(failCount != 0) {
			System.out.println("User Logged-In Test Finshed with errors.");
		} else {
			System.out.println("User Logged-In Test Succeeded.");

			System.out.println("\n\nTesting alternate case.");
			temp = generateString();
			b = bsr.isLoggedIn(temp);
			if(!b) {
				System.out.println("User: " + temp + " is not logged in.");
			}
		}
	}

	/*
	 * Logs dummy users out to test logout method
	 *
	 * @param	none
	 */
	public void testLogout() {
		String temp_u = "";
		boolean b = false;
		int failCount = 0;

		for(int i=0;i<usrnmList.size();i++) {
			temp_u = usrnmList.get(i);
			b = bsr.logout(temp_u);
			System.out.println("");
			if(!b) {
				System.out.println("User: " + temp_u + " not logged in.");
				failCount++;
			}
			System.out.println("User: " + temp_u + " logged out.");
		}

		if(failCount != 0) {
			System.out.println("User Logout Test Finishd with errors.");
		} else {
			System.out.println("User Logout Test Succeeded.");

			System.out.println("\n\nTesting alternate case.");
			b = bsr.logout(temp_u);
			if(!b) {
				System.out.println("User: " + temp_u + " not logged in.");
			}
		}
	}

	/*
	 * Picks a random dummy bug and changes description to dummy data to test
	 * changing bug data
	 *
	 * @param void
	 */
	public void testChangeBug() {
		Bug bug;
		boolean b = false;
		int bindex = (int) Math.random() * bidList.size();
		int uindex = 0;

		for(int i=0;i<usrList.size();i++) {
			if(usrList.get(i).hasKey(bidList.get(bindex)))
				uindex = i;
		}
		bug = bsr.getBug(bidList.get(bindex));
		System.out.println("\n\nBug before change: ");
		System.out.println("Bug status: NOT_FIXED, bug priority: LOW, bug description: " + bug.getDescription() + ", bug name: " + bug.getName() + ", bug id: " + bidList.get(bindex));
		bug.setDescription(generateString());
		b = bsr.addBug(bug.getStatus(), bug.getPriority(), bug.getDescription(), bug.getName(), bidList.get(bindex), usrnmList.get(uindex));
		if(!b) {
			System.out.println("Changing Bug Test Succeeded.");
			System.out.println("\n\nBug after change: ");
			System.out.println("Bug status: NOT_FIXED, bug priority: LOW, bug description: " + bug.getDescription() + ", bug name: " + bug.getName() + ", bug id: " + bidList.get(bindex));

			System.out.println("\n\nTesting alternate case.");
			setBugData();
			b = bsr.addBug(bs, bp, des, name, id, usrnmList.get(uindex));
			if(b) {
				System.out.println("Bug does not exist, added new bug instead of changing.");
			}
		} else {
			System.out.println("Changing Bug Test Failed.");
		}
	}

	/*
	 * Removes a random dummy bug to test removeBug method
	 *
	 * @param	none
	 */
	public void testRemoveBug() {
		int bindex = (int) Math.random() * bidList.size();
		boolean b = false;
		Bug bug;

		System.out.println("\n\nBug to be removed: ");
		bug = bsr.getBug(bidList.get(bindex));
		System.out.println("Bug status: NOT_FIXED, bug priority: LOW, bug description: " + bug.getDescription() + ", bug name: " + bug.getName() + ", bug id: " + bidList.get(bindex));
		b = bsr.removeBug(bidList.get(bindex));
		if(!b) {
			System.out.println("Removing Bug Test Failed.");
		} else {
			System.out.println("Removing Bug Test Succeeded.");
		}
	}

	/*
	 * Sets all user data to generated dummy data
	 *
	 * @param	none
	 */
	public void setUserData() {
		usr = generateString();
		pwd = generateString();
		fname = generateString();
		lname = generateString();
		email = generateString() + "@gmail.com";
	}

	/*
	 * Sets all bug data to generated dummy data
	 *
	 * @param	none
	 */
	public void setBugData() {
		des = generateString();
		name = generateString();
		id = generateInt();
	}

	/*
	 * Generates a random string using all letters of the alphabet
	 *
	 * @param	none
	 * @return String 	containing random appended characters
	 */
	public String generateString() {
		StringBuffer b = new StringBuffer();
		String c = "abcdefghijklmnopqrstuvwxyz";
		int l = 6;

		for(int i=0;i<l;i++) {
			double index = Math.random() * l+1;
			b.append(c.charAt((int) index));
		}
		return b.toString();
	}

	/*
	 * Generates a random integer string using all numbers
	 *
	 * @param	none
	 * @return String 	containing random appended numbers
	 */
	public String generateInt() {
		StringBuffer b = new StringBuffer();
		String c = "1234567890";
		int l = 6;

		for(int i=0;i<l;i++) {
			double index = Math.random() * l+1;
			b.append(c.charAt((int) index));
		}

		return b.toString();
	}
}