package ie.gmit.sw;

import java.util.Date;

//DlRequestLogger is a RequestLogger
public class DlRequestLog extends RequestLog{
	//Variables
	private static final long serialVersionUID = 1L;
	private String filename;
	
	//Constructor
	public DlRequestLog(String command, String host, Date d) {
		super(command, host, d);
	}
	
	//Getters & Setters
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
