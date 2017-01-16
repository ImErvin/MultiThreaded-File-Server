package ie.gmit.sw;

//Imports to allow us to parse xml using w3c DOM libraries.
//Imported everything in the io,xml and dom libaries cause it's easier
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class Parser {
	//Variables
	private String username;
	private String ip;
	private int port;
	private String dlDir;
	
	//Constructor
	public Parser(){
		//Calls the parse method
		parse();
	}
	
	//Found this method @ https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
	//Helps me parse the XML file from DOM.
	public void parse(){
		try {
			//Finds the file to parse
			File fXmlFile = new File("client-config.xml");
			//A document builder factory
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			//A document builder
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			//Build the xml file above
			Document doc = dBuilder.parse(fXmlFile);
			
			//NodeList set to the parent node "client-config" inside the .xml file
			NodeList nList = doc.getElementsByTagName("client-config");
			
			//Root is defined as the first item in nList
			Node root = nList.item(0);
			
			//If statement that will set the variables to what is found inside the fields below.
			if (root.getNodeType() == Node.ELEMENT_NODE) {
				
				Element e = (Element) root;
				
				//Sets the variables to what is found in the .xml file.
				username = e.getAttribute("username");
				ip = e.getElementsByTagName("server-host").item(0).getTextContent();
				port = Integer.parseInt(e.getElementsByTagName("server-port").item(0).getTextContent());
				dlDir = e.getElementsByTagName("download-dir").item(0).getTextContent();
			}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	//Getters & Setters
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDlDir() {
		return dlDir;
	}

	public void setDlDir(String dlDir) {
		this.dlDir = dlDir;
	}	
	
}
