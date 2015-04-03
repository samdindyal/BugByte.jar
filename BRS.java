import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class BRS implements ActionListener, MouseListener, KeyListener
{
	private BugSystem 	bugSystem;
	private JPanel 	  	currentPanel, previousPanel;
	private String	  	currentPanelName, previousPanelName;
	private Pattern 	emailAddressPattern, usernamePattern, namePattern;
	private Matcher 	matcher;

	//Components for the frame
	private JFrame frame;
	private JPanel	titlePanel, loginPanel, signUpPanel, navigationPanel, forgotPasswordPanel, forgotUsernamePanel, submitBugPanel, dashboardPanel;

	//Components for the login panel
	private JLabel		 usernameLabel, passwordLabel, forgotPassword, forgotUsername, signUp, loginStatus;
	private JPanel		 buttonPanel;
	private JTextField 	 usernameField, passwordField;
	private JButton		 loginButton;
	private	GridBagConstraints c;

	//Components for the sign up panel
	private JLabel		usernameLbl, passwordLbl, confirmPasswordLbl, firstNameLbl, lastNameLbl, emailAddressLbl, failedSignUpLbl;
	private JTextField	usernameFld, passwordFld, confirmPasswordFld, firstNameFld, lastNameFld, emailAddressFld;
	private JButton 	signUpButton;

	//Components for the navigation panel
	private JButton	backButton, resetButton;

	//Components for the forgot password panel
	private JLabel			usernameLbl2, emailAddressLbl2, resetTypeLbl;
	private JTextField		usernameFld2, emailAddressFld2;
	private JPanel			usernameResetPanel, emailAddressResetPanel, togglePanel;
	private JRadioButton	usernameResetButton, emailAddressResetButton;
	private	ButtonGroup		buttonGroup;
	private JButton 		submitButton;

	//Components for forgot username panel
	private JLabel		emailAddressLbl3;
	private JTextField	emailAddressFld3;
	private JButton 	submitButton2;
	private JPanel		inputLinePanel;




/**
	Creates a new BRS object
*/
	public BRS()
	{
		initializeFrame();

		initializePatterns();
		bugSystem = new BugSystem();



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
		initializeForgotPasswordPanel();
		initializeForgotUsernamePanel();

		//Add components to the frame
		frame.add(titlePanel, BorderLayout.NORTH);
		frame.add(loginPanel, BorderLayout.CENTER);
		frame.add(navigationPanel, BorderLayout.SOUTH);
		currentPanel 		= loginPanel;
		currentPanelName 	= "Login";
	}

	public void initializeSubmitBugPanel()
	{
		submitBugPanel = new JPanel();

		submitBugPanel.setBackground(Color.GREEN);
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

		loginButton.setEnabled(false);

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
		usernameField.addKeyListener(this);
		passwordField.addKeyListener(this);
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
		failedSignUpLbl		= new JLabel("User already exists.", SwingConstants.CENTER);

		usernameFld 		= new JTextField("", 15);
		passwordFld 		= new JPasswordField("", 15);
		confirmPasswordFld 	= new JPasswordField("", 15);
		firstNameFld		= new JTextField("", 15);
		lastNameFld			= new JTextField("", 15);
		emailAddressFld		= new JTextField("", 15);

		signUpButton = new JButton("Finish Sign Up");
		signUpButton.setEnabled(false);

		failedSignUpLbl.setForeground(Color.WHITE);

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

		c.gridx 	= 0;
		c.gridwidth = 2;
		c.insets 	= new Insets(25,0,25,0);
		c.gridy++;

		signUpPanel.add(failedSignUpLbl, c);
		
		c.gridy++;
		c.insets 	= new Insets(0,0,0,0);

		signUpPanel.add(signUpButton, c);

		usernameFld.addKeyListener(this);
		passwordFld.addKeyListener(this);
		confirmPasswordFld.addKeyListener(this);
		firstNameFld.addKeyListener(this);
		lastNameFld.addKeyListener(this);
		emailAddressFld.addKeyListener(this);
		
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

	public void initializeForgotUsernamePanel()
	{
		forgotUsernamePanel = new JPanel();
		forgotUsernamePanel.setLayout(new GridBagLayout());
		forgotUsernamePanel.setBackground(Color.WHITE);

		emailAddressLbl3 = new JLabel("Email Address:");
		emailAddressFld3 = new JTextField("", 10);
		submitButton2 	 = new JButton("Submit");
		submitButton2.setEnabled(false);

		inputLinePanel = new JPanel();
		inputLinePanel.setBackground(Color.WHITE);

		inputLinePanel.add(emailAddressLbl3);
		inputLinePanel.add(emailAddressFld3);

		c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;

		forgotUsernamePanel.add(inputLinePanel, c);

		c.gridy++;

		forgotUsernamePanel.add(submitButton2, c);

	}

	public void initializeForgotPasswordPanel()
	{
		forgotPasswordPanel = new JPanel();
		forgotPasswordPanel.setLayout(new GridBagLayout());
		forgotPasswordPanel.setBackground(Color.WHITE);

		submitButton = new JButton("Submit");
		submitButton.setEnabled(false);

		initializeUsernameResetPanel();
		initializeEmailAddressResetPanel();
		initializeTogglePanel();

		c = new GridBagConstraints();


		c.gridx = 0;
		c.gridy = 0;

		forgotPasswordPanel.add(togglePanel, c);

		c.gridy++;

		forgotPasswordPanel.add(usernameResetPanel, c);		

		c.gridy++;
		
		forgotPasswordPanel.add(submitButton, c);
	}

	public void initializeUsernameResetPanel()
	{
		usernameResetPanel = new JPanel();
		usernameResetPanel.setBackground(Color.WHITE);

		usernameLbl2 = new JLabel("Username:");
		usernameFld2 = new JTextField("", 10);

		usernameResetPanel.add(usernameLbl2);
		usernameResetPanel.add(usernameFld2);
	}

	public void initializeEmailAddressResetPanel()
	{
		emailAddressResetPanel = new JPanel();

		emailAddressResetPanel.setBackground(Color.WHITE);

		emailAddressLbl2 = new JLabel("Email Address:");
		emailAddressFld2 = new JTextField("", 10);

		emailAddressResetPanel.add(emailAddressLbl2);
		emailAddressResetPanel.add(emailAddressFld2);

	}

	public void initializeTogglePanel()
	{
		togglePanel = new JPanel();
		togglePanel.setBackground(Color.WHITE);

		usernameResetButton 	= new JRadioButton("Reset with username");
		emailAddressResetButton = new JRadioButton("Reset with email address");
		buttonGroup = new ButtonGroup();

		usernameResetButton.setSelected(true);

		buttonGroup.add(usernameResetButton);
		buttonGroup.add(emailAddressResetButton);

		togglePanel.add(usernameResetButton);
		togglePanel.add(emailAddressResetButton);

		usernameResetButton.addActionListener(this);
		emailAddressResetButton.addActionListener(this);
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

	public void initializePatterns()
	{
		String regex;

		regex 					= "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		emailAddressPattern 	= Pattern.compile(regex);

		regex 				= "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+";
		usernamePattern 	= Pattern.compile(regex);

		regex 			= "^[a-zA-Z0-9]+";
		namePattern 	= Pattern.compile(regex);
	}

	public boolean isValidEmailAddress(String emailAddress)
	{
		matcher = emailAddressPattern.matcher(emailAddress);
		return matcher.matches();
	}

	public boolean isValidUsername(String username)
	{
		matcher = usernamePattern.matcher(username);
		return matcher.matches();
	}

	public boolean isValidName(String name)
	{
		matcher = namePattern.matcher(name);
		return matcher.matches();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == backButton && previousPanel != null)
			swapPanels(previousPanel, previousPanelName);
		if (e.getSource() == loginButton)
		{
			if (bugSystem.login(usernameField.getText(), String.valueOf(((JPasswordField)passwordField).getPassword())))
			{
				loginStatus.setForeground(Color.WHITE);
				swapPanels(dashboardPanel, "Dashboard");
				System.out.println("Login successful");
			}
			else
			{
				loginStatus.setForeground(Color.RED);
			}
		}
		else if (usernameResetButton.isSelected())
		{
			forgotPasswordPanel.remove(usernameResetPanel);
			
			c.gridy = 1;
			c.gridx = 0;

			forgotPasswordPanel.add(emailAddressResetPanel, c);
			forgotPasswordPanel.validate();
			forgotPasswordPanel.repaint();
		}
		else if(emailAddressResetButton.isSelected())
		{
			forgotPasswordPanel.remove(emailAddressResetPanel);
			
			c.gridy = 1;
			c.gridx = 0;

			forgotPasswordPanel.add(usernameResetPanel, c);
			forgotPasswordPanel.validate();
			forgotPasswordPanel.repaint();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (e.getSource() == signUp)
			swapPanels(signUpPanel, "Sign Up");
		else if (e.getSource() == forgotPassword)
			swapPanels(forgotPasswordPanel, "Forgot Password");
		else if (e.getSource() == forgotUsername)
			swapPanels(forgotUsernamePanel, "Forgot Username");

	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if (e.getSource() == usernameField || e.getSource() == passwordField)
		{
			loginButton.setEnabled(isValidUsername(usernameField.getText()) 
				&& ((JPasswordField)passwordField).getPassword().length > 0);
		}

		if (	e.getSource() == usernameFld
			||  e.getSource() == passwordFld
			||	e.getSource() == confirmPasswordFld
			||  e.getSource() == firstNameFld
			|| 	e.getSource() == lastNameFld
			|| 	e.getSource() == emailAddressFld)
				signUpButton.setEnabled(	isValidUsername(usernameFld.getText())
										&&  ((JPasswordField)passwordFld).getPassword().length > 0
										&&	new String(((JPasswordField)confirmPasswordFld).getPassword()).equals(new String(((JPasswordField)passwordFld).getPassword()))
										&& 	isValidName(firstNameFld.getText())
										&&	isValidName(lastNameFld.getText())
										&&  isValidEmailAddress(emailAddressFld.getText())
										);
	}

	@Override
	public void keyPressed(KeyEvent e){}

	@Override
	public void keyTyped(KeyEvent e){}
}