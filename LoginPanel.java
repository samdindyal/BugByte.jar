import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.JButton;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.ComponentOrientation;
import java.awt.Color;
import java.awt.Insets;

public class LoginPanel extends JPanel
{
	private JLabel		 usernameLabel, passwordLabel, forgotPassword, forgotUsername, signUp;
	private JPanel		 buttonPanel;
	private JTextField 	 usernameField, passwordField;
	private JButton		 loginButton;

	private	GridBagConstraints c;

	public LoginPanel()
	{
		setLayout(new GridBagLayout());
		setBackground(Color.WHITE);

		c = new GridBagConstraints();

		usernameLabel 			= new JLabel("Username:", SwingConstants.RIGHT);
		passwordLabel 			= new JLabel("Password:", SwingConstants.RIGHT);
		forgotPassword 			= new JLabel("Forgot Password");
		forgotUsername			= new JLabel("Forgot Username");
		signUp					= new JLabel("Sign Up");

		usernameField 	= new JTextField("", 10);
		passwordField 	= new JPasswordField("", 10);

		loginButton 		 	= new JButton("Log In");
		
		forgotPassword.setForeground(Color.GRAY);
		forgotUsername.setForeground(Color.GRAY);
		signUp.setForeground(Color.GRAY);


		c.gridx = 0;
		c.gridy = 0;

		add(usernameLabel, c);

		c.gridx = 1;

		add(usernameField, c);

		c.gridx = 0;
		c.gridy = 1;

		add(passwordLabel, c);

		c.gridx = 1;

		add(passwordField,c);

		c.gridx 	= 0;
		c.gridy 	= 2;
		c.gridwidth = 2;
		c.insets = new Insets(0,0,50,0);

		add(loginButton, c);

		c.gridwidth = 1;
		c.gridx 	= 0;
		c.gridy 	= 3;
		c.insets = new Insets(0,0,0,0);

		add(forgotPassword, c);

		c.gridy = 4;

		add(forgotUsername, c);

		c.gridy = 3;
		c.gridx = 1;
		c.gridheight = 2;

		add(signUp, c);


	}
}