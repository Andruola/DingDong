package Controller.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import Controller.TalkPanelController;
import util.Message;
import util.MessageType;


public class TalkThread implements Runnable {

	private static File rootDirectory;
	
	private Socket connection;
    private static ObjectOutputStream oos;
    private InputStream is;
    private ObjectInputStream input;
    private OutputStream outputStream;
    
    private String host;
    private int port;
    private String peername;

    public TalkPanelController controller; 
    
    //������캯����Ϊ�˽��ܱ��˵���������,ֻ��Ҫһ���ѽ����õ�socket
    public TalkThread (Socket connection, TalkPanelController controller, File rootDirectory) throws IOException {
    	if(!rootDirectory.exists())
		{
			throw new IOException(rootDirectory + "doesn't exist as a directory");
		}
		this.rootDirectory = rootDirectory;
    	this.port = 0;
		this.connection = connection;
	}
    
    //������캯����Ϊ������������������ ��Ҫ������ַ���˿ںţ�����Ϣ
    public TalkThread(String host, int port, TalkPanelController controller)
    {
    	this.host = host;
    	this.port = port;
    	this.controller = controller;
    }
    
	@Override
	public void run() {
		//�ͶԷ���������
		if(port != 0) {
			try {
				connection = new Socket(host,port);
			} catch (IOException e) {
				System.out.println("connetion error with " + host + " at " + Integer.toString(port));
			}
		}
		
		//��ȡ���������
		try {
			outputStream = connection.getOutputStream();
	        oos = new ObjectOutputStream(outputStream);
	        is = connection.getInputStream();
	        input = new ObjectInputStream(is);
	        
			if(connection.isConnected()) {
				//����һ��������Ϣ����֤���
				send("hello there",MessageType.VALID);
			}
		} catch (IOException e1) {
			System.out.println("stream error at TalkThread when talking with " + peername);
		}

			
		//���ܶԷ�����Ϣ
		try {
            while(connection.isConnected())
            {
            	Message message = null;
            	message = (Message) input.readObject();
            	
            	//�Կ��������з���
            	if(message != null)
            	{
            		switch (message.getType()) {
					case NORMAL:
						controller.addToChat(message.getMsg());
						break;

					default:
						break;
					}
            	}
            }
         
            
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			try {
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//����һ����Ϣ
	protected void send(String message, MessageType type) throws IOException {
		Message msg = new Message();
		msg.setName(TalkPanelController.Myself());
		msg.setMsg(message);
		msg.setType(type);
		oos.writeObject(msg);
		oos.flush();
	}
	
	public void filedeliver(String filepath){
		
	}

}
