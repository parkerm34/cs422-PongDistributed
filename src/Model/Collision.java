package model;
import controller.PongServer;

import view.PongGUI;


public class Collision {
	private final double DT = 0.01f;
	// private static int[][] PongServer.workerBodies;
	
	/* These will need to be passed in initially, after the first time it is good
	 * topWall should always be 0
	 * botWall comes from PongGUI.SIZE from PongGUI
	 * leftPaddle is the front of the paddle (should be altered to paddle us possible)
	 * rightPaddle is the "front" of the right paddle (see code below, needs to be altered a bit)
	 * PongGUI.SIZE comes from PongGUI
	 * 
	 * These are dynamic and must be passed in every time there is a move made
	 * paddleTop[0] will be passed in every time up or down is pressed on the left client
	 * paddleBot[0] ""
	 * paddleTop[1] "" except for on the right client
	 * paddleBot[1] ""
	 * 
	 * This should be simplified into paddleTop and paddleBot if possible
	 */
	private int topWall = 0;
	private int botWall = 750;
	private int leftPaddle = 10;
	private int rightPaddle = 720;

	private int paddleTop[];
	private int paddleBot[];
	
	private double PADDLE_CORNER = 0.0;
	public final int PADDLE_MOVE = 10;
	// private Semaphore mutex;
	// private Semaphore[] barrier;
	
	public boolean debug = false;
	private PongServer server;
	
	public Collision(PongServer server)
	{
		this.server = server;
		paddleTop = new int[2];
		paddleBot = new int[2];
		paddleTop[0] = 313;
		paddleBot[0] = 438;
		paddleTop[1] = 313;
		paddleBot[1] = 438;
	}
	
	/*
	public void parallelStart()
	{
		barrier = new Semaphore[2];
		barrier[0] = new Semaphore(0);
		barrier[1] = new Semaphore(0);
		mutex = new Semaphore(1);
	
		CollisionWorker[] threads = new CollisionWorker[numWorkers];
		for(int i = 0; i < numWorkers; i++)
			threads[i] = new CollisionWorker(i, this);
		
		for(int i = 0; i < numWorkers; i++)
			threads[i].start();
		
		for(int i = 0; i < numWorkers; i++)
		{
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	*/
	
	public void sequentialStep()
	{
		if(debug)
		{
			for(int j = 0; j < server.numBodies; j++)
			{
				System.out.println("Body: " + j);
				System.out.println(" - Before move: xPos: " + server.bodies[j].getXPos() + " yPos: " + server.bodies[j].getYPos());
				System.out.println(" - Before move: xVel: " + server.bodies[j].getXVel() + " yVel: " + server.bodies[j].getYVel());
			}
		}
		
		moveBodies();
		movePaddles();
		detectCollisions();
		
		if(debug)
		{
			for(int j = 0; j < server.numBodies; j++)
			{
				System.out.println("Body: " + j);
				System.out.println(" - After move: xPos: " + server.bodies[j].getXPos() + " yPos: " + server.bodies[j].getYPos());
				System.out.println(" - After move: xVel: " + server.bodies[j].getXVel() + " yVel: " + server.bodies[j].getYVel());
			}
			System.out.println();
		}
	}

	private void movePaddles() {
		if(server.keyPressed[0] == PongGUI.UP_KEY)
		{
			if(server.paddleYPos[0] > -75)
			{
				server.paddleYPos[0] -= PADDLE_MOVE;
				paddleBot[0] -= PADDLE_MOVE;
				paddleTop[0] -= PADDLE_MOVE;
			}
		}
		else if(server.keyPressed[0] == PongGUI.DOWN_KEY)
		{
			if(server.paddleYPos[0] < 675)
			{
				server.paddleYPos[0] += PADDLE_MOVE;
				paddleBot[0] += PADDLE_MOVE;
				paddleTop[0] += PADDLE_MOVE;
			}
		}
		
		if(server.keyPressed[1] == PongGUI.UP_KEY)
		{
			if(server.paddleYPos[1] > -75)
			{
				server.paddleYPos[1] -= PADDLE_MOVE;
				paddleBot[1] -= PADDLE_MOVE;
				paddleTop[1] -= PADDLE_MOVE;
			}
		}
		else if(server.keyPressed[1] == PongGUI.DOWN_KEY)
		{
			if(server.paddleYPos[1] < 675)
			{
				server.paddleYPos[1] += PADDLE_MOVE;
				paddleBot[1] += PADDLE_MOVE;
				paddleTop[1] += PADDLE_MOVE;
			}
		}
	}

	// This function is for the sequential instantiation of Collision.
	// This function defaults to use all of the bodies for moving the bodies.
	private void moveBodies() {
		moveBodiesHelper( 0 );
	}
	
	// This function is for the parallel instantiation of Collision.
	// This function takes the thread id and says tells the main function
	// how many bodies to go through as well as exactly which bodies are being
	// accounted for by this thread
	protected void moveBodies( int num ) {
		moveBodiesHelper( num );
	}
	
	// This function has been changed to run through a loop from a given input
	// rather than going from 0 to PongServer.numBodies. This is because when going through
	// the threads, we will not be going through every body in every thread when
	// this function is called. We also did not want to just create a new function
	// because the code would be all the same, the only difference being the beginning
	// and end of the main loop within the function
	private void moveBodiesHelper( int num ) {
		int body;
		
		for(int i = 0; i < server.workerBodies[num].length; i++)
		{
			body = server.workerBodies[num][i];
			
			server.bodies[body].setXPos(server.bodies[body].getXPos() + server.bodies[body].getXVel() * DT);
			server.bodies[body].setYPos(server.bodies[body].getYPos() + server.bodies[body].getYVel() * DT);
		}
	}
	
	// This function is for the sequential instantiation of Collision.
	// This function defaults to use all of the bodies for detecting collisions.
	private void detectCollisions() {
		detectCollisionsHelper( 0 );
		detectCollisionsWalls( 0 );
		detectCollisionsPaddle( 0 );
	}
	
	// This function is for the parallel instantiation of Collision.
	// This function takes the thread id and says tells the main function
	// how many bodies to go through as well as exactly which bodies are being
	// accounted for by this thread
	protected void detectCollisions( int num ) {
		detectCollisionsHelper( num );
		detectCollisionsWalls( num );
		detectCollisionsPaddle( num );
	}

	// This function has been changed to run through a loop from a given input
	// rather than going from 0 to PongServer.numBodies. This is because when going through
	// the threads, we will not be going through every body in every thread when
	// this function is called. We also did not want to just create a new function
	// because the code would be all the same, the only difference being the beginning
	// and end of the main loop within the function
	private void detectCollisionsHelper( int num )
	{
		double distance;
		int body;
		
		for(int i = 0; i < server.workerBodies[num].length; i++)
		{
			body = server.workerBodies[num][i];
			for(int j = body + 1; j < server.numBodies; j++)
			{
				distance = Math.sqrt((server.bodies[body].getXPos() - server.bodies[j].getXPos()) *
						(server.bodies[body].getXPos() - server.bodies[j].getXPos()) +
						(server.bodies[body].getYPos() - server.bodies[j].getYPos()) *
						(server.bodies[body].getYPos() - server.bodies[j].getYPos()));
				
				if( distance <= server.radius * 2 )
					ResolveCollision(body, j);
			}
		}
		
	}
	
	private void detectCollisionsWalls( int num )
	{
		int body;
		
		for(int i = 0; i < server.workerBodies[num].length; i++)
		{
			body = server.workerBodies[num][i];
			
			if(server.bodies[body].getYPos() * 10 + PongGUI.SIZE / 2 - topWall <= server.radius)
				ResolveCollisionWall(i);
			
			else if(server.bodies[body].getYPos() * 10 + PongGUI.SIZE / 2 - botWall + 30 >= server.radius)
				ResolveCollisionWall(i);
		}
		
	}
	
	/* This function should be simplified to only had to use top and bottom 
	 * as well as rightPaddle and leftPaddle replaced by front. This isnt a hard fix but
	 * should be done once the servers are talking
	 */
	private void detectCollisionsPaddle( int num ) {
		int body;
		double temp;
		for(int i = 0; i < server.workerBodies[num].length; i++)
		{
			body = server.workerBodies[num][i];
			if(server.bodies[body].getXPos() * 10  < leftPaddle - PongGUI.SIZE )
			{
				temp = server.bodies[body].getYPos() * 10 + PongGUI.SIZE / 2 + server.radius * 10;
				if(temp > paddleTop[0] + 1 &&  temp < paddleBot[0] - 1)
				{
					System.out.println("collision left");
					ResolveCollisionPaddle(i);
				}
				else if(temp >= paddleTop[0] - 3 && temp <= paddleTop[0] + 1)
				{
					ResolveCollisionPaddle(i);
					server.bodies[i].setYVel(-PADDLE_CORNER);
				}
				else if(temp >= paddleBot[0] - 1 && temp <= paddleBot[0] + 3)
				{
					ResolveCollisionPaddle(i);
					server.bodies[i].setYVel(PADDLE_CORNER);
				}
				else
				{
					//System.out.println(server.bodies[body].getXPos() * 10);
					//System.out.println("Round Over, right side wins");
					// BALL OFF MAP, call game
				}
			}
			if(server.bodies[body].getXPos() * 10  > rightPaddle - 10 )
			{
				temp = server.bodies[body].getYPos() * 10 + PongGUI.SIZE / 2 + server.radius * 10;
				if(temp > paddleTop[1] + 1  &&  temp < paddleBot[1] - 1)
				{
					System.out.println("collision right");
					ResolveCollisionPaddle(i);
				}
				else if(temp >= paddleTop[1] - 3 && temp <= paddleTop[1] + 1)
				{
					ResolveCollisionPaddle(i);
					server.bodies[i].setYVel(-PADDLE_CORNER);
				}
				else if(temp >= paddleBot[1] - 1 && temp <= paddleBot[1] + 3)
				{
					ResolveCollisionPaddle(i);
					server.bodies[i].setYVel(PADDLE_CORNER);
				}
				else
				{
					//System.out.println(server.bodies[body].getXPos() * 10);
					//System.out.println("Round Over, left side wins");
					// BALL OFF MAP, call game
				}
			}
		}
	}
	
	private void ResolveCollisionPaddle(int b) {
		System.out.println("PADDLE: " + server.bodies[b].getXPos() * 10 + " " + server.bodies[b].getYPos() * 10);
		server.bodies[b].setXVel((-1)*(server.bodies[b].getXVel()));
	}
	
	private void ResolveCollisionWall(int b1) {
		System.out.println("WALL: " + server.bodies[b1].getXPos() * 10 + " " + server.bodies[b1].getYPos() * 10);
		server.bodies[b1].setYVel(-1 * server.bodies[b1].getYVel());
	}
	
	private void ResolveCollision(int b1, int b2) {
		double distSquared;
		double v1fx, v1fy, v2fx, v2fy;
		double v1nfxNumerator, v1txNumerator, v2nfxNumerator, v2txNumerator;
		double v1nfyNumerator, v1tyNumerator, v2nfyNumerator, v2tyNumerator;
		double diffXPos, diffYPos;
		
		// dist = b1.r + b2.r
		distSquared = Math.pow(server.radius * 2, 2);
		
		// x2 - x1
		diffXPos = server.bodies[b2].getXPos() - server.bodies[b1].getXPos();
		// y2 - y1
		diffYPos = server.bodies[b2].getYPos() - server.bodies[b1].getYPos();
		
		// Find final normal and tangent vectors for Body 1's x
		v1nfxNumerator = server.bodies[b2].getXVel() * diffXPos * diffXPos +
				server.bodies[b2].getYVel() * diffXPos * diffYPos;
		v1txNumerator = server.bodies[b1].getXVel() * diffYPos * diffYPos -
				server.bodies[b1].getYVel() * diffXPos * diffYPos;
		// Find the final total x vector for Body 1
		v1fx = (v1nfxNumerator + v1txNumerator) / distSquared;
		
		// Find final normal and tangent y vectors for Body 1
		v1nfyNumerator = server.bodies[b2].getXVel() * diffXPos * diffYPos +
				server.bodies[b2].getYVel() * diffYPos * diffYPos;
		v1tyNumerator = -(server.bodies[b1].getXVel() * diffYPos * diffXPos) +
				server.bodies[b1].getYVel() * diffXPos * diffXPos;
		// Find the final total y vector for Body 1
		v1fy = (v1nfyNumerator + v1tyNumerator) / distSquared;
		
		// Find final normal and tangent x vectors for Body 2
		v2nfxNumerator = server.bodies[b1].getXVel() * diffXPos * diffXPos +
				server.bodies[b1].getYVel() * diffXPos * diffYPos;
		v2txNumerator = server.bodies[b2].getXVel() * diffYPos * diffYPos -
				server.bodies[b2].getYVel() * diffXPos * diffYPos;
		// Find the final total x vector for Body 1
		v2fx = (v2nfxNumerator + v2txNumerator) / distSquared;
		
		// Find final normal and tangent y vectors for Body 2
		v2nfyNumerator = server.bodies[b1].getXVel() * diffXPos * diffYPos +
				server.bodies[b1].getYVel() * diffYPos * diffYPos;
		v2tyNumerator = -(server.bodies[b2].getXVel() * diffYPos * diffXPos) +
				server.bodies[b2].getYVel() * diffXPos * diffXPos;
		// Find the final total y vector for Body 1
		v2fy = (v2nfyNumerator + v2tyNumerator) / distSquared;
		
		// Update the final velocities
		server.bodies[b1].setXVel(v1fx);
		server.bodies[b1].setYVel(v1fy);
		server.bodies[b2].setXVel(v2fx);
		server.bodies[b2].setYVel(v2fy);
	}

	/*
	public void aquireMutex() {
		try {
			mutex.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void releaseMutex() {
		mutex.release();
	}
	
	public void acquireBarrier(int barrierIndex) {
		try {
			barrier[barrierIndex].acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void releaseAllBarrier(int barrierIndex) {
		barrier[barrierIndex].release(numWorkers - 1);
	}
	*/
	
	public void usage()
	{
		System.out.println("Collisions Usage\n");
		System.out.println("\tjava Collision w b s\n");
		System.out.println("\tw - Number of workers, 1 to 16. Ignored by sequential program.");
		System.out.println("\tb - number of bodies.");
		System.out.println("\ts - PongGUI.SIZE of each body.");
	}
}
