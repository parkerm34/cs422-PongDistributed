package model;

import view.PongGUI;

public class Body {
	private Point pos;
	private Point vel;
	
	public Body( Point pos, Point vel, double radius )
	{
		this.pos = new Point(pos.x, pos.y);
		this.vel = new Point(vel.x, vel.y);
	}
	
	public Body( double xPos, double yPos, double xVel, double yVel, double radius )
	{
		this.pos = new Point(xPos, yPos);
		this.vel = new Point(xVel, yVel);
	}
	
	public Body()
	{
		double defaultXPos = PongGUI.SIZE / 2.0;
		double defaultYPos = PongGUI.YSIZE / 2.0;
		
		this.pos = new Point(0.0f, 0.0f);
		this.vel = new Point(10.0f, 5.0f);
	}
	
	public void setXPos( double xPos )
	{
		this.pos.x = xPos;
	}

	public void setYPos( double yPos )
	{
		this.pos.y = yPos;
	}

	public void setXVel( double xVel )
	{
		this.vel.x = xVel;
	}

	public void setYVel( double yVel )
	{
		this.vel.y = yVel;
	}

	public double getXPos()
	{
		return this.pos.x;
	}
	
	public double getYPos()
	{
		return this.pos.y;
	}
	
	public double getXVel()
	{
		return this.vel.x;
	}
	
	public double getYVel()
	{
		return this.vel.y;
	}

	public void setPos( Point pos )
	{
		this.pos = pos;
	}

	public void setVel( Point vel )
	{
		this.vel = vel;
	}
	public Point getPos( )
	{
		return this.pos;
	}

	public Point getVel( )
	{
		return this.vel;
	}
}
