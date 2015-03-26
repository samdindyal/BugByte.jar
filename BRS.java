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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BRS implements ActionListener, MouseListener
{
	private BugSystem bugSystem;
	private JPanel 	  currentPanel, previousPanel;
	private String	  currentPanelName, previousPanelName;

	//Components for the frame
	private JFrame frame;
	private JPanel	titlePanel, loginPanel, signUpPanel, navigationPanel;

	//Components for the login panel
	private JLabel		 usernameLabel, passwordLabel, forgotPassword, forgotUsername, signUp, loginStatus;
	private JPanel		 buttonPanel;
	private JTextField 	 usernameField, passwordField;
	private JButton		 loginButton;
	private	GridBagConstraints c;

	//Components for the sign up panel
	private JLabel		usernameLbl, passwordLbl, confirmPasswordLbl, firstNameLbl, lastNameLbl, emailAddressLbl;
	private JTextField	usernameFld, passwordFld, confirmPasswordFld, firstNameFld, lastNameFld, emailAddressFld;

	//Components for the navigation panel
	private JButton	backButton, resetButton;

/**
	Creates a new BRS object
*/
	public BRS()
	{
		initializeFrame();

		frame.setVisible(true);
	}

/**
	Intializes the frame and initializes and adds all of the appropriate components to it.
*/
	public void initializeFrame()
	{
		//Build the frame
		frame = new JFrame("Bug Report System (BSR)");
		frame.setSize(720, 480);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setLayout(new BorderLayout());

		//Initialize the frame's components
		titlePanel = new TitlePanel("Login");
		initializeLoginPanel();
		initializeSignUpPanel();
		initializeNavigationPanel();

		//Add components to the frame
		frame.add(titlePanel, BorderLayout.NORTH);
		frame.add(loginPanel, BorderLayout.CENTER);
		frame.add(navigationPanel, BorderLayout.SOUTH);
		currentPanel 		= loginPanel;
		currentPanelName 	= "Login";
	}

/**
	Initializes the login panel, along with all of its components, and adds its components to it using a
	GridBagLayout.
*/
	public void initializeLoginPanel()
	{
		loginPanel = new JPanel();

		loginPanel.setLayout(new GridBagLayout());
		loginPanel.setBackground(Color.WHITE);

		c = new GridBagConstraints();

		//Intialize all components
		usernameLabel 			= new JLabel("Username:", SwingConstants.RIGHT);
		passwordLabel 			= new JLabel("Password:", SwingConstants.RIGHT);
		forgotPassword 			= new JLabel("Forgot Password");
		forgotUsername			= new JLabel("Forgot Username");
		signUp					= new JLabel("Sign Up");
		loginStatus				= new JLabel("Incorrect login information. Please try again.",
												SwingConstants.CENTER);

		usernameField 	= new JTextField("", 10);
		passwordField 	= new JPasswordField("", 10);
		loginButton 	= new JButton("Log In");
		
		//Set text colour for some components
		forgotPassword.setForeground(Color.GRAY);
		forgotUsername.setForeground(Color.GRAY);
		signUp.setForeground(Color.GRAY);
		loginStatus.setForeground(Color.RED);

		//Set layout parameters and add the current component to the panel
		c.gridx 	= 0;
		c.gridy 	= 0;
		c.weightx	= 1.0;
		c.anchor 	= GridBagConstraints.CENTER;

		loginPanel.add(usernameLabel, c);

		c.gridy = 1;

		loginPanel.add(usernameField, c);

		c.gridy = 2;

		loginPanel.add(passwordLabel, c);

		c.gridy = 3;

		loginPanel.add(passwordField,c);

		c.gridy 	= 4;
		c.gridwidth = 2;
		

		loginPanel.add(loginButton, c);

		c.gridy 	= 5;
		c.gridwidth = 3;
		c.insets = new Insets(25,0,25,0);

		loginPanel.add(loginStatus, c);

		c.gridwidth = 2;
		c.gridy 	= 6;
		c.insets = new Insets(0,0,0,0);
		

		loginPanel.add(forgotPassword, c);

		c.gridy = 7;

		loginPanel.add(forgotUsername, c);

		c.gridy = 8;

		loginPanel.add(signUp, c);

		//Hide "loginStatus" while keeping its spot in the GridBagLayout
		loginStatus.setForeground(Color.WHITE);

		//Add listeners to components
		forgotUsername.addMouseListener(this);
		forgotPassword.addMouseListener(this);
		signUp.addMouseListener(this);
		loginButton.addActionListener(this);
	}

/**
	Initializes the sign up panel and its components. Adds all the components to it using a GridBagLayout.
*/
	public void initializeSignUpPanel()
	{
		signUpPanel = new JPanel();
		signUpPanel.setLayout(new GridBagLayout());
		signUpPanel.setBackground(Color.WHITE);

		c = new GridBagConstraints();

		usernameLbl 		= new JLabel("Username:");
		passwordLbl 		= new JLabel("Password:");
		confirmPasswordLbl 	= new JLabel("Confirm Password:");
		firstNameLbl		= new JLabel("First Name:");
		lastNameLbl			= new JLabel("Last Name:");
		emailAddressLbl		= new JLabel("Email Address:");

		usernameFld 		= new JTextField("", 15);
		passwordFld 		= new JPasswordField("", 15);
		confirmPasswordFld 	= new JPasswordField("", 15);
		firstNameFld		= new JTextField("", 15);
		lastNameFld			= new JTextField("", 15);
		emailAddressFld		= new JTextField("", 15);

		c.gridx = 0;
		c.gridy = 0;

		signUpPanel.add(usernameLbl, c);

		c.gridx++;		

		signUpPanel.add(usernameFld, c);

		c.gridx = 0;
		c.gridy++;

		signUpPanel.add(passwordLbl, c);

		c.gridx++;

		signUpPanel.add(passwordFld, c);

		c.gridx = 0;
		c.gridy++;

		signUpPanel.add(confirmPasswordLbl, c);

		c.gridx++;

		signUpPanel.add(confirmPasswordFld, c);

		c.gridx = 0;
		c.gridy++;

		signUpPanel.add(firstNameLbl, c);

		c.gridx++;

		signUpPanel.add(firstNameFld, c);

		c.gridx = 0;
		c.gridy++;

		signUpPanel.add(lastNameLbl, c);

		c.gridx++;

		signUpPanel.add(lastNameFld, c);

		c.gridx = 0;
		c.gridy++;

		signUpPanel.add(emailAddressLbl, c);

		c.gridx++;

		signUpPanel.add(emailAddressFld, c);
	}

	public void initializeNavigationPanel()
	{
		navigationPanel = new JPanel();

		backButton 		= new JButton("Go Back");
		resetButton 	= new JButton("Reset");

		navigationPanel.add(backButton);
		navigationPanel.add(resetButton);

		backButton.addActionListener(this);

	}

	public void swapPanels(JPanel panel, String panelName)
	{
		frame.remove(currentPanel);
		frame.add(panel, BorderLayout.CENTER);
		frame.validate();
		frame.repaint();

		previousPanelName 	= currentPanelName;
		currentPanelName 	= panelName;
		previousPanel 		= currentPanel;
		currentPanel 		= panel;

		((TitlePanel)titlePanel).setCurrentPanel(panelName);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == backButton && previousPanel != null)
			swapPanels(previousPanel, previousPanelName);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (e.getSource() == signUp)
			swapPanels(signUpPanel, "Sign Up");

	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}