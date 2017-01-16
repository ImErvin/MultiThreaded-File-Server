package ie.gmit.sw;

import java.io.*;
import java.util.concurrent.BlockingQueue;

public class RequestLogger extends Thread{
	//Variables
	private BlockingQueue<RequestLog> bQueue;
	private FileWriter fw;
	
	//Constructor that parses the BlockingQueue
	public RequestLogger(BlockingQueue<RequestLog> bQueue){
		this.bQueue = bQueue;
	}
	
	//Run Method for threads
	public void run(){
		try {
			//create a new instance of fw to write to log-file.txt
			fw = new FileWriter(new File("log-file.txt"));
			
			//Infinite loop to constantly check and clear the queue
			while(true){
				//Check head and remove it from the queue
				RequestLog rl = bQueue.poll();
				
				//If there are items in the queue
				if (rl != null){
					//Write into the file different options
					if (rl.getCommand() == "1"){
						fw.write("[INFO] Client connected from " + rl.getHost() + " at " + rl.getDate() + "\n");
						fw.flush();
					}else if (rl.getCommand() == "2"){
						fw.write("[INFO] Listing requested from " + rl.getHost() + " at " + rl.getDate() + "\n");
						fw.flush();
					} else if (rl.getCommand() == "3"){
						fw.write("[INFO] File downloaded from " + rl.getHost() + " at " + rl.getDate() + "\n");
						fw.flush();
					} else if (rl.getCommand() == "4"){
						fw.write("[INFO] Disconnected from client " + rl.getHost() + " at " + rl.getDate() + "\n");
						fw.close();
					}
					
					rl = null;
				}else {
					//Else wait and try again. This while loop never ends as the queue is constantly growing.
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
