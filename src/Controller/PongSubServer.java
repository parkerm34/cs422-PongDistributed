package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import model.Body;
import model.Point;

import view.OptionGUI;
import view.PongGUI;

public class PongSubServer extends Thread
{
	public final static int NO_WIN = 0;
	public final int LEFT_WIN = 1;
	public final int RIGHT_WIN = 2;
	public final int SUCCESS = 0;
	
	private ObjectInputStream inStream;
	private ObjectOutputStream outStream;
	private Point bodyPoints[];
	
	public PongServer parent;
	public Socket socket;
	public int num;
	public int side;
	
	public PongSubServer(PongServer server, Socket socket, int serverNum)
	{
		parent = server;
		this.socket = socket;
		bodyPoints = new Point[parent.numBodies];
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
			receiveKeyPress();
			parent.barrier[2].release();
		}
	}

	private void receiveKeyPress() {
		int keyPress = PongGUI.NO_KEY;
		
		try {
			keyPress = (int)inStream.readObject();
		} catch (ClassNotFoundException | IOException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		parent.keyPressed[side] = keyPress;
	}

	public void sendUpdates()
	{
		ServerMessage update = new ServerMessage();
		
		for(int i = 0; i < parent.numBodies; i++) {
			bodyPoints[i] = parent.bodies[i].getPos();
		}
		
		update.setBallPositions(bodyPoints);
		update.setMatchWon(NO_WIN);
		update.setPaddleYPos(parent.paddleYPos[side]);
		
		try {
			outStream.reset();
			outStream.writeObject(update);
			outStream.flush();
			
			//System.out.println("Sent body positions");
			
			//parent.keyPressed[side] = (int)inStream.readObject();
		} catch (IOException e ) {//| ClassNotFoundException e) {
		//	System.out.println("press the int button");
//			System.err.println(e);
	//		e.printStackTrace();
		}
	}

	public void initSocketConnections()
	{
		int input = 0;
		
		try {
			outStream = new ObjectOutputStream(socket.getOutputStream());

			inStream = new ObjectInputStream(socket.getInputStream());
			input = (int) inStream.readObject();
			
		} catch (IOException | ClassNotFoundException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		
		// if one input is not left and the other right, then inStream got incorrect value(s); exit(1)
		
		if(input != 0 && input != 1)
		{
			System.out.println("SubServer: initSocketConnections did not get expected inStream messages from client");
			System.out.println("Input message expected: " + OptionGUI.LEFT_SIDE + " (left) OR " + OptionGUI.RIGHT_SIDE + " (right)");
			System.out.println("inStream message received: " + input);
			System.exit(1);
		}
		
		side = input;
		
		System.out.println("Subserver " + num + " got init message from: " +
				(input == OptionGUI.LEFT_SIDE ? "left" : "right") + " side.");

		System.out.println("Subserver " + num + " finished receiving input and sending output!");
	}
}
