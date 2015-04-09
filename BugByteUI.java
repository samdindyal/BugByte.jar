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
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


import java.util.UUID;

import java.io.File;

public class BugByteUI implements ActionListener, MouseListener, KeyListener, ChangeListener, ListSelectionListener
{
	private 				BugReportSystem 		bugReportSystem;
	private					GridBagConstraints 		c;
	private 				JComponent  			currentComponent, previousComponent;
	
	private 				Color 					mainColour, accentColour, successColour, failureColour, backgroundColour;
	private 				Font 					subtitle;
	private					String					currentUserID, password;
	private static final 	boolean					NOT_OSX = !System.getProperty("os.name").startsWith("Mac OS X");
	private static final 	int 					LOGIN_PANEL 			= 0,
													FORGOT_PASSWORD_PANEL 	= 1,
													FORGOT_USERNAME_PANEL 	= 2,
													SIGN_UP_PANEL 			= 3,
													ACCOUNT_SUMMARY_PANEL 	= 4,
													VIEW_BUG_PANEL 			= 5;
	//Common Components
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
	private Grapher grapher;

/**
	Creates a new BugByteUI object
*/
	public BugByteUI()
	{
		initializeColours();
		initializeFonts();

		bugReportSystem = new BugReportSystem("bugreportsystem.bb");
		
		initializeFrame();
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
		titlePanel.setBackground(backgroundColour);
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
											new JPasswordField("", NOT_OSX ? 20 : 15) : new JTextField("", NOT_OSX ? 20 : 15);
				commonComponents[i][j][0].setForeground(accentColour);
				commonComponents[i][j][1].addKeyListener(this);

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

		loginButton = new JButton("Log In");
		
		//Set text colour for some components

		forgotPassword.setForeground(accentColour);
		forgotUsername.setForeground(accentColour);
		signUp.setForeground(accentColour);
		loginStatus.setForeground(backgroundColour);

		loginButton.setEnabled(false);

		c.gridx = 0;
		c.gridy = 0;

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
		backButton.setEnabled(false);

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

		for (int i = 0; i < commonComponents[FORGOT_USERNAME_PANEL][0].length; i++)
		{
			if (i == 0)
				commonComponents[FORGOT_USERNAME_PANEL][0][i].setForeground(accentColour);
			else
				commonComponents[FORGOT_USERNAME_PANEL][0][1].addKeyListener(this);	
			inputLinePanel.add(commonComponents[FORGOT_USERNAME_PANEL][0][i]);	
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

		for (int i = 0; i < commonComponents[FORGOT_PASSWORD_PANEL][0].length; i++)
			usernameResetPanel.add(commonComponents[FORGOT_PASSWORD_PANEL][0][i]);
	}

	public void initializeEmailAddressResetPanel()
	{
		emailAddressResetPanel = new JPanel();
		emailAddressResetPanel.setBackground(backgroundColour);

		for (int i = 0; i < commonComponents[FORGOT_PASSWORD_PANEL][1].length; i++)
			emailAddressResetPanel.add(commonComponents[FORGOT_PASSWORD_PANEL][1][i]);
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
		initializeBugsPanel();
		initializeTrendsPanel();

		dashboardPanel.addTab("Bugs", bugsPanel);
		dashboardPanel.addTab("Account Summary", accountSummaryPanel);
		dashboardPanel.addTab("Trends", trendsPanel);
	}

	public void initializeAccountSummaryPanel()
	{
		accountSummaryPanel = new JPanel();
		accountSummaryPanel.setLayout(new GridBagLayout());
		accountSummaryPanel.setBackground(backgroundColour);

		if (NOT_OSX)
			accountSummaryPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(accentColour), "", TitledBorder.CENTER, TitledBorder.TOP, subtitle, accentColour));
		accountSummaryMessageLabel 	= new JLabel("All changes have been sucessfully saved."); 

		submitSummaryButton = new JButton("Submit changes");
		accountSummaryMessageLabel.setForeground(backgroundColour);

		c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;

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

		navigationPanel.add(submitSummaryButton);

		submitSummaryButton.setVisible(false);
		submitSummaryButton.setEnabled(false);

		submitSummaryButton.addActionListener(this);
	}

	public void initializeTrendsPanel()
	{
		trendsPanel = new JPanel();
		trendsPanel.setLayout(new BorderLayout());
		trendsPanel.setBackground(backgroundColour);
		if (NOT_OSX)
			trendsPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(accentColour), "", TitledBorder.CENTER, TitledBorder.TOP, subtitle, accentColour));

		grapher = new Grapher(accentColour, backgroundColour, 60, bugReportSystem.getStatistics());
		trendsPanel.add(grapher, BorderLayout.CENTER);
	}

	public void initializeBugsPanel()
	{
		UIManager.put("List.background", backgroundColour);
		UIManager.put("List.foreground", accentColour);
		UIManager.put("List.selectionBackground", accentColour);
		UIManager.put("List.selectionForeground", backgroundColour);

		initializeViewBugPanel();

		bugsPanel = new JPanel();
		bugsPanel.setLayout(new BorderLayout());
		bugsPanel.setBackground(backgroundColour);
		if (NOT_OSX)
			bugsPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(accentColour), "", TitledBorder.CENTER, TitledBorder.TOP, subtitle, accentColour));

		bugList = new JList();
		bugList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane listScrollPane 		= new JScrollPane(bugList);
		JScrollPane viewBugScrollPanel 	= new JScrollPane(viewBugPanel);

		viewBugScrollPanel.setBorder(BorderFactory.createEmptyBorder());
		listScrollPane.setBorder(BorderFactory.createEmptyBorder());

		bugSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, viewBugScrollPanel);
        bugSplitPane.setOneTouchExpandable(false);
        bugSplitPane.setDividerLocation(150);
        bugSplitPane.setBackground(backgroundColour);
        bugSplitPane.setOneTouchExpandable(true);

        bugList.setBackground(backgroundColour);
        bugList.addListSelectionListener(this);

        bugsPanel.add(bugSplitPane, BorderLayout.CENTER);
	}

	public void initializeViewBugPanel()
	{
		viewBugPanel = new JPanel();
		viewBugPanel.setBackground(backgroundColour);
		viewBugPanel.setLayout(new GridBagLayout());

		saveButton 			= new JButton("Save Bug");
		revertChangesButton = new JButton("Revert Changes");
		addButton 			= new JButton("+");
		removeButton		= new JButton("-");

		navigationPanel.add(saveButton);
		navigationPanel.add(revertChangesButton);
		navigationPanel.add(addButton);
		navigationPanel.add(removeButton);

		saveButton.addActionListener(this);
		revertChangesButton.addActionListener(this);
		viewBugPanel.addKeyListener(this);
		addButton.addActionListener(this);
		removeButton.addActionListener(this);

		saveButton.setVisible(false);
		revertChangesButton.setVisible(false);
		addButton.setVisible(false);
		removeButton.setVisible(false);

		commonComponents[VIEW_BUG_PANEL][1][1] = new JTextField("", 25);
		commonComponents[VIEW_BUG_PANEL][1][1].setEnabled(false);
		((JTextArea)commonComponents[VIEW_BUG_PANEL][2][1]).setLineWrap(true);
		((JTextArea)commonComponents[VIEW_BUG_PANEL][2][1]).setWrapStyleWord(true);

		c = new GridBagConstraints();

		c.gridy = 0;
		c.gridx = 0;
		c.insets = new Insets(10, 0, 10, 0);

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

	public void prepareDashBoardPanel()
	{
		UIManager.put("TabbedPane.contentAreaColor ",backgroundColour);
		UIManager.put("TabbedPane.selected", accentColour);
		UIManager.put("TabbedPane.unselected", backgroundColour);
		UIManager.put("TabbedPane.tabAreaForeground", backgroundColour);
  		UIManager.put("TabbedPane.background",backgroundColour);
  		UIManager.put("TabbedPane.HightLight", accentColour);
  		UIManager.put("TabbedPane.borderHightlightColor", accentColour);
  		UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));

		dashboardPanel = new JTabbedPane();
		dashboardPanel.setForeground(accentColour);

		dashboardPanelBorder = BorderFactory.createTitledBorder(NOT_OSX ? new LineBorder(mainColour) : null, "Dashboard", TitledBorder.CENTER, TitledBorder.TOP, subtitle, mainColour);
		dashboardPanel.setBorder(dashboardPanelBorder);
		for (int i = 0; i < dashboardPanel.getTabCount(); i++)
		{
			dashboardPanel.setForegroundAt(i, accentColour);
			dashboardPanel.setBackgroundAt(i, NOT_OSX ? backgroundColour : Color.BLACK);
		}

		dashboardPanel.addChangeListener(this);
	}

	public void initializeColours()
	{
		mainColour = new Color(72, 157, 2);
		accentColour = mainColour.brighter().brighter();

		successColour 		= new Color(152, 255, 152);
		failureColour 		= new Color(255, 152, 152);
		backgroundColour 	= Color.DARK_GRAY.darker();

		UIManager.put("TextField.selectionBackground", accentColour);
		UIManager.put("TextArea.selectionBackground", accentColour);
		UIManager.put("PasswordField.selectionBackground", accentColour);
	}

	public void initializeFonts()
	{
		try{subtitle = Font.createFont(Font.TRUETYPE_FONT, new File("res/FORCED_SQUARE.ttf")).deriveFont(28f);}
		catch(Exception e){subtitle = new Font("Arial", Font.BOLD, 14);}
	}

	public void swapComponents(JComponent component)
	{
		frame.remove(currentComponent);
		frame.add(component, BorderLayout.CENTER);
		frame.validate();
		frame.repaint();

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

		previousComponent 		= currentComponent;
		currentComponent 		= component;
	}

	public void swapPasswordResetPanels(JPanel panel)
	{
		if (panel == usernameResetPanel)
		{
			forgotPasswordPanel.remove(emailAddressResetPanel);
			submitButton.setEnabled(BugByteLibrary.isValidUsername(((JTextField)commonComponents[FORGOT_PASSWORD_PANEL][0][1]).getText()));
		}
		else if (panel == emailAddressResetPanel)
			{
				forgotPasswordPanel.remove(usernameResetPanel);
				submitButton.setEnabled(BugByteLibrary.isValidEmailAddress(((JTextField)commonComponents[FORGOT_PASSWORD_PANEL][1][1]).getText()));
			}

			c.gridy = 1;
			c.gridx = 0;

			forgotPasswordPanel.add(panel, c);
			forgotPasswordPanel.validate();
			forgotPasswordPanel.repaint();
	}

	

	public boolean passwordFieldCheck(int panelIdentifier, int fieldNumber)
	{
		return		new String(((JPasswordField)commonComponents[panelIdentifier][fieldNumber][1]).getPassword()).equals(new String(((JPasswordField)commonComponents[panelIdentifier][fieldNumber+1][1]).getPassword()))
				&& BugByteLibrary.isValidPassword(new String(((JPasswordField)commonComponents[panelIdentifier][fieldNumber][1]).getPassword()));
	}

	public void toggleLogin()
	{
		if (!bugReportSystem.hasActiveUsers())
		{
			if (bugReportSystem.login(((JTextField)commonComponents[LOGIN_PANEL][0][1]).getText(), String.valueOf(((JPasswordField)commonComponents[LOGIN_PANEL][1][1]).getPassword())))
			{
				resetComponent(SIGN_UP_PANEL);
				resetComponent(FORGOT_PASSWORD_PANEL);
				resetComponent(FORGOT_USERNAME_PANEL);

				password = new String(((JPasswordField)commonComponents[LOGIN_PANEL][1][1]).getPassword());

								createNewBug();
				
				currentUserID = ((JTextField)commonComponents[LOGIN_PANEL][0][1]).getText();
				loginStatus.setForeground(backgroundColour);
				swapComponents(dashboardPanel);

				System.out.println("Login successful.");

				commonComponents[LOGIN_PANEL][0][1].setEnabled(false);
				commonComponents[LOGIN_PANEL][1][1].setEnabled(false);

				loginButton.setText("Logout");

				loginBorder.setTitle("Logged in as " + ((JTextField)commonComponents[LOGIN_PANEL][0][1]).getText());

				populateAccountSummaryFields();
				bugList.setListData(generateBugList());

				forgotUsername.setForeground(backgroundColour.brighter());
				forgotPassword.setForeground(backgroundColour.brighter());
				signUp.setForeground(backgroundColour.brighter());
				commonComponents[LOGIN_PANEL][0][1].setBackground(Color.WHITE);
				commonComponents[LOGIN_PANEL][1][1].setBackground(Color.WHITE);
			}
			else
			{
				loginStatus.setForeground(Color.RED);
				loginStatus.setText("Incorrect login information. Please try again.");
				System.out.println("Login failed. Incorrect credentials.");

				commonComponents[LOGIN_PANEL][0][1].setBackground(failureColour);
				commonComponents[LOGIN_PANEL][1][1].setBackground(failureColour);

				return;
			}
		}
		else if (bugReportSystem.logout(currentUserID))
		{
			resetComponent(ACCOUNT_SUMMARY_PANEL);

			currentUserID = "";
			password = "";
			loginBorder.setTitle("Login");
			swapComponents(loginPanel);

			commonComponents[LOGIN_PANEL][0][1].setEnabled(true);
			commonComponents[LOGIN_PANEL][1][1].setEnabled(true);
			loginButton.setEnabled(false);

			((JTextField)commonComponents[LOGIN_PANEL][0][1]).setText("");
			((JPasswordField)commonComponents[LOGIN_PANEL][1][1]).setText("");

			loginButton.setText("Login");

			dashboardButton.setEnabled(false);
			forgotUsername.setForeground(accentColour);
			forgotPassword.setForeground(accentColour);
			signUp.setForeground(accentColour);

			loginStatus.setForeground(accentColour);
			loginStatus.setText("You have successfully logged out.");

			System.out.println("Logout successful.");
		}

	}

	public void signUp()
	{
		if (bugReportSystem.addUser(	((JTextField)commonComponents[SIGN_UP_PANEL][0][1]).getText(),
							new String(((JPasswordField)commonComponents[SIGN_UP_PANEL][4][1]).getPassword()),
							((JTextField)commonComponents[SIGN_UP_PANEL][1][1]).getText(),
							((JTextField)commonComponents[SIGN_UP_PANEL][2][1]).getText(),
							((JTextField)commonComponents[SIGN_UP_PANEL][3][1]).getText()))
		{
			currentUserID 	= ((JTextField)commonComponents[SIGN_UP_PANEL][0][1]).getText();
			password 		= new String(((JPasswordField)commonComponents[SIGN_UP_PANEL][4][1]).getPassword());
			bugReportSystem.writeToDisk();
			createNewBug();
			swapComponents(dashboardPanel);
			System.out.println("Sign Up Successful.");

			commonComponents[LOGIN_PANEL][0][1].setEnabled(false);
			commonComponents[LOGIN_PANEL][1][1].setEnabled(false);
			loginButton.setEnabled(true);

			((JTextField)commonComponents[LOGIN_PANEL][0][1]).setText(((JTextField)commonComponents[SIGN_UP_PANEL][0][1]).getText());
			((JPasswordField)commonComponents[LOGIN_PANEL][1][1]).setText(new String(((JPasswordField)commonComponents[SIGN_UP_PANEL][4][1]).getPassword()));

			loginButton.setText("Logout");
			loginStatus.setForeground(backgroundColour);
			commonComponents[LOGIN_PANEL][0][1].setBackground(Color.WHITE);
			commonComponents[LOGIN_PANEL][1][1].setBackground(Color.WHITE);

			previousComponent 		= loginPanel;

			bugReportSystem.login(((JTextField)commonComponents[SIGN_UP_PANEL][0][1]).getText(), password);

			populateAccountSummaryFields();
		}
		else
		{
			failedSignUpLbl.setForeground(Color.RED);
			failedSignUpLbl.setText("Sorry, that username has already been taken.");
			System.out.println("Username already taken. Sign Up Unsuccessful.");
			commonComponents[SIGN_UP_PANEL][0][1].setBackground(failureColour);
		}

		signUpButton.setEnabled(false);
	}

	public void populateAccountSummaryFields()
	{
		User user = bugReportSystem.getUserAccount(currentUserID, password);
		
		((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][0][1]).setText(user.getUsername());
		((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][1][1]).setText(user.getFirstName());
		((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][2][1]).setText(user.getLastName());
		((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][3][1]).setText(user.getEmailAddress());
	}

	public void resetPassword()
	{
		resetPasswordMessageLbl.setForeground(accentColour);
		if (emailAddressResetButton.isSelected())
			resetPasswordMessageLbl.setText("An email with a password reset link has been sent to " + ((JTextField)commonComponents[FORGOT_PASSWORD_PANEL][1][1]).getText() + ".");
		else
			resetPasswordMessageLbl.setText("An email with a password reset link has been sent to the email associated it your account.");
	}

	public boolean submitAccountChanges(String password)
	{
		User user;
		if ((user = bugReportSystem.getUserAccount(currentUserID, new String(((JPasswordField)commonComponents[ACCOUNT_SUMMARY_PANEL][4][1]).getPassword()))) == null)
		{
			accountSummaryMessageLabel.setForeground(Color.RED);
			accountSummaryMessageLabel.setText("Incorrect password. Please try again.");
			commonComponents[ACCOUNT_SUMMARY_PANEL][4][1].setBackground(failureColour);
		 	return false;
		}

		user.setfirstName(((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][1][1]).getText());
		user.setlastName(((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][2][1]).getText());
		user.setEmailAddress(((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][3][1]).getText());

		if (password.length() > 0)
			user.setPassword(password);

		accountSummaryMessageLabel.setForeground(accentColour);
		accountSummaryMessageLabel.setText("All changes have been sucessfully saved.");

		for (int i = 0; i < commonComponents[ACCOUNT_SUMMARY_PANEL].length; i++)
			commonComponents[ACCOUNT_SUMMARY_PANEL][i][1].setBackground(Color.WHITE);

		bugReportSystem.writeToDisk();

		return true;
	}

	public boolean accountSummaryCheck()
	{
		return 		BugByteLibrary.isValidName(((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][1][1]).getText())
				&&	BugByteLibrary.isValidName(((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][2][1]).getText())
				&&	BugByteLibrary.isValidEmailAddress(((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][3][1]).getText())
				&&  (new String(((JPasswordField)commonComponents[ACCOUNT_SUMMARY_PANEL][5][1]).getPassword()).equals(new String(((JPasswordField)commonComponents[ACCOUNT_SUMMARY_PANEL][6][1]).getPassword()))
					&& ((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][5][1]).getText().length() > 0)
				&&  ((JPasswordField)commonComponents[ACCOUNT_SUMMARY_PANEL][4][1]).getPassword().length > 0;
	}

	public boolean signUpCheck()
	{
		return 		BugByteLibrary.isValidUsername(((JTextField)commonComponents[SIGN_UP_PANEL][0][1]).getText())
				&&  ((JPasswordField)commonComponents[SIGN_UP_PANEL][4][1]).getPassword().length > 0
				&&	new String(((JPasswordField)commonComponents[SIGN_UP_PANEL][5][1]).getPassword()).equals(new String(((JPasswordField)commonComponents[SIGN_UP_PANEL][4][1]).getPassword()))
				&& 	BugByteLibrary.isValidName(((JTextField)commonComponents[SIGN_UP_PANEL][1][1]).getText())
				&&	BugByteLibrary.isValidName(((JTextField)commonComponents[SIGN_UP_PANEL][2][1]).getText())
				&&  BugByteLibrary.isValidEmailAddress(((JTextField)commonComponents[SIGN_UP_PANEL][3][1]).getText());
	}

	public boolean isInPanel(int n, Object obj)
	{
		for (int i = 0; i < commonComponents[n].length; i++)
			if (obj == commonComponents[n][i][1])
				return true;
		return false;
	}

	public void resetComponent(int identifier)
	{
		for (int i = 0; i < commonComponents[identifier].length; i++)
		{
			((JTextField)commonComponents[identifier][i][1]).setText("");
			commonComponents[identifier][i][1].setBackground(Color.WHITE);
		}

		if (identifier == SIGN_UP_PANEL)
			failedSignUpLbl.setForeground(backgroundColour);
		else if (identifier == FORGOT_USERNAME_PANEL)
			forgotUsernameMessageLbl.setForeground(backgroundColour);
		else if (identifier == FORGOT_PASSWORD_PANEL)
			resetPasswordMessageLbl.setForeground(backgroundColour);
		else if (identifier == ACCOUNT_SUMMARY_PANEL)
			accountSummaryMessageLabel.setForeground(backgroundColour);
	}

	public void resetAllComponents()
	{
		for (int i = 0; i < commonComponents.length; i++)
			resetComponent(i);
	}

	public void submitBug()
	{
		User user;
		if ((user = bugReportSystem.getUserAccount(currentUserID, password)) != null)
		{
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

			bugList.setListData(generateBugList());
		}

	}

	public void createNewBug()
	{
		((JTextArea)commonComponents[VIEW_BUG_PANEL][2][1]).setText("");
		((JTextField)commonComponents[VIEW_BUG_PANEL][0][1]).setText("");
		String id = UUID.randomUUID().toString();
		((JTextField)commonComponents[VIEW_BUG_PANEL][1][1]).setText(id);
	}

	public void removeBug()
	{
		String id = ((JTextField)commonComponents[VIEW_BUG_PANEL][1][1]).getText();
		bugReportSystem.removeBug(id);
		bugReportSystem.writeToDisk();
		bugList.setListData(generateBugList());
	}

	public void loadBug()
	{
		if (bugList.getSelectedIndex() < 0)
			return;
		java.util.LinkedList<String> keys = bugReportSystem.getUserAccount(currentUserID, password).getKeys(password);
		Bug bug = bugReportSystem.getBug(keys.get(bugList.getSelectedIndex()));
		((JTextField)commonComponents[VIEW_BUG_PANEL][0][1]).setText(bug.getName());
		((JTextField)commonComponents[VIEW_BUG_PANEL][1][1]).setText(bug.getID());
		((JTextArea)commonComponents[VIEW_BUG_PANEL][2][1]).setText(bug.getDescription());

		((JComboBox)commonComponents[VIEW_BUG_PANEL][3][1]).setSelectedIndex(bug.getPriority().priority);
		((JComboBox)commonComponents[VIEW_BUG_PANEL][4][1]).setSelectedIndex(bug.getStatus().status);
	}

	public String[] generateBugList()
	{
		java.util.LinkedList<String> keys = bugReportSystem.getUserAccount(currentUserID, password).getKeys(password);

		String array[] = new String[keys.size()];

		for (int i = 0; i < keys.size(); i++)
				array[i] = bugReportSystem.getBug(keys.get(i)).getName() + " (" + keys.get(i) + ")";
		return array;

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
		if (e.getSource() == commonComponents[LOGIN_PANEL][0][1] || e.getSource() == commonComponents[LOGIN_PANEL][1][1])
		{
			loginButton.setEnabled(BugByteLibrary.isValidUsername(((JTextField)commonComponents[LOGIN_PANEL][0][1]).getText()) 
				&& ((JPasswordField)commonComponents[LOGIN_PANEL][1][1]).getPassword().length > 0);
			loginStatus.setForeground(backgroundColour);

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
				commonComponents[SIGN_UP_PANEL][0][1].setBackground(BugByteLibrary.isValidUsername(((JTextField)commonComponents[SIGN_UP_PANEL][0][1]).getText()) ? successColour : failureColour);
			else if (e.getSource() == commonComponents[SIGN_UP_PANEL][1][1])
					commonComponents[SIGN_UP_PANEL][1][1].setBackground(BugByteLibrary.isValidName(((JTextField)commonComponents[SIGN_UP_PANEL][1][1]).getText()) ? successColour : failureColour);
			else if (e.getSource() == commonComponents[SIGN_UP_PANEL][2][1])
					commonComponents[SIGN_UP_PANEL][2][1].setBackground(BugByteLibrary.isValidName(((JTextField)commonComponents[SIGN_UP_PANEL][2][1]).getText()) ? successColour : failureColour);
			else if (e.getSource() == commonComponents[SIGN_UP_PANEL][3][1])
					commonComponents[SIGN_UP_PANEL][3][1].setBackground(BugByteLibrary.isValidEmailAddress(((JTextField)commonComponents[SIGN_UP_PANEL][3][1]).getText()) ? successColour : failureColour);
			else if (e.getSource() == commonComponents[SIGN_UP_PANEL][4][1] || e.getSource() == commonComponents[SIGN_UP_PANEL][5][1])
			{
				if (passwordFieldCheck(SIGN_UP_PANEL, 4))
				{
					commonComponents[SIGN_UP_PANEL][4][1].setBackground(successColour);
					commonComponents[SIGN_UP_PANEL][5][1].setBackground(successColour);
				}
				else if (((JTextField)commonComponents[SIGN_UP_PANEL][5][1]).getText().length() > 0)
				{
					commonComponents[SIGN_UP_PANEL][4][1].setBackground(failureColour);
					commonComponents[SIGN_UP_PANEL][5][1].setBackground(failureColour);
				}
			}
			else if (e.getSource() == commonComponents[ACCOUNT_SUMMARY_PANEL][4][1])
				commonComponents[ACCOUNT_SUMMARY_PANEL][4][1].setBackground(Color.WHITE);
		}
		else if (e.getSource() == commonComponents[FORGOT_PASSWORD_PANEL][0][1])
		{
			submitButton.setEnabled(BugByteLibrary.isValidUsername(((JTextField)commonComponents[FORGOT_PASSWORD_PANEL][0][1]).getText()));
			resetPasswordMessageLbl.setForeground(backgroundColour);
			if (e.getKeyChar() == KeyEvent.VK_ENTER)
				submitButton.doClick();

			if (((JTextField)commonComponents[FORGOT_PASSWORD_PANEL][0][1]).getText().length() == 0)
				commonComponents[FORGOT_PASSWORD_PANEL][0][1].setBackground(Color.WHITE);
			else
				commonComponents[FORGOT_PASSWORD_PANEL][0][1].setBackground(BugByteLibrary.isValidUsername(((JTextField)commonComponents[FORGOT_PASSWORD_PANEL][0][1]).getText()) ? successColour : failureColour);
		}
		else if (e.getSource() == commonComponents[FORGOT_PASSWORD_PANEL][1][1])
		{
			submitButton.setEnabled(BugByteLibrary.isValidEmailAddress(((JTextField)commonComponents[FORGOT_PASSWORD_PANEL][1][1]).getText()));
			resetPasswordMessageLbl.setForeground(backgroundColour);
			if (e.getKeyChar() == KeyEvent.VK_ENTER)
				submitButton.doClick();
			if (((JTextField)commonComponents[FORGOT_PASSWORD_PANEL][1][1]).getText().length() == 0)
				commonComponents[FORGOT_PASSWORD_PANEL][1][1].setBackground(Color.WHITE);
			else
				commonComponents[FORGOT_PASSWORD_PANEL][1][1].setBackground(BugByteLibrary.isValidEmailAddress(((JTextField)commonComponents[FORGOT_PASSWORD_PANEL][1][1]).getText()) ? successColour : failureColour);
		}
		else if (e.getSource() == commonComponents[FORGOT_USERNAME_PANEL][0][1])
		{
			submitButton2.setEnabled(BugByteLibrary.isValidEmailAddress(((JTextField)commonComponents[FORGOT_USERNAME_PANEL][0][1]).getText()));
			forgotUsernameMessageLbl.setForeground(backgroundColour);
			if (e.getKeyChar() == KeyEvent.VK_ENTER)
				submitButton2.doClick();
			if (((JTextField)commonComponents[FORGOT_USERNAME_PANEL][0][1]).getText().length() == 0)
				commonComponents[FORGOT_USERNAME_PANEL][0][1].setBackground(Color.WHITE);
			else
				commonComponents[FORGOT_USERNAME_PANEL][0][1].setBackground(BugByteLibrary.isValidEmailAddress(((JTextField)commonComponents[FORGOT_USERNAME_PANEL][0][1]).getText()) ? successColour : failureColour);
		}
		else if(isInPanel(4, e.getSource()))
		{
			submitSummaryButton.setEnabled(accountSummaryCheck());

			accountSummaryMessageLabel.setForeground(backgroundColour);
			if (e.getKeyChar() == KeyEvent.VK_ENTER)
				submitSummaryButton.doClick();

			if (e.getSource() == commonComponents[ACCOUNT_SUMMARY_PANEL][1][1])
					commonComponents[ACCOUNT_SUMMARY_PANEL][1][1].setBackground(BugByteLibrary.isValidName(((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][1][1]).getText()) ? successColour : failureColour);
			if (e.getSource() == commonComponents[ACCOUNT_SUMMARY_PANEL][2][1])
					commonComponents[ACCOUNT_SUMMARY_PANEL][2][1].setBackground(BugByteLibrary.isValidName(((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][2][1]).getText()) ? successColour : failureColour);
			if (e.getSource() == commonComponents[ACCOUNT_SUMMARY_PANEL][3][1])
					commonComponents[ACCOUNT_SUMMARY_PANEL][3][1].setBackground(BugByteLibrary.isValidEmailAddress(((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][3][1]).getText()) ? successColour : failureColour);
			if (e.getSource() == commonComponents[ACCOUNT_SUMMARY_PANEL][5][1] 
				|| e.getSource() == commonComponents[ACCOUNT_SUMMARY_PANEL][6][1]
				&& e.getKeyChar() != KeyEvent.VK_ENTER)
			{
				if (passwordFieldCheck(ACCOUNT_SUMMARY_PANEL, 5))
				{
					commonComponents[ACCOUNT_SUMMARY_PANEL][5][1].setBackground(successColour);
					commonComponents[ACCOUNT_SUMMARY_PANEL][6][1].setBackground(successColour);
				}
				else if (((JTextField)commonComponents[ACCOUNT_SUMMARY_PANEL][6][1]).getText().length() > 0)
				{
					commonComponents[ACCOUNT_SUMMARY_PANEL][5][1].setBackground(failureColour);
					commonComponents[ACCOUNT_SUMMARY_PANEL][6][1].setBackground(failureColour);
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
		if (e.getSource() == dashboardPanel)
		{
			for (int i = 0; i < dashboardPanel.getTabCount(); i++)
			{
				dashboardPanel.setForegroundAt(i, accentColour);
				dashboardPanel.setBackgroundAt(i, NOT_OSX ? backgroundColour : Color.BLACK);
			}
			dashboardPanel.setForegroundAt(dashboardPanel.getSelectedIndex(), NOT_OSX ? backgroundColour : accentColour);
			submitSummaryButton.setVisible(dashboardPanel.getSelectedIndex() == 1 && currentComponent == dashboardPanel);
			saveButton.setVisible(dashboardPanel.getSelectedIndex() == 0 && currentComponent == dashboardPanel);
			revertChangesButton.setVisible(dashboardPanel.getSelectedIndex() == 0 && currentComponent == dashboardPanel);
			addButton.setVisible(dashboardPanel.getSelectedIndex() == 0 && currentComponent == dashboardPanel);
			removeButton.setVisible(dashboardPanel.getSelectedIndex() == 0 && currentComponent == dashboardPanel);

			if (dashboardPanel.getSelectedIndex ()== 2)
					grapher.recalculate();
		}
	}
	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		if (e.getSource() == bugList)
			loadBug();
	}

	public void show()
	{
		frame.setVisible(true);
	}
}