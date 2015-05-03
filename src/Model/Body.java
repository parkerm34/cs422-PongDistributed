package Model;

public class Body {
	private Point pos;
	private Point vel;
	private double radius;
	
	public Body( Point pos, Point vel, Point force, double mass, double radius )
	{
		this.pos = new Point(pos.x, pos.y);
		this.vel = new Point(vel.x, vel.y);
		this.radius = radius;
	}
	
	public Body( double xPos, double yPos, double xVel, double yVel, double radius )
	{
		this.pos = new Point(xPos, yPos);
		this.vel = new Point(xVel, yVel);
		this.radius = radius;
	}
	
	public Body( )
	{
		this.pos = new Point(1.0f, 1.0f);
		this.vel = new Point(0.0f, 0.0f);
		this.radius = 0.0f;
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
	
	public void setRadius( double radius )
	{
		this.radius = radius;
	}
	
	public double getRadius( )
	{
		return this.radius;
	}
}
