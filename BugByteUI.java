import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTabbedPane;
import javax.swing.JComponent;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.BorderFactory;
import javax.swing.border.LineBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.io.File;


public class BugByteUI implements ActionListener, MouseListener, KeyListener
{
	private BugReportSystem 		bugReportSystem;
	private	GridBagConstraints 		c;
	private JComponent  			currentComponent, previousComponent;
	private Pattern 				emailAddressPattern, usernamePattern, namePattern;
	private Matcher 				matcher;
	private Color 					mainColour, accentColour, successColour, failureColour;
	private Font 					subtitle;
	private static final boolean	NOT_OSX = !System.getProperty("os.name").startsWith("Mac OS X");

	//Components for the frame
	private JFrame 		frame;
	private JPanel		titlePanel, loginPanel, signUpPanel, navigationPanel, forgotPasswordPanel, forgotUsernamePanel, submitBugPanel, accountSummaryPanel;
	private JTabbedPane dashboardPanel;

	//Components for the login panel
	private JLabel		 usernameLabel, passwordLabel, forgotPassword, forgotUsername, signUp, loginStatus;
	private JPanel		 buttonPanel;
	private JTextField 	 usernameField, passwordField;
	private JButton		 loginButton;
	private TitledBorder loginBorder;
	
	//Components for the sign up panel
	private JLabel			usernameLbl, passwordLbl, confirmPasswordLbl, firstNameLbl, lastNameLbl, emailAddressLbl, failedSignUpLbl;
	private JTextField		usernameFld, passwordFld, confirmPasswordFld, firstNameFld, lastNameFld, emailAddressFld;
	private JButton 		signUpButton;
	private TitledBorder 	signUpBorder;

	//Components for the navigation panel
	private JButton	backButton, dashboardButton;

	//Components for the forgot password panel
	private JLabel			usernameLbl2, emailAddressLbl2, resetTypeLbl, resetPasswordMessageLbl;
	private JTextField		usernameFld2, emailAddressFld2;
	private JPanel			usernameResetPanel, emailAddressResetPanel, togglePanel;
	private JRadioButton	usernameResetButton, emailAddressResetButton;
	private	ButtonGroup		buttonGroup;
	private JButton 		submitButton;
	private TitledBorder 	forgotPasswordBorder;

	//Components for the forgot username panel
	private JLabel			emailAddressLbl3, forgotUsernameMessageLbl;
	private JTextField		emailAddressFld3;
	private JButton 		submitButton2;
	private JPanel			inputLinePanel;
	private TitledBorder 	forgotUsernameBorder;

	//Components for the dashboard panel
	private JLabel 			firstNameSummaryLabel, lastNameSummaryLabel, usernameSummaryLabel, emailAddressSummaryLabel, passwordSummaryLabel, confirmPasswordSummaryLabel, oldPasswordSummaryLabel, accountSummaryMessageLabel;
	private JTextField		firstNameSummaryField, lastNameSummaryField, usernameSummaryField, emailAddressSummaryField, passwordSummaryField, confirmPasswordSummaryField, oldPasswordSummaryField;
	private JButton     	submitSummaryButton;
	private TitledBorder 	dashboardPanelBorder;

/**
	Creates a new BugByteUI object
*/
	public BugByteUI()
	{
		initializeColours();
		initializePatterns();
		initializeFonts();

		bugReportSystem = new BugReportSystem("res/bugreportsystem.bb");
		
		initializeFrame();

		frame.setVisible(true);
	}

/**
	Intializes the frame and initializes and adds all of the appropriate components to it.
*/
	public void initializeFrame()
	{
		//Build the frame
		frame = new JFrame("BugByte");
		frame.setSize(720, 520);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.setIconImage(new ImageIcon("res/logo.png").getImage());
		frame.setLayout(new BorderLayout());

		//Initialize the frame's components
		titlePanel = new TitlePanel();
		initializeNavigationPanel();
		initializeLoginPanel();
		initializeSignUpPanel();
		initializeForgotPasswordPanel();
		initializeForgotUsernamePanel();
		initializeDashboardPanel();

		//Add components to the frame
		frame.add(titlePanel, BorderLayout.NORTH);
		frame.add(loginPanel, BorderLayout.CENTER);
		frame.add(navigationPanel, BorderLayout.SOUTH);
		currentComponent 	= loginPanel;
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
		loginPanel.setBackground(Color.DARK_GRAY);

		loginBorder = BorderFactory.createTitledBorder(NOT_OSX ? new LineBorder(accentColour) : null, "Login", TitledBorder.CENTER, TitledBorder.TOP, subtitle, accentColour);

		loginPanel.setBorder(loginBorder);

		c = new GridBagConstraints();

		//Intialize all components
		usernameLabel 			= new JLabel("Username:", SwingConstants.RIGHT);
		passwordLabel 			= new JLabel("Password:", SwingConstants.RIGHT);
		forgotPassword 			= new JLabel("Forgot Password");
		forgotUsername			= new JLabel("Forgot Username");
		signUp					= new JLabel("Sign Up");
		loginStatus				= new JLabel("", SwingConstants.CENTER);

		usernameField 	= new JTextField("", 15);
		passwordField 	= new JPasswordField("", 15);
		loginButton 	= new JButton("Log In");
		
		//Set text colour for some components
		forgotPassword.setForeground(Color.LIGHT_GRAY);
		forgotUsername.setForeground(Color.LIGHT_GRAY);
		signUp.setForeground(Color.LIGHT_GRAY);
		loginStatus.setForeground(Color.RED);

		usernameLabel.setForeground(accentColour);
		passwordLabel.setForeground(accentColour);
		forgotPassword.setForeground(accentColour);
		forgotUsername.setForeground(accentColour);
		signUp.setForeground(accentColour);
		loginStatus.setForeground(accentColour);

		loginButton.setEnabled(false);

		//Set layout parameters and add the current component to the panel
		c.gridx 	= 0;
		c.gridy 	= 0;
		c.weightx	= 1.0;
		c.anchor 	= GridBagConstraints.CENTER;

		loginPanel.add(usernameLabel, c);

		c.gridy++;

		loginPanel.add(usernameField, c);

		c.gridy++;
		c.insets = new Insets(10, 0, 0, 0);

		loginPanel.add(passwordLabel, c);

		c.gridy++;
		c.insets = new Insets(0, 0, 0, 0);

		loginPanel.add(passwordField,c);

		navigationPanel.add(loginButton);

		c.gridy++;
		c.gridwidth = 3;
		c.insets = new Insets(25,0,25,0);

		loginPanel.add(loginStatus, c);

		c.gridwidth = 2;
		c.gridy++;
		c.insets = new Insets(0,0,0,0);
		

		loginPanel.add(forgotPassword, c);

		c.gridy++;

		loginPanel.add(forgotUsername, c);

		c.gridy++;

		loginPanel.add(signUp, c);

		loginStatus.setForeground(Color.RED);

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
		signUpPanel.setBackground(Color.DARK_GRAY);

		signUpBorder = BorderFactory.createTitledBorder(NOT_OSX ? new LineBorder(accentColour) : null, "Sign Up", TitledBorder.CENTER, TitledBorder.TOP, subtitle, accentColour);
		signUpPanel.setBorder(signUpBorder);

		c = new GridBagConstraints();

		usernameLbl 		= new JLabel("Username:");
		passwordLbl 		= new JLabel("Password:");
		confirmPasswordLbl 	= new JLabel("Confirm Password:");
		firstNameLbl		= new JLabel("First Name:");
		lastNameLbl			= new JLabel("Last Name:");
		emailAddressLbl		= new JLabel("Email Address:");
		failedSignUpLbl		= new JLabel("", SwingConstants.CENTER);

		usernameFld 		= new JTextField("", 15);
		passwordFld 		= new JPasswordField("", 15);
		confirmPasswordFld 	= new JPasswordField("", 15);
		firstNameFld		= new JTextField("", 15);
		lastNameFld			= new JTextField("", 15);
		emailAddressFld		= new JTextField("", 15);

		signUpButton = new JButton("Finish Sign Up");
		signUpButton.setEnabled(false);

		usernameLbl.setForeground(accentColour);
		passwordLbl.setForeground(accentColour);
		confirmPasswordLbl.setForeground(accentColour);
		firstNameLbl.setForeground(accentColour);
		lastNameLbl.setForeground(accentColour);
		emailAddressLbl.setForeground(accentColour);
		failedSignUpLbl.setForeground(accentColour);

		failedSignUpLbl.setForeground(Color.RED);

		c.gridx = 0;
		c.gridy = 0;

		signUpPanel.add(usernameLbl, c);

		c.gridx++;		

		signUpPanel.add(usernameFld, c);

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


		c.gridx 	= 0;
		c.gridwidth = 2;
		c.insets 	= new Insets(25,0,25,0);
		c.gridy++;

		signUpPanel.add(failedSignUpLbl, c);
		
		c.gridy++;
		c.insets 	= new Insets(0,0,0,0);

		navigationPanel.add(signUpButton);

		signUpButton.setVisible(false);

		usernameFld.addKeyListener(this);
		passwordFld.addKeyListener(this);
		confirmPasswordFld.addKeyListener(this);
		firstNameFld.addKeyListener(this);
		lastNameFld.addKeyListener(this);
		emailAddressFld.addKeyListener(this);
		signUpButton.addActionListener(this);
	}

	public void initializeNavigationPanel()
	{
		navigationPanel = new JPanel();
		navigationPanel.setBackground(Color.DARK_GRAY);

		backButton 		= new JButton("Go Back");
		dashboardButton = new JButton("Dashboard");

		dashboardButton.setEnabled(false);

		navigationPanel.add(backButton);
		navigationPanel.add(dashboardButton);

		backButton.addActionListener(this);
		dashboardButton.addActionListener(this);

	}

	public void initializeForgotUsernamePanel()
	{
		forgotUsernamePanel = new JPanel();
		forgotUsernamePanel.setLayout(new GridBagLayout());
		forgotUsernamePanel.setBackground(Color.DARK_GRAY);

		forgotUsernameBorder = BorderFactory.createTitledBorder(NOT_OSX ? new LineBorder(accentColour) : null, "Forgot Username", TitledBorder.CENTER, TitledBorder.TOP, subtitle, accentColour);

		forgotUsernamePanel.setBorder(forgotUsernameBorder);

		emailAddressLbl3 			= new JLabel("Email Address:");
		emailAddressFld3 			= new JTextField("", 15);
		submitButton2 	 			= new JButton("Submit");
		forgotUsernameMessageLbl 	= new JLabel("", SwingConstants.CENTER);

		submitButton2.setEnabled(false);
		forgotUsernameMessageLbl.setForeground(Color.GREEN.darker());

		emailAddressLbl3.setForeground(accentColour);

		inputLinePanel = new JPanel();
		inputLinePanel.setBackground(Color.DARK_GRAY);

		inputLinePanel.add(emailAddressLbl3);
		inputLinePanel.add(emailAddressFld3);

		c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;

		forgotUsernamePanel.add(inputLinePanel, c);

		c.gridy++;
		c.insets = new Insets(25, 0, 25, 0);

		forgotUsernamePanel.add(forgotUsernameMessageLbl, c);

		navigationPanel.add(submitButton2);

		submitButton2.setVisible(false);

		emailAddressFld3.addKeyListener(this);
		submitButton2.addActionListener(this);
	}

	public void initializeForgotPasswordPanel()
	{
		forgotPasswordPanel = new JPanel();
		forgotPasswordPanel.setLayout(new GridBagLayout());
		forgotPasswordPanel.setBackground(Color.DARK_GRAY);

		forgotPasswordBorder = BorderFactory.createTitledBorder(NOT_OSX ? new LineBorder(accentColour) : null, "Forgot Password", TitledBorder.CENTER, TitledBorder.TOP, subtitle, accentColour);

		forgotPasswordPanel.setBorder(forgotPasswordBorder);

		submitButton = new JButton("Submit");
		submitButton.setEnabled(false);

		resetPasswordMessageLbl = new JLabel("", SwingConstants.CENTER);

		resetPasswordMessageLbl.setForeground(Color.GREEN.darker());


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
		c.insets = new Insets(25,0,25,0);
		
		forgotPasswordPanel.add(resetPasswordMessageLbl, c);

		navigationPanel.add(submitButton);

		submitButton.setVisible(false);

		submitButton.addActionListener(this);
	}

	public void initializeUsernameResetPanel()
	{
		usernameResetPanel = new JPanel();
		usernameResetPanel.setBackground(Color.DARK_GRAY);

		usernameLbl2 = new JLabel("Username:");
		usernameFld2 = new JTextField("", 15);

		usernameLbl2.setForeground(accentColour);

		usernameResetPanel.add(usernameLbl2);
		usernameResetPanel.add(usernameFld2);

		usernameFld2.addKeyListener(this);
	}

	public void initializeEmailAddressResetPanel()
	{
		emailAddressResetPanel = new JPanel();

		emailAddressResetPanel.setBackground(Color.DARK_GRAY);

		emailAddressLbl2 = new JLabel("Email Address:");
		emailAddressFld2 = new JTextField("", 15);

		emailAddressLbl2.setForeground(accentColour);

		emailAddressResetPanel.add(emailAddressLbl2);
		emailAddressResetPanel.add(emailAddressFld2);

		emailAddressFld2.addKeyListener(this);
	}

	public void initializeTogglePanel()
	{
		togglePanel = new JPanel();
		togglePanel.setBackground(Color.DARK_GRAY);

		usernameResetButton 	= new JRadioButton("Reset with username");
		emailAddressResetButton = new JRadioButton("Reset with email address");
		buttonGroup 			= new ButtonGroup();

		usernameResetButton.setForeground(accentColour);
		emailAddressResetButton.setForeground(accentColour);

		emailAddressResetButton.setOpaque(false);
		usernameResetButton.setOpaque(false);

		usernameResetButton.setSelected(true);

		buttonGroup.add(usernameResetButton);
		buttonGroup.add(emailAddressResetButton);

		togglePanel.add(usernameResetButton);
		togglePanel.add(emailAddressResetButton);

		usernameResetButton.addActionListener(this);
		emailAddressResetButton.addActionListener(this);
	}

	public void initializeDashboardPanel()
	{
		UIManager.put("TabbedPane.contentAreaColor ",Color.DARK_GRAY);
		UIManager.put("TabbedPane.selected", Color.DARK_GRAY);
  		UIManager.put("TabbedPane.background",Color.DARK_GRAY);
  		UIManager.put("TabbedPane.HightLight", accentColour);
  		UIManager.put("TabbedPane.borderHightlightColor", accentColour);
  		UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));

		dashboardPanel = new JTabbedPane();
		dashboardPanel.setForeground(accentColour);

		dashboardPanelBorder = BorderFactory.createTitledBorder(NOT_OSX ? new LineBorder(accentColour) : null, "Dashboard", TitledBorder.CENTER, TitledBorder.TOP, subtitle, accentColour);

		dashboardPanel.setBorder(dashboardPanelBorder);

		initializeAccountSummaryPanel();

		dashboardPanel.addTab("Account Summary", accountSummaryPanel);

		dashboardPanel.setBackgroundAt(0, Color.DARK_GRAY);
	}

	public void initializeAccountSummaryPanel()
	{
		accountSummaryPanel = new JPanel();
		accountSummaryPanel.setLayout(new GridBagLayout());
		accountSummaryPanel.setBackground(Color.DARK_GRAY);

		if (NOT_OSX)
			accountSummaryPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(accentColour), "", TitledBorder.CENTER, TitledBorder.TOP, subtitle, accentColour));


		firstNameSummaryLabel 		= new JLabel("First Name:");
		lastNameSummaryLabel 		= new JLabel("Last Name:");
		usernameSummaryLabel 		= new JLabel("Username:");
		emailAddressSummaryLabel 	= new JLabel("Email address:");
		oldPasswordSummaryLabel		= new JLabel("Old password:");
		passwordSummaryLabel 		= new JLabel("Password:");
		confirmPasswordSummaryLabel = new JLabel("Confirm Password:");
		accountSummaryMessageLabel 	= new JLabel(""); 

		firstNameSummaryLabel.setForeground(accentColour);
		lastNameSummaryLabel.setForeground(accentColour);
		usernameSummaryLabel.setForeground(accentColour);
		emailAddressSummaryLabel.setForeground(accentColour);
		oldPasswordSummaryLabel.setForeground(accentColour);
		passwordSummaryLabel.setForeground(accentColour);
		confirmPasswordSummaryLabel.setForeground(accentColour);

		firstNameSummaryField 		= new JTextField("", 15);
		lastNameSummaryField 		= new JTextField("", 15);
		usernameSummaryField 		= new JTextField("", 15);
		emailAddressSummaryField 	= new JTextField("", 15);
		oldPasswordSummaryField 	= new JPasswordField("", 15);
		passwordSummaryField 		= new JPasswordField("", 15);
		confirmPasswordSummaryField = new JPasswordField("", 15);

		submitSummaryButton = new JButton("Submit changes");

		usernameSummaryField.setEnabled(false);
		accountSummaryMessageLabel.setForeground(Color.GREEN.darker());

		c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;

		accountSummaryPanel.add(usernameSummaryLabel, c);

		c.gridx++;

		accountSummaryPanel.add(usernameSummaryField, c);

		c.gridy++;
		c.gridx = 0;

		accountSummaryPanel.add(firstNameSummaryLabel, c);

		c.gridx++;

		accountSummaryPanel.add(firstNameSummaryField, c);

		c.gridx = 0;
		c.gridy++;

		accountSummaryPanel.add(lastNameSummaryLabel, c);

		c.gridx++;

		accountSummaryPanel.add(lastNameSummaryField, c);

		c.gridx = 0;
		c.gridy++;

		accountSummaryPanel.add(emailAddressSummaryLabel, c);

		c.gridx++;

		accountSummaryPanel.add(emailAddressSummaryField, c);

		c.gridy++;
		c.gridx = 0;

		accountSummaryPanel.add(oldPasswordSummaryLabel, c);

		c.gridx++;

		accountSummaryPanel.add(oldPasswordSummaryField, c);

		c.gridy++;
		c.gridx = 0;

		accountSummaryPanel.add(passwordSummaryLabel, c);

		c.gridx++;

		accountSummaryPanel.add(passwordSummaryField, c);

		c.gridy++;
		c.gridx = 0;

		accountSummaryPanel.add(confirmPasswordSummaryLabel, c);

		c.gridx++;

		accountSummaryPanel.add(confirmPasswordSummaryField, c);

		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 3;
		c.insets = new Insets(15, 0, 0, 0);

		accountSummaryPanel.add(accountSummaryMessageLabel, c);

		navigationPanel.add(submitSummaryButton);

		submitSummaryButton.setVisible(false);
		submitSummaryButton.setEnabled(false);

		firstNameSummaryField.addKeyListener(this);
		lastNameSummaryField.addKeyListener(this);
		emailAddressSummaryField.addKeyListener(this);
		passwordSummaryField.addKeyListener(this);
		confirmPasswordSummaryField.addKeyListener(this);
		oldPasswordSummaryField.addKeyListener(this);

		submitSummaryButton.addActionListener(this);
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

	public void initializeColours()
	{
		mainColour = new Color(72, 157, 2);
		accentColour = mainColour.brighter().brighter();

		successColour = new Color(152, 255, 152);
		failureColour = new Color(255, 152, 152);
	}

	public void initializeFonts()
	{
		try
		{
			subtitle = Font.createFont(Font.TRUETYPE_FONT, new File("res/FORCED_SQUARE.ttf")).deriveFont(28f);
		}
		catch(Exception e)
		{
			subtitle = new Font("Arial", Font.BOLD, 14);
		}
	}

	public void swapComponents(JComponent component)
	{
		frame.remove(currentComponent);
		frame.add(component, BorderLayout.CENTER);
		frame.validate();
		frame.repaint();

		submitSummaryButton.setVisible(component == dashboardPanel);
		submitButton.setVisible(component == forgotPasswordPanel);
		submitButton2.setVisible(component == forgotUsernamePanel);
		signUpButton.setVisible(component == signUpPanel);
		dashboardButton.setEnabled(component != dashboardPanel && bugReportSystem.isLoggedIn());
		
		previousComponent 		= currentComponent;
		currentComponent 		= component;
	}

	public void swapPasswordResetPanels(JPanel panel)
	{
		if (panel == usernameResetPanel)
		{
			forgotPasswordPanel.remove(emailAddressResetPanel);
			submitButton.setEnabled(isValidUsername(usernameFld2.getText()));
		}
		else if (panel == emailAddressResetPanel)
			{
				forgotPasswordPanel.remove(usernameResetPanel);
				submitButton.setEnabled(isValidEmailAddress(emailAddressFld2.getText()));
			}

			c.gridy = 1;
			c.gridx = 0;

			forgotPasswordPanel.add(panel, c);
			forgotPasswordPanel.validate();
			forgotPasswordPanel.repaint();

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

	public void toggleLogin()
	{
		if (!bugReportSystem.isLoggedIn())
		{
			if (bugReportSystem.login(usernameField.getText(), String.valueOf(((JPasswordField)passwordField).getPassword())))
			{
				loginStatus.setText("");
				swapComponents(dashboardPanel);
				System.out.println("Login successful.");

				usernameField.setEnabled(false);
				passwordField.setEnabled(false);

				loginButton.setText("Logout");

				loginBorder.setTitle("Logged in as " + usernameField.getText());

				populateAccountSummaryFields(new String(((JPasswordField)passwordField).getPassword()));

				submitSummaryButton.setVisible(true);
			}
			else
			{
				loginStatus.setForeground(Color.RED);
				loginStatus.setText("Incorrect login information. Please try again.");
				System.out.println("Login failed. Incorrect credentials.");

				usernameField.setBackground(failureColour);
				passwordField.setBackground(failureColour);
			}
		}
		else if (bugReportSystem.logout())
		{
			loginBorder.setTitle("Login");
			swapComponents(loginPanel);

			usernameField.setEnabled(true);
			passwordField.setEnabled(true);
			loginButton.setEnabled(false);

			usernameField.setText("");
			passwordField.setText("");

			loginButton.setText("Login");

			dashboardButton.setEnabled(false);

			loginStatus.setForeground(Color.GREEN.darker());
			loginStatus.setText("You have successfully been logged out.");

			System.out.println("Logout successful.");
		}
	}

	public void signUp()
	{
		if (bugReportSystem.addUser(	usernameFld.getText(),
							new String(((JPasswordField)passwordFld).getPassword()),
							firstNameFld.getText(),
							lastNameFld.getText(),
							emailAddressFld.getText()))
		{
			bugReportSystem.writeToDisk();
			swapComponents(dashboardPanel);
			System.out.println("Sign Up Successful.");

			usernameField.setEnabled(false);
			passwordField.setEnabled(false);
			loginButton.setEnabled(true);

			usernameField.setText(usernameFld.getText());
			passwordField.setText(new String(((JPasswordField)passwordFld).getPassword()));

			loginButton.setText("Logout");
			loginStatus.setText("");

			previousComponent 		= loginPanel;

			bugReportSystem.login(usernameFld.getText(), new String(((JPasswordField)passwordFld).getPassword()));

			populateAccountSummaryFields(new String(((JPasswordField)passwordFld).getPassword()));
		}
		else
		{
			failedSignUpLbl.setText("User already exists.");
			usernameFld.setBackground(failureColour);
		}
	}

	public void populateAccountSummaryFields(String password)
	{
		User user = bugReportSystem.getUserAccount(password);
		
		usernameSummaryField.setText(user.getUsername());
		firstNameSummaryField.setText(user.getFirstName());
		lastNameSummaryField.setText(user.getLastName());
		emailAddressSummaryField.setText(user.getEmailAddress());
	}

	public void resetPassword()
	{
		if (emailAddressResetButton.isSelected())
			resetPasswordMessageLbl.setText("An email with a password reset link has been send to " + emailAddressFld2.getText() + ".");
		else
			resetPasswordMessageLbl.setText("An email with a password reset link has been send to the email associated it your account.");
	}

	public boolean submitAccountChanges(String password)
	{
		User user;
		if ((user = bugReportSystem.getUserAccount(new String(((JPasswordField)oldPasswordSummaryField).getPassword()))) == null)
		{
			accountSummaryMessageLabel.setForeground(Color.RED);
			accountSummaryMessageLabel.setText("Incorrect password. Please try again.");
			oldPasswordSummaryField.setBackground(failureColour);
		 	return false;
		}

		user.setfirstName(firstNameSummaryField.getText());
		user.setlastName(lastNameSummaryField.getText());
		user.setEmailAddress(emailAddressSummaryField.getText());

		if (password.length() > 0)
			user.setPassword(password);

		accountSummaryMessageLabel.setForeground(Color.GREEN.darker());
		accountSummaryMessageLabel.setText("All changes have been sucessfully saved.");

		firstNameSummaryField.setBackground(Color.WHITE);
		lastNameSummaryField.setBackground(Color.WHITE);
		emailAddressSummaryField.setBackground(Color.WHITE);
		passwordSummaryField.setBackground(Color.WHITE);
		oldPasswordSummaryField.setBackground(Color.WHITE);
		confirmPasswordSummaryField.setBackground(Color.WHITE);

		bugReportSystem.writeToDisk();

		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == backButton && previousComponent != null)
			swapComponents(previousComponent);
		if (e.getSource() == loginButton)
			toggleLogin();
		else if (e.getSource() == usernameResetButton)
			swapPasswordResetPanels(usernameResetPanel);
		else if(e.getSource() == emailAddressResetButton)
			swapPasswordResetPanels(emailAddressResetPanel);
		else if (e.getSource() == submitButton)
			resetPassword();
		else if (e.getSource() == submitButton2)
			forgotUsernameMessageLbl.setText("An email containing your username has been sent to " + emailAddressFld3.getText() + ".");
		else if (e.getSource() == signUpButton)
			signUp();
		else if (e.getSource() == submitSummaryButton)
			submitAccountChanges(new String(((JPasswordField)passwordSummaryField).getPassword()));
		else if (e.getSource() == dashboardButton)
			swapComponents(dashboardPanel);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (e.getSource() == signUp)
			swapComponents(signUpPanel);
		else if (e.getSource() == forgotPassword)
			swapComponents(forgotPasswordPanel);
		else if (e.getSource() == forgotUsername)
			swapComponents(forgotUsernamePanel);
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
			loginStatus.setText("");

			usernameField.setBackground(Color.WHITE);
			passwordField.setBackground(Color.WHITE);

			if (e.getKeyChar() == KeyEvent.VK_ENTER)
				loginButton.doClick();

		}

		else if (	e.getSource() == usernameFld
			||  e.getSource() == passwordFld
			||	e.getSource() == confirmPasswordFld
			||  e.getSource() == firstNameFld
			|| 	e.getSource() == lastNameFld
			|| 	e.getSource() == emailAddressFld)
		{
				signUpButton.setEnabled(	isValidUsername(usernameFld.getText())
										&&  ((JPasswordField)passwordFld).getPassword().length > 0
										&&	new String(((JPasswordField)confirmPasswordFld).getPassword()).equals(new String(((JPasswordField)passwordFld).getPassword()))
										&& 	isValidName(firstNameFld.getText())
										&&	isValidName(lastNameFld.getText())
										&&  isValidEmailAddress(emailAddressFld.getText())
										);
				if (e.getKeyChar() == KeyEvent.VK_ENTER)
				signUpButton.doClick();

						if (e.getKeyChar() == KeyEvent.VK_ENTER)
				submitSummaryButton.doClick();

			if (e.getSource() == usernameFld)
				usernameFld.setBackground(isValidUsername(usernameFld.getText()) ? successColour : failureColour);
			else if (e.getSource() == firstNameFld)
					firstNameFld.setBackground(isValidName(firstNameFld.getText()) ? successColour : failureColour);
			else if (e.getSource() == lastNameFld)
					lastNameFld.setBackground(isValidName(lastNameFld.getText()) ? successColour : failureColour);
			else if (e.getSource() == emailAddressFld)
					emailAddressFld.setBackground(isValidEmailAddress(emailAddressFld.getText()) ? successColour : failureColour);
			else if (e.getSource() == passwordFld || e.getSource() == confirmPasswordFld)
			{
				if (new String(((JPasswordField)passwordFld).getPassword()).equals(new String(((JPasswordField)confirmPasswordFld).getPassword()))
								&& passwordFld.getText().length() > 0)
				{
					passwordFld.setBackground(successColour);
					confirmPasswordFld.setBackground(successColour);
				}
				else if (confirmPasswordSummaryField.getText().length() > 0)
				{
					passwordFld.setBackground(failureColour);
					confirmPasswordFld.setBackground(failureColour);
				}
			}
		}
		else if (e.getSource() == usernameFld2)
		{
			submitButton.setEnabled(isValidUsername(usernameFld2.getText()));
			resetPasswordMessageLbl.setText("");
			if (e.getKeyChar() == KeyEvent.VK_ENTER)
				submitButton.doClick();

			if (usernameFld2.getText().length() == 0)
				usernameFld2.setBackground(Color.WHITE);
			else
				usernameFld2.setBackground(isValidUsername(usernameFld2.getText()) ? successColour : failureColour);
		}
		else if (e.getSource() == emailAddressFld2)
		{
			submitButton.setEnabled(isValidEmailAddress(emailAddressFld2.getText()));
			resetPasswordMessageLbl.setText("");
			if (e.getKeyChar() == KeyEvent.VK_ENTER)
				submitButton.doClick();
			if (emailAddressFld2.getText().length() == 0)
				emailAddressFld2.setBackground(Color.WHITE);
			else
				emailAddressFld2.setBackground(isValidEmailAddress(emailAddressFld2.getText()) ? successColour : failureColour);
		}
		else if (e.getSource() == emailAddressFld3)
		{
			submitButton2.setEnabled(isValidEmailAddress(emailAddressFld3.getText()));
			forgotUsernameMessageLbl.setText("");
			if (e.getKeyChar() == KeyEvent.VK_ENTER)
				submitButton2.doClick();
			if (emailAddressFld3.getText().length() == 0)
				emailAddressFld3.setBackground(Color.WHITE);
			else
				emailAddressFld3.setBackground(isValidEmailAddress(emailAddressFld3.getText()) ? successColour : failureColour);
		}
		else if(	e.getSource() == firstNameSummaryField
				||  e.getSource() == lastNameSummaryField
				|| 	e.getSource() == emailAddressSummaryField
				|| 	e.getSource() == passwordSummaryField
				|| 	e.getSource() == confirmPasswordSummaryField
				|| 	e.getSource() == oldPasswordSummaryField)
		{
			submitSummaryButton.setEnabled(	isValidName(firstNameSummaryField.getText())
										&&	isValidName(lastNameSummaryField.getText())
										&&	isValidEmailAddress(emailAddressSummaryField.getText())
										&&  (new String(((JPasswordField)passwordSummaryField).getPassword()).equals(new String(((JPasswordField)confirmPasswordSummaryField).getPassword()))
																		&& passwordSummaryField.getText().length() > 0)
										&&  ((JPasswordField)oldPasswordSummaryField).getPassword().length > 0
				);

			accountSummaryMessageLabel.setText("");
			if (e.getKeyChar() == KeyEvent.VK_ENTER)
				submitSummaryButton.doClick();

			if (e.getSource() == firstNameSummaryField)
					firstNameSummaryField.setBackground(isValidName(firstNameSummaryField.getText()) ? successColour : failureColour);
			if (e.getSource() == lastNameSummaryField)
					lastNameSummaryField.setBackground(isValidName(lastNameSummaryField.getText()) ? successColour : failureColour);
			if (e.getSource() == emailAddressSummaryField)
					emailAddressSummaryField.setBackground(isValidEmailAddress(emailAddressSummaryField.getText()) ? successColour : failureColour);
			if (e.getSource() == passwordSummaryField || e.getSource() == confirmPasswordSummaryField)
			{
				if (new String(((JPasswordField)passwordSummaryField).getPassword()).equals(new String(((JPasswordField)confirmPasswordSummaryField).getPassword()))
								&& passwordSummaryField.getText().length() > 0)
				{
					passwordSummaryField.setBackground(successColour);
					confirmPasswordSummaryField.setBackground(successColour);
				}
				else if (confirmPasswordSummaryField.getText().length() > 0)
				{
					passwordSummaryField.setBackground(failureColour);
					confirmPasswordSummaryField.setBackground(failureColour);
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e){}

	@Override
	public void keyTyped(KeyEvent e){}
}