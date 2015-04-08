import javax.swing.JFrame;
import javax.swing.JComponent;

public class ComponentViewer extends JFrame
{
	public ComponentViewer(JComponent component)
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(component);
		setSize(component.getWidth(), component.getHeight());
		setVisible(true);
	}

	public ComponentViewer(JComponent component, int width, int height)
	{
		this(component);
		setSize(width, height);
	}
}