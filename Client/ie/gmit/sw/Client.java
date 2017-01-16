package ie.gmit.sw;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
		
		//Set up server connection variables and control variables
		Socket requestSocket = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		boolean connected = false;
		
		//Instance of parser
		Parser p = new Parser();
		
		//Variables, control variables and Scanner
		Scanner stdin = new Scanner(System.in);
		final int PORT_NUMBER = p.getPort();
		String host = p.getIp();
		String dlDir = p.getDlDir();
		int control = 0;
		int option;
		String message = null;
		String file;
		//Creates a directory for file downloads
		new File(dlDir).mkdir();
		
		do{
			System.out.println("=========Menu=========\n"
							 + "[1] Connect to Server\n"
							 + "[2] Print File Listing\n"
							 + "[3] Download File\n"
							 + "[4] Exit\n"
							 + "======================");
			System.out.print("\tEnter Option:");
			try{
				option = stdin.nextInt();
			}catch(InputMismatchException e){
				System.out.println("Incorrect Input. Re-try");
				continue;
			}
			
			//Option 1: Connect to the Server
			if(option == 1){
				//If we're not connected yet...
				if(!connected){
					//Set up data-flow through socket
					requestSocket = new Socket(host, PORT_NUMBER);
					//Set up input and output streams (flush the output stream)
					out = new ObjectOutputStream(requestSocket.getOutputStream());
					out.flush();
					in = new ObjectInputStream(requestSocket.getInputStream());
					//Change control variable connected to true
					connected = true;
					
					//Log the request message to server.
					RequestLog r = new RequestLog(Integer.toString(option), host, new Date());
					out.writeObject(r);
					out.flush();
					
					//Reset the message to null for while control
					message = null;
					do{
						//Read in the message and wait. Rinse and repeat if no message received.
						message = (String)in.readObject();
						Thread.sleep(100);
					}while(message.equals(null));
				}else{
					System.out.println("Seems you are already connected to the server!");
				}
			}else if(option == 2){
				//If we're connected...
				if(connected){
					//Log the request message to server.
					RequestLog r = new RequestLog(Integer.toString(option), host, new Date());
					out.writeObject(r);
					out.flush();
					
					message = null;
					do{
						message = (String)in.readObject();
						Thread.sleep(100);
					}while(message.equals(null));
					
					System.out.println(message);
				}else{
					System.out.println("Seems that you not connected to the server.\n"
									 + "Connect to the server using Option [1].");
				}
			}else if(option == 3){
				if(connected){
					//Log the request message to server.
					RequestLog r = new RequestLog(Integer.toString(option), host, new Date());
					out.writeObject(r);
					out.flush();
					
					//Flush the scanner
					stdin.nextLine();
					//Request a the user to enter the Path of the file.
					System.out.print("Enter path of the file to download: ");
					file = stdin.nextLine();
					
					//Send the file name as a message to the server
					out.writeObject(file);
					out.flush();
					
					//Reset message
					message = null;
					//Wait for the servers response
					do{
						message = (String)in.readObject();
						Thread.sleep(100);
					}while(message.equals(null));
					
					//Checks if the server found the file
					if(message == "Error loading file"){
						System.out.println(message);
					}else{
						//Writes the file to a file in the the download directory.
						PrintWriter pr = new PrintWriter(dlDir + "/" + file);
						pr.print(message);
						pr.close();
					}
					
					System.out.println(message);
				}else{
					System.out.println("Seems that you not connected to the server.\n"
									 + "Connect to the server using Option [1].");
				}
			}else if(option == 4){
				//Exits the menu loop.
				//Close connection and streams/scanner.
				requestSocket.close();
				out.close();
				in.close();
				stdin.close();
				control = 1;
			}else{
				//Default option
				System.out.println("Incorrect Option! Re-try.");
			}
		}while(control == 0);
	}
}
