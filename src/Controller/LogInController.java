package Controller;

import Controller.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import javax.sound.midi.Soundbank;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public final class LogInController implements Initializable{

    @FXML
    private Button ConnectToServer;

    @FXML
    private Label Loading;

    @FXML
    private TextField pinInput;

    @FXML
    private TextField nameInput;
    
    @FXML
    public void ConnectClicked(ActionEvent event) throws UnknownHostException, IOException  
    {
    	ConnectToServer.setVisible(false);
    	Loading.setVisible(true);
    	connect();
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		// Keyboard Event  输完密码后的确认键
		pinInput.setOnKeyPressed(event->
		{
			switch (event.getCode()) {
			case ENTER:
				try {
					ConnectToServer.setVisible(false);
			    	Loading.setVisible(true);
					connect();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		});
	}
	
    //和服务器建立连接
    public void connect() throws UnknownHostException,IOException {
    
    	ToServerSocket socket = new ToServerSocket();
    	
//    	socket.login_out("2014011507", true);
    	socket.login_out(nameInput.getText(), true);
    	String resp = socket.readecho(0);

    	if(resp.equals("lol")){
    		ConnectOk();
    	}
    	else  {
			Loading.setVisible(false);
			ConnectToServer.setVisible(true);
		}
    }
    
    //连接成功，打开聊天界面
    public void ConnectOk()	throws UnknownHostException, IOException
    {
    	Loading.getScene().getWindow().hide();
		System.out.println("log in ok");
		String user = nameInput.getText();
		
		//创建控制器
		TalkPanelController talkpanel = new TalkPanelController(user);
		
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/Controller/view/Talkpanel.fxml"));
		loader.setController(talkpanel);
		
		//load a root node 
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage stage_f = new Stage();
		stage_f.setScene(scene);
		stage_f.show();
    }
}
