import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.Font;
import java.awt.Color;
import java.awt.BorderLayout;

import java.io.File;

public class TitlePanel extends JPanel
{
	private JLabel 	title, panelText, panelImage;
	private JPanel 	textPanel;
	private String 	currentPanel;
	private Color 	accentColour;

	private Font titleFont, bodyTextFont;

	public TitlePanel(String currentPanel)
	{
		setBackground(Color.DARK_GRAY);
		setBorder(new EmptyBorder(10, 10, 10, 10));


		try{
			titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/FORCED_SQUARE.ttf")).deriveFont(72f);
		}catch(Exception e)
		{
			titleFont = new Font ("Arial", Font.BOLD, 36);
		}	

		panelImage = new JLabel(new ImageIcon("res/logo.png"));
		panelImage.setBorder(new EmptyBorder(0, 10, 25, 10));
		accentColour = new Color(72, 157, 2);

		bodyTextFont 	= new Font("Arial", Font.PLAIN, 14);

		title 		= new JLabel("BugByte", SwingConstants.CENTER);
		panelText 	= new JLabel(currentPanel, SwingConstants.CENTER);

		panelText.setForeground(accentColour.brighter());
		title.setForeground(accentColour);

		textPanel = new JPanel();
		textPanel.setBackground(Color.DARK_GRAY);
		textPanel.setLayout(new BorderLayout());

		title.setFont(titleFont);
		panelText.setFont(bodyTextFont);

		textPanel.add(title, BorderLayout.CENTER);
		textPanel.add(panelText, BorderLayout.AFTER_LAST_LINE);

		add(panelImage);
		add(textPanel);
		

	}

	public void setCurrentPanel(String newPanelText)
	{
		currentPanel = newPanelText;
		panelText.setText(currentPanel);
	}

	public String getCurrentPanel()
	{
		return currentPanel;
	}
}