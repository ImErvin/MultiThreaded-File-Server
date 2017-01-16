package ie.gmit.sw;

import java.io.*;
import java.util.*;

public class RequestLog implements Serializable{
	//Variables [Encapsulation]
	private static final long serialVersionUID = 1L;
	private String command;
	private String host;
	private Date d;
	
	//Constructor with full fields
	public RequestLog(String command, String host, Date d) {
		super();
		this.command = command;
		this.host = host;
		this.d = d;
	}
	
	//Getters & Setters
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Date getDate() {
		return d;
	}

	public void setDate(Date d) {
		this.d = d;
	}
}
