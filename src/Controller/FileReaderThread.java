package controller;

import java.io.*;
import java.net.*;

public class FileReaderThread extends Thread {
	private Socket mySocket;
	
	public FileReaderThread(Socket socket)
	{
		mySocket = socket;
	}
	
	public void run() {
		InetAddress inetAddr;
		String filename, line, side;
		BufferedReader from_client, input;
		PrintWriter to_client;
		File inputFile;
		
		try {
			inetAddr = mySocket.getInetAddress();
			System.out.println("thread: connection from " + inetAddr.toString());
			
			from_client = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			to_client = new PrintWriter(mySocket.getOutputStream());
			
			side = from_client.readLine();
			System.out.println("Side: " + side);
			
			filename = from_client.readLine();
			System.out.println("thread: filename " + filename);
			inputFile = new File(filename);
			if(!inputFile.exists())
				to_client.println("cannot open " + filename);
			else {
				System.out.println("reading from file " + filename);
				input = new BufferedReader( new FileReader(inputFile));
				while((line = input.readLine()) != null) {
					to_client.println(line);
					sleep(100);
				}
			}
			to_client.close();
			from_client.close();
			mySocket.close();
		}
		catch (Exception e) {
			System.out.println("caught an exception!");
		}
	}
	
}
