package controller;

import model.Point;

public class ServerMessage {
	private Point ballPositions[];
	private int paddleYPos;
	private int matchWon;
	
	public ServerMessage()
	{
		ballPositions = new Point[1];
		ballPositions[0] = new Point(0, 0);
		paddleYPos = 0;
		matchWon = PongSubServer.NO_WIN;
	}
	
	public ServerMessage(Point ballPositions[], int paddleYPos, int matchWon)
	{
		this.ballPositions = new Point[ballPositions.length];
		for(int i = 0; i < ballPositions.length; i++)
			this.ballPositions[i] = ballPositions[i];
		
		this.paddleYPos = paddleYPos;
		this.matchWon = matchWon;
	}
	
	public Point[] getBallPositions()
	{
		return ballPositions;
	}
	
	public int getPaddleYPos()
	{
		return paddleYPos;
	}
	
	public int getMatchWon()
	{
		return matchWon;
	}
	
	public void setBallPositions(Point ballPositions[])
	{
		this.ballPositions = new Point[ballPositions.length];
		for(int i = 0; i < ballPositions.length; i++)
			this.ballPositions[i] = ballPositions[i];
	}
	
	public void setPaddleYPos(int paddleYPos)
	{
		this.paddleYPos = paddleYPos;
	}
	
	public void setMatchWon(int matchWon)
	{
		this.matchWon = matchWon;
	}
}
