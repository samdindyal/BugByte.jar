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
	private JLabel 	title, panelImage;

	public TitlePanel()
	{
		setBorder(new EmptyBorder(10, 10, 10, 10));

		panelImage = new JLabel(new ImageIcon(TitlePanel.class.getResource("res/logo.png")));
		panelImage.setBorder(new EmptyBorder(0, 10, 0, 10));

		
		title = new JLabel("BugByte", SwingConstants.LEFT);
		title.setForeground(BugByteLibrary.MAIN_COLOUR);
		title.setFont(BugByteLibrary.TITLE_FONT);
		
		add(panelImage);
		add(title);
	}

	//Self-testing main
	public static void main(String[] args)
	{
		ComponentViewer viewer = new ComponentViewer(new TitlePanel());
		viewer.pack();
	}
}