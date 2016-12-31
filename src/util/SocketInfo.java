package util;

import java.net.Socket;

public final class SocketInfo {

	  private Socket connection;
	  private String peername;

	  // 缺省的构造函数
	  public SocketInfo() {
		this.connection = null;
		this.peername = null;
	  }
	  
	  public SocketInfo(Socket connection, String peername) {
	    this.connection = connection;
	    this.peername = peername;
	  }

	  public Socket getconnection() { return connection; }
	  public String getpeername() { return peername; }

	  public void setconnection(Socket connection) {  this.connection = connection; }
	  public void setpeername(String peername) { this.peername = peername;  }
	  
	  @Override
	  public int hashCode() { return connection.hashCode() ^ peername.hashCode(); }

	  @Override
	  public boolean equals(Object o) {
	    if (!(o instanceof SocketInfo)) return false;
	    SocketInfo SocketInfoo = (SocketInfo) o;
	    return this.connection.equals(SocketInfoo.getconnection()) &&
	           this.peername.equals(SocketInfoo.getpeername());
	  }

	}
