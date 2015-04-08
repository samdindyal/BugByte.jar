import java.util.ArrayList;
import java.util.Collections;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D;

public class Trend
{
	private Color 				colour;
	private ArrayList<Double> 	yCoordinates;
	private int					margin, horizontalLimit, verticalLimit, spacing;
	private Line2D.Double		line;
	private double 				scale;

	public Trend(Color colour, ArrayList<Double> yCoordinates, int margin, int horizontalLimit, int verticalLimit)
	{
		this.colour 			= colour;
		this.yCoordinates 		= yCoordinates;
		this.margin 			= margin;
		this.horizontalLimit	= horizontalLimit;
		this.verticalLimit		= verticalLimit;
		
		calculateScale();
		calculateSpacing();

	}

	public void calculateScale()
	{
		scale = ((verticalLimit - margin)/Collections.max(yCoordinates));
	}
	public void calculateSpacing()
	{
		spacing = (horizontalLimit - margin)/yCoordinates.size();
	}

	public void draw(Graphics2D g2)
	{
		int x, y;
		x = y = 0;

		g2.setColor(colour);

		for (int i = 0; i < yCoordinates.size() - 1; i++)
		{

			line = new Line2D.Double(margin + (spacing * i), (verticalLimit - margin - (yCoordinates.get(i))*scale),
										margin + (spacing * (i+1)), ((verticalLimit - margin -(yCoordinates.get(i+1)))*scale));
			g2.fill(new Ellipse2D.Double(margin + (spacing * i)-5, ((verticalLimit - margin - (yCoordinates.get(i)))*scale)-5, 10, 10));
			g2.fill(new Ellipse2D.Double(margin + (spacing * (i+1))-5, (verticalLimit - margin - (yCoordinates.get(i+1))*scale)-5,10, 10));
			g2.draw(line);
		}

	}


}