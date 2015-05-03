package Controller;

import java.io.*;
import java.net.*;

import View.OptionGUI;

public class PongClient {
	
	private static OptionGUI options;
	private static int side;
	private static String[] start_args;
	
	public static void main(String[] args) {
		start_args = args;
		options = new OptionGUI();
		while(options.isOpen() == true)
			System.out.print(" l ");
		side = options.getSide();
		System.out.println("done");
		init();
	}
	public static void init() {
		String line, host, filename;
		int tabs;
		Socket socket;
		BufferedReader from_server;
		PrintWriter to_server;
		System.out.println(side);
		try {
			if(start_args.length < 2) {
				System.out.println("need at least 2 args");
				System.exit(1);
			}
			host = start_args[0];
			filename = start_args[1];
			tabs = Integer.parseInt(start_args[2]);
			
			socket = new Socket(host, 9999);
			from_server = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			to_server = new PrintWriter(socket.getOutputStream());
			
			to_server.println(side);
			to_server.flush();
			
			to_server.println(filename);
			to_server.flush();
			while((line = from_server.readLine()) != null)
			{
				for(int i = 0; i < tabs; i++)
					System.out.print("\t");
				System.out.println(line);
			}
		}
		catch(Exception e) {
			System.err.println(e);
		}
	}
}
