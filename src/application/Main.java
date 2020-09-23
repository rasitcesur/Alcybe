package application;

import java.util.HashMap;

import alcybe.tools.ToolKit;
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
import javafx.scene.control.Accordion;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
			uri[i]=Globals.getMenuIconURI(data[i]);
		return uri;
	}
	
	@SuppressWarnings("unchecked")
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
			
			HashMap<String, MenuContainer> menuData = 
					new HashMap<String, MenuContainer>();
					
			menuData.put("file", new MenuContainer(
					new String[] {"Home", "Open", "Save", "Save As", "Licensing"},
					new EventHandler[] {Events.homePage, Events.noPageDialog,
							Events.noPageDialog,Events.noPageDialog,
							Events.noPageDialog}));
			
			menuData.put("engine", new MenuContainer( new String[] {"Engine Settings", 
					"New Engine Block", "Block Priority"},
					new EventHandler[] {Events.noPageDialog,Events.noPageDialog,
							Events.noPageDialog}));
			
			menuData.put("reporting", new MenuContainer(new String[] {"Tables", "Pivot", 
					"Gantt Chart", "Bar Chart", "Line Chart", "Pie Chart", 
					"Histogram", "Box Plot"}, 
					new EventHandler[] {Events.noPageDialog,Events.noPageDialog,
							Events.noPageDialog,Events.noPageDialog,Events.noPageDialog,
							Events.noPageDialog,Events.noPageDialog,
							Events.noPageDialog}));
			
			menuData.put("model", new MenuContainer(new String[] {"New Model", 
					"Import Model", "Export Model", "Event Definition", "Play", "Pause", 
					"Stop", "Run Settings", "Experiment", "Optimization"}, 
					new EventHandler[] {Events.noPageDialog,Events.noPageDialog,
							Events.noPageDialog,Events.noPageDialog,Events.noPageDialog,
							Events.noPageDialog,Events.noPageDialog,
							Events.noPageDialog,Events.noPageDialog,
							Events.noPageDialog}));
			
			menuData.put("layout", new MenuContainer(new String[] {"Flow Layout", 
					"Grid Layout", "Vertical Box", "Horizontal Box", "Free Form", 
					"Insert Chart" }, 
					new EventHandler[] {Events.noPageDialog,Events.noPageDialog,
							Events.noPageDialog,Events.noPageDialog,
							Events.dragDropSimObjects, Events.noPageDialog}));
			
			menuData.put("data", new MenuContainer(new String[] {"Tables", "Views", 
					"Variables", "Data Sources" }, 
					new EventHandler[] {Events.noPageDialog,Events.noPageDialog,
							Events.noPageDialog,Events.noPageDialog}));
			
			menuData.put("support", new MenuContainer(new String[] {"Help", 
					"Documentation", "Examples"}, 
					new EventHandler[] {Events.noPageDialog,Events.noPageDialog,
							Events.noPageDialog}));
			
			
			
			
			TabPane tp=(TabPane)scene.lookup("#topMenu");
			for(Tab t:tp.getTabs()) {
				String key=t.getId();
				if(key==null || key.equals(""))
					key = t.getText().toLowerCase(Globals.language);
				MenuContainer menuContainer=menuData.get(key);
				if(menuContainer.text!=null) {
					String[] uri=getIconList(menuContainer.text);
					HBox hb=ToolKit.getHorizontalMenu(menuContainer.text, uri, 
							32, 32,menuContainer.events);
					t.setContent(hb);
				}
			}
			
			String[] data = {"Entity Instance Factory", "Source", "Sink", "Entity", 
					"Store", "Workstation", "Resource"};
			Accordion t=(Accordion)scene.lookup("#leftMenu");
			String[] uri=getIconList(data);
			VBox vb=ToolKit.getVerticalMenu(data, uri, 24, 24);
			t.getPanes().get(0).setContent(vb);
			
			TabPane mainWindow=(TabPane)scene.lookup("#mainWindow");
			mainWindow.setTabDragPolicy(TabPane.TabDragPolicy.REORDER);
			Globals.mainWindow=mainWindow;
			Globals.homePage=mainWindow.getTabs().get(0);
			mainWindow.getTabs().add(new Tab("deneme"));
			
			WebView wv=(WebView) scene.lookup("#homePage");
			WebEngine we=wv.getEngine();
			we.setJavaScriptEnabled(true);
			we.getLoadWorker().stateProperty().addListener(
			        new ChangeListener<State>() {
			            public void changed(@SuppressWarnings("rawtypes") 
			            ObservableValue ov, State oldState, State newState) {
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
