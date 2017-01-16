package ie.gmit.sw;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;

public class ClientServiceThread extends Thread {
	//Variables [Encapsulation]
	private Socket clientSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String message = null;
	private String command = "";
	private RequestLog rl = null; 
	private BlockingQueue<RequestLog> bQueue;
	
	ClientServiceThread(Socket socket, BlockingQueue<RequestLog> bQueue){
		this.clientSocket = socket;
		this.bQueue = bQueue;
	}
	
	public void run(){
		//Server log the connection made with the client thread.
		try {
			//Set the command and put it into the blocking queue
			command = "1";
			bQueue.put(new RequestLog(command, clientSocket.getInetAddress().getHostName(), new Date()));
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		try{
			//Set up output and input streams
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(clientSocket.getInputStream());
			
			//Reset the requestLogger variable.
			rl = null;
			do{
				do{
					//Set the requestLogger variable by reading in the object.
					//Repeat till set.
					rl = (RequestLog) in.readObject();
					Thread.sleep(1000);
				}while (rl == null);
				
				//Switch statement to control the outcome of the client command.
				switch(rl.getCommand()){
					//Connected to the server and log this.
					case "1":
						sendMessage("You are connected to the Server :-)");
						command = rl.getCommand();
						bQueue.add(new RequestLog(command, clientSocket.getInetAddress().getHostName(), new Date()));
						
						break;
					case "2":
						//Call the fileList method to send a message of files in the current directory.
						fileList();
						
						break;
					case "3":
						// Send file to user
						file();
						
						break;
					default:
						//Default if the command was misread.
						System.out.println("Error reading command");
				}
			}while(!clientSocket.isClosed());
			
		}catch (Exception e){
			System.out.println("Connection was interupted or lost.");
			try {
				command = "4";
				bQueue.put(new RequestLog(command, clientSocket.getInetAddress().getHostName(), new Date()));
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	private void fileList() throws InterruptedException{
		// Create a folder that looks at current directory (.)
		File folder = new File(".");
		// Array of files that is populated by all the files in the directory
		File [] fileList = folder.listFiles();
		
		//String to be sent to client
		String listFiles = "";
		
		//Build the string by iterating over the array and adding each name to the string.
		for (int i = 0; i < fileList.length; i++) {
			listFiles += (fileList[i].getName()) + "\n"; 
		}
		
		//Send the client the list of files in the form of a string
		sendMessage(listFiles);
		
		//Log the command to the server
		command = "2";
		bQueue.put(new RequestLog(command, clientSocket.getInetAddress().getHostName(), new Date()));
	}
	
	private void file() throws FileNotFoundException, InterruptedException{
		//Reset the message
		message = null;
		do{
			try {
				//Set the message to what is being passed in.
				//Will keep attempting till a message is recieved.
				message = (String) in.readObject();
				Thread.sleep(100);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}while(message == null);
		
		//Create a file with the message passed in above.
		File getFile = new File(message);
		
		//If the file is actually a file and exists
		if (getFile.isFile() && getFile.exists()){
			//Create a buffered reader
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(getFile)));
			//String file to be built in the while loop below.
			String file = "File Contents\n";
			//br control variable
			String next = null;
			
			try {
				//While there are lines to be read in the file requested.
				while((next = br.readLine()) != null){
					//Append line by line each sentence onto the string File.
					file += next + "\n";
				}
				//Parse the string to sendMessage that will send the message.
				sendMessage(file);
				//Close the buffered reader.
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{ 
			//Error message if the file was not found.
			sendMessage("Error loading file");
		}
		
		//Set the command
		command = "3";
		//Create an instance of DlRequestLogger and put it in the logging blockingQueue
		DlRequestLog r = new DlRequestLog(command, clientSocket.getInetAddress().getHostName(), new Date());
		r.setFilename(message);
		bQueue.put(r);
	}
}
