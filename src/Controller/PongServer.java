package controller;

import java.io.*;
import java.net.*;

public class PongServer {
	
	public PongServer()
	{
		ServerSocket listen = null;
		Socket socket = null;
		PongSubServer subServer1 = null;
		PongSubServer subServer2 = null;
		
		System.out.println("Entered server (parent function)");
		
		try {
			listen = new ServerSocket(PongClient.COM_PORT);
			
			System.out.println("Server: Waiting for 1st client to accept connection to port");
			socket = listen.accept();
			subServer1 = new PongSubServer(socket);
			
			System.out.println("Server: Waiting for 2nd client to accept connection to port");
			socket = listen.accept();
			subServer2 = new PongSubServer(socket);
			
		}  catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println("Server: Both clients accepted connection to port. Beginning subserver threads");
		
		subServer1.start();
		subServer2.start();
		
		System.out.println("Server: Both subServer threads running");
		
	}
}
