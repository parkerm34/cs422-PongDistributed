package controller;

import java.io.*;
import java.net.*;

import model.Body;
import model.Point;
import view.OptionGUI;
import view.PongGUI;

public class PongClient
{
	public final static int COM_PORT = 9998;
	
	public int paddleYPos;

	private int side;
	private ObjectInputStream inStream;
	private ObjectOutputStream outStream;
	
	public int numBalls;
	private PongGUI gui;
	public Point pointPos[];
	public int keyPressed;
	
	// Input: Needs string that is the hosts IP on startup, gotten through command line.
	//		  Needs input to declare the ball array. I think we should do this on the server
	//			and implement it by having a function on the server that redefines the ball array
	//			given an input and a player can do it at any time by clicking a button the resets the
	//			game with the number of balls specified.... if you want to do it through a command
	//			line argument to make it easier then by all means. Command line argument should probably
	//			be the first idea to do, and figure out the former later.
	//
	// Outputs: rather than sending when a button is pressed, I would rather send the position
	//				of the paddle. The only problem I see is that there might be a little bit of lag time
	//				but if it comes down to it we should be able to make a quick adjustment for the location
	//				to be calculated on the server then sent out or something like that.... let me know
	public PongClient( String host, int balls )
	{
		numBalls = balls;
		chooseSide();
		initSocketConnections(host, COM_PORT);
		
		//balls = ClientHelper.readPoints();
		gui = new PongGUI(this);
		playPong();
	}

	private void chooseSide()
	{
		OptionGUI options = new OptionGUI();
		
		while(options.isOpen() == true)
			System.out.print(" l ");
		
		side = options.getSide();
		System.out.println("Side = " + side);
	}

	private void initSocketConnections(String host, int port)
	{
		Socket socket = null;
		
		try {
			socket = new Socket(host, port);
			
			System.out.println("Connected to socket");
			
			outStream = new ObjectOutputStream(socket.getOutputStream());
			outStream.writeObject(side);
			outStream.flush();
			
			System.out.println("Sent ouptut");
			
			inStream = new ObjectInputStream(socket.getInputStream());
		}
		catch(Exception e) {
			System.err.println(e);
		}
		
	}
	
	private void playPong()
	{
		ServerMessage inMsg;
		int maxWins = 10;
		int wins = 0;
		
		while(wins < maxWins)
		{
			try {
				// Get new positions from server.
				inMsg = (ServerMessage)inStream.readObject();
				
				if(inMsg.getMatchWon() > 0)
					wins++;

				
				// Immediately send server whether up or down are pressed
				//  so it can start calculating next positions.
				outStream.reset();
				outStream.writeObject(keyPressed);
				outStream.flush();
				
				pointPos = inMsg.getBallPositions();
				paddleYPos = inMsg.getPaddleYPos();
				gui.updateCircles();
				gui.updatePaddle();
				
			} catch (ClassNotFoundException | IOException e) {
				System.err.println(e);
				e.printStackTrace();
			}
		}
		
		System.out.println("Game over!");
	}

	public int getSide()
	{
		return side;
	}
}
