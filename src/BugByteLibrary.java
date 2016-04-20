/**
 	Title: 			The "BugByteLibrary" class
	Date Written: 	March 2015 - April 2015
	Author: 		Samuel Dindyal
	Description: 	A library of methods and constants used throughout BugByte.
*/

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.awt.Color;
import java.awt.Font;

import java.io.File;
import java.io.InputStream;

public class BugByteLibrary
{
	//Constants for use in BugByte classes
	public static final	Color	MAIN_COLOUR 		= new Color(72, 157, 2),
								ACCENT_COLOUR 		= MAIN_COLOUR.brighter().brighter(),
								SUCCESS_COLOUR 		= new Color(152, 255, 152),
								FAILURE_COLOUR 		= new Color(255, 152, 152),
								BACKGROUND_COLOUR 	= Color.DARK_GRAY.darker();

	public static final Font 	SUBTITLE_FONT 	= compileSubtitleFont(),
								TITLE_FONT 		= compileTitleFont();

/**
	Check a string if it is in the form of a valid email address.

	@param 	A string representation of an email address.
*/
	public static boolean isValidEmailAddress(String emailAddress)
	{
		String regex 					= "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		Pattern emailAddressPattern 	= Pattern.compile(regex);
		Matcher matcher					= emailAddressPattern.matcher(emailAddress);

		return matcher.matches();
	}

/**
	Check a string if it is in the form of a username.

	@param 	A string representation of a username.
*/
	public static boolean isValidUsername(String username)
	{
		String regex 				= "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+";
		Pattern usernamePattern 	= Pattern.compile(regex);
		Matcher matcher 			= usernamePattern.matcher(username);

		return matcher.matches();
	}

/**
	Check a string if it is in the form of a valid name.

	@param 	A string representation of a name.
*/
	public static boolean isValidName(String name)
	{
		String regex 			= "^[a-zA-Z]+";
		Pattern namePattern 	= Pattern.compile(regex);
		Matcher matcher 		= namePattern.matcher(name);

		return matcher.matches();
	}

/**
	Check a string if it is in the form of a valid password.

	@param 	A string representation of a password.
*/
	public static boolean isValidPassword(String password)
	{
		String regex 			= "^[a-zA-Z0-9]+";
		Pattern passwordPattern = Pattern.compile(regex);
		Matcher matcher 		= passwordPattern.matcher(password);

		return password.length() >= 6 && matcher.matches();
	}

/**
	Compiles a font from a file for the SUBTITLE_FONT constant.
*/
	private static Font compileSubtitleFont()
	{
		try{
			InputStream input = BugByteLibrary.class.getResourceAsStream("res/FORCED_SQUARE.ttf");
			return Font.createFont(Font.TRUETYPE_FONT, input).deriveFont(28f);
		}catch(Exception e){return new Font ("Arial", Font.BOLD, 14);}
	}

/**
	Compiles a font from a file for the TITLE_FONT constant.
*/
	private static Font compileTitleFont()
	{
		try{
			InputStream input = BugByteLibrary.class.getResourceAsStream("res/FORCED_SQUARE.ttf");
			return Font.createFont(Font.TRUETYPE_FONT, input).deriveFont(72f);
		}catch(Exception e){return new Font ("Arial", Font.BOLD, 36);}
	}
}