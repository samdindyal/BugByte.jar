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
	private 				BugReportSystem 		bugReportSystem;
	private					GridBagConstraints 		c;
	private 				JComponent  			currentComponent, previousComponent;
	private 				Pattern 				emailAddressPattern, usernamePattern, namePattern;
	private 				Matcher 				matcher;
	private 				Color 					mainColour, accentColour, successColour, failureColour, backgroundColour;
	private 				Font 					subtitle;
	private static final 	boolean	NOT_OSX = !System.getProperty("os.name").startsWith("Mac OS X");

	//Common Components
	private					JComponent commonComponents[][][];
	private static final 	String commonComponentText[][] = {
		{"Username:", "Password:"},	//Login Panel Text
		{"Username:", "Email Address:"}, //Forgot Password Panel Text
		{"Email Address:"}, //Forgot Username Panel Text
		{"Username:", "First Name:", "Last Name:", "Email Address:", "Password:", "Confirm Password:"} , //Sign up Panel Text
		{"Username:", "First Name:", "Last Name:", "Email Address:", "Old Password:", "New Password:", "Confirm Password:"} //Account Summary Panel Text
	};

	//Components for the frame
	private JFrame 		frame;
	private JPanel		titlePanel, loginPanel, signUpPanel, navigationPanel, forgotPasswordPanel, forgotUsernamePanel, submitBugPanel, accountSummaryPanel;
	private JTabbedPane dashboardPanel;		

	//Components for the login panel
	private JLabel		 forgotPassword, forgotUsername, signUp, loginStatus;
	private JPanel		 buttonPanel;
	private JButton		 loginButton;
	private TitledBorder loginBorder;
	
	//Components for the sign up panel
	private JLabel			failedSignUpLbl;
	private JButton 		signUpButton;
	private TitledBorder 	signUpBorder;

	//Components for the navigation panel
	private JButton	backButton, dashboardButton;

	//Components for the forgot password panel
	private JLabel			resetTypeLbl, resetPasswordMessageLbl;
	private JPanel			usernameResetPanel, emailAddressResetPanel, togglePanel;
	private JRadioButton	usernameResetButton, emailAddressResetButton;
	private	ButtonGroup		buttonGroup;
	private JButton 		submitButton;
	private TitledBorder 	forgotPasswordBorder;

	//Components for the forgot username panel
	private JLabel			forgotUsernameMessageLbl;
	private JButton 		submitButton2;
	private JPanel			inputLinePanel;
	private TitledBorder 	forgotUsernameBorder;

	//Components for the dashboard panel
	private JLabel 			accountSummaryMessageLabel;
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
		frame.getContentPane().setBackground(backgroundColour);
		frame.setIconImage(new ImageIcon("res/logo.png").getImage());
		frame.setLayout(new BorderLayout());

		//Initialize the frame's components
		titlePanel = new TitlePanel();
		prepareDashBoardPanel();

		try{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){}

		initializeCommonComponents();
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

	public void initializeCommonComponents()
	{
		commonComponents = new JComponent[commonComponentText.length][][];
		for (int i = 0; i < commonComponents.length; i++)
		{
			commonComponents[i] = new JComponent[commonComponentText[i].length][2];
			for (int j = 0; j < commonComponents[i].length; j++)
			{
				commonComponents[i][j][0] = new JLabel(commonComponentText[i][j], SwingConstants.RIGHT);
				commonComponents[i][j][1] = commonComponentText[i][j].contains("Password") ? 
											new JPasswordField("", 15) : new JTextField("", 15);
			}
		}
	}

/**
	Initializes the login panel, along with all of its components, and adds its components to it using a
	GridBagLayout.
*/
	public void initializeLoginPanel()
	{
		loginPanel = new JPanel();

		loginPanel.setLayout(new GridBagLayout());
		loginPanel.setBackground(backgroundColour);

		loginBorder = BorderFactory.createTitledBorder(NOT_OSX ? new LineBorder(mainColour) : null, "Login", TitledBorder.CENTER, TitledBorder.TOP, subtitle, mainColour);

		loginPanel.setBorder(loginBorder);

		c = new GridBagConstraints();

		//Intialize all components
		forgotPassword 			= new JLabel("Forgot Password");
		forgotUsername			= new JLabel("Forgot Username");
		signUp					= new JLabel("Sign Up");
		loginStatus				= new JLabel("Incorrect login information. Please try again.", SwingConstants.CENTER);

		loginButton 	= new JButton("Log In");
		
		//Set text colour for some components
		forgotPassword.setForeground(Color.LIGHT_GRAY);
		forgotUsername.setForeground(Color.LIGHT_GRAY);
		signUp.setForeground(Color.LIGHT_GRAY);

		forgotPassword.setForeground(accentColour);
		forgotUsername.setForeground(accentColour);
		signUp.setForeground(accentColour);
		loginStatus.setForeground(backgroundColour);

		loginButton.setEnabled(false);

		c.gridx = 0;
		c.gridy = 0;

		for (int i = 0; i < commonComponents[0].length; i++)
		{
			c.gridx = 0;

			for (int j = 0; j < commonComponents[0][i].length; j++)
			{
				
				loginPanel.add(commonComponents[0][i][j], c);

				if (i == 0 && j == 1)
					c.insets = new Insets(10, 0, 0, 0);
				else
					c.insets = new Insets(0, 0, 0, 0);

				if (j == 1)
					commonComponents[0][i][j].addKeyListener(this);
				else
					commonComponents[0][i][j].setForeground(accentColour);

				c.gridy++;
			}			

			c.gridy++;
		}

	
		navigationPanel.add(loginButton);

		c.gridx = 0;
		c.insets = new Insets(25,0,25,0);

		loginPanel.add(loginStatus, c);

		c.gridy++;
		c.insets = new Insets(0,0,0,0);
		

		loginPanel.add(forgotPassword, c);

		c.gridy++;

		loginPanel.add(forgotUsername, c);

		c.gridy++;

		loginPanel.add(signUp, c);

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
		signUpPanel.setBackground(backgroundColour);

		signUpBorder = BorderFactory.createTitledBorder(NOT_OSX ? new LineBorder(mainColour) : null, "Sign Up", TitledBorder.CENTER, TitledBorder.TOP, subtitle, mainColour);
		signUpPanel.setBorder(signUpBorder);

		c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;

		failedSignUpLbl		= new JLabel("", SwingConstants.CENTER);

		signUpButton = new JButton("Finish Sign Up");
		signUpButton.setEnabled(false);

		for (int i = 0; i < commonComponents[3].length; i++)
		{
			for (int j = 0; j < commonComponents[3][i].length; j++)
			{
				if (j == 0)
					commonComponents[3][i][j].setForeground(accentColour);
				else
					commonComponents[3][i][j].addKeyListener(this);
				signUpPanel.add(commonComponents[3][i][j], c);
				c.gridx++;
			}
			c.gridy++;
			c.gridx = 0;
		}

		failedSignUpLbl.setForeground(accentColour);

		c.gridwidth = 2;
		c.insets 	= new Insets(25,0,25,0);
		c.gridy++;

		signUpPanel.add(failedSignUpLbl, c);
		
		c.gridy++;
		c.insets 	= new Insets(0,0,0,0);

		navigationPanel.add(signUpButton);

		signUpButton.setVisible(false);
		signUpButton.addActionListener(this);
	}

	public void initializeNavigationPanel()
	{
		navigationPanel = new JPanel();
		navigationPanel.setBackground(backgroundColour);

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
		forgotUsernamePanel.setBackground(backgroundColour);

		forgotUsernameBorder = BorderFactory.createTitledBorder(NOT_OSX ? new LineBorder(mainColour) : null, "Forgot Username", TitledBorder.CENTER, TitledBorder.TOP, subtitle, mainColour);

		forgotUsernamePanel.setBorder(forgotUsernameBorder);

		submitButton2 	 			= new JButton("Submit");
		forgotUsernameMessageLbl 	= new JLabel("Test", SwingConstants.CENTER);
		inputLinePanel 				= new JPanel();

		submitButton2.setEnabled(false);
		forgotUsernameMessageLbl.setForeground(backgroundColour);
		inputLinePanel.setOpaque(false);

		c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;

		for (int i = 0; i < commonComponents[2][0].length; i++)
		{
			if (i == 0)
				commonComponents[2][0][i].setForeground(accentColour);
			else
				commonComponents[2][0][1].addKeyListener(this);	
			inputLinePanel.add(commonComponents[2][0][i]);	
		}

		c.gridy++;

		forgotUsernamePanel.add(inputLinePanel, c);

		c.insets = new Insets(25, 0, 25, 0);

		c.gridy++;

		forgotUsernamePanel.add(forgotUsernameMessageLbl, c);

		navigationPanel.add(submitButton2);

		submitButton2.setVisible(false);
		submitButton2.addActionListener(this);
	}

	public void initializeForgotPasswordPanel()
	{
		forgotPasswordPanel = new JPanel();
		forgotPasswordPanel.setLayout(new GridBagLayout());
		forgotPasswordPanel.setBackground(backgroundColour);

		forgotPasswordBorder = BorderFactory.createTitledBorder(NOT_OSX ? new LineBorder(mainColour) : null, "Forgot Password", TitledBorder.CENTER, TitledBorder.TOP, subtitle, mainColour);

		forgotPasswordPanel.setBorder(forgotPasswordBorder);

		submitButton = new JButton("Submit");
		submitButton.setEnabled(false);

		resetPasswordMessageLbl = new JLabel("An email with a password reset link has been sent to the email associated it your account.", SwingConstants.CENTER);

		resetPasswordMessageLbl.setForeground(backgroundColour);


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
		usernameResetPanel.setBackground(backgroundColour);

		for (int i = 0; i < commonComponents[1][0].length; i++)
		{
			if (i == 0)
				commonComponents[1][0][i].setForeground(accentColour);				
			else
				commonComponents[1][0][i].addKeyListener(this);
			usernameResetPanel.add(commonComponents[1][0][i]);
		}
	}

	public void initializeEmailAddressResetPanel()
	{
		emailAddressResetPanel = new JPanel();

		emailAddressResetPanel.setBackground(backgroundColour);

		for (int i = 0; i < commonComponents[1][1].length; i++)
		{
			if (i == 0)
				commonComponents[1][1][i].setForeground(accentColour);				
			else
				commonComponents[1][1][i].addKeyListener(this);
			emailAddressResetPanel.add(commonComponents[1][1][i]);
		}
	}

	public void initializeTogglePanel()
	{
		togglePanel = new JPanel();
		togglePanel.setBackground(backgroundColour);

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

		initializeAccountSummaryPanel();

		dashboardPanel.addTab("Account Summary", accountSummaryPanel);
	}

	public void initializeAccountSummaryPanel()
	{
		accountSummaryPanel = new JPanel();
		accountSummaryPanel.setLayout(new GridBagLayout());
		accountSummaryPanel.setBackground(backgroundColour);

		if (NOT_OSX)
			accountSummaryPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(accentColour), "", TitledBorder.CENTER, TitledBorder.TOP, subtitle, accentColour));


		accountSummaryMessageLabel 	= new JLabel(""); 

		submitSummaryButton = new JButton("Submit changes");
		accountSummaryMessageLabel.setForeground(Color.GREEN.darker());

		c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;

		commonComponents[4][0][1].setEnabled(false);

		for (int i = 0; i < commonComponents[4].length; i++)
		{
			for (int j = 0; j < commonComponents[4][i].length; j++)
			{
				if (j == 0)
					commonComponents[4][i][j].setForeground(accentColour);
				else
					commonComponents[4][i][j].addKeyListener(this);
				accountSummaryPanel.add(commonComponents[4][i][j], c);
				c.gridx++;
			}
			c.gridy++;
			c.gridx = 0;
		}

		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 3;
		c.insets = new Insets(15, 0, 0, 0);

		accountSummaryPanel.add(accountSummaryMessageLabel, c);

		navigationPanel.add(submitSummaryButton);

		submitSummaryButton.setVisible(false);
		submitSummaryButton.setEnabled(false);

		submitSummaryButton.addActionListener(this);
	}

	public void prepareDashBoardPanel()
	{
		UIManager.put("TabbedPane.contentAreaColor ",backgroundColour);
		UIManager.put("TabbedPane.selected", backgroundColour);
  		UIManager.put("TabbedPane.background",backgroundColour);
  		UIManager.put("TabbedPane.HightLight", accentColour);
  		UIManager.put("TabbedPane.borderHightlightColor", accentColour);
  		UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));

		dashboardPanel = new JTabbedPane();
		dashboardPanel.setForeground(accentColour);

		dashboardPanelBorder = BorderFactory.createTitledBorder(NOT_OSX ? new LineBorder(mainColour) : null, "Dashboard", TitledBorder.CENTER, TitledBorder.TOP, subtitle, mainColour);

		dashboardPanel.setBorder(dashboardPanelBorder);
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

		successColour 		= new Color(152, 255, 152);
		failureColour 		= new Color(255, 152, 152);
		backgroundColour 	= Color.DARK_GRAY;
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
			submitButton.setEnabled(isValidUsername(((JTextField)commonComponents[1][0][1]).getText()));
		}
		else if (panel == emailAddressResetPanel)
			{
				forgotPasswordPanel.remove(usernameResetPanel);
				submitButton.setEnabled(isValidEmailAddress(((JTextField)commonComponents[1][1][1]).getText()));
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
			if (bugReportSystem.login(((JTextField)commonComponents[0][0][1]).getText(), String.valueOf(((JPasswordField)commonComponents[0][1][1]).getPassword())))
			{
				loginStatus.setForeground(backgroundColour);
				swapComponents(dashboardPanel);
				System.out.println("Login successful.");

				commonComponents[0][0][1].setEnabled(false);
				commonComponents[0][1][1].setEnabled(false);

				loginButton.setText("Logout");

				loginBorder.setTitle("Logged in as " + ((JTextField)commonComponents[0][0][1]).getText());

				populateAccountSummaryFields(new String(((JPasswordField)commonComponents[0][1][1]).getPassword()));

				submitSummaryButton.setVisible(true);
			}
			else
			{
				loginStatus.setForeground(Color.RED);
				loginStatus.setText("Incorrect login information. Please try again.");
				System.out.println("Login failed. Incorrect credentials.");

				commonComponents[0][0][1].setBackground(failureColour);
				commonComponents[0][1][1].setBackground(failureColour);
			}
		}
		else if (bugReportSystem.logout())
		{
			loginBorder.setTitle("Login");
			swapComponents(loginPanel);

			commonComponents[0][0][1].setEnabled(true);
			commonComponents[0][1][1].setEnabled(true);
			loginButton.setEnabled(false);

			((JTextField)commonComponents[0][0][1]).setText("");
			((JPasswordField)commonComponents[0][1][1]).setText("");

			loginButton.setText("Login");

			dashboardButton.setEnabled(false);

			loginStatus.setForeground(Color.GREEN.darker());
			loginStatus.setText("You have successfully logged out.");

			System.out.println("Logout successful.");
		}
	}

	public void signUp()
	{
		if (bugReportSystem.addUser(	((JTextField)commonComponents[3][0][1]).getText(),
							new String(((JPasswordField)commonComponents[3][4][1]).getPassword()),
							((JTextField)commonComponents[3][1][1]).getText(),
							((JTextField)commonComponents[3][2][1]).getText(),
							((JTextField)commonComponents[3][3][1]).getText()))
		{
			bugReportSystem.writeToDisk();
			swapComponents(dashboardPanel);
			System.out.println("Sign Up Successful.");

			commonComponents[0][0][1].setEnabled(false);
			commonComponents[0][1][1].setEnabled(false);
			loginButton.setEnabled(true);

			((JTextField)commonComponents[0][0][1]).setText(((JTextField)commonComponents[3][0][1]).getText());
			((JPasswordField)commonComponents[0][1][1]).setText(new String(((JPasswordField)commonComponents[3][4][1]).getPassword()));

			loginButton.setText("Logout");
			loginStatus.setForeground(backgroundColour);

			previousComponent 		= loginPanel;

			bugReportSystem.login(((JTextField)commonComponents[3][0][1]).getText(), new String(((JPasswordField)commonComponents[3][4][1]).getPassword()));

			populateAccountSummaryFields(new String(((JPasswordField)commonComponents[3][4][1]).getPassword()));
		}
		else
		{
			failedSignUpLbl.setText("User already exists.");
			commonComponents[3][0][1].setBackground(failureColour);
		}
	}

	public void populateAccountSummaryFields(String password)
	{
		User user = bugReportSystem.getUserAccount(password);
		
		((JTextField)commonComponents[4][0][1]).setText(user.getUsername());
		((JTextField)commonComponents[4][1][1]).setText(user.getFirstName());
		((JTextField)commonComponents[4][2][1]).setText(user.getLastName());
		((JTextField)commonComponents[4][3][1]).setText(user.getEmailAddress());
	}

	public void resetPassword()
	{
		resetPasswordMessageLbl.setForeground(Color.GREEN.darker());
		if (emailAddressResetButton.isSelected())
			resetPasswordMessageLbl.setText("An email with a password reset link has been sent to " + ((JTextField)commonComponents[1][1][1]).getText() + ".");
		else
			resetPasswordMessageLbl.setText("An email with a password reset link has been sent to the email associated it your account.");
	}

	public boolean submitAccountChanges(String password)
	{
		User user;
		if ((user = bugReportSystem.getUserAccount(new String(((JPasswordField)commonComponents[4][4][1]).getPassword()))) == null)
		{
			accountSummaryMessageLabel.setForeground(Color.RED);
			accountSummaryMessageLabel.setText("Incorrect password. Please try again.");
			commonComponents[4][4][1].setBackground(failureColour);
		 	return false;
		}

		user.setfirstName(((JTextField)commonComponents[4][1][1]).getText());
		user.setlastName(((JTextField)commonComponents[4][2][1]).getText());
		user.setEmailAddress(((JTextField)commonComponents[4][3][1]).getText());

		if (password.length() > 0)
			user.setPassword(password);

		accountSummaryMessageLabel.setForeground(Color.GREEN.darker());
		accountSummaryMessageLabel.setText("All changes have been sucessfully saved.");

		commonComponents[4][1][1].setBackground(Color.WHITE);
		commonComponents[4][2][1].setBackground(Color.WHITE);
		commonComponents[4][3][1].setBackground(Color.WHITE);
		commonComponents[4][5][1].setBackground(Color.WHITE);
		commonComponents[4][4][1].setBackground(Color.WHITE);
		commonComponents[4][6][1].setBackground(Color.WHITE);

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
			forgotUsernameMessageLbl.setText("An email containing your username has been sent to " + ((JTextField)commonComponents[2][0][1]).getText() + ".");
		else if (e.getSource() == signUpButton)
			signUp();
		else if (e.getSource() == submitSummaryButton)
			submitAccountChanges(new String(((JPasswordField)commonComponents[4][5][1]).getPassword()));
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
		if (e.getSource() == commonComponents[0][0][1] || e.getSource() == commonComponents[0][1][1])
		{
			loginButton.setEnabled(isValidUsername(((JTextField)commonComponents[0][0][1]).getText()) 
				&& ((JPasswordField)commonComponents[0][1][1]).getPassword().length > 0);
			loginStatus.setForeground(backgroundColour);

			commonComponents[0][0][1].setBackground(Color.WHITE);
			commonComponents[0][1][1].setBackground(Color.WHITE);

			if (e.getKeyChar() == KeyEvent.VK_ENTER)
				loginButton.doClick();
		}
		else if (	e.getSource() == commonComponents[3][0][1]
			||  e.getSource() == commonComponents[3][4][1]
			||	e.getSource() == commonComponents[3][5][1]
			||  e.getSource() == commonComponents[3][1][1]
			|| 	e.getSource() == commonComponents[3][2][1]
			|| 	e.getSource() == commonComponents[3][3][1])
		{
				signUpButton.setEnabled(	isValidUsername(((JTextField)commonComponents[3][0][1]).getText())
										&&  ((JPasswordField)commonComponents[3][4][1]).getPassword().length > 0
										&&	new String(((JPasswordField)commonComponents[3][5][1]).getPassword()).equals(new String(((JPasswordField)commonComponents[3][4][1]).getPassword()))
										&& 	isValidName(((JTextField)commonComponents[3][1][1]).getText())
										&&	isValidName(((JTextField)commonComponents[3][2][1]).getText())
										&&  isValidEmailAddress(((JTextField)commonComponents[3][3][1]).getText())
										);
				if (e.getKeyChar() == KeyEvent.VK_ENTER)
				signUpButton.doClick();

						if (e.getKeyChar() == KeyEvent.VK_ENTER)
				submitSummaryButton.doClick();

			if (e.getSource() == commonComponents[3][0][1])
				commonComponents[3][0][1].setBackground(isValidUsername(((JTextField)commonComponents[3][0][1]).getText()) ? successColour : failureColour);
			else if (e.getSource() == commonComponents[3][1][1])
					commonComponents[3][1][1].setBackground(isValidName(((JTextField)commonComponents[3][1][1]).getText()) ? successColour : failureColour);
			else if (e.getSource() == commonComponents[3][2][1])
					commonComponents[3][2][1].setBackground(isValidName(((JTextField)commonComponents[3][2][1]).getText()) ? successColour : failureColour);
			else if (e.getSource() == commonComponents[3][3][1])
					commonComponents[3][3][1].setBackground(isValidEmailAddress(((JTextField)commonComponents[3][3][1]).getText()) ? successColour : failureColour);
			else if (e.getSource() == commonComponents[3][4][1] || e.getSource() == commonComponents[3][5][1])
			{
				if (new String(((JPasswordField)commonComponents[3][4][1]).getPassword()).equals(new String(((JPasswordField)commonComponents[3][5][1]).getPassword()))
								&& ((JTextField)commonComponents[3][4][1]).getText().length() > 0)
				{
					commonComponents[3][4][1].setBackground(successColour);
					commonComponents[3][5][1].setBackground(successColour);
				}
				else if (((JTextField)commonComponents[4][6][1]).getText().length() > 0)
				{
					commonComponents[3][4][1].setBackground(failureColour);
					commonComponents[3][5][1].setBackground(failureColour);
				}
			}
		}
		else if (e.getSource() == commonComponents[1][0][1])
		{
			submitButton.setEnabled(isValidUsername(((JTextField)commonComponents[1][0][1]).getText()));
			resetPasswordMessageLbl.setForeground(backgroundColour);
			if (e.getKeyChar() == KeyEvent.VK_ENTER)
				submitButton.doClick();

			if (((JTextField)commonComponents[1][0][1]).getText().length() == 0)
				commonComponents[1][0][1].setBackground(Color.WHITE);
			else
				commonComponents[1][0][1].setBackground(isValidUsername(((JTextField)commonComponents[1][0][1]).getText()) ? successColour : failureColour);
		}
		else if (e.getSource() == commonComponents[1][1][1])
		{
			submitButton.setEnabled(isValidEmailAddress(((JTextField)commonComponents[1][1][1]).getText()));
			resetPasswordMessageLbl.setForeground(backgroundColour);
			if (e.getKeyChar() == KeyEvent.VK_ENTER)
				submitButton.doClick();
			if (((JTextField)commonComponents[1][1][1]).getText().length() == 0)
				commonComponents[1][1][1].setBackground(Color.WHITE);
			else
				commonComponents[1][1][1].setBackground(isValidEmailAddress(((JTextField)commonComponents[1][1][1]).getText()) ? successColour : failureColour);
		}
		else if (e.getSource() == commonComponents[2][0][1])
		{
			submitButton2.setEnabled(isValidEmailAddress(((JTextField)commonComponents[2][0][1]).getText()));
			forgotUsernameMessageLbl.setForeground(backgroundColour);
			if (e.getKeyChar() == KeyEvent.VK_ENTER)
				submitButton2.doClick();
			if (((JTextField)commonComponents[2][0][1]).getText().length() == 0)
				commonComponents[2][0][1].setBackground(Color.WHITE);
			else
				commonComponents[2][0][1].setBackground(isValidEmailAddress(((JTextField)commonComponents[2][0][1]).getText()) ? successColour : failureColour);
		}
		else if(	e.getSource() == commonComponents[4][1][1]
				||  e.getSource() == commonComponents[4][2][1]
				|| 	e.getSource() == commonComponents[4][3][1]
				|| 	e.getSource() == commonComponents[4][5][1]
				|| 	e.getSource() == commonComponents[4][6][1]
				|| 	e.getSource() == commonComponents[4][4][1])
		{
			submitSummaryButton.setEnabled(	isValidName(((JTextField)commonComponents[4][1][1]).getText())
										&&	isValidName(((JTextField)commonComponents[4][2][1]).getText())
										&&	isValidEmailAddress(((JTextField)commonComponents[4][3][1]).getText())
										&&  (new String(((JPasswordField)commonComponents[4][5][1]).getPassword()).equals(new String(((JPasswordField)commonComponents[4][6][1]).getPassword()))
																		&& ((JTextField)commonComponents[4][5][1]).getText().length() > 0)
										&&  ((JPasswordField)commonComponents[4][4][1]).getPassword().length > 0
				);

			accountSummaryMessageLabel.setText("");
			if (e.getKeyChar() == KeyEvent.VK_ENTER)
				submitSummaryButton.doClick();

			if (e.getSource() == commonComponents[4][1][1])
					commonComponents[4][1][1].setBackground(isValidName(((JTextField)commonComponents[4][1][1]).getText()) ? successColour : failureColour);
			if (e.getSource() == commonComponents[4][2][1])
					commonComponents[4][2][1].setBackground(isValidName(((JTextField)commonComponents[4][2][1]).getText()) ? successColour : failureColour);
			if (e.getSource() == commonComponents[4][3][1])
					commonComponents[4][3][1].setBackground(isValidEmailAddress(((JTextField)commonComponents[4][3][1]).getText()) ? successColour : failureColour);
			if (e.getSource() == commonComponents[4][5][1] || e.getSource() == commonComponents[4][6][1])
			{
				if (new String(((JPasswordField)commonComponents[4][5][1]).getPassword()).equals(new String(((JPasswordField)commonComponents[4][6][1]).getPassword()))
								&& ((JTextField)commonComponents[4][5][1]).getText().length() > 0)
				{
					commonComponents[4][5][1].setBackground(successColour);
					commonComponents[4][6][1].setBackground(successColour);
				}
				else if (((JTextField)commonComponents[4][6][1]).getText().length() > 0)
				{
					commonComponents[4][5][1].setBackground(failureColour);
					commonComponents[4][6][1].setBackground(failureColour);
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e){}

	@Override
	public void keyTyped(KeyEvent e){}
}