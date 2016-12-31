package Controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;


public final class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			LogInController controller_log = new LogInController();
			//FXMLLoader : load object hierarchy from fxml file
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/Controller/view/Login.fxml"));
			loader.setController(controller_log);
			//set a scene with pane from the object
			//load 
			Pane root = (AnchorPane)loader.load();
			Scene scene = new Scene(root);
			
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("RIA");
			primaryStage.show();
			primaryStage.setOnCloseRequest(e -> Platform.exit());
			//Stage MainStage = new Stage();
			  
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
