/**
 	Title: 			The "Graph" class
	Date Written: 	March 2015 - April 2015
	Author: 		Samuel Dindyal
	Description: 	A panel which contains a graph based on given parameters.
*/

import javax.swing.JScrollPane;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.geom.Line2D;

import java.util.ArrayList;
import java.util.Collections;

public class Graph extends JPanel
{

	//Instance variables
	private Line2D 				x_axis, y_axis;
	private int 				margin, horizontalSpacing, verticalSpacing;
	private Color 				foreground, background;
	private Trend 				trend;
	private ArrayList<Double> 	list;
	private double 				scale, numericalVerticalSpacing;
	private int[]				xAxisValues, yAxisValues;

/**
	Construct a "Graph" object with foreground and background colours, as well as a margin and list of x and y coordinates.

	@param 	foreground 	The foreground colour of the panel.
	@param 	background 	The background colour of the panel.
	@param 	margin	 	The margin of the graph. This is also the x location for the x axis and the y location for the y axis.
	@param 	list 		An array list containing the y coordinates of a set. 
*/
	public Graph(Color foreground, Color background, int margin, ArrayList<Double> list)
	{
		//Set instance variables
		this.background = background;
		this.foreground = foreground;
		this.margin 	= margin;
		this.list 		= list;
		
		//Position everything for painting
		calculateNumericalVerticalSpacing();
		calculateXAxis();
		calculateYAxis();
		calculateHorizontalSpacing();
		calculateVerticalSpacing();

		//Create a trend from the coordinates
		trend = new Trend(Color.GREEN, list, margin, 500, 500);

		//Create axes
		x_axis = new Line2D.Double(margin, getHeight()-margin, getWidth(), getHeight()-margin);
		y_axis = new Line2D.Double(margin, 0, margin, getHeight()-margin);
	}

/**
		Recalculate the locations of all elements being painted on the panel.
*/
	public void recalculate()
	{
		calculateXAxis();
		calculateYAxis();
		calculateHorizontalSpacing();
		calculateVerticalSpacing();
		calculateNumericalVerticalSpacing();
		trend = new Trend(Color.GREEN, list, margin, 500, 500);

		x_axis = new Line2D.Double(margin, getHeight()-margin, getWidth(), getHeight()-margin);
		y_axis = new Line2D.Double(margin, 0, margin, getHeight()-margin);
	}

	@Override
	public void paintComponent(Graphics g)
	{
		//Draw background
		g.setColor(background);
		g.fillRect(0, 0, getWidth(), getHeight());

		//Draw axes
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(foreground);
		g2.draw(x_axis);
		g2.draw(y_axis);

		//Draw axes labels
		for (int i = 0; i < list.size(); i++)
			g.drawString(xAxisValues[i] + "", margin + (horizontalSpacing * i), getHeight()-margin + 50);
		for (int i = 0; i < yAxisValues.length; i++)
			g.drawString(yAxisValues[i] + "", margin - 50, getHeight() - margin - (verticalSpacing*i));

		//Draw points and line connecting them
		trend.draw(g2);
	}	

/**
	Add the numbers on the x axis to an array.
*/
	public void calculateXAxis()
	{
		xAxisValues = new int[list.size()];
		for (int i = 0; i < xAxisValues.length; i++)
			xAxisValues[i] = i;
	}

/**
	Calculate the numbers on the y axis and addes to to an array.
*/
	public void calculateYAxis()
	{
		yAxisValues = new int [5];
		for (int i = 0; i < yAxisValues.length; i++)
			yAxisValues[i] = (int)(i*numericalVerticalSpacing);
	}

/**
 	Calculate the scale based on the biggest value in the list of coordinates.
*/
	public void calculateScale(){scale = (getHeight() - margin)/Collections.max(list);}
	
/**
 	Calculate the horizontal spacing between labels on the x axis based on the amount of coordinates.
*/
	public void calculateHorizontalSpacing(){horizontalSpacing = (getWidth() - margin)/list.size();}
	
/**
	Calculate the vertical spacing between labels on the y axis.
*/
	public void calculateVerticalSpacing(){verticalSpacing = (getHeight()-margin)/5;}
	
/**
	Calculate the numerical spacing between the numbers on the y axis.
*/
	public void calculateNumericalVerticalSpacing(){numericalVerticalSpacing = ((Collections.max(list) - Collections.min(list))/list.size());}

	//Self-testing main
	public static void main (String[] args)
	{
		ArrayList<Double> list = new ArrayList<Double>();
		list.add(20.0);
		list.add(30.0);
		list.add(20.0);
		list.add(10.0);
		list.add(40.0);		

		Graph grapher = new Graph(Color.WHITE, Color.DARK_GRAY.darker(), 75, list);
		ComponentViewer componentViewer = new ComponentViewer(grapher, 500, 500);

		componentViewer.validate();
		componentViewer.repaint();
		grapher.recalculate();
		
	}
}