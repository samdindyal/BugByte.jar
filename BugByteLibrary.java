import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class BugByteLibrary
{

	public static boolean isValidEmailAddress(String emailAddress)
	{
		String regex 					= "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		Pattern emailAddressPattern 	= Pattern.compile(regex);
		Matcher matcher					= emailAddressPattern.matcher(emailAddress);

		return matcher.matches();
	}

	public static boolean isValidUsername(String username)
	{
		String regex 				= "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+";
		Pattern usernamePattern 	= Pattern.compile(regex);
		Matcher matcher 			= usernamePattern.matcher(username);

		return matcher.matches();
	}

	public static boolean isValidName(String name)
	{
		String regex 			= "^[a-zA-Z]+";
		Pattern namePattern 	= Pattern.compile(regex);
		Matcher matcher 		= namePattern.matcher(name);

		return matcher.matches();
	}

	public static boolean isValidPassword(String password)
	{
		String regex 			= "^[a-zA-Z0-9]+";
		Pattern passwordPattern = Pattern.compile(regex);
		Matcher matcher 		= passwordPattern.matcher(password);

		return password.length() >= 6 && matcher.matches();
	}
}