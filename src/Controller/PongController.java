package controller;

import view.PongGUI;
import model.Collision;

public class PongController {

	public static PongGUI gui;
	public static Collision col;
	public static String[] startArgs;
	public static int NUM_PROCS = 1;
	public static PongServer server;
	public static PongClient leftClient;
	public static PongClient rightClient;
	public static int numBalls = 1;
	public static String host;
	public static String first;
	public static double size = 1.0f;
	
	public static void main( String[] args ) {
		startArgs = args;
		startCollisionGUI( );
	}
	public static void startCollisionGUI( ) {

		String[] args = startArgs;
		
		if( args.length < 2 )
		{
			System.out.println("Too few arguments: 2 are required, " + args.length + " were given.");
			usage();
			return;
		}
		if( args.length >= 3 )
			if(args[2].compareTo("c") != 0)
				NUM_PROCS = Integer.parseInt(args[2]);

		first = args[0];
		host = args[1];
		
		init();
	}
	
	public static void init() {
		if( first.compareTo("s") == 0 || first.compareTo("server") == 0 )
		{
			
			server = new PongServer();
		}
		else if( first.compareTo("l") == 0 || first.compareTo("left") == 0 )
			leftClient = new PongClient(host);
		else if( first.compareTo("r") == 0 || first.compareTo("right") == 0 )
			rightClient = new PongClient(host);
		else
		{
			System.out.println("First argument is wrong, must be s, l, r, server, left, or right");
			usage();
			System.exit(1);
		}
		if(NUM_PROCS == 1)
			col = new Collision( numBalls, size );
		else
			col = new Collision( NUM_PROCS, numBalls, size );

		
		
		if(NUM_PROCS == 1)
			col.sequentialStart();
		else
			col.parallelStart();
		
		//gui.updateCircles();
	}
	
	public static void usage() {
		System.out.println("Distributed Pong Usage\n");
		System.out.println("java PongController s i [w]\n");
		System.out.println("s - flag for server or client, options are s (or server), l (or left), r (or right)");
		System.out.println("i - is an ip address to use as the host in IPv4 Address format (10.0.0.16)");
		System.out.println("w - is the number of processes to use to calculate collisions and position on the server (optional, default is " + NUM_PROCS + ")");
		//System.out.println("c - is an optional argument that is used to create a csv output c for on or nothing for off)");
	}
}
