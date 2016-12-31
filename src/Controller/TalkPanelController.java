package Controller;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.sql.rowset.serial.SerialArray;

import Controller.model.Listener;
import Controller.model.TalkThread;
import Controller.model.ToServerSocket;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import util.Message;
import util.MessageType;
import util.SocketInfo;
import util.User;

public class TalkPanelController implements Initializable {

    @FXML
    private TextArea revtext;

    @FXML
    private ListView<String> namelist;

    @FXML
    private Button sendButn;
    
    @FXML
    private Button connecttButn;
    
    @FXML
    private TextField sendtext;
    
    @FXML
    private Button exitbutton;

    @FXML
    void ExitClicked(MouseEvent event) throws IOException {
    	System.out.println("log out");
    	server.login_out(username, false);
    	System.out.println("log out success ! ");
		exitbutton.getScene().getWindow().hide();
		Platform.exit();
		System.exit(0);	
    }
	
    @FXML
    void SendButnClicked(MouseEvent event) throws IOException {
    	sendAction();
    	
    }
    
    private static ObjectOutputStream oos;
    
    //��ĳ��������һ����Ϣ  ʹ��Ԥ��ı��е� sokcet
    public void sendAction() throws IOException {
        String msg = sendtext.getText();
        if (!sendtext.getText().isEmpty()) {
        	String peername = namelist.getSelectionModel().getSelectedItem();
        	
        	for(SocketInfo info : socketlist)
        	{
        		//��ȡ�ͶԷ������� socket
        		if(info.getpeername() == peername)
        		{
        			oos  = new ObjectOutputStream(info.getconnection().getOutputStream());
        			Message message = new Message();
        			message.setName(TalkPanelController.Myself());
        			message.setMsg(msg);
        			message.setType(MessageType.NORMAL);
        			oos.writeObject(message);
        			oos.flush();
        			
        			break;
        		}
        	}
            sendtext.clear();
        }
    }

	@FXML
    void connectButnClicked(MouseEvent event) {
    	String peername = namelist.getSelectionModel().getSelectedItem();
    	System.out.println("asking to connect to " + peername);
    	
    	//����������ӳ�䵽ѧ�ţ����������ѯ
    	server.query(peername);
    	
    	String ipaddr = server.readecho(0);
    	
    	//����һ���Ự
    	TalkThread talkThread = new TalkThread(ipaddr, listenport, instance);
    	Thread thread = new Thread(talkThread);
    	thread.start();
    }
    

    @FXML
    void sendAreaEnterPressed(KeyEvent event) throws IOException {
    	if(event.getCode() == KeyCode.ENTER)
    	{
    		sendAction();
    	}
    }
    
    public static TalkPanelController instance;
    
    //���������б�
    private static ObservableList<String> friendnamelist = FXCollections.observableArrayList();
    
    private static ObservableList<User> userlist = FXCollections.observableArrayList();
    
	private static String username;
	
	private final int listenport = 30000;
	
	private static final int THREAD_NUM = 5;
	
	private static File rootDirectory;
	
	private static ToServerSocket server;
	
	public Future<String> future;
	
	public static ObservableList<SocketInfo> socketlist = FXCollections.observableArrayList();
	
	//���صĺ����б�
	public static ArrayList<String> nameSet = new ArrayList<String>();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		nameSet.add("2014011507");
		nameSet.add("2014011505");
		nameSet.add("2014011497");
		nameSet.add("2014011517");
		
		userlist.add(new User("songyy"));
		userlist.add(new User("wangp"));
		userlist.add(new User("dongy"));
		userlist.add(new User("xdd"));
		
		updateName();
		
		namelist.setItems(friendnamelist);
		
		sendtext.setOnKeyPressed(e->{
			switch (e.getCode()) {
			case ENTER:
				System.out.println("send a message");
				break;
			default:
				break;
			}
		});
		
		//���ӵ����������Ա��ȡ�����б�
		try {
			server = new ToServerSocket();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		ExecutorService pool = Executors.newFixedThreadPool(THREAD_NUM);
		Listener listener = new Listener();
//		ListenerSocket listener = new ListenerSocket(username, this, rootDirectory);
		// step 1 ��ʼһ���������߳�	
		future = pool.submit(listener);
		
	}
	
	public TalkPanelController (String username) throws IOException {
		//String basePath = new File("").getAbsolutePath();
	    //System.out.println(basePath);
		this.username = username;
		System.out.println("now "+username+" is online");
		String path = "src/resources/txt/txt1.txt";
		rootDirectory = new File(path);
		instance = this;
	}
	
	private static void updateName() {
		friendnamelist.clear();
		for(User user : userlist)
		{
			friendnamelist.add(user.getName());
		}
	}
	
	public static void updateOnlineFriendList(){
		for(String name: nameSet)
		{
			server.query(name);
			String resp = server.readecho(0);
			if(resp.startsWith("1"))
				System.out.println(name + " is online");
			else {
				System.out.println(name + " is not online");
			}
		}
	}
	
	//�ڵ�ǰ��������¼���һ������
	public static void addSocket (Socket connection, String peername) throws IOException {
		socketlist.add(new SocketInfo(connection, peername));
		userlist.add(new User(peername));
		System.out.println("link is set with "+ peername);
		TalkThread talk = new TalkThread(connection, instance, rootDirectory);
		Thread thread = new Thread(talk);
		thread.start();
	}
	
	public void fileDeliver(String filename, String reciever){
		String fullpath = rootDirectory.getPath() + "/" + filename;
	}
	
	public static String Myself() {
		return username;
	}

	public void addToChat(String msg) {
		System.out.println(msg);
		revtext.setText(msg);
	}
}
