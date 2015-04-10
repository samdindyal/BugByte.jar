/**
 	Title: 			The "BugByteUI" class
	Date Written: 	March 2015 - April 2015
	Author: 		Samuel Dindyal
	Description: 	The graphical user interface for BugByte. It implements swing and a variety of listeners with a colour scheme featuring a dark gray and bright green.
*/

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
import javax.swing.JList;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.BorderFactory;
import javax.swing.border.LineBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.ListSelectionModel;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.UUID;

public class BugByteUI implements ActionListener, MouseListener, KeyListener, ChangeListener, ListSelectionListener
{
	//Instance variables
	private 				BugReportSystem 		bugReportSystem;
	private					GridBagConstraints 		c;
	private 				JComponent  			currentComponent, previousComponent;
	
	private					String					currentUserID, password;
	private static final 	boolean					NOT_OSX = !System.getProperty("os.name").startsWith("Mac OS X");
	private static final 	int 					LOGIN_PANEL 			= 0,
													FORGOT_PASSWORD_PANEL 	= 1,
													FORGOT_USERNAME_PANEL 	= 2,
													SIGN_UP_PANEL 			= 3,
													ACCOUNT_SUMMARY_PANEL 	= 4,
													VIEW_BUG_PANEL 			= 5;
	//Common Components between panels
	private					JComponent commonComponents[][][];
	private static final 	String commonComponentText[][] = {
		{"Username:", "Password:"},	//Login Panel Text
		{"Username:", "Email Address:"}, //Forgot Password Panel Text
		{"Email Address:"}, //Forgot Username Panel Text
		{"Username:", "First Name:", "Last Name:", "Email Address:", "Password:", "Confirm Password:"} , //Sign up Panel Text
		{"Username:", "First Name:", "Last Name:", "Email Address:", "Old Password:", "New Password:", "Confirm Password:"}, //Account Summary Panel Text
		{"Bug Name:", "Bug ID: ", "Bug Summary: ", "Bug Priority: ", "Bug Status: "}
	};

	//Components for the frame
	private JFrame 		frame;
	private JPanel		titlePanel, loginPanel, signUpPanel, navigationPanel, forgotPasswordPanel, forgotUsernamePanel, bugsPanel, accountSummaryPanel, trendsPanel;
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

	//Components for the bugs panel
	private JList 		bugList;
	private JSplitPane 	bugSplitPane;
	private JPanel		viewBugPanel;
	private JButton 	saveButton, revertChangesButton, addButton, removeButton;

	//Components for trends panel
	private Graph graph;

/**
	Creates a new BugByteUI object
*/
	public BugByteUI()
	{

		bugReportSystem = new BugReportSystem("bugreportsystem.bb");
		initializeFrame();
	}

/**
	Intialize the frame and initialize and add all of the appropriate components to it using a BorderLayout.
*/
	public void initializeFrame()
	{
		//Build the frame
		frame = new JFrame("BugByte");
		frame.setSize(720, 520);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(BugByteLibrary.BACKGROUND_COLOUR);
		frame.setIconImage(new ImageIcon("res/logo.png").getImage());
		frame.setLayout(new BorderLayout());

		//Initialize the frame's components
		titlePanel = new TitlePanel();
		titlePanel.setBackground(BugByteLibrary.BACKGROUND_COLOUR);
		setColours();
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

/**
	Initialize components common to all components. 
	This method uses an algorthm to create a 3 dimension, jagged array based on the amount of elements predefined in the commonComponentText string array. The first index is an identifier for which panel the 2 dimensional array of components it holds belongs to. Each index of that 2 dimensional array contains a pair of a label and another component determined by the algorithm below. It is intended to take on a polymorphic approach.
*/
	public void initializeCommonComponents()
	{

		//Create the first dimension of the array with the size of commonComponentText
		commonComponents = new JComponent[commonComponentText.length][][];
		for (int i = 0; i < commonComponents.length; i++)
		{
			//Create the second and third dimensions of the array with the sizes being the second dimension of commonComponentText's size and 2, for pairs of a label and another JComponent, respectively
			commonComponents[i] = new JComponent[commonComponentText[i].length][2];
			for (int j = 0; j < commonComponents[i].length; j++)
			{
				commonComponents[i][j][0] = new JLabel(commonComponentText[i][j], SwingConstants.RIGHT);

				//Creates either a JTextField or JPasswordField depending on the label text
				commonComponents[i][j][1] = commonComponentText[i][j].contains("Password") ? 
											new JPasswordField("", NOT_OSX ? 20 : 15) : new JTextField("", NOT_OSX ? 20 : 15);

				//Change the colour of the label to the accent colour
				commonComponents[i][j][0].setForeground(BugByteLibrary.ACCENT_COLOUR);
				commonComponents[i][j][1].addKeyListener(this);

				//Specific code for the JTextArea and JComboBox on the view bug panel
				if (i == VIEW_BUG_PANEL)
				{
					if (j == 2)
					{
						commonComponents[i][j][1] = new JTextArea(10, 20);
						commonComponents[i][j][1].addKeyListener(this);
					}
					else if (j == 3 || j == 4)
					{
						commonComponents[i][j][1] = new JComboBox(j == 3 ? new String[]{"Low", "Medium", "High"} : new String[]{"Not Fixed", "Fixed"});
						((JComboBox)commonComponents[i][j][1]).addActionListener(this);
					}
					
				}
			}
		}
	}

/**
	Initialize the login panel, along with all of its components, and add its components to it using a GridBagLayout.
*/
	public void initializeLoginPanel()
	{
		loginPanel = new JPanel();

		loginPanel.setLayout(new GridBagLayout());
		loginPanel.setBackground(BugByteLibrary.BACKGROUND_COLOUR);

		//Create a platform dependent border
		loginBorder = BorderFactory.createTitledBorder(NOT_OSX ? new LineBorder(BugByteLibrary.MAIN_COLOUR) : null, "Login", TitledBorder.CENTER, TitledBorder.TOP, BugByteLibrary.SUBTITLE_FONT, BugByteLibrary.MAIN_COLOUR);

		loginPanel.setBorder(loginBorder);

		c = new GridBagConstraints();

		//Intialize all components
		forgotPassword 			= new JLabel("Forgot Password");
		forgotUsername			= new JLabel("Forgot Username");
		signUp					= new JLabel("Sign Up");
		loginStatus				= new JLabel("Incorrect login information. Please try again.", SwingConstants.CENTER);

		loginButton = new JButton("Log In");
		
		//Set text colour for some components
		forgotPassword.setForeground(BugByteLibrary.ACCENT_COLOUR);
		forgotUsername.setForeground(BugByteLibrary.ACCENT_COLOUR);
		signUp.setForeground(BugByteLibrary.ACCENT_COLOUR);
		loginStatus.setForeground(BugByteLibrary.BACKGROUND_COLOUR);

		loginButton.setEnabled(false);

		//Set x and y coordinates for layout to 0
		c.gridx = 0;
		c.gridy = 0;

		//Loop through and add all components to the login panel accordingly using a GridBagLayout
		for (int i = 0; i < commonComponents[LOGIN_PANEL].length; i++)
		{
			for (int j = 0; j < commonComponents[LOGIN_PANEL][i].length; j++)
			{
				loginPanel.add(commonComponents[LOGIN_PANEL][i][j], c);
				c.insets = new Insets((i == 0 && j == 1) ? 10 : 0, 0, 0, 0);
				c.gridy++;
			}		
			c.gridx = 0;	
			c.gridy++;
		}
	
		//Add the rest of the compoents to the login panel
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
	Initialize the sign up panel and its components. Add all the components to it using a GridBagLayout.
*/
	public void initializeSignUpPanel()
	{
		//Initialize the sign up panel and set its layout and background colour
		signUpPanel = new JPanel();
		signUpPanel.setLayout(new GridBagLayout());
		signUpPanel.setBackground(BugByteLibrary.BACKGROUND_COLOUR);

		//Create and set a platform dependent border
		signUpBorder = BorderFactory.createTitledBorder(NOT_OSX ? new LineBorder(BugByteLibrary.MAIN_COLOUR) : null, "Sign Up", TitledBorder.CENTER, TitledBorder.TOP, BugByteLibrary.SUBTITLE_FONT, BugByteLibrary.MAIN_COLOUR);
		signUpPanel.setBorder(signUpBorder);

		//Create the constraints for the GridBagLayout
		c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;

		//Loop through and add all components to the sign up panel accordingly using a GridBagLayout
		for (int i = 0; i < commonComponents[SIGN_UP_PANEL].length; i++)
		{
			for (int j = 0; j < commonComponents[SIGN_UP_PANEL][i].length; j++)
			{
				signUpPanel.add(commonComponents[SIGN_UP_PANEL][i][j], c);
				c.gridx++;
			}
			c.gridy++;
			c.gridx = 0;
		}

		//Prepare the rest of the components and add them to the sign up panel
		failedSignUpLbl	= new JLabel("", SwingConstants.CENTER);
		signUpButton = new JButton("Finish Sign Up");
		signUpButton.setEnabled(false);
		failedSignUpLbl.setForeground(BugByteLibrary.ACCENT_COLOUR);

		c.gridwidth = 2;
		c.insets 	= new Insets(25,0,25,0);
		c.gridy++;

		signUpPanel.add(failedSignUpLbl, c);
		
		c.gridy++;
		c.insets 	= new Insets(0,0,0,0);

		//Add the sign up button to the navigation panel
		navigationPanel.add(signUpButton);

		//Hide the button and set its listener
		signUpButton.setVisible(false);
		signUpButton.addActionListener(this);
	}

/**
	Initialize the naviagation panel and add its components to it.
*/
	public void initializeNavigationPanel()
	{
		//Instantiate the navigation panel and set its background colour
		navigationPanel = new JPanel();
		navigationPanel.setBackground(BugByteLibrary.BACKGROUND_COLOUR);

		//Instantiate the components and prepare them
		backButton 		= new JButton("Go Back");
		dashboardButton = new JButton("Dashboard");

		dashboardButton.setEnabled(false);
		backButton.setEnabled(false);

		//Add the components to the navigation panel
		navigationPanel.add(backButton);
		navigationPanel.add(dashboardButton);

		//Add the appropriate listeners
		backButton.addActionListener(this);
		dashboardButton.addActionListener(this);

	}

/**
	Initialize the forgot username panel and all all of its components to it using a GridBagLayout.
*/
	public void initializeForgotUsernamePanel()
	{
		//Initialize the forgot username panel and set its layour and background colour
		forgotUsernamePanel = new JPanel();
		forgotUsernamePanel.setLayout(new GridBagLayout());
		forgotUsernamePanel.setBackground(BugByteLibrary.BACKGROUND_COLOUR);

		//Set a platform dependent border
		forgotUsernameBorder = BorderFactory.createTitledBorder(NOT_OSX ? new LineBorder(BugByteLibrary.MAIN_COLOUR) : null, "Forgot Username", TitledBorder.CENTER, TitledBorder.TOP, BugByteLibrary.SUBTITLE_FONT, BugByteLibrary.MAIN_COLOUR);
		forgotUsernamePanel.setBorder(forgotUsernameBorder);

		//Initialize some components
		submitButton2 	 			= new JButton("Submit");
		forgotUsernameMessageLbl 	= new JLabel("Test", SwingConstants.CENTER);
		inputLinePanel 				= new JPanel();

		//Prepare components
		submitButton2.setEnabled(false);
		forgotUsernameMessageLbl.setForeground(BugByteLibrary.BACKGROUND_COLOUR);
		inputLinePanel.setOpaque(false);

		//Prepare the GridBagConstraints
		c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;

		//Loop through the commonComponents section for the forgot username panel and add them to the inputLinePanel
		for (int i = 0; i < commonComponents[FORGOT_USERNAME_PANEL][0].length; i++)
			inputLinePanel.add(commonComponents[FORGOT_USERNAME_PANEL][0][i]);	

		//Increment the y value for the constraints
		c.gridy++;

		//Add the remaning components
		forgotUsernamePanel.add(inputLinePanel, c);

		c.insets = new Insets(25, 0, 25, 0);

		c.gridy++;

		forgotUsernamePanel.add(forgotUsernameMessageLbl, c);

		//Add submit button to the navigation panel
		navigationPanel.add(submitButton2);

		//HIde the submit button and give it a listener
		submitButton2.setVisible(false);
		submitButton2.addActionListener(this);
	}

/**
	Initialize the forgot password panel and add all of its components to it using a GridBagLayout.
*/
	public void initializeForgotPasswordPanel()
	{
		//Initialize the forgot password panel and set its layout and background colour
		forgotPasswordPanel = new JPanel();
		forgotPasswordPanel.setLayout(new GridBagLayout());
		forgotPasswordPanel.setBackground(BugByteLibrary.BACKGROUND_COLOUR);

		//Create and set a platform dependent border
		forgotPasswordBorder = BorderFactory.createTitledBorder(NOT_OSX ? new LineBorder(BugByteLibrary.MAIN_COLOUR) : null, "Forgot Password", TitledBorder.CENTER, TitledBorder.TOP, BugByteLibrary.SUBTITLE_FONT, BugByteLibrary.MAIN_COLOUR);

		forgotPasswordPanel.setBorder(forgotPasswordBorder);

		//Prepare components
		submitButton = new JButton("Submit");
		submitButton.setEnabled(false);

		resetPasswordMessageLbl = new JLabel("An email with a password reset link has been sent to the email associated it your account.", SwingConstants.CENTER);
		resetPasswordMessageLbl.setForeground(BugByteLibrary.BACKGROUND_COLOUR);

		initializeUsernameResetPanel();
		initializeEmailAddressResetPanel();
		initializeTogglePanel();

		c = new GridBagConstraints();

		//Add components to the forgot password panel
		c.gridx = 0;
		c.gridy = 0;

		forgotPasswordPanel.add(togglePanel, c);

		c.gridy++;

		forgotPasswordPanel.add(usernameResetPanel, c);		

		c.gridy++;
		c.insets = new Insets(25,0,25,0);
		
		forgotPasswordPanel.add(resetPasswordMessageLbl, c);

		//Add the submit button to the navigation panel, hide it and give it a listener
		navigationPanel.add(submitButton);

		submitButton.setVisible(false);
		submitButton.addActionListener(this);
	}

/**
	Initialize the username reset panel and add its components to it using a flow layout.
*/
	public void initializeUsernameResetPanel()
	{
		usernameResetPanel = new JPanel();
		usernameResetPanel.setBackground(BugByteLibrary.BACKGROUND_COLOUR);

		//Loop through the forgot passowrd panel section of the common components and add everything to the username reset panel
		for (int i = 0; i < commonComponents[FORGOT_PASSWORD_PANEL][0].length; i++)
			usernameResetPanel.add(commonComponents[FORGOT_PASSWORD_PANEL][0][i]);
	}

/**
	Initialize the email address reset panel and add all of its elements to it using a flow layout.
*/
	public void initializeEmailAddressResetPanel()
	{
		//Initialize the email address reset panel and set its background
		emailAddressResetPanel = new JPanel();
		emailAddressResetPanel.setBackground(BugByteLibrary.BACKGROUND_COLOUR);

		//Loop through a section in commonComponents and add some elements to the forgot password panel
		for (int i = 0; i < commonComponents[FORGOT_PASSWORD_PANEL][1].length; i++)
			emailAddressResetPanel.add(commonComponents[FORGOT_PASSWORD_PANEL][1][i]);
	}

/**
	Initialize the panel for toggling between resetting a password with a username or email address and its components to it using a flow layout.
*/
	public void initializeTogglePanel()
	{
		//Initialize the toggle panel and set its background
		togglePanel = new JPanel();
		togglePanel.setBackground(BugByteLibrary.BACKGROUND_COLOUR);

		//Prepare its components
		usernameResetButton 	= new JRadioButton("Reset with username");
		emailAddressResetButton = new JRadioButton("Reset with email address");
		buttonGroup 			= new ButtonGroup();

		//Set the text of the JRadioButtons to the accent colour
		usernameResetButton.setForeground(BugByteLibrary.ACCENT_COLOUR);
		emailAddressResetButton.setForeground(BugByteLibrary.ACCENT_COLOUR);

		//Prevent any backgrounds from showing up from the JRadioButtons
		emailAddressResetButton.setOpaque(false);
		usernameResetButton.setOpaque(false);

		//Set the username reset button to selected
		usernameResetButton.setSelected(true);

		//Add the JRadioButtons to a ButtonGroup
		buttonGroup.add(usernameResetButton);
		buttonGroup.add(emailAddressResetButton);

		//Add everything to the toggle panel
		togglePanel.add(usernameResetButton);
		togglePanel.add(emailAddressResetButton);

		//Add listeners to components
		usernameResetButton.addActionListener(this);
		emailAddressResetButton.addActionListener(this);
	}

/**
	Initialize the dashboard panel.
*/
	public void initializeDashboardPanel()
	{
		//Initialize all components and add them to the dashboard panel as tabs
		initializeAccountSummaryPanel();
		initializeBugsPanel();
		initializeTrendsPanel();

		dashboardPanel.addTab("Bugs", bugsPanel);
		dashboardPanel.addTab("Account Summary", accountSummaryPanel);
		dashboardPanel.addTab("Trends", trendsPanel);
	}

/**
	Initialize the account summary panel
*/
	public void initializeAccountSummaryPanel()
	{
		//Initialize the account summary panel and set its layout and background colour
		accountSummaryPanel = new JPanel();
		accountSummaryPanel.setLayout(new GridBagLayout());
		accountSummaryPanel.setBackground(BugByteLibrary.BACKGROUND_COLOUR);

		//Add a border if not running on Mac OS X
		if (NOT_OSX)
			accountSummaryPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(BugByteLibrary.ACCENT_COLOUR), "", TitledBorder.CENTER, TitledBorder.TOP, BugByteLibrary.SUBTITLE_FONT, BugByteLibrary.ACCENT_COLOUR));
		accountSummaryMessageLabel 	= new JLabel("All changes have been sucessfully saved."); 

		c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;

		//Add all components to the account summary panel
		commonComponents[ACCOUNT_SUMMARY_PANEL][0][1].setEnabled(false);

		for (int i = 0; i < commonComponents[ACCOUNT_SUMMARY_PANEL].length; i++)
		{
			for (int j = 0; j < commonComponents[ACCOUNT_SUMMARY_PANEL][i].length; j++)
			{
				accountSummaryPanel.add(commonComponents[ACCOUNT_SUMMARY_PANEL][i][j], c);
				c.gridx++;
			}
			c.gridx = 0;
			c.gridy++;
		}

		c.gridwidth = 3;
		c.insets = new Insets(15, 0, 0, 0);

		accountSummaryPanel.add(accountSummaryMessageLabel, c);

		//Set the text colour of the accountSummaryMessageLabel
		accountSummaryMessageLabel.setForeground(BugByteLibrary.BACKGROUND_COLOUR);
		
		//Initialize submit button
		submitSummaryButton = new JButton("Submit changes");

		//Add the submit button to the navigation panel
		navigationPanel.add(submitSummaryButton);

		//Hide the submit button and disable it
		submitSummaryButton.setVisible(false);
		submitSummaryButton.setEnabled(false);

		//Add listener to submit button
		submitSummaryButton.addActionListener(this);
	}

/**
	Initialize the tends panel and add its components to it using a BorderLayout.
*/
	public void initializeTrendsPanel()
	{
		//Initialize the trends panel and set its layout and background colour
		trendsPanel = new JPanel();
		trendsPanel.setLayout(new BorderLayout());
		trendsPanel.setBackground(BugByteLibrary.BACKGROUND_COLOUR);

		//Add a platform dependent border
		if (NOT_OSX)
			trendsPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(BugByteLibrary.ACCENT_COLOUR), "", TitledBorder.CENTER, TitledBorder.TOP, BugByteLibrary.SUBTITLE_FONT, BugByteLibrary.ACCENT_COLOUR));

		//Create a new graph and add it to the trends panel
		graph = new Graph(BugByteLibrary.ACCENT_COLOUR, BugByteLibrary.BACKGROUND_COLOUR, 60, bugReportSystem.getStatistics());
		trendsPanel.add(graph, BorderLayout.CENTER);
	}

/**
	Initialize the bugs panel and add its components to it using a BorderLayout.
*/
	public void initializeBugsPanel()
	{
		//Initialize the bugs panel and set its layout and border
		bugsPanel = new JPanel();
		bugsPanel.setLayout(new BorderLayout());
		bugsPanel.setBackground(BugByteLibrary.BACKGROUND_COLOUR);

		//Set a platform dependent border
		if (NOT_OSX)
			bugsPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(BugByteLibrary.ACCENT_COLOUR), "", TitledBorder.CENTER, TitledBorder.TOP, BugByteLibrary.SUBTITLE_FONT, BugByteLibrary.ACCENT_COLOUR));

		//Initialize the view bug panel
		initializeViewBugPanel();

		//Prepare components
		bugList = new JList();
		bugList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane listScrollPane 		= new JScrollPane(bugList);
		JScrollPane viewBugScrollPanel 	= new JScrollPane(viewBugPanel);

		viewBugScrollPanel.setBorder(BorderFactory.createEmptyBorder());
		listScrollPane.setBorder(BorderFactory.createEmptyBorder());

		bugSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, viewBugScrollPanel);
        bugSplitPane.setOneTouchExpandable(false);
        bugSplitPane.setDividerLocation(150);
        bugSplitPane.setBackground(BugByteLibrary.BACKGROUND_COLOUR);
        bugSplitPane.setOneTouchExpandable(true);

        bugList.setBackground(BugByteLibrary.BACKGROUND_COLOUR);
        bugList.addListSelectionListener(this);

        //Add components to the bugs panel
        bugsPanel.add(bugSplitPane, BorderLayout.CENTER);
	}

/**
	Initialize the view bug panel.
*/
	public void initializeViewBugPanel()
	{
		//Initialize the view bug panel and set its background colour and layout
		viewBugPanel = new JPanel();
		viewBugPanel.setBackground(BugByteLibrary.BACKGROUND_COLOUR);
		viewBugPanel.setLayout(new GridBagLayout());

		//Prepare components
		saveButton 			= new JButton("Save Bug");
		revertChangesButton = new JButton("Revert Changes");
		addButton 			= new JButton("+");
		removeButton		= new JButton("-");

		//Add components to the frame
		navigationPanel.add(saveButton);
		navigationPanel.add(revertChangesButton);
		navigationPanel.add(addButton);
		navigationPanel.add(removeButton);

		//Add listeners to components
		saveButton.addActionListener(this);
		revertChangesButton.addActionListener(this);
		viewBugPanel.addKeyListener(this);
		addButton.addActionListener(this);
		removeButton.addActionListener(this);

		//Hide buttons
		saveButton.setVisible(false);
		revertChangesButton.setVisible(false);
		addButton.setVisible(false);
		removeButton.setVisible(false);

		//Prepare more components
		commonComponents[VIEW_BUG_PANEL][1][1] = new JTextField("", 25);
		commonComponents[VIEW_BUG_PANEL][1][1].setEnabled(false);
		((JTextArea)commonComponents[VIEW_BUG_PANEL][2][1]).setLineWrap(true);
		((JTextArea)commonComponents[VIEW_BUG_PANEL][2][1]).setWrapStyleWord(true);

		c = new GridBagConstraints();

		c.gridy = 0;
		c.gridx = 0;
		c.insets = new Insets(10, 0, 10, 0);

		//Add components to the view bug panel
		for (int i = 0; i < commonComponents[VIEW_BUG_PANEL].length; i++)
		{
			for (int j = 0; j < commonComponents[VIEW_BUG_PANEL][i].length; j++)
			{
				viewBugPanel.add(commonComponents[VIEW_BUG_PANEL][i][j], c);
				c.gridx++;
			}
			c.gridx = 0;
			c.gridy++;
		}

	}

/**
	Prepare the dashboard panel for adding tabs.
*/
	public void prepareDashBoardPanel()
	{
		//Initialize the dashboard panel and set its tab text colour
		dashboardPanel = new JTabbedPane();
		dashboardPanel.setForeground(BugByteLibrary.ACCENT_COLOUR);

		//Set a platform dependent border
		dashboardPanelBorder = BorderFactory.createTitledBorder(NOT_OSX ? new LineBorder(BugByteLibrary.MAIN_COLOUR) : null, "Dashboard", TitledBorder.CENTER, TitledBorder.TOP, BugByteLibrary.SUBTITLE_FONT, BugByteLibrary.MAIN_COLOUR);
		dashboardPanel.setBorder(dashboardPanelBorder);

		//Set tab text colours and backgrounds
		for (int i = 0; i < dashboardPanel.getTabCount(); i++)
		{
			dashboardPanel.setForegroundAt(i, BugByteLibrary.ACCENT_COLOUR);
			dashboardPanel.setBackgroundAt(i, NOT_OSX ? BugByteLibrary.BACKGROUND_COLOUR : Color.BLACK);
		}

		//Add a change listener to the dashboard panel
		dashboardPanel.addChangeListener(this);
	}

/**
	Override the default UI colour scheme.
*/
	public void setColours()
	{
		UIManager.put("TextField.selectionBackground", BugByteLibrary.ACCENT_COLOUR);
		UIManager.put("TextField.selectionForeground", BugByteLibrary.BACKGROUND_COLOUR);
		UIManager.put("PasswordField.selectionBackground", BugByteLibrary.ACCENT_COLOUR);
		UIManager.put("PasswordField.selectionForeground", BugByteLibrary.BACKGROUND_COLOUR);
		UIManager.put("TextArea.selectionBackground", BugByteLibrary.ACCENT_COLOUR);
		UIManager.put("TextArea.selectionForeground", BugByteLibrary.BACKGROUND_COLOUR);
		UIManager.put("TabbedPane.contentAreaColor ",BugByteLibrary.BACKGROUND_COLOUR);
		UIManager.put("TabbedPane.selected", BugByteLibrary.ACCENT_COLOUR);
		UIManager.put("TabbedPane.unselected", BugByteLibrary.BACKGROUND_COLOUR);
		UIManager.put("TabbedPane.tabAreaForeground", BugByteLibrary.BACKGROUND_COLOUR);
  		UIManager.put("TabbedPane.background",BugByteLibrary.BACKGROUND_COLOUR);
  		UIManager.put("TabbedPane.HightLight", BugByteLibrary.ACCENT_COLOUR);
  		UIManager.put("TabbedPane.borderHightlightColor", BugByteLibrary.ACCENT_COLOUR);
  		UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));	
  		UIManager.put("List.background", BugByteLibrary.BACKGROUND_COLOUR);
		UIManager.put("List.foreground", BugByteLibrary.ACCENT_COLOUR);
		UIManager.put("List.selectionBackground", BugByteLibrary.ACCENT_COLOUR);
		UIManager.put("List.selectionForeground", BugByteLibrary.BACKGROUND_COLOUR);
		UIManager.put("ComboBox.selectionBackground", BugByteLibrary.ACCENT_COLOUR);
		UIManager.put("ComboBox.selectionForeground", BugByteLibrary.BACKGROUND_COLOUR);
	}

/**
	Swap the component in the center of the frame.

	@param 	component 	The component to swap in.
*/
	public void swapComponents(JComponent component)
	{
		//Remove the current component and add the new one to the frame
		frame.remove(currentComponent);
		frame.add(component, BorderLayout.CENTER);

		//Refresh the frame
		frame.validate();
		frame.repaint();

		//Set the visibilty on all the buttons on the navigation panel accordingly
		submitSummaryButton.setVisible(component == dashboardPanel && dashboardPanel.getSelectedIndex() == 1);
		submitButton.setVisible(component == forgotPasswordPanel);
		submitButton2.setVisible(component == forgotUsernamePanel);
		signUpButton.setVisible(component == signUpPanel);
		dashboardButton.setEnabled(component != dashboardPanel && bugReportSystem.isLoggedIn(currentUserID));
		backButton.setEnabled(bugReportSystem.isLoggedIn(currentUserID) || component != loginPanel);
		saveButton.setVisible(component == dashboardPanel && dashboardPanel.getSelectedIndex() == 0);
		revertChangesButton.setVisible(component == dashboardPanel && dashboardPanel.getSelectedIndex() == 0);
		addButton.setVisible(component == dashboardPanel && dashboardPanel.getSelectedIndex() == 0);
		removeButton.setVisible(component == dashboardPanel && dashboardPanel.getSelectedIndex() == 0);

		//Set the previous component and current component
		previousComponent 		= currentComponent;
		currentComponent 		= component;
	}

/**
	Swap panels on the password reset panel.

	@param 	panel 	Panel to show on the password reset panel.
*/
	public void swapPasswordResetPanels(JPanel panel)
	{
		//If the panel to be swapped in is the username reset panel
		if (panel == usernameResetPanel)
		{
			forgotPasswordPanel.remove(emailAddressResetPanel);
			submitButton.setEnabled(BugByteLibrary.isValidUsername(((JTextField)commonComponents[FORGOT_PASSWORD_PANEL][0][1]).getText()));
		}

		//If the panel to be swapped in is the email address reset panel
		else if (panel == emailAddressResetPanel)
			{
				forgotPasswordPanel.remove(usernameResetPanel);
				submitButton.setEnabled(BugByteLibrary.isValidEmailAddress(((JTextField)commonComponents[FORGOT_PASSWORD_PANEL][1][1]).getText()));
			}

			c.gridy = 1;
			c.gridx = 0;

			//Add the panel to the forgot password panel
			forgotPasswordPanel.add(panel, c);

			//Refresh the forgot password panel
			forgotPasswordPanel.validate();
			forgotPasswordPanel.repaint();
	}

/**
	Check a password field given the panel and number of the field where the password fields start.

	@param 	panelIdentifier 	The identifier of the panel to check.
	@param 	fieldNumber 		The index value of the first password field.
*/
	public boolean passwordFieldCheck(int panelIdentifier, int fieldNumber)
	{
		return		new String(((JPasswordField)commonComponents[panelIdentifier][fieldNumber][1]).getPassword()).equals(new String(((JPasswordField)commonComponents[panelIdentifier][fieldNumber+1][1]).getPassword()))
				&& BugByteLibrary.isValidPassword(new String(((JPasswordField)commonComponents[panelIdentifier][fieldNumber][1]).getPassword()));
	}

/**
	Attempt to log in or logout.
*/
	public void toggleLogin()
	{
		//If there are no active users in the system
		if (!bugReportSystem.hasActiveUsers())
		{
			//If an attempted login was successful
			if (bugReportSystem.login(((JTextField)commonComponents[LOGIN_PANEL][0][1]).getText(), String.valueOf(((JPasswordField)commonComponents[LOGIN_PANEL][1][1]).getPassword())))
			{
				//Reset the appropriate components
				resetComponent(SIGN_UP_PANEL);
				resetComponent(FORGOT_PASSWORD_PANEL);
				resetComponent(FORGOT_USERNAME_PANEL);

				//Fetch the entered password from the fields and create a new bug for the user to begin
				password = new String(((JPasswordField)commonComponents[LOGIN_PANEL][1][1]).getPassword());
				createNewBug();
				
				//Prepare components
				currentUserID = ((JTextField)commonComponents[LOGIN_PANEL][0][1]).getText();
				loginStatus.setForeground(BugByteLibrary.BACKGROUND_COLOUR);
				swapComponents(dashboardPanel);

				System.out.println("Login successful.");

				commonComponents[LOGIN_PANEL][0][1].setEnabled(false);
				commonComponents[LOGIN_PANEL][1][1].setEnabled(false);

				loginButton.setText("Logout");

				loginBorder.setTitle("Logged in as " + ((JTextField)commonComponents[LOGIN_PANEL][0][1]).getText());

				populateAccountSummaryFields();
				bugList.setListData(generateBugList());

				forgotUsername.setForeground(BugByteLibrary.BACKGROUND_COLOUR.brighter());
				forgotPassword.setForeground(BugByteLibrary.BACKGROUND_COLOUR.brighter());
				signUp.setForeground(BugByteLibrary.BACKGROUND_COLOUR.brighter());
				commonComponents[LOGIN_PANEL][0][1].setBackground(Color.WHITE);
				commonComponents[LOGIN_PANEL][1][1].setBackground(Color.WHITE);
			}
			else
			{
				//Set components accordingly for a failed login
				loginStatus.setForeground(Color.RED);
				loginStatus.setText("Incorrect login information. Please try again.");
				System.out.println("Login failed. Incorrect credentials.");

				commonComponents[LOGIN_PANEL][0][1].setBackground(BugByteLibrary.FAILURE_COLOUR);
				commonComponents[LOGIN_PANEL][1][1].setBackground(BugByteLibrary.FAILURE_COLOUR);

				//Terminate method
				return;
			}
		}
		//If a logout was successful
		else if (bugReportSystem.logout(currentUserID))
		{
			//Reset the account summary panel
			resetComponent(ACCOUNT_SUMMARY_PANEL);

			//Clear the user fields in the UI class and set the border of the login panel accordingly
			currentUserID = "";
			password = "";
			loginBorder.setTitle("Login");

			//Swap to the login panel
			swapComponents(loginPanel);

			//Enable the login fields again
			commonComponents[LOGIN_PANEL][0][1].setEnabled(true);
			commonComponents[LOGIN_PANEL][1][1].setEnabled(true);

			//Disable the login button
			loginButton.setEnabled(false);

			//Clear the textfields
			((JTextField)commonComponents[LOGIN_PANEL][0][1]).setText("");
			((JPasswordField)commonComponents[LOGIN_PANEL][1][1]).setText("");

			//Change the text on the login button
			loginButton.setText("Login");

			//Set all other components accordingly
			dashboardButton.setEnabled(false);
			forgotUsername.setForeground(BugByteLibrary.ACCENT_COLOUR);
			forgotPassword.setForeground(BugByteLibrary.ACCENT_COLOUR);
			signUp.setForeground(BugByteLibrary.ACCENT_COLOUR);

			loginStatus.setForeground(BugByteLibrary.ACCENT_COLOUR);
			loginStatus.setText("You have successfully logged out.");

			System.out.println("Logout successful.");
		}

	}
/**
	Attempt to sign the user up for an account.
*/
	public void signUp()
	{
		//If adding a user to the system is successful
		if (bugReportSystem.addUser(	((JTextField)commonComponents[SIGN_UP_PANEL][0][1]).getText(),
							new String(((JPasswordField)commonComponents[SIGN_UP_PANEL][4][1]).getPassword()),
							((JTextField)commonComponents[SIGN_UP_PANEL][1][1]).getText(),
							((JTextField)commonComponents[SIGN_UP_PANEL][2][1]).getText(),
							((JTextField)commonComponents[SIGN_UP_PANEL][3][1]).getText()))
		{
			//Set the appropriate values for login
			currentUserID 	= ((JTextField)commonComponents[SIGN_UP_PANEL][0][1]).getText();
			password 		= new String(((JPasswordField)commonComponents[SIGN_UP_PANEL][4][1]).getPassword());

			//Write the bug system to a file
			bugReportSystem.writeToDisk();

			//Create a new bug and swap to the dashboard panel
			createNewBug();
			swapComponents(dashboardPanel);
			System.out.println("Sign Up Successful.");

			//Set compoenents accordingly
			commonComponents[LOGIN_PANEL][0][1].setEnabled(false);
			commonComponents[LOGIN_PANEL][1][1].setEnabled(false);
			loginButton.setEnabled(true);

			((JTextField)commonComponents[LOGIN_PANEL][0][1]).setText(((JTextField)commonComponents[SIGN_UP_PANEL][0][1]).getText());
			((JPasswordField)commonComponents[LOGIN_PANEL][1][1]).setText(new String(((JPasswordField)commonComponents[SIGN_UP_PANEL][4][1]).getPassword()));

			loginButton.setText("Logout");
			loginStatus.setForeground(BugByteLibrary.BACKGROUND_COLOUR);
			commonComponents[LOGIN_PANEL][0][1].setBackground(Color.WHITE);
			commonComponents[LOGIN_PANEL][1][1].setBackground(Color.WHITE);

			//Set the previous component to the loginPanel
			previousComponent = loginPanel;

			//Log the user in
			bugReportSystem.login(((JTextField)commonComponents[SIGN_UP_PANEL][0][1]).getText(), password);

			//Pull the user's information in from the bug report system
			populateAccountSummaryFields();
		}
		else
		{
			//Set components accordingly for a failed sign up
			failedSignUpLbl.setForeground(Color.RED);
			failedSignUpLbl.setText("Sorry, that username has already been taken.");
			System.out.println("Username already taken. Sign Up Unsuccessful.");
			commonComponents[SIGN_UP_PANEL][0][1].setBackground(BugByteLibrary.FAILURE_COLOUR);
		}

		signUpButton.setEnabled(false);
	}

/**
	Populate the fields in the account summary panel with information pulled from the bug report system.
*/
	public void populateAccountSummaryFields()
	{
		User user = bugReportSystem.getUserAccount(currentUserID, password);
		
		((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][0][1]).setText(user.getUsername());
		((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][1][1]).setText(user.getFirstName());
		((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][2][1]).setText(user.getLastName());
		((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][3][1]).setText(user.getEmailAddress());
	}

/**
	Action method for the reset password panel.
*/
	public void resetPassword()
	{
		resetPasswordMessageLbl.setForeground(BugByteLibrary.ACCENT_COLOUR);
		if (emailAddressResetButton.isSelected())
			resetPasswordMessageLbl.setText("An email with a password reset link has been sent to " + ((JTextField)commonComponents[FORGOT_PASSWORD_PANEL][1][1]).getText() + ".");
		else
			resetPasswordMessageLbl.setText("An email with a password reset link has been sent to the email associated it your account.");
	}

/**
	Submit the new account modifications on the account summary panel.

	@param 	password 	The password of the user currently signed in.

	@return 	A flag indicating the success or failure of submitting account changes.
*/
	public boolean submitAccountChanges(String password)
	{
		User user;

		//If fetching the user's account's object was not a success
		if ((user = bugReportSystem.getUserAccount(currentUserID, new String(((JPasswordField)commonComponents[ACCOUNT_SUMMARY_PANEL][4][1]).getPassword()))) == null)
		{
			//Set components accordingly and return false
			accountSummaryMessageLabel.setForeground(Color.RED);
			accountSummaryMessageLabel.setText("Incorrect password. Please try again.");
			commonComponents[ACCOUNT_SUMMARY_PANEL][4][1].setBackground(BugByteLibrary.FAILURE_COLOUR);
		 	return false;
		}

		//Set fields accordingly
		user.setfirstName(((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][1][1]).getText());
		user.setlastName(((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][2][1]).getText());
		user.setEmailAddress(((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][3][1]).getText());

		accountSummaryMessageLabel.setForeground(BugByteLibrary.ACCENT_COLOUR);
		accountSummaryMessageLabel.setText("All changes have been sucessfully saved.");

		//Set the background of all the other components to white
		for (int i = 0; i < commonComponents[ACCOUNT_SUMMARY_PANEL].length; i++)
			commonComponents[ACCOUNT_SUMMARY_PANEL][i][1].setBackground(Color.WHITE);

		//Write the bug report system to a file
		bugReportSystem.writeToDisk();

		return true;
	}

/**
	Check the values in the fields in the account summary panel and validate them accordingly.

	@return 	A flag representing the validity of all text in the fields on the account summary panel.
*/
	public boolean accountSummaryCheck()
	{
		return 		BugByteLibrary.isValidName(((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][1][1]).getText())
				&&	BugByteLibrary.isValidName(((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][2][1]).getText())
				&&	BugByteLibrary.isValidEmailAddress(((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][3][1]).getText())
				&&  (new String(((JPasswordField)commonComponents[ACCOUNT_SUMMARY_PANEL][5][1]).getPassword()).equals(new String(((JPasswordField)commonComponents[ACCOUNT_SUMMARY_PANEL][6][1]).getPassword()))
					&& ((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][5][1]).getText().length() >= 6)
				&&  ((JPasswordField)commonComponents[ACCOUNT_SUMMARY_PANEL][4][1]).getPassword().length >= 6;
	}

/**
	Check the values in the fields in the sign up panel and validate them accordingly.

	@return 	A flag representing the validity of all the text in the fields on the sign up panel.
*/
	public boolean signUpCheck()
	{
		return 		BugByteLibrary.isValidUsername(((JTextField)commonComponents[SIGN_UP_PANEL][0][1]).getText())
				&&  ((JPasswordField)commonComponents[SIGN_UP_PANEL][4][1]).getPassword().length > 0
				&&	new String(((JPasswordField)commonComponents[SIGN_UP_PANEL][5][1]).getPassword()).equals(new String(((JPasswordField)commonComponents[SIGN_UP_PANEL][4][1]).getPassword()))
				&& 	BugByteLibrary.isValidName(((JTextField)commonComponents[SIGN_UP_PANEL][1][1]).getText())
				&&	BugByteLibrary.isValidName(((JTextField)commonComponents[SIGN_UP_PANEL][2][1]).getText())
				&&  BugByteLibrary.isValidEmailAddress(((JTextField)commonComponents[SIGN_UP_PANEL][3][1]).getText());
	}

/**
	Check if an object corresponds to an element in a given panel.

	@param 	n 		Identifier of panel to search.
	@param 	obj 	Object to search for.
*/
	public boolean isInPanel(int n, Object obj)
	{
		//Loop through the elements on the panel of the identifier n in search of the object
		for (int i = 0; i < commonComponents[n].length; i++)
			if (obj == commonComponents[n][i][1])
				return true;
		return false;
	}

/**
	Reset a component.

	@param 	identifier 	Identifier of the component to reset.
*/
	public void resetComponent(int identifier)
	{
		//Reset all text fields and their backgrounds
		for (int i = 0; i < commonComponents[identifier].length; i++)
		{
			((JTextField)commonComponents[identifier][i][1]).setText("");
			commonComponents[identifier][i][1].setBackground(Color.WHITE);
		}

		//Panel specific reset procedures
		if (identifier == SIGN_UP_PANEL)
			failedSignUpLbl.setForeground(BugByteLibrary.BACKGROUND_COLOUR);
		else if (identifier == FORGOT_USERNAME_PANEL)
			forgotUsernameMessageLbl.setForeground(BugByteLibrary.BACKGROUND_COLOUR);
		else if (identifier == FORGOT_PASSWORD_PANEL)
			resetPasswordMessageLbl.setForeground(BugByteLibrary.BACKGROUND_COLOUR);
		else if (identifier == ACCOUNT_SUMMARY_PANEL)
			accountSummaryMessageLabel.setForeground(BugByteLibrary.BACKGROUND_COLOUR);
	}

/**
	Reset all components.
*/
	public void resetAllComponents()
	{
		//Loop through all identifiers and reset components accordingly
		for (int i = 0; i < commonComponents.length; i++)
			resetComponent(i);
	}

/**
	Submit a bug.
*/
	public void submitBug()
	{
		User user;

		//If fetching a user account is successful
		if ((user = bugReportSystem.getUserAccount(currentUserID, password)) != null)
		{
			//Pull user entries from the UI and send them to the bug report system
			BugPriority priority;
			switch(((JComboBox)commonComponents[VIEW_BUG_PANEL][3][1]).getSelectedIndex())
			{
				case 0: priority = BugPriority.LOW;
						break;
				case 1: priority = BugPriority.MEDIUM;
						break;
				case 2: priority = BugPriority.HIGH;
						break;
				default: priority = BugPriority.LOW;
						 break;
			}

			bugReportSystem.addBug(((JComboBox)commonComponents[VIEW_BUG_PANEL][4][1]).getSelectedIndex() == 0 ? BugStatus.NOT_FIXED : BugStatus.FIXED, 
				priority, 
				((JTextArea)commonComponents[VIEW_BUG_PANEL][2][1]).getText(),
				((JTextField)commonComponents[VIEW_BUG_PANEL][0][1]).getText(), 
				((JTextField)commonComponents[VIEW_BUG_PANEL][1][1]).getText(),
				currentUserID);
			bugReportSystem.writeToDisk();

			//Reset the data on the JList in the bug view panel
			bugList.setListData(generateBugList());
		}

	}

/**
	Create a new bug.
*/
	public void createNewBug()
	{
		//Reset fields in the view bug panel and create a new ID for a new bug
		((JTextArea)commonComponents[VIEW_BUG_PANEL][2][1]).setText("");
		((JTextField)commonComponents[VIEW_BUG_PANEL][0][1]).setText("");
		String id = UUID.randomUUID().toString();
		((JTextField)commonComponents[VIEW_BUG_PANEL][1][1]).setText(id);
	}

/**
	Remove a bug from the system.
*/
	public void removeBug()
	{
		//Remove a bug from the system
		String id = ((JTextField)commonComponents[VIEW_BUG_PANEL][1][1]).getText();
		bugReportSystem.removeBug(id);

		//Write the system to a file
		bugReportSystem.writeToDisk();

		//Refresh the bug list
		bugList.setListData(generateBugList());
	}

/**
	Load a bug from the bug report system based on what's selected in bugList.
*/
	public void loadBug()
	{
		//If nothing is selected
		if (bugList.getSelectedIndex() < 0)
			return;

		//Fetch the user's keys
		java.util.LinkedList<String> keys = bugReportSystem.getUserAccount(currentUserID, password).getKeys(password);

		//Sets the fields accordingly to the information in the selected bug
		Bug bug = bugReportSystem.getBug(keys.get(bugList.getSelectedIndex()));
		((JTextField)commonComponents[VIEW_BUG_PANEL][0][1]).setText(bug.getName());
		((JTextField)commonComponents[VIEW_BUG_PANEL][1][1]).setText(bug.getID());
		((JTextArea)commonComponents[VIEW_BUG_PANEL][2][1]).setText(bug.getDescription());

		((JComboBox)commonComponents[VIEW_BUG_PANEL][3][1]).setSelectedIndex(bug.getPriority().priority);
		((JComboBox)commonComponents[VIEW_BUG_PANEL][4][1]).setSelectedIndex(bug.getStatus().status);
	}

/**
	Generate a user friendly string array to display bugs.

	@return 	A string array of names to put onto the bugList.
*/
	public String[] generateBugList()
	{
		//Get keys from the user account
		java.util.LinkedList<String> keys = bugReportSystem.getUserAccount(currentUserID, password).getKeys(password);

		String array[] = new String[keys.size()];

		//Compile an array of strings for displaying
		for (int i = 0; i < keys.size(); i++)
				array[i] = bugReportSystem.getBug(keys.get(i)).getName() + " (" + keys.get(i) + ")";
		return array;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		//Handle all action events
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
			forgotUsernameMessageLbl.setText("An email containing your username has been sent to " + ((JTextField)commonComponents[FORGOT_USERNAME_PANEL][0][1]).getText() + ".");
		else if (e.getSource() == signUpButton)
			signUp();
		else if (e.getSource() == submitSummaryButton)
			submitAccountChanges(new String(((JPasswordField)commonComponents[ACCOUNT_SUMMARY_PANEL][5][1]).getPassword()));
		else if (e.getSource() == dashboardButton)
			swapComponents(dashboardPanel);
		else if (e.getSource() == addButton)
			createNewBug();
		else if (e.getSource() == removeButton)
			removeBug();
		else if (e.getSource() == saveButton)
			submitBug();
		else if (e.getSource() == revertChangesButton)
			loadBug();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		//Handle all mouse events
		if (!bugReportSystem.isLoggedIn(currentUserID))
		{
			if (e.getSource() == signUp)
				swapComponents(signUpPanel);
			else if (e.getSource() == forgotPassword)
				swapComponents(forgotPasswordPanel);
			else if (e.getSource() == forgotUsername)
				swapComponents(forgotUsernamePanel);
		}
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
		//Handle key events for all text fields and text areas in all components
		if (e.getSource() == commonComponents[LOGIN_PANEL][0][1] || e.getSource() == commonComponents[LOGIN_PANEL][1][1])
		{
			loginButton.setEnabled(BugByteLibrary.isValidUsername(((JTextField)commonComponents[LOGIN_PANEL][0][1]).getText()) 
				&& ((JPasswordField)commonComponents[LOGIN_PANEL][1][1]).getPassword().length > 0);
			loginStatus.setForeground(BugByteLibrary.BACKGROUND_COLOUR);

			commonComponents[LOGIN_PANEL][0][1].setBackground(Color.WHITE);
			commonComponents[LOGIN_PANEL][1][1].setBackground(Color.WHITE);

			if (e.getKeyChar() == KeyEvent.VK_ENTER)
				loginButton.doClick();
		}
		else if (isInPanel(3, e.getSource()))
		{
				signUpButton.setEnabled(signUpCheck());
				if (e.getKeyChar() == KeyEvent.VK_ENTER)
				signUpButton.doClick();
						if (e.getKeyChar() == KeyEvent.VK_ENTER)
				submitSummaryButton.doClick();

			if (e.getSource() == commonComponents[SIGN_UP_PANEL][0][1] && e.getKeyChar() !=  KeyEvent.VK_ENTER)
				commonComponents[SIGN_UP_PANEL][0][1].setBackground(BugByteLibrary.isValidUsername(((JTextField)commonComponents[SIGN_UP_PANEL][0][1]).getText()) ? BugByteLibrary.SUCCESS_COLOUR : BugByteLibrary.FAILURE_COLOUR);
			else if (e.getSource() == commonComponents[SIGN_UP_PANEL][1][1])
					commonComponents[SIGN_UP_PANEL][1][1].setBackground(BugByteLibrary.isValidName(((JTextField)commonComponents[SIGN_UP_PANEL][1][1]).getText()) ? BugByteLibrary.SUCCESS_COLOUR : BugByteLibrary.FAILURE_COLOUR);
			else if (e.getSource() == commonComponents[SIGN_UP_PANEL][2][1])
					commonComponents[SIGN_UP_PANEL][2][1].setBackground(BugByteLibrary.isValidName(((JTextField)commonComponents[SIGN_UP_PANEL][2][1]).getText()) ? BugByteLibrary.SUCCESS_COLOUR : BugByteLibrary.FAILURE_COLOUR);
			else if (e.getSource() == commonComponents[SIGN_UP_PANEL][3][1])
					commonComponents[SIGN_UP_PANEL][3][1].setBackground(BugByteLibrary.isValidEmailAddress(((JTextField)commonComponents[SIGN_UP_PANEL][3][1]).getText()) ? BugByteLibrary.SUCCESS_COLOUR : BugByteLibrary.FAILURE_COLOUR);
			else if (e.getSource() == commonComponents[SIGN_UP_PANEL][4][1] || e.getSource() == commonComponents[SIGN_UP_PANEL][5][1])
			{
				if (passwordFieldCheck(SIGN_UP_PANEL, 4))
				{
					commonComponents[SIGN_UP_PANEL][4][1].setBackground(BugByteLibrary.SUCCESS_COLOUR);
					commonComponents[SIGN_UP_PANEL][5][1].setBackground(BugByteLibrary.SUCCESS_COLOUR);
				}
				else if (((JTextField)commonComponents[SIGN_UP_PANEL][5][1]).getText().length() > 0)
				{
					commonComponents[SIGN_UP_PANEL][4][1].setBackground(BugByteLibrary.FAILURE_COLOUR);
					commonComponents[SIGN_UP_PANEL][5][1].setBackground(BugByteLibrary.FAILURE_COLOUR);
				}
			}
			else if (e.getSource() == commonComponents[ACCOUNT_SUMMARY_PANEL][4][1])
				commonComponents[ACCOUNT_SUMMARY_PANEL][4][1].setBackground(Color.WHITE);
		}
		else if (e.getSource() == commonComponents[FORGOT_PASSWORD_PANEL][0][1])
		{
			submitButton.setEnabled(BugByteLibrary.isValidUsername(((JTextField)commonComponents[FORGOT_PASSWORD_PANEL][0][1]).getText()));
			resetPasswordMessageLbl.setForeground(BugByteLibrary.BACKGROUND_COLOUR);
			if (e.getKeyChar() == KeyEvent.VK_ENTER)
				submitButton.doClick();

			if (((JTextField)commonComponents[FORGOT_PASSWORD_PANEL][0][1]).getText().length() == 0)
				commonComponents[FORGOT_PASSWORD_PANEL][0][1].setBackground(Color.WHITE);
			else
				commonComponents[FORGOT_PASSWORD_PANEL][0][1].setBackground(BugByteLibrary.isValidUsername(((JTextField)commonComponents[FORGOT_PASSWORD_PANEL][0][1]).getText()) ? BugByteLibrary.SUCCESS_COLOUR : BugByteLibrary.FAILURE_COLOUR);
		}
		else if (e.getSource() == commonComponents[FORGOT_PASSWORD_PANEL][1][1])
		{
			submitButton.setEnabled(BugByteLibrary.isValidEmailAddress(((JTextField)commonComponents[FORGOT_PASSWORD_PANEL][1][1]).getText()));
			resetPasswordMessageLbl.setForeground(BugByteLibrary.BACKGROUND_COLOUR);
			if (e.getKeyChar() == KeyEvent.VK_ENTER)
				submitButton.doClick();
			if (((JTextField)commonComponents[FORGOT_PASSWORD_PANEL][1][1]).getText().length() == 0)
				commonComponents[FORGOT_PASSWORD_PANEL][1][1].setBackground(Color.WHITE);
			else
				commonComponents[FORGOT_PASSWORD_PANEL][1][1].setBackground(BugByteLibrary.isValidEmailAddress(((JTextField)commonComponents[FORGOT_PASSWORD_PANEL][1][1]).getText()) ? BugByteLibrary.SUCCESS_COLOUR : BugByteLibrary.FAILURE_COLOUR);
		}
		else if (e.getSource() == commonComponents[FORGOT_USERNAME_PANEL][0][1])
		{
			submitButton2.setEnabled(BugByteLibrary.isValidEmailAddress(((JTextField)commonComponents[FORGOT_USERNAME_PANEL][0][1]).getText()));
			forgotUsernameMessageLbl.setForeground(BugByteLibrary.BACKGROUND_COLOUR);
			if (e.getKeyChar() == KeyEvent.VK_ENTER)
				submitButton2.doClick();
			if (((JTextField)commonComponents[FORGOT_USERNAME_PANEL][0][1]).getText().length() == 0)
				commonComponents[FORGOT_USERNAME_PANEL][0][1].setBackground(Color.WHITE);
			else
				commonComponents[FORGOT_USERNAME_PANEL][0][1].setBackground(BugByteLibrary.isValidEmailAddress(((JTextField)commonComponents[FORGOT_USERNAME_PANEL][0][1]).getText()) ? BugByteLibrary.SUCCESS_COLOUR : BugByteLibrary.FAILURE_COLOUR);
		}
		else if(isInPanel(4, e.getSource()))
		{
			submitSummaryButton.setEnabled(accountSummaryCheck());

			accountSummaryMessageLabel.setForeground(BugByteLibrary.BACKGROUND_COLOUR);
			if (e.getKeyChar() == KeyEvent.VK_ENTER)
				submitSummaryButton.doClick();

			if (e.getSource() == commonComponents[ACCOUNT_SUMMARY_PANEL][1][1])
					commonComponents[ACCOUNT_SUMMARY_PANEL][1][1].setBackground(BugByteLibrary.isValidName(((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][1][1]).getText()) ? BugByteLibrary.SUCCESS_COLOUR : BugByteLibrary.FAILURE_COLOUR);
			if (e.getSource() == commonComponents[ACCOUNT_SUMMARY_PANEL][2][1])
					commonComponents[ACCOUNT_SUMMARY_PANEL][2][1].setBackground(BugByteLibrary.isValidName(((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][2][1]).getText()) ? BugByteLibrary.SUCCESS_COLOUR : BugByteLibrary.FAILURE_COLOUR);
			if (e.getSource() == commonComponents[ACCOUNT_SUMMARY_PANEL][3][1])
					commonComponents[ACCOUNT_SUMMARY_PANEL][3][1].setBackground(BugByteLibrary.isValidEmailAddress(((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][3][1]).getText()) ? BugByteLibrary.SUCCESS_COLOUR : BugByteLibrary.FAILURE_COLOUR);
			if (e.getSource() == commonComponents[ACCOUNT_SUMMARY_PANEL][5][1] 
				|| e.getSource() == commonComponents[ACCOUNT_SUMMARY_PANEL][6][1]
				&& e.getKeyChar() != KeyEvent.VK_ENTER)
			{
				if (passwordFieldCheck(ACCOUNT_SUMMARY_PANEL, 5))
				{
					commonComponents[ACCOUNT_SUMMARY_PANEL][5][1].setBackground(BugByteLibrary.SUCCESS_COLOUR);
					commonComponents[ACCOUNT_SUMMARY_PANEL][6][1].setBackground(BugByteLibrary.SUCCESS_COLOUR);
				}
				else if (((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][6][1]).getText().length() > 0)
				{
					commonComponents[ACCOUNT_SUMMARY_PANEL][5][1].setBackground(BugByteLibrary.FAILURE_COLOUR);
					commonComponents[ACCOUNT_SUMMARY_PANEL][6][1].setBackground(BugByteLibrary.FAILURE_COLOUR);
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
	}

	@Override
	public void keyTyped(KeyEvent e){}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		//Handle all change events
		if (e.getSource() == dashboardPanel)
		{
			for (int i = 0; i < dashboardPanel.getTabCount(); i++)
			{
				dashboardPanel.setForegroundAt(i, BugByteLibrary.ACCENT_COLOUR);
				dashboardPanel.setBackgroundAt(i, NOT_OSX ? BugByteLibrary.BACKGROUND_COLOUR : Color.BLACK);
			}
			dashboardPanel.setForegroundAt(dashboardPanel.getSelectedIndex(), NOT_OSX ? BugByteLibrary.BACKGROUND_COLOUR : BugByteLibrary.ACCENT_COLOUR);
			
			//Set visibilty of buttons accordingly
			submitSummaryButton.setVisible(dashboardPanel.getSelectedIndex() == 1 && currentComponent == dashboardPanel);
			saveButton.setVisible(dashboardPanel.getSelectedIndex() == 0 && currentComponent == dashboardPanel);
			revertChangesButton.setVisible(dashboardPanel.getSelectedIndex() == 0 && currentComponent == dashboardPanel);
			addButton.setVisible(dashboardPanel.getSelectedIndex() == 0 && currentComponent == dashboardPanel);
			removeButton.setVisible(dashboardPanel.getSelectedIndex() == 0 && currentComponent == dashboardPanel);

			//Recalculate graph if necessary
			if (dashboardPanel.getSelectedIndex ()== 2)
					graph.recalculate();
		}
	}
	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		//Display information on the selected bug
		if (e.getSource() == bugList)
			loadBug();
	}

/**
	Show the frame.
*/
	public void show()
	{
		frame.setVisible(true);
	}
}