import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.Font;
import java.awt.Color;
import java.awt.BorderLayout;

public class TitlePanel extends JPanel
{
	private JLabel title, panelText, panelImage;
	private JPanel textPanel, redStripe;
	private String currentPanel;

	private Font titleFont, bodyTextFont;

	public TitlePanel(String currentPanel)
	{
		setBackground(Color.WHITE);
		setBorder(new EmptyBorder(10, 10, 10, 10) );
		setLayout(new BorderLayout());

		panelImage = new JLabel(new ImageIcon("res/bug.png"));

		redStripe = new JPanel();
		redStripe.setBackground(Color.RED);

		titleFont 		= new Font ("Arial", Font.BOLD, 36);
		bodyTextFont 	= new Font("Arial", Font.PLAIN, 14);

		title 		= new JLabel("Bug Report System (BRS)", SwingConstants.CENTER);
		panelText 	= new JLabel(currentPanel, SwingConstants.RIGHT);

		panelText.setForeground(Color.DARK_GRAY);

		textPanel = new JPanel();
		textPanel.setBackground(Color.WHITE);
		textPanel.setLayout(new BorderLayout());

		title.setFont(titleFont);
		panelText.setFont(bodyTextFont);

		textPanel.add(title, BorderLayout.CENTER);
		textPanel.add(panelText, BorderLayout.AFTER_LAST_LINE);

		add(textPanel, BorderLayout.CENTER);
		add(panelImage, BorderLayout.WEST);
		add(redStripe, BorderLayout.SOUTH);

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