package controller;

import java.io.*;
import java.net.*;

import view.OptionGUI;

public class PongServer {
	public final static int NO_WIN = 0;
	public final static int LEFT_WIN = 1;
	public final static int RIGHT_WIN = 2;
	
	public final static int SUCCESS = 0;
	
	private static ServerSocket listen;
	private static Socket socket;
	private static ObjectInputStream inStream;
	private static ObjectOutputStream outStream;
	
	public PongServer()
	{
		initSocketConnections();
		confirmClientConnections();
		playPong();
	}

	public static void initSocketConnections()
	{
		boolean workingPortNum = false;
		
		System.out.println("in play");
		while(!workingPortNum)
		{
			try {
				listen = new ServerSocket(PongClient.COM_PORT);
				
				System.out.println("Server connected to port num" + PongClient.COM_PORT);
				
				socket = listen.accept();
				
				System.out.println("Server socket accepted!");
				
				outStream = new ObjectOutputStream(socket.getOutputStream());
				inStream = new ObjectInputStream(socket.getInputStream());
				
				System.out.println("Server got in and out streams!");
				
			} catch (SecurityException e) {
				System.out.println("Security Exception hit. Current port number blocked. Iterating and will try another one.");
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println(e);
				e.printStackTrace();
			}
		}
		
	}
	
	/* 
	 * Receive empty (null) messages from clients to confirm
	 * connection with both. Then send an empty message to
	 * both of them. This will let each client know that
	 * they both are connected and the pong game can begin.
	 */
	private static void confirmClientConnections()
	{
		int input1, input2;
		
		System.out.println("helper");
		try {
			input1 = (int) inStream.readObject();
			input2 = (int) inStream.readObject();
			
			// if one input is not left and the other right, then inStream got incorrect value(s); exit(1)
			if(!(input1 == OptionGUI.LEFT_SIDE && input2 == OptionGUI.RIGHT_SIDE))
			{
				if(!(input1 == OptionGUI.RIGHT_SIDE && input2 == OptionGUI.LEFT_SIDE))
				{
					System.out.println("Server: confirmClientConnections did not get expected inStream messages from clients");
					System.exit(1);
				}
			}
			outStream.writeObject(SUCCESS);
			outStream.flush();
			
			// Thread.sleep(10000);
			
			outStream.writeObject(SUCCESS);
			outStream.flush();
		} catch (ClassNotFoundException | IOException e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}
	
	private static void playPong() {
		System.out.println("in playPong");
		ClientMessage inMsg[] = new ClientMessage[2];
		ServerMessage outMsg = new ServerMessage();
		
		inMsg[0] = new ClientMessage();
		inMsg[1] = new ClientMessage();
		
		while(true)
		{
			try {
				outStream.writeObject(outMsg);
				outStream.flush();
				outStream.writeObject(outMsg);
				outStream.flush();
				
				inMsg[0] = (ClientMessage)inStream.readObject();
				inMsg[1] = (ClientMessage)inStream.readObject();
				
				/* 
				 * Call collision functions to determine new positions, velocities, 
				 * and collisions of ball(s) and paddles
				 */
				
			} catch (IOException | ClassNotFoundException e) {
				System.err.println(e);
				e.printStackTrace();
			}
		}
	}

}
