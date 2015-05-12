package controller;

import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.concurrent.Semaphore;

import view.PongGUI;

import model.Body;
import model.Collision;
import model.Point;

public class PongServer
{
	public Body[] bodies;
	public int workerBodies[][];
	public int numBodies;
	public float radius = 1.0f;
	public boolean noWinner = true;
	public int paddleYPos[];
	public int keyPressed[];
	int numWorkers;
	private Collision col;
	public int ready = 0;
	
	PongSubServer subServer1 = null;
	PongSubServer subServer2 = null;
	Semaphore barrier[];
	
	public PongServer(int numBodies, int numWorkers)
	{
		this.numWorkers = numWorkers;
		this.numBodies = numBodies;
		
		barrier = new Semaphore[3];
		barrier[0] = new Semaphore(0);
		barrier[1] = new Semaphore(0);
		barrier[2] = new Semaphore(0);
		
		paddleYPos = new int[2];
		paddleYPos[0] = PongGUI.SIZE / 2 - PongGUI.SIZE / 12;
		paddleYPos[1] = paddleYPos[0];
		
		keyPressed = new int[2];
		keyPressed[0] = PongGUI.NO_KEY;
		keyPressed[1] = PongGUI.NO_KEY;
		
		if(numWorkers > 1)
		{
			workerBodies = new int[numWorkers][];
			parseBodies();
		}
		else
		{	
			workerBodies = new int[numWorkers][numBodies];
			for(int i = 0; i < numBodies; i++)
				workerBodies[0][i] = i;
		}
		
		startSubServers();
		playPong();
	}
	
	// This function is used to separate the bodies via reverse striping into the workers
	private void parseBodies()
	{
		int curr;
		int division = numBodies / numWorkers;
		boolean iterateFirst = division % 2 == 0;
		
		for(int i = 0; i < numWorkers; i++)
		{
			if(iterateFirst && i < numBodies % numWorkers)
				workerBodies[i] = new int[division + 1];
			else if(!iterateFirst && i >= (numWorkers - numBodies % numWorkers))
					workerBodies[i] = new int[division + 1];
			else
				workerBodies[i] = new int[division];
		}
		
		for(int i = 0; i < numWorkers; i++)
		{
			curr = 0;
			for(int j = 0; j < workerBodies[i].length; j++)
			{
				workerBodies[i][j] = curr + (j % 2 == 0 ? i : numWorkers - (i + 1));
				curr += numWorkers;
			}
		}
	}

	private void initBodies()
	{
		double div;
		double xFactor, yFactor;
		Random rand = new Random();;
		// Initialize bodies
		bodies = new Body[numBodies];
		for(int i = 0; i < numBodies; i++)
			bodies[i] = new Body();
		
		// Parse bodies evenly along middle of screen
		for(int i = 0; i < numBodies; i++)
		{
			div = PongGUI.SIZE / ((double)numBodies + 1);
			//bodies[i].setYPos(div * i);
			bodies[i].setYPos(0);
			bodies[i].setXPos(0);
		}
		
		// Set random velocities for the balls
		for(int i = 0; i < numBodies; i++)
		{
			xFactor = rand.nextDouble();
			yFactor = rand.nextDouble();
			
			bodies[i].setXVel(xFactor * 10);
			bodies[i].setYVel(yFactor * 10);
		}
	}

	public void startSubServers()
	{
		ServerSocket listen = null;
		Socket socket = null;
		
		System.out.println("Entered server (parent function)");
		
		try {
			listen = new ServerSocket(PongClient.COM_PORT);
			
			System.out.println("Server: Waiting for 1st client to accept connection to port");
			socket = listen.accept();
			subServer1 = new PongSubServer(this, socket, 0);
			subServer1.start();
			ready++;
			
			System.out.println("Server: Waiting for 2nd client to accept connection to port");
			socket = listen.accept();
			subServer2 = new PongSubServer(this, socket, 1);
			subServer2.start();
			ready++;
			
		}  catch(IOException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		System.out.println("Server: Both clients accepted connection to port. Running subserver threads");
		System.out.println(ready);
	}
	
	private void playPong() {
		System.out.println("in playPong");
		int contin;
		col = new Collision(this);
		
		while(true)
		{
			initBodies();
			contin = 0;
			while(contin == 0)
			{
				barrier[0].release();
				barrier[1].release();
				
				try {
					barrier[2].acquire(2);
				} catch (InterruptedException e) {
					System.err.println(e);
					e.printStackTrace();
				}
				// System.out.println("Left pressed: " + keyPressed[0] + ", right pressed: " + keyPressed[1]);
				
				col.sequentialStep();
				contin = col.getCont();
			}
			
			/*
			 * Expect contin to be 0 for round continuing/new round
			 * Expect contin to be 1 for left side win, then pause for 3 seconds
			 * Expect contin to be 2 for right side win, then pause for 3 seconds
			 */
			
			if(contin == 1)
			{
				//Left side win
				col.setCont(0);
				System.out.println("Left side won round, continuing in 3");
			}
			else if(contin == 2)
			{
				//Right side win
				col.setCont(0);
				System.out.println("Right side won round, continuing in 3");
			}
			/*
			try { 
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			*/
			System.out.println("ROUND START");
		}
	}
}
