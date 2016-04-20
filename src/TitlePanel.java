/**
 	Title: 			The "TitlePanel" class
	Date Written: 	March 2015 - April 2015
	Author: 		Samuel Dindyal
	Description: 	A panel containing the BugByte logo and name for BugByte app.
*/

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;

import java.io.File;
import java.io.InputStream;

public class TitlePanel extends JPanel
{
	//Instance variables
	private JLabel 	title, panelImage;

/**
	Construct a "TitlePanel" object
*/
	public TitlePanel()
	{
		//Set an empty border for padding
		setBorder(new EmptyBorder(10, 10, 10, 10));

		//Initialize panel for logo and set its padding
		panelImage = new JLabel(new ImageIcon(TitlePanel.class.getResource("res/logo.png")));
		panelImage.setBorder(new EmptyBorder(0, 10, 0, 10));
		
		//Create a JLabel for the title of BugByte
		title = new JLabel("BugByte", SwingConstants.LEFT);

		//Stylize the title
		title.setForeground(BugByteLibrary.MAIN_COLOUR);
		title.setFont(BugByteLibrary.TITLE_FONT);
		
		//Add components to the title panel
		add(panelImage);
		add(title);
	}

	//Self-testing main
	public static void main(String[] args)
	{
		ComponentViewer viewer = new ComponentViewer(new TitlePanel());
	}
}