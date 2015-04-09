/**
 	Title: 			The "ComponentViewer" class
	Date Written: 	March 2015 - April 2015
	Author: 		Samuel Dindyal
	Description: 	A JFrame which displays a single panel. This class is meant for testing purposes.
*/

import javax.swing.JFrame;
import javax.swing.JComponent;

public class ComponentViewer extends JFrame
{

/**
	Constructs a "ComponentViewer" object. The frame is sized according to the panel.

	@param 	component 	Componnet to add and display on the frame.
*/
	public ComponentViewer(JComponent component)
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(component);
		pack();
		setVisible(true);
	}

/**
	Constructs a "ComponentViewer" object. The frame is sized according to the arguments entered.

	@param 	component 	Componnet to add and display on the frame.
	@param 	width 		The width of the component to display.
	@param 	height 		The height of the component to display.
*/
	public ComponentViewer(JComponent component, int width, int height)
	{
		this(component);
		setSize(width, height);
	}
}