/**
 	Title: 			The "Trend" class
	Date Written: 	March 2015 - April 2015
	Author: 		Samuel Dindyal
	Description: 	A painted element consisting of points, defined by an ArrayList, and a line connecting them.
*/

import java.util.ArrayList;
import java.util.Collections;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Trend
{
	//Instance variables
	private Color 				colour;
	private ArrayList<Double> 	yCoordinates;
	private int					margin, horizontalLimit, verticalLimit, spacing;
	private Line2D.Double		line;
	private double 				scale;

/**
	Construct a "Trend" object given a colour, list of y coordinates, a margin and a horizontal and vertical limit for painting.
*/
	public Trend(Color colour, ArrayList<Double> yCoordinates, int margin, int horizontalLimit, int verticalLimit)
	{
		//Set instance variables
		this.colour 			= colour;
		this.yCoordinates 		= yCoordinates;
		this.margin 			= margin;
		this.horizontalLimit	= horizontalLimit;
		this.verticalLimit		= verticalLimit;
		
		calculateScale();
		calculateSpacing();
	}

/**
	Calculate the scale based on the vertical limit.
*/
	public void calculateScale(){scale = ((verticalLimit - margin)/Collections.max(yCoordinates));}
	public void calculateSpacing()
	{
		spacing = (horizontalLimit - margin)/yCoordinates.size();
	}

/**
	Draw the trend and its points.

	@param 	g2 	Graphics to draw onto.
*/
	public void draw(Graphics2D g2)
	{
		int x, y;
		x = y = 0;

		g2.setColor(colour);

		for (int i = 0; i < yCoordinates.size() - 1; i++)
		{

			Point2D.Double p0, p1;

			//Calculate the positioning for a point and the point after it
			p0 = new Point2D.Double(margin + (spacing * i), verticalLimit - margin - 22 - yCoordinates.get(i)*scale);
			p1 = new Point2D.Double(margin + (spacing * (i+1)), verticalLimit- margin-22 - yCoordinates.get(i+1)*scale);

			//Create a line connected the two points
			line = new Line2D.Double(p0, p1);

			//Draw the line and points
			g2.draw(line);

			g2.fill(new Ellipse2D.Double(p0.getX()-5, p0.getY()-5, 10, 10));
			g2.fill(new Ellipse2D.Double(p1.getX()-5, p1.getY()-5, 10, 10));
		}

	}


}