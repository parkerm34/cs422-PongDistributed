package Controller;

import View.CollisionGUI;
import Model.Collision;

public class PongController {

	public static CollisionGUI gui;
	public static Collision col;
	public static String[] startArgs;
	
	public static void main( String[] args ) {
		startArgs = args;
		startCollisionGUI( );
	}
	public static void startCollisionGUI( ) {

		String[] args = startArgs;
		
		if( args.length < 4 )
		{
			System.out.println("Too few arguments: 4 are required, " + args.length + " were given.");
			Collision.usage();
			return;
		}
		
		init();
	}
	
	public static void init() {
		if(Integer.parseInt(startArgs[0]) == 1)
			col = new Collision( Integer.parseInt(startArgs[1]), Integer.parseInt(startArgs[2]), Integer.parseInt(startArgs[3]) );
		else
			col = new Collision( Integer.parseInt(startArgs[0]), Integer.parseInt(startArgs[1]), Integer.parseInt(startArgs[2]), Integer.parseInt(startArgs[3]) );

		if(startArgs.length == 6)
			col.csv = true;
		

		if(Integer.parseInt(startArgs[0]) == 1)
			col.sequentialStart( gui );
		else
			col.parallelStart( gui );
		
		//gui.updateCircles();
	}
}
