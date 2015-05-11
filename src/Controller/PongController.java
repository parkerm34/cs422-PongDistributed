package controller;

import view.PongGUI;
import model.Collision;

public class PongController {
	public static int numProcs = 1;
	public static int numBalls = 1;
	public static String host;
	public static String source;
	
	public static void main( String[] args )
	{
		if( args.length < 2 )
		{
			System.out.println("Too few arguments: 1 are required, " + args.length + " were given.");
			usage();
			return;
		}
		switch(args.length)
		{
			case 4:
				numProcs = Integer.parseInt(args[3]);
			case 3:
				numBalls = Integer.parseInt(args[2]);
			case 2:
				host = args[1];
				source = args[0];
				break;
			default:
				System.out.println("Too many arguments: 6 is the maximum, " + args.length + " were given.");
				usage();
				return;
		}
		
		if( source.equals("s") || source.equals("server") )
			new PongServer(numBalls, numProcs);
		
		else if( source.equals("l") || source.equals("left") )
			new PongClient(host);
		
		else if( source.equals("r") || source.equals("right") )
			new PongClient(host);
		
		else
		{
			System.out.println("First argument is wrong, must be s, l, r, server, left, or right");
			usage();
			System.exit(1);
		}
	}
	
	public static void usage() {
		System.out.println("Distributed Pong Usage\n");
		System.out.println("java PongController s i [n] [w]\n");
		System.out.println("s - flag for server or client, options are s (or server), l (or left), r (or right)");
		System.out.println("i - is an ip address to use as the host in IPv4 Address format (10.0.0.16)");
		System.out.println("n - is the number of balls to start the game with(optional, default is 1)");		
		System.out.println("w - is the number of processes to use to calculate collisions and position on the server (optional, default is 1)");
	}
}
