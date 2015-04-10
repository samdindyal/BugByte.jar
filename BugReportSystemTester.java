import java.util.concurrent.TimeUnit;

public class BugReportSystemTester {

	public static void main(String args[]) {		
		BugReportSystemHelper bsrh = new BugReportSystemHelper();

		System.out.println("\n\nStarting adding users test...");
		delay(2);
		bsrh.testAddUser();
		delay(3);
		clear();
		
		System.out.println("\n\nStarting user login test...");
		delay(2);
		bsrh.testLogin();
		delay(3);
		clear();
		
		System.out.println("\n\nStarting user logged-in test...");
		delay(2);
		bsrh.testLoggedIn();
		delay(3);
		clear();

		System.out.println("\n\nStarting add bug test...");
		delay(2);
		bsrh.testAddBug();
		delay(3);
		clear();

		System.out.println("\n\nStarting change bug test...");
		delay(2);
		bsrh.testChangeBug();
		delay(3);
		clear();

		System.out.println("\n\nStarting remove bug test...");
		delay(2);
		bsrh.testRemoveBug();
		delay(3);
		clear();

		System.out.println("\n\nStarting user logout test...");
		delay(2);
		bsrh.testLogout();
		delay(3);
	}

	public static void clear() {
		for(int i=0;i<60;i++) {
			System.out.println("");
		}
	}

	public static void delay(int d) {
		try {
			TimeUnit.SECONDS.sleep(d);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}