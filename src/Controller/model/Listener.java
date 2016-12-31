package Controller.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.omg.CORBA.portable.OutputStream;

import Controller.TalkPanelController;
import util.Message;
import util.MessageType;

public class Listener implements Callable<String> {

	private static int port = 30000;
	private static File rootDirectory = new File("src/resources/txt");
	private int link_num;

    private InputStream is;
    private ObjectInputStream input;

	//����ĳ���˿ڵ���Ϣ��������Ϣ������������ talkpanelcontroller
	public Listener() {
		link_num = 0;
	}
	
	@Override
	public String call() throws Exception {
    	// max link numbers
		
        TalkPanelController.updateOnlineFriendList();
        String resp = "No.";
        
        try (ServerSocket serversocket = new ServerSocket(port))
        {
        	while(true){
        		System.out.println("listening...");
				Socket request = serversocket.accept();
				
				link_num ++;
				System.out.println(resp + Integer.toString(link_num) + " link");
				
				//�յ����������ȴ��Է�������ַ��� һ��ʱ�䣬��ʱ�򲻷��͸����߳�
	            is = request.getInputStream();
	            input = new ObjectInputStream(is);
	            Message msgMessage = (Message) input.readObject();
				String peername = msgMessage.getName();
				
				if(peername.startsWith("2014011")){
					TalkPanelController.addSocket(request, peername);
				}
			}
        } catch (IOException e) {
			System.err.println("error occured when accepting a request");
		}
		return "listener off line";
	}

	public static void send(String msg) {
		Message msgMessage = new Message();
		msgMessage.setMsg(msg);
		msgMessage.setName(TalkPanelController.Myself());
		msgMessage.setType(MessageType.NORMAL);
		
	}
}
