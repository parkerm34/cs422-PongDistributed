package model;

public class CollisionWorker extends Thread {

	private int id;
	private Collision parent;
	
	public CollisionWorker( int id, Collision parent )
	{
		this.id = id;
		this.parent = parent;
	}
	
	public void run()
	{
		for(int i = 0; i < parent.getNumTimeSteps(); i++)
		{
			if(parent.isDebug())
			{
				System.out.println("Before TR " + i + ": Number of collisions: " + parent.getNumCollisions());
				int body;
				for(int j = 0; j < parent.getWorkerBodies()[id].length; j++)
				{
					body = parent.getWorkerBodies()[id][j];
					System.out.println("Body: " + body);
					System.out.println(" - Before move: xPos: " + parent.getBodies()[body].getXPos() + " yPos: " + parent.getBodies()[body].getYPos());
					System.out.println(" - Before move: xVel: " + parent.getBodies()[body].getXVel() + " yVel: " + parent.getBodies()[body].getYVel());
				}
			}
			parent.moveBodies( id );
			barrier(0);
			
			parent.detectCollisions( id );
			barrier(1);
			
			
			if(parent.isDebug())
			{
				int body;
				for(int j = 0; j < parent.getWorkerBodies()[id].length; j++)
				{
					body = parent.getWorkerBodies()[id][j];
					System.out.println("Body: " + body);
					System.out.println(" - After move: xPos: " + parent.getBodies()[body].getXPos() + " yPos: " + parent.getBodies()[body].getYPos());
					System.out.println(" - After move: xVel: " + parent.getBodies()[body].getXVel() + " yVel: " + parent.getBodies()[body].getYVel());
				}
				System.out.println();
			}
		}
	}
	
	private void barrier(int barrierIndex) {
		// All processes before the last wait
		parent.aquireMutex();
		if(parent.getNumArrived() < parent.getNumWorkers())
		{
			parent.setNumArrived(parent.getNumArrived() + 1);
			parent.releaseMutex();
			parent.acquireBarrier(barrierIndex);
		}
		else // all processes waiting, release them all
		{
			parent.setNumArrived(1);
			parent.releaseAllBarrier(barrierIndex);
			parent.releaseMutex();
		}
	}
}
