import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.BorderLayout;

public class UserInterface extends JFrame
{
	private JPanel titlePanel, loginPanel;
	public UserInterface()
	{
		setSize(720, 480);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.WHITE);

		setLayout(new BorderLayout());

		titlePanel = new TitlePanel("login");
		loginPanel = new LoginPanel();

		add(titlePanel, BorderLayout.NORTH);
		add(loginPanel, BorderLayout.CENTER);


		setVisible(true);
	}


}