import javax.swing.JScrollPane;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.geom.Line2D;

import java.util.ArrayList;
import java.util.Collections;

public class Grapher extends JPanel
{

	private Line2D 				x_axis, y_axis;
	private int 				margin, horizontalSpacing, verticalSpacing;
	private Color 				foreground, background;
	private Trend 				trend;
	private ArrayList<Double> 	list;
	private double 				scale, numericalVerticalSpacing;
	private int[]				xAxisValues, yAxisValues;

	public Grapher(Color foreground, Color background, int margin, ArrayList<Double> list)
	{
		this.background = background;
		this.foreground = foreground;
		this.margin 	= margin;
		this.list 		= list;
		
		calculateNumericalVerticalSpacing();
		calculateXAxis();
		calculateYAxis();
		calculateHorizontalSpacing();
		calculateVerticalSpacing();

		trend = new Trend(Color.GREEN, list, margin, 500, 500);

		x_axis = new Line2D.Double(margin, getHeight()-margin, getWidth(), getHeight()-margin);
		y_axis = new Line2D.Double(margin, 0, margin, getHeight()-margin);
	}

	public void recalculate()
	{
		calculateXAxis();
		calculateYAxis();
		calculateHorizontalSpacing();
		calculateVerticalSpacing();
		calculateNumericalVerticalSpacing();
		trend = new Trend(Color.GREEN, list, margin, 500, 500);
		repaint();
	}

	public void paintComponent(Graphics g)
	{
		g.setColor(background);
		g.fillRect(0, 0, getWidth(), getHeight());

		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(foreground);
		g2.draw(x_axis);
		g2.draw(y_axis);

		for (int i = 0; i < list.size(); i++)
			g.drawString(xAxisValues[i] + "", margin + (horizontalSpacing * i), getHeight()-margin + 50);
		for (int i = 0; i < yAxisValues.length; i++)
			g.drawString(yAxisValues[i] + "", margin - 50, getHeight() - margin - (verticalSpacing*i));

		trend.draw(g2);
	}	

	public void calculateXAxis()
	{
		xAxisValues = new int[list.size()];
		for (int i = 0; i < xAxisValues.length; i++)
			xAxisValues[i] = i;
	}

	public void calculateYAxis()
	{
		yAxisValues = new int [5];
		for (int i = 0; i < yAxisValues.length; i++)
			yAxisValues[i] = (int)(i*numericalVerticalSpacing);
	}

	public void calculateScale(){scale = (getHeight() - margin)/Collections.max(list);}
	public void calculateHorizontalSpacing(){horizontalSpacing = (getWidth() - margin)/list.size();}
	public void calculateVerticalSpacing(){verticalSpacing = (getHeight()-margin)/5;}
	public void calculateNumericalVerticalSpacing(){numericalVerticalSpacing = ((Collections.max(list) - Collections.min(list))/list.size());}
	public void changeList(ArrayList<Double> list){this.list = list;}

	//Self-testing main
	public static void main (String[] args)
	{
		ArrayList<Double> list = new ArrayList<Double>();
		list.add(20.0);
		list.add(30.0);
		list.add(20.0);
		list.add(10.0);
		list.add(40.0);		

		new ComponentViewer(new Grapher(Color.WHITE, Color.DARK_GRAY.darker(), 75, list));
	}
}