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
		parent.moveBodies( id );
		barrier(0);
		
		parent.detectCollisions( id );
		barrier(1);
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
			if(barrierIndex == 0)
				parent.movePaddles();
			parent.releaseMutex();
		}
	}
}
