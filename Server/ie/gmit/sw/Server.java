package ie.gmit.sw;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {
	//Main method to start the server
	public static void main(String[] args) throws IOException {
		//Create a new socket on port 7777
		ServerSocket serverSocket = new ServerSocket(7777);
		//Create a blocking queue of RequestLog class with a limit of 7
		BlockingQueue<RequestLog> bQueue = new ArrayBlockingQueue<RequestLog>(7);
		
		System.out.println("Opening Server..");
		System.out.println("Server open and listening on port 7777...");
		RequestLogger rl = new RequestLogger(bQueue);
		//Start the request logger and pass in the blocking queue
    	rl.start();
    	
    	//An infinite loop to keep the server alive 
	    while (true) {
	    	//Accept client sockets on serverSocket port 7777
	    	Socket clientSocket = serverSocket.accept();
	    	//Create a new instance of clientThread and delegate work to it.
	    	ClientServiceThread ClientServiceThread = new ClientServiceThread(clientSocket, bQueue);
	    	//Call the run method for the client Thread.
	    	ClientServiceThread.start();
	    }
	}
}
