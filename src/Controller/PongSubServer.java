package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import model.Body;
import model.Point;

import view.OptionGUI;

public class PongSubServer extends Thread
{
	public final static int NO_WIN = 0;
	public final static int LEFT_WIN = 1;
	public final static int RIGHT_WIN = 2;
	public final static int SUCCESS = 0;
	
	private static ObjectInputStream inStream;
	private static ObjectOutputStream outStream;
	
	PongServer parent;
	Socket socket;
	int num;
	int side;
	
	public PongSubServer(PongServer server, Socket socket, int serverNum)
	{
		parent = server;
		this.socket = socket;
		num = serverNum;
	}
	
	public void run()
	{
		initSocketConnections();
		while(true)
		{
			try {
				parent.barrier[side].acquire();
			} catch (InterruptedException e) {
				System.err.println(e);
				e.printStackTrace();
			}
			sendUpdates();
			parent.barrier[2].release();
		}
	}

	public void sendUpdates()
	{
		ServerMessage update = null;
		Point bodyPoints[] = null;
		
		for(int i = 0; i < parent.numBodies; i++)
			bodyPoints[i] = parent.bodies[i].getPos();
		
		update.setBallPositions(bodyPoints);
		update.setMatchWon(NO_WIN);
		update.setPaddleYPos(parent.paddleYPos[side]);
		
		try {
			outStream.writeObject(bodyPoints);
			outStream.flush();
			
			System.out.println("Sent body positions");
			
			parent.keyPressed[side] = (int)inStream.readObject();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	public void initSocketConnections()
	{
		Point bodyPoints[] = null;
		int input = 0;
		
		try {
			inStream = new ObjectInputStream(socket.getInputStream());
			input = (int) inStream.readObject();
			
		} catch (IOException | ClassNotFoundException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		
		// if one input is not left and the other right, then inStream got incorrect value(s); exit(1)
		if(input != OptionGUI.LEFT_SIDE || input != OptionGUI.RIGHT_SIDE)
		{
			System.out.println("SubServer: initSocketConnections did not get expected inStream messages from client");
			System.out.println("Input message expected: " + OptionGUI.LEFT_SIDE + " (left) OR " + OptionGUI.RIGHT_SIDE + " (right)");
			System.out.println("inStream message received: " + input);
			System.exit(1);
		}
		
		side = input;
		
		System.out.println("Subserver " + num + " got init message from: " +
				(input == OptionGUI.LEFT_SIDE ? "left" : "right") + " side.");

		try {
			outStream = new ObjectOutputStream(socket.getOutputStream());
			
			for(int i = 0; i < parent.numBodies; i++)
				bodyPoints[i] = parent.bodies[i].getPos();
			
			outStream.writeObject(bodyPoints);
			outStream.flush();
			
		} catch (IOException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		
		
		System.out.println("Subserver " + num + " finished receiving input and sending output!");
	}
}
