package Controller;

import java.io.*;
import java.net.*;

public class PongServer {

	public static void main(String[] args) {
		ServerSocket listen;
		Socket socket;
		FileReaderThread new_thread;
		
		try {
			listen = new ServerSocket(9999, 5);
			
			while (true) {
				System.out.println();
				System.out.println("server: waiting for connection");
				socket = listen.accept();
				System.out.println("server: creating thread for socket");
				new_thread = new FileReaderThread(socket);
				new_thread.start();
			}
		}
		catch (Exception e) {
			System.err.println(e);
		}

	}

}
