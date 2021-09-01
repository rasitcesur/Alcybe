package tools;

import application.Globals;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ToolKit {
	
	public static HBox getHorizontalMenu(String[] text, String[] imageURI, 
			int iconWidth, int iconHeight, EventHandler<MouseEvent>[] events) {
		HBox hb=new HBox();
		
		for (int i = 0; i < text.length; i++) {
			AlcybeVBox vb=new AlcybeVBox();
			vb.addEventHandler(MouseEvent.MOUSE_CLICKED, events[i]);
			vb.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
			vb.setAlignment(Pos.BASELINE_CENTER);
			ImageView iv=new ImageView("file:"+imageURI[i]);
			iv.setFitWidth(iconWidth);
			iv.setFitHeight(iconHeight);
			vb.getChildren().add(iv);
			Label lbl=new Label(text[i]);
			
			//Label text binding for localization management
			vb.textBinding="Menu."+text[i];
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
		
		hb.textBinding = "Objects."+text;
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
