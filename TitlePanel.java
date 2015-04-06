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
	private JLabel 	title, panelImage;
	private Color 	accentColour;

	private Font titleFont;

	public TitlePanel()
	{
		setBorder(new EmptyBorder(10, 10, 10, 10));
		try{
			titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/FORCED_SQUARE.ttf")).deriveFont(72f);
		}catch(Exception e)
		{
			titleFont = new Font ("Arial", Font.BOLD, 36);
		}	

		panelImage = new JLabel(new ImageIcon("res/logo.png"));
		panelImage.setBorder(new EmptyBorder(0, 10, 0, 10));
		accentColour = new Color(72, 157, 2);

		

		title = new JLabel("BugByte", SwingConstants.LEFT);

		title.setForeground(accentColour);

		title.setFont(titleFont);
		
		add(panelImage);
		add(title);
	}
}