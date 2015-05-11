package model;

import java.io.Serializable;

public class Point implements Serializable{
	private static final long serialVersionUID = -1474684134467098291L;
	public double x, y;
	
	public Point(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
}
