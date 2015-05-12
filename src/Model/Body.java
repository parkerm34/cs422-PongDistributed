package model;

import view.PongGUI;

public class Body {
	private Point pos;
	private Point vel;
	private boolean ballCollide[];
	private boolean wallCollide;
	private boolean paddleCollide;
	
	/*
	public Body( Point pos, Point vel, boolean collide[] )
	{
		this.pos = new Point(pos.x, pos.y);
		this.vel = new Point(vel.x, vel.y);
	}
	
	public Body( double xPos, double yPos, double xVel, double yVel, boolean collide[] )
	{
		pos = new Point(xPos, yPos);
		vel = new Point(xVel, yVel);
	}
	*/
	public Body()
	{
		pos = new Point(0.0f, 0.0f);
		vel = new Point(5.0f, 5.0f);
		
		ballCollide = new boolean[] {false};
		wallCollide = false;
		paddleCollide = false;
	}
	
	public void setWallCollide(boolean wallCollide)
	{
		this.wallCollide = wallCollide;
	}
	
	public boolean getWallCollide()
	{
		return wallCollide;
	}
	
	public void setPaddleCollide(boolean paddleCollide)
	{
		this.paddleCollide = paddleCollide;
	}
	
	public boolean getPaddleCollide()
	{
		return paddleCollide;
	}
	
	public void setBallCollides(boolean ballCollide[])
	{
		this.ballCollide = new boolean[ballCollide.length];
		
		for(int i = 0; i < ballCollide.length; i++)
			this.ballCollide[i] = ballCollide[i];
	}
	
	public void setIndivBallCollide(int body, boolean ballCollide)
	{
		this.ballCollide[body] = ballCollide;
	}
	
	public boolean[] getBallCollides()
	{
		return ballCollide;
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
