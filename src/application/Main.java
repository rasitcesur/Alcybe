package application;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import alcybe.tools.ToolKit;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
	
	private String[] getIconList(String[] data) {
		String[] uri=new String[data.length];
		for (int i = 0; i < uri.length; i++)
			uri[i]="images/menu/"+data[i].replace(' ', '-').
					toLowerCase(Globals.language)+".png";
		return uri;
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
			
			TabPane tp=(TabPane)scene.lookup("#topMenu");
			
			HashMap<String, String[]> menuData = 
					new HashMap<String, String[]>();
			menuData.put("file", new String[] {"Home", "Open", "Save", "Save As", 
					"Licensing"});
			
			menuData.put("engine", new String[] {"Engine Settings", "New Engine Block", 
					"Block Priority"});
			
			menuData.put("reporting", new String[] {"Tables", "Pivot", "Gantt Chart", 
					"Bar Chart", "Line Chart", "Pie Chart", "Histogram", "Box Plot"});
			
			menuData.put("model", new String[] {"New Model", "Import Model", 
					"Export Model", "Event Definition", "Play", "Pause", "Stop", 
					"Run Settings", "Experiment", "Optimization"});
			
			menuData.put("layout", new String[] {"Flow Layout", "Grid Layout", 
					"Vertical Box", "Horizontal Box", "Free Form", "Insert Chart" });
			
			menuData.put("data", new String[] {"Tables", "Views", "Variables", 
					"Data Sources" });
			
			menuData.put("support", new String[] {"Help", "Documentation", "Examples"});
			
			for(Tab t:tp.getTabs()) {
				String key=t.getId();
				if(key==null || key.equals(""))
					key = t.getText().toLowerCase(Globals.language);
				String[] data=menuData.get(key);
					if(data!=null) {
					String[] uri=getIconList(data);
					HBox hb=ToolKit.getHorizontalMenu(data, uri, 32, 32);
					t.setContent(hb);
				}
			}
			
			String[] data = {"Entity", "Store", "Workstation", "Resource"};
			Accordion t=(Accordion)scene.lookup("#leftMenu");
			String[] uri=getIconList(data);
			VBox vb=ToolKit.getVerticalMenu(data, uri, 32, 32);
			t.getPanes().get(0).setContent(vb);
			
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
