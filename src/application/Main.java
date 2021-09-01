package application;

import java.net.URL;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;

import controller.Events;
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
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import tools.LocalComboContainer;
import tools.ToolKit;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Main extends Application {
	
	
	//Software startup animation.
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
				try {
					Image icon=new Image("file:images/alcybe_icon.png");
	
					showMainWindow(icon);
					try {
						Thread.sleep(100);
					} catch(InterruptedException e) {
						Globals.logger.log(Level.SEVERE, "Animation Sleeping", e);
					}
					stage.close();
				} catch(Exception e) {
					Globals.logger.log(Level.SEVERE, "Animation Finished", e);
				}
			}
		});
    }
	
	@Override
	public void start(Stage primaryStage) {

        FlowPane root = new FlowPane();
        try {
			Globals.logger.info("Startup Window Loading");
			Image icon=new Image("file:images/alcybe_icon.png");
			Image openningImage=new Image("file:images/alcybe.png");
			
			primaryStage.getIcons().add(icon);
			primaryStage.initStyle(StageStyle.TRANSPARENT);
	        root.setAlignment(Pos.CENTER);
	        Scene scene = new Scene(root, 730, 430);
	        scene.setFill(Color.TRANSPARENT);
	        
			ImageView imageView = new ImageView(openningImage); 
		    imageView.setPreserveRatio(true);  
	        root.getChildren().add(imageView);
	        primaryStage.setScene(scene);
			
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(icon);
	        primaryStage.show();
        } catch(Exception e) {
			Globals.logger.log(Level.SEVERE, "Startup Window Loading", e);
		}
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
	
	static URL getResource(String resourcePath) {
		return formClass.getResource(resourcePath);

	}
	private static Class<?> formClass;
	
	@SuppressWarnings("unchecked")
	public Stage showMainWindow(Image icon) {
		
		try {
			Stage stage = new Stage();
			Scene scene = null;
			try {
				formClass=getClass();
				Pane root = FXMLLoader.load(formClass.getResource("/MainForm.fxml"));
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
				
	
				Globals.resourceFactory.setResources(ResourceBundle.getBundle("en_EN"));
				
				scene = new Scene(root,1280,720);
				
				scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
		            @Override
		            public void handle(KeyEvent event) {
		            	if(event.getCode()==KeyCode.SHIFT)
		            		//newTabMode=true if wanted to add same tab again
		            		Events.newTabMode=false;
		           
		            }
		        });
	
		        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
		            @Override
		            public void handle(KeyEvent event) {
		            	if(event.getCode()==KeyCode.SHIFT)
		            		Events.newTabMode=false;
		            }
		        });
	
				
				stage.setScene(scene);
				stage.getIcons().add(icon);
				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

					@Override
					public void handle(WindowEvent event) {
						Alert alert = new Alert(AlertType.NONE,
								Globals.getResourceBundle().getString("Alerts.CloseWindowMessage"), ButtonType.NO,
								ButtonType.YES);
						alert.setTitle(Globals.getResourceBundle().getString("Alerts.CloseWindowTitle"));
						Optional<ButtonType> result = alert.showAndWait();
						if (result.get() == ButtonType.YES) 
							Globals.logger.info("Stage Closing");
						else
							event.consume();
					}
				});
				
				stage.show();
			} catch(Exception e) {
				Globals.logger.log(Level.SEVERE, "Stage Loading", e);
			}
			
			try{			
				HashMap<String, MenuContainer> menuData = 
						new HashMap<String, MenuContainer>();
						
				menuData.put("file", new MenuContainer(
						new String[] {"Home", "Open", "Save", "SaveAs", "Licensing"},
						new EventHandler[] {Events.menuItemClick, Events.noPageDialog,
								Events.noPageDialog,Events.noPageDialog,
								Events.noPageDialog}));
				
				menuData.put("engine", new MenuContainer( new String[] {"EngineSettings", 
						"NewEngineBlock", "BlockPriority"},
						new EventHandler[] {Events.noPageDialog,Events.noPageDialog,
								Events.noPageDialog}));
				
				menuData.put("reporting", new MenuContainer(new String[] {"ReportDesign", "Pivot", 
						"GanttChart", "BarChart", "LineChart", "PieChart", 
						"Histogram", "BoxPlot"}, 
						new EventHandler[] {Events.noPageDialog,Events.noPageDialog,
								Events.noPageDialog,Events.noPageDialog,Events.noPageDialog,
								Events.noPageDialog,Events.noPageDialog,
								Events.noPageDialog}));
				
				menuData.put("model", new MenuContainer(new String[] {"NewModel", 
						"ImportModel", "ExportModel", "EventDefinition", "Play", "Pause", 
						"Stop", "RunSettings", "Experiment", "Optimization"}, 
						new EventHandler[] {Events.noPageDialog,Events.noPageDialog,
								Events.noPageDialog,Events.noPageDialog,Events.noPageDialog,
								Events.noPageDialog,Events.noPageDialog,
								Events.noPageDialog,Events.noPageDialog,
								Events.noPageDialog}));
				
				menuData.put("layout", new MenuContainer(new String[] {"FlowLayout", 
						"GridLayout", "FreeForm2D", "UserDefinedForms" }, 
						new EventHandler[] {Events.noPageDialog,Events.noPageDialog,
								Events.menuItemClick, Events.noPageDialog}));
				
				menuData.put("data", new MenuContainer(new String[] {"Tables", "Views", 
						"Variables", "DataSources" }, 
						new EventHandler[] {Events.noPageDialog,Events.noPageDialog,
								Events.menuItemClick,Events.noPageDialog}));
				
				menuData.put("support", new MenuContainer(new String[] {"Help", 
						"Documentation", "Examples"}, 
						new EventHandler[] {Events.noPageDialog,Events.noPageDialog,
								Events.noPageDialog}));
				
				TabPane tp=(TabPane)scene.lookup("#topMenu");
				
				for(Tab tab:tp.getTabs())
					tab.textProperty().bind(Globals.resourceFactory.getStringBinding("Tab."+tab.getText()));
					
				for(Tab t:tp.getTabs()) {
					String key=t.getId();
					if(key==null || key.equals(""))
						key = t.getText().toLowerCase(Globals.systemLanguage);
					MenuContainer menuContainer=menuData.get(key);
					if(menuContainer.text!=null) {
						String[] uri=getIconList(menuContainer.text);
						HBox hb=ToolKit.getHorizontalMenu(menuContainer.text, uri, 
								32, 32,menuContainer.events);
						t.setContent(hb);
					}
				}
				
				HBox fileTabContent=(HBox) tp.getTabs().get(0).getContent();
				
				final ComboBox<LocalComboContainer> cb=new ComboBox<LocalComboContainer>();
				cb.getItems().addAll(new LocalComboContainer("English", ResourceBundle.getBundle("en_EN")),
						new LocalComboContainer("Türkçe",ResourceBundle.getBundle("tr_TR")));
				fileTabContent.getChildren().add(cb);
				cb.valueProperty().addListener(new ChangeListener<LocalComboContainer>() {
					@Override
					public void changed(ObservableValue<? extends LocalComboContainer> element, 
							LocalComboContainer oldValue, LocalComboContainer newValue) {
						
						Globals.resourceFactory.setResources(newValue.getResource());
						
					}
			    });
			
			} catch(Exception e) {
				Globals.logger.log(Level.SEVERE, "Menu Loading", e);
			}

			try {
				String[] data = {"EntityInstanceFactory", "Source", "Sink", "Entity", 
						"Store", "Workstation", "Resource", "Sensor"};
				Accordion t=(Accordion)scene.lookup("#leftMenu");
				String[] uri=getIconList(data);
				VBox vb=ToolKit.getVerticalMenu(data, uri, 24, 24);
				t.getPanes().get(0).setContent(vb);
			} catch(Exception e) {
				Globals.logger.log(Level.SEVERE, "Simulation Objects Pane Loading", e);
			}
			
			try {
				TabPane mainWindow=(TabPane)scene.lookup("#mainWindow");			
				mainWindow.setTabDragPolicy(TabPane.TabDragPolicy.REORDER);
				Globals.mainWindow=mainWindow;
				
				Tab tab=mainWindow.getTabs().get(0);
				tab.textProperty().bind(Globals.resourceFactory.getStringBinding("Menu."+tab.getText()));
				Globals.setTabContent(tab.getText(), tab.getContent());
				//mainWindow.getTabs().add(new Tab("deneme"));
			} catch(Exception e) {
				Globals.logger.log(Level.SEVERE, "Main Tab Loading", e);
			}
			
			
			try {
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
			} catch(Exception e) {
				Globals.logger.log(Level.SEVERE, "Web View Loading", e);
			}
			return stage;
		} catch(Exception e) {
			Globals.logger.log(Level.SEVERE, "Main Window Loading", e);
		}
        return null;
	}
	
	public static void main(String[] args) throws Exception {
		launch(args);
		
	}
	
}
