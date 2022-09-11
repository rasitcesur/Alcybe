package tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import application.Globals;
import application.Main;
import controller.UIController;
import controller.forms.MenuContainer;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.Section;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import tools.monitoring.MonitoringFormContainer;
import tools.monitoring.MonitoringFormDesignerContainer;

public class ToolKit {
	
	private static HashMap<String,Node> tabContents=new HashMap<>();
	
	private static TabPane getMainWindow() {
		return Globals.mainWindow;
	}
	
	public static void setTabContent(String tabTitle, Node node) {
		tabContents.put(tabTitle, node);
	}
	
	public static void setTabContent(Tab tab) throws Exception {
		Node n=tabContents.get(tab.getText());
		if(n==null) {
			FXMLLoader fxmlLoader = new FXMLLoader();
			try {
			Pane p = fxmlLoader.load(Main.getResource("/"+tab.getId()+".fxml").openStream());
			if(fxmlLoader.getController()!=null) {
				UIController c = (UIController)fxmlLoader.getController();
				c.loaded(p);
			}
			n=p;
			tabContents.put(tab.getText(), p);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		tab.setContent(n);
	}
	
	public static Node getTabContent(String name) {
		return tabContents.get(name);
	}
	
	public static Tab getTab(String name) {
		Tab tab=null;
		for(Tab t:getMainWindow().getTabs()) {
			if(t.getText().equals(name)) {
				tab=t;
				break;
			}
		}
		return tab;
	}
	
	public static Tab getTab(int index) {
		return getMainWindow().getTabs().get(index);
	}
	
	public static Integer[] findTab(String name) {
		List<Integer> indexes=new ArrayList<>();
		int ix=0;
		for(Tab t:getMainWindow().getTabs()) {
			if(t.getText().equals(name))
				indexes.add(ix);
			ix++;
		}
		return indexes.toArray(new Integer[0]);
	}
	
	public static void selectTab(Tab tab) {
		SingleSelectionModel<Tab> selectionModel = 
				getMainWindow().getSelectionModel();
		selectionModel.select(tab); //select by object    
	}
	
	public static boolean isTabActive(Tab tab) {
		boolean found=false;
		for(Tab t:getMainWindow().getTabs()) {
			found=t.equals(tab);
			if(found) break;
		}
		return found;
	}
	
	public static String getMenuIconURI(String text) {
		return "images/menu/"+text.replace(' ', '-').
				toLowerCase(Globals.systemLanguage)+".png";
		
	}
	
	public static HBox getHorizontalMenu(MenuContainer[] menuContainers, 
			int iconWidth, int iconHeight) {
		HBox hb=new HBox();
		
		
		MenuContainer c=null;
		for (int i = 0; i < menuContainers.length; i++) {
			c = menuContainers[i];
			AlcybeVBox vb=new AlcybeVBox();
			vb.addEventHandler(MouseEvent.MOUSE_CLICKED, c.clickEvent);
			vb.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
			vb.setAlignment(Pos.BASELINE_CENTER);
			ImageView iv=new ImageView("file:"+getMenuIconURI(c.id));
			iv.setFitWidth(iconWidth);
			iv.setFitHeight(iconHeight);
			vb.getChildren().add(iv);
			Label lbl=new Label(c.id);
			
			//Label text binding for localization management
			vb.textBinding="Menu."+c.id;
			lbl.textProperty().bind(Globals.resourceFactory.getStringBinding(vb.textBinding));
	
			vb.getChildren().add(lbl);
			vb.autosize();
			vb.setPadding(new Insets(5, 7, 0, 7)); //top, right, bottom, left
			vb.hoverProperty().addListener((observable, oldValue, show) -> {
			    if (show) {
			    	vb.setStyle("-fx-background-color: snow");  
			    }else
			    	vb.setStyle("-fx-border-color: none");  
			});
			hb.getChildren().add(vb);
		}
		
		hb.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
		hb.setAlignment(Pos.TOP_LEFT);

		return hb;
		
	}
	
	public static AlcybeHBox getVerticalMenuNode(String text, String imageURI, int iconWidth, 
			int iconHeight) {
		AlcybeHBox hb=new AlcybeHBox();
		hb.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
		hb.setAlignment(Pos.CENTER_LEFT);
		ImageView iv=new ImageView("file:"+imageURI);
		iv.setFitWidth(iconWidth);
		iv.setFitHeight(iconHeight);
		hb.getChildren().add(iv);
		Label lbl=new Label(text);
		
		hb.textBinding = "Object."+text;
		lbl.textProperty().bind(Globals.resourceFactory.getStringBinding(hb.textBinding));
		
		lbl.setPadding(new Insets(0,0,0,3));
		lbl.setWrapText(true);
		hb.getChildren().add(lbl);
		hb.autosize();
		hb.setPadding(new Insets(3, 0, 3, 5)); //top, right, bottom, left
		return hb;
	}
	
	public static AlcybeHBox getSimulationObject(String text, String imageURI, int iconWidth, 
			int iconHeight) {
		AlcybeHBox hb=new AlcybeHBox();
		hb.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
		hb.setAlignment(Pos.CENTER_LEFT);
		ImageView iv=new ImageView("file:"+imageURI);
		iv.setFitWidth(iconWidth);
		iv.setFitHeight(iconHeight);
		hb.getChildren().add(iv);
		Label lbl=new Label(text);
		
		lbl.setPadding(new Insets(0,0,0,3));
		lbl.setWrapText(true);
		hb.getChildren().add(lbl);
		hb.autosize();
		hb.setPadding(new Insets(3, 0, 3, 5)); //top, right, bottom, left
		return hb;
	}
	
	public static MonitoringFormContainer getMonitoringForm(MonitoringFormDesignerContainer... contents) {
		FlowPane p = new FlowPane();
		Gauge[] gauges = new Gauge[contents.length];
		int i=0;
		for (MonitoringFormDesignerContainer c : contents) {
			Label label=new Label();
			label.textProperty().bind(Globals.resourceFactory.getStringBinding("Monitoring."+c.textBinding));
			//label.setText(c.textBinding);
			Gauge gauge = GaugeBuilder.create().build();
			gauges[i]=gauge;
			i++;
			switch(c.skinType) {
			case GAUGE:{
				gauge.setPrefSize(250, 250);
				gauge.setAutoScale(false);
				double range = (c.maxValue - c.minValue) / 10; 
				gauge.setMinorTickSpace(range/10);
				gauge.setMajorTickSpace(range);
				break;
			}
			case LCD:{
				gauge.setPrefHeight(100);
				gauge.setPrefWidth(c.getWidth()/2);
				break;
			}
			default:
				gauge.setPrefSize(250, 250);
				break;
			}

			gauge.setAnimated(true);
			//gauge.setLedType(LedType.FLAT);
			gauge.setSkinType(c.skinType);
			gauge.setMaxValue(c.maxValue);
			gauge.setMinValue(c.minValue);
			gauge.setDecimals(c.decimals);
			gauge.setValue(c.value);
			gauge.setId(c.sensorId);
			VBox vBox = new VBox();
			vBox.getChildren().add(label);
			vBox.getChildren().add(gauge);
			vBox.setPadding(new Insets(0, 10, 0, 0));
			p.getChildren().add(vBox);
		}
		return new MonitoringFormContainer(p, gauges);
	}
	
	public static VBox getVerticalMenu(String[] text, String[] imageURI, 
			int iconWidth, int iconHeight) {
		VBox vb=new VBox();
		
		for (int i = 0; i < text.length; i++) {
			final AlcybeHBox hb=getVerticalMenuNode(text[i], imageURI[i], 
					iconWidth, iconHeight);
			
			hb.setOnDragDetected(new EventHandler <MouseEvent>() {
	            public void handle(MouseEvent event) {
	                /* drag was detected, start drag-and-drop gesture*/
	               // System.out.println("onDragDetected");
	                
	                /* allow any transfer mode */
	                Dragboard db = hb.startDragAndDrop(TransferMode.ANY);
	                
	                /* put a string on dragboard */
	                ClipboardContent content = new ClipboardContent();
	                content.putString(hb.textBinding);
	                db.setContent(content);
	                
	                event.consume();
	            }
	        });
	        
			hb.setOnDragDone(new EventHandler <DragEvent>() {
	            public void handle(DragEvent event) {
	                /* the drag-and-drop gesture ended */
	                //System.out.println("onDragDone");
	                /* if the data was successfully moved, clear it */
	                /**if (event.getTransferMode() == TransferMode.MOVE) {
	                	System.out.println(hb.toString()+" added");
	                }*/
	                
	                event.consume();
	            }
	        });
			
			hb.hoverProperty().addListener((observable, oldValue, show) -> {
			    if (show) {
			    	vb.setStyle("-fx-background-color: snow");  
			    }else
			    	vb.setStyle("-fx-border-color: none");  
			});
			vb.getChildren().add(hb);
		}
		
		vb.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
		vb.setAlignment(Pos.TOP_LEFT);

		return vb;
	}
}
