package controller;

import java.io.*;
import java.net.*;

public class PongServer {
	public final static int NO_WIN = 0;
	public final static int LEFT_WIN = 1;
	public final static int RIGHT_WIN = 2;
	public final static int SUCCESS = 0;
	
	private static ServerSocket listen[];
	private static Socket socket[];
	private static ObjectInputStream inStream[];
	private static ObjectOutputStream outStream[];
	
	public PongServer()
	{
		initSocketConnections();
		confirmClientConnections();
		playPong();
	}

	/* 
	 * Receive empty (null) messages from clients to confirm
	 * connection with both. Then send an empty message to
	 * both of them. This will let each client know that
	 * they both are connected and the pong game can begin.
	 */
	private static void confirmClientConnections() {
		System.out.println("helper");
		try {
			inStream[0].readObject();
			//inStream[1].readObject();
			
			outStream[0].writeObject(null);
			//outStream[1].writeObject(null);
			outStream[0].flush();
			//outStream[1].flush();
		} catch (ClassNotFoundException | IOException e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	public static void initSocketConnections()
	{
		listen = new ServerSocket[2];
		socket = new Socket[2];
		inStream = new ObjectInputStream[2];
		outStream = new ObjectOutputStream[2];
		try {
			System.out.println("in play");
			listen[0] = new ServerSocket(PongClient.LEFT_PORT);
//			listen[1] = new ServerSocket(PongClient.RIGHT_PORT);
			System.out.println("in play -");
			socket[0] = listen[0].accept();
			System.out.println("in play 2");
	//		socket[1] = listen[1].accept();
			System.out.println("in play 3");
			
			outStream[0] = new ObjectOutputStream(socket[0].getOutputStream());
		//	outStream[1] = new ObjectOutputStream(socket[1].getOutputStream());
			inStream[0] = new ObjectInputStream(socket[0].getInputStream());
			//inStream[1] = new ObjectInputStream(socket[1].getInputStream());
		}
		catch(Exception e) {
			System.err.println(e);
		}
	}
	
	private static void playPong() {
		System.out.println("in play 4");
		ServerMessage outMsg[] = new ServerMessage[2];
		ClientMessage inMsg[] = new ClientMessage[2];
		
		outMsg[0] = new ServerMessage();
		//outMsg[1] = new ServerMessage();
		
		while(true)
		{
			try {
				outStream[0].writeObject(outMsg[0]);
				outStream[0].flush();
				outStream[1].writeObject(outMsg[1]);
				outStream[1].flush();
				
				inMsg[0] = (ClientMessage)inStream[0].readObject();
				//inMsg[1] = (ClientMessage)inStream[1].readObject();
				
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
