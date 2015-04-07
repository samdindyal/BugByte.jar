import javax.swing.JFrame;
import javax.swing.JComponent;

public class ComponentViewer extends JFrame
{
	public ComponentViewer(JComponent component)
	{
		add(component);
		pack();
		setVisible(true);
	}
}