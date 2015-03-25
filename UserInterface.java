import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.BorderLayout;

public class UserInterface extends JFrame
{
	private JPanel titlePanel;
	public UserInterface()
	{
		setSize(720, 480);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.WHITE);

		setLayout(new BorderLayout());

		titlePanel = new TitlePanel("login");

		add(titlePanel, BorderLayout.NORTH);


		setVisible(true);
	}


}