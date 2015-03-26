import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BRS
{
	//Components for the frame
	private JFrame frame;
	private JPanel	titlePanel, loginPanel;

	//Components for the login panel
	private JLabel		 usernameLabel, passwordLabel, forgotPassword, forgotUsername, signUp, loginStatus;
	private JPanel		 buttonPanel;
	private JTextField 	 usernameField, passwordField;
	private JButton		 loginButton;
	private	GridBagConstraints c;



	public BRS()
	{
		initializeFrame();

		frame.setVisible(true);
	}

	public void initializeFrame()
	{
		frame = new JFrame("Bug Report System (BSR)");
		frame.setSize(720, 480);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.WHITE);

		frame.setLayout(new BorderLayout());

		titlePanel = new TitlePanel("Login");
		initializeLoginPanel();

		frame.add(titlePanel, BorderLayout.NORTH);
		frame.add(loginPanel, BorderLayout.CENTER);
	}

	public void initializeLoginPanel()
	{
		loginPanel = new JPanel();

		loginPanel.setLayout(new GridBagLayout());
		loginPanel.setBackground(Color.WHITE);

		c = new GridBagConstraints();

		usernameLabel 			= new JLabel("Username:", SwingConstants.RIGHT);
		passwordLabel 			= new JLabel("Password:", SwingConstants.RIGHT);
		forgotPassword 			= new JLabel("Forgot Password");
		forgotUsername			= new JLabel("Forgot Username");
		signUp					= new JLabel("Sign Up");
		loginStatus				= new JLabel("Incorrect login information. Please try again.",
												SwingConstants.CENTER);

		usernameField 	= new JTextField("", 10);
		passwordField 	= new JPasswordField("", 10);

		loginButton 		 	= new JButton("Log In");
		
		forgotPassword.setForeground(Color.GRAY);
		forgotUsername.setForeground(Color.GRAY);
		signUp.setForeground(Color.GRAY);
		loginStatus.setForeground(Color.RED);


		c.gridx = 0;
		c.gridy = 0;

		loginPanel.add(usernameLabel, c);

		c.gridx = 1;

		loginPanel.add(usernameField, c);

		c.gridx = 0;
		c.gridy = 1;

		loginPanel.add(passwordLabel, c);

		c.gridx = 1;

		loginPanel.add(passwordField,c);

		c.gridx 	= 0;
		c.gridy 	= 2;
		c.gridwidth = 2;
		

		loginPanel.add(loginButton, c);

		c.gridy 	= 3;
		c.gridx 	= 0;
		c.gridwidth = 3;
		c.insets = new Insets(25,0,25,0);

		loginPanel.add(loginStatus, c);

		c.gridwidth = 1;
		c.gridx 	= 0;
		c.gridy 	= 4;
		c.insets = new Insets(0,0,0,0);
		

		loginPanel.add(forgotPassword, c);

		c.gridy = 5;

		loginPanel.add(forgotUsername, c);

		c.gridy = 4;
		c.gridx = 1;
		c.gridheight = 2;

		loginPanel.add(signUp, c);

		loginStatus.setForeground(Color.WHITE);
	}
}