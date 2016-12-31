package Controller.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public final class ToServerSocket {
	
	/* let the user decide how many Strings to read*/
	private  String[] response = new String[10];
	private Socket socket;
	private InputStream in;
	private BufferedReader inr;
	private OutputStream out;

	/* socket  constructor */
	public ToServerSocket () throws UnknownHostException, IOException {
			// set up a socket to server
	    	String serveraddr = "166.111.140.14";
	    	int serverport = 8000;

	    	/* if net is wrong */
	    	try {
	    		this.socket = new Socket(serveraddr, serverport);
	    		// wait for 100 msec
	    		socket.setSoTimeout(100);
			} catch (UnknownHostException e) {
				// TODO: handle exception
				System.err.println(e);
				this.response[0] = "Internet not ready";
			}
	    	
	    	if(this.socket.isConnected())
	    	{
				this.in = this.socket.getInputStream();
				this.inr = new BufferedReader(new InputStreamReader(in)) ;
				this.out = socket.getOutputStream();
	    	}
	    	
	}
	
	/* log in || log out    & return ok or failure */
	public void login_out (String name , boolean in_xout) throws IOException 
	{
		String command;
		String resp ="";
		
		if(in_xout)		//log in
		{
			command = name + "_net2016";
			try {
				this.out.write(command.getBytes("US-ASCII"));
				
				try {
					while(true)
						resp += (char)inr.read();	
				} catch (SocketTimeoutException e) {
					// TODO: handle exception
					System.out.println(resp);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else 			//log out
		{
			command = "logout" + name;
			try {
				this.out.write(command.getBytes("US-ASCII"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.socket.close();
		}
		
		response[0] = resp;
	}
	
	/* query */
	public void query (String name)
	{
		String studengID = getStudentID(name);
		String command = "q" + studengID ;
		String resp="";
		try {
			this.out.write(command.getBytes("US-ASCII"));
			
			try {
				while(true)
					resp += (char)inr.read();	
			} catch (SocketTimeoutException e) {
				// TODO: handle exception
				response[0] = resp;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* read response from the server */
	public String readecho (int index) {
		return this.response[index];
	}
	
	public String getStudentID (String peername) {
		String id = null;
		switch (peername) {
		case "songyy":
			id = "2014011505";
			break;
		case "dongy":
			id = "2014011517";
			break;
		case "wangp":
			id= "2014011493";
			break;
		case "xdd":
			id = "2014011507";
			break;
		default:
			break;
		}
		return id;
	}
	
}
