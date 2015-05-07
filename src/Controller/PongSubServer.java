package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import view.OptionGUI;

public class PongSubServer extends Thread
{
	public final static int NO_WIN = 0;
	public final static int LEFT_WIN = 1;
	public final static int RIGHT_WIN = 2;
	public final static int SUCCESS = 0;
	
	private static ObjectInputStream inStream;
	private static ObjectOutputStream outStream;
	
	private int side;
	
	private Socket socket;
	
	public PongSubServer(Socket socket)
	{
		this.socket = socket;
	}
	
	public void run()
	{
		initSocketConnections();
		playPong();
	}

	public void initSocketConnections()
	{
		int input = 0;
		
		try {
			inStream = new ObjectInputStream(socket.getInputStream());
			input = (int) inStream.readObject();
			
			System.out.println("Got first input from: " + input);
			
		} catch (IOException | ClassNotFoundException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		
		// if one input is not left and the other right, then inStream got incorrect value(s); exit(1)
		if(input == OptionGUI.LEFT_SIDE)
			side = OptionGUI.LEFT_SIDE;
		else if(input == OptionGUI.RIGHT_SIDE)
			side = OptionGUI.RIGHT_SIDE;
		else
		{
			System.out.println("SubServer: initSocketConnections did not get expected inStream messages from client");
			System.out.println("Input message expected: " + OptionGUI.LEFT_SIDE + " (left) OR " + OptionGUI.RIGHT_SIDE + " (right)");
			System.out.println("inStream message received: " + input);
			System.exit(1);
		}
		
		System.out.println("Passed input check");
		
		try {
			outStream = new ObjectOutputStream(socket.getOutputStream());
			outStream.writeObject(SUCCESS);
			outStream.flush();
			
			System.out.println("Sent output");
		} catch (IOException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		
		System.out.println("Server got in and out streams!");
	}
	
	private void playPong() {
		ClientMessage inMsg = new ClientMessage();
		ServerMessage outMsg = new ServerMessage();
		
		System.out.println("in playPong");
		
		while(true)
		{
			try {
				outStream.writeObject(outMsg);
				outStream.flush();
				
				inMsg = (ClientMessage)inStream.readObject();
				
				System.out.println("Side: " + side + " sent: " + inMsg.getDownKey());
				
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
