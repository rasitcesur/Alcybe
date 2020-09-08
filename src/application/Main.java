package application;

import java.util.LinkedList;
import java.util.Queue;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;


public class Main extends Application {
	 
	public void fadeAnimate(final Stage stage, Node node, int duration) {
        FadeTransition ft = new FadeTransition(Duration.millis(duration));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
        ft.setOnFinished(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				
				Image icon=new Image("file:images/alcybe_icon.png");

				showMainWindow(icon);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stage.close();
			}
		});
    }
	
	@Override
	public void start(Stage primaryStage) {
		Image icon=new Image("file:images/alcybe_icon.png");
		Image openningImage=new Image("file:images/alcybe.png");
		
		primaryStage.getIcons().add(icon);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
        FlowPane root = new FlowPane();
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 730, 430);
        
		ImageView imageView = new ImageView(openningImage); 
	    imageView.setPreserveRatio(true);  
        root.getChildren().add(imageView);
        primaryStage.setScene(scene);
		
		//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(icon);
        primaryStage.show();
        fadeAnimate(primaryStage, root, 1500);
	}
	
	public Node getNodeById(Pane root, String id) {
		return null;
	}
	
	public Stage showMainWindow(Image icon) {
		
		try {

			Stage stage = new Stage();
			Pane root = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
			/**((BorderPane)root).setStyle("-fx-padding: 2;" +
		                "-fx-border-style: solid inside;" +
		                "-fx-border-width: 1;" +
		                "-fx-border-insets: 1;" +
		                "-fx-border-radius: 5;" +
		                "-fx-border-color: black;");*/
			/**EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
			    @Override
			    public void handle(ActionEvent event) {
			    	((Label)root.getChildren().get(5)).setText("deneme");
			        event.consume();
			    }
			};
			((Button)root.getChildren().get(0)).setOnAction(buttonHandler);*/
			
			Scene scene = new Scene(root,1280,720);
			stage.setScene(scene);
			stage.getIcons().add(icon);
			stage.show();
			

			
			WebView ww=(WebView) scene.lookup("#homePage");
			WebEngine we=ww.getEngine();
			we.setJavaScriptEnabled(true);
			we.getLoadWorker().stateProperty().addListener(
			        new ChangeListener<State>() {
			            public void changed(ObservableValue ov, State oldState, State newState) {
			                if (newState == State.SUCCEEDED) {
			                    //stage.setTitle(we.getLocation());
			                }
			            }
			        });
			//we.load("http://javafx.com");
			we.load("https://rasitcesur.github.io/Alcybe/");
			
			return stage;
		} catch(Exception e) {
			e.printStackTrace();
		}
        return null;
	}
	
	public static void main(String[] args) throws Exception {
		launch(args);
	}
	
}
