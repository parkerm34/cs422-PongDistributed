package controller;

import java.io.*;
import java.net.*;

import model.Body;
import view.OptionGUI;
import view.PongGUI;

public class PongClient
{
	public final static int COM_PORT = 9998;
	
	private int side;
	private ObjectInputStream inStream;
	private ObjectOutputStream outStream;
	
	private PongGUI gui;
	private Body[] balls;
	
	public PongClient( String host )
	{
		//gui = new PongGUI(1, this);
		chooseSide();
		initSocketConnections(host, COM_PORT);
		
		balls = ClientHelper.readPoints();
		// confirmConnection();
//		gui = new PongGUI(balls.length, this);
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
		int serverResponse;
		Socket socket = null;
		
		try {
			socket = new Socket(host, port);
			
			System.out.println("Connected to socket");
			
			outStream = new ObjectOutputStream(socket.getOutputStream());
			outStream.writeObject(side);
			outStream.flush();
			
			System.out.println("Sent ouptut");
			
			inStream = new ObjectInputStream(socket.getInputStream());
			serverResponse = (int) inStream.readObject();
			
			System.out.println("Read input: " + serverResponse);
			
			if(serverResponse != PongSubServer.SUCCESS)
			{
				System.out.println("ConfirmConnection: Server did not send SUCCESS. Sent " + serverResponse + " instead.");
				System.exit(1);
			}
			
			System.out.println("SocketInitialization finished!");
		}
		catch(Exception e) {
			System.err.println(e);
		}
		
	}
	
	private void playPong()
	{
		ServerMessage inMsg;
		ClientMessage outMsg = new ClientMessage();
		int maxWins = 10;
		int wins = 0;
		
		// initialize pongGUI
		
		while(wins < maxWins)
		{
			System.out.println("Within playPong, in while loop");
			
			outMsg.setDownKey(getDK());
			outMsg.setUpKey(getUK());
			try {
				// Get new positions from server.
				inMsg = (ServerMessage)inStream.readObject();
				
				if(inMsg.getMatchWon() > 0)
				{
					wins++;
					// TODO: Add in some sort of pause after a won game
				}
				
				// Immediately send server whether up or down are pressed
				//  so it can start calculating next positions.
				outStream.writeObject(outMsg);
				outStream.flush();
				
				// update pongGUI with inMsg
				//   -> Use inMsg.getBallPositions(), inMsg.getPaddleYPos(),
				//        and inMsg.getMatchWon()
				
			} catch (ClassNotFoundException | IOException e) {
				System.err.println(e);
				e.printStackTrace();
			}
		}
	}

	private static boolean getUK()
	{
		// looks like you have to use listen to KeyEvent to determine if key pressed
		// source: https://stackoverflow.com/questions/18037576/how-do-i-check-if-the-user-is-pressing-a-key
		return false;
	}

	private static boolean getDK()
	{
		// looks like you have to use listen to KeyEvent to determine if key pressed
		// source: https://stackoverflow.com/questions/18037576/how-do-i-check-if-the-user-is-pressing-a-key
		return false;
	}
	
	public Body[] getBodies()
	{
		return balls;
	}
	
	public int getSide()
	{
		return side;
	}
}
