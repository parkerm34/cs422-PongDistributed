package controller;

import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.concurrent.Semaphore;

import view.PongGUI;

import model.Body;
import model.Collision;

public class PongServer
{
	public Body[] bodies;
	public int workerBodies[][];
	public int numBodies;
	public static double radius = 0.5;
	public boolean noWinner = true;
	public int paddleYPos[];
	public int keyPressed[];
	public int numWorkers;
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
		paddleYPos[0] = PongGUI.YSIZE / 2 - PongGUI.YSIZE / 12;
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
		double xFactor, yFactor, negFactor;
		double xVel;
		int currY = -40;
		boolean initCollide[];
		Random rand = new Random();;
		// Initialize bodies
		bodies = new Body[numBodies];
		for(int i = 0; i < numBodies; i++)
			bodies[i] = new Body();
		
		// Parse bodies evenly along middle of screen
		// div = PongGUI.YSIZE / ((double)numBodies + 1);
		for(int i = 0; i < numBodies; i++)
		{
			//bodies[i].setYPos(div * i);
			bodies[i].setYPos(currY);
			bodies[i].setXPos(0);
			currY += 5;
		}
		
		// Set random velocities for the balls
		for(int i = 0; i < numBodies; i++)
		{
			xFactor = rand.nextDouble();
			yFactor = rand.nextDouble();
			negFactor = rand.nextDouble();
			
			xVel = 5 + xFactor * 2;
			if(negFactor < 0.5)
				xVel *= -1;
			bodies[i].setXVel(xVel);
			
			if(yFactor < 0.5)
				bodies[i].setYVel(5);
			else
				bodies[i].setYVel(-5);
		}
		
		// setup collide boolean array (mark which balls it's collided with currently)
		initCollide = new boolean[numBodies];
		for(int i = 0; i < numBodies; i++)
			initCollide[i] = false;
		
		for(int i = 0; i < numBodies; i++)
		{
			bodies[i].setBallCollides(initCollide);
			bodies[i].setWallCollide(false);
			bodies[i].setPaddleCollide(false);
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
		col = new Collision(this, numWorkers);
		
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
				
				if(numWorkers == 1)
					col.sequentialStep();
				else
					col.parallelStep();
				
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
