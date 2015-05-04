package controller;

import java.io.*;
import java.net.*;

import model.Body;
import view.OptionGUI;
import view.PongGUI;

public class PongClient
{
	public final static int LEFT_PORT = 9998;
	public final static int RIGHT_PORT = 9999;
	
	private static OptionGUI options;
	public static int side;
	private static Socket socket;
	private static ObjectInputStream inStream;
	private static ObjectOutputStream outStream;
	private PongGUI gui;
	private Body[] balls;
	
	public PongClient( String host )
	{
		gui = new PongGUI(1, this);
		getSide();
		if(side == OptionGUI.LEFT_SIDE)
			initSocketConnections(host, LEFT_PORT);
		else
			initSocketConnections(host, RIGHT_PORT);
		
		confirmConnection();
//		gui = new PongGUI(balls.length, this);
		playPong();
	}

	private static void getSide()
	{
		options = new OptionGUI();
		
		while(options.isOpen() == true)
			System.out.print(" l ");
		
		side = options.getSide();
		System.out.println("Side = " + side);
	}

	public static void initSocketConnections(String host, int port)
	{
		try {
			socket = new Socket(host, port);
			outStream = new ObjectOutputStream(socket.getOutputStream());
			inStream = new ObjectInputStream(socket.getInputStream());
		}
		catch(Exception e) {
			System.err.println(e);
		}
		
	}

	/* 
	 * Send an empty (null) message just to mark connection
	 * Then receive an empty message from the server. This
	 * will signify that it has received messages from both
	 * clients and the pong game can begin.
	 */
	private static void confirmConnection() {
		try {
			outStream.writeObject("hello world\n");
			outStream.flush();
			
			inStream.readObject();
			
		} catch (IOException | ClassNotFoundException e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}
	
	private static void playPong()
	{
		ServerMessage inMsg;
		ClientMessage outMsg = new ClientMessage();
		int maxWins = 10;
		int wins = 0;
		
		// initialize pongGUI
		
		while(wins < maxWins)
		{
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
	
}
