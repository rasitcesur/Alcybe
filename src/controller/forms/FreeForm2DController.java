package controller.forms;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import application.Globals;
import controller.UIController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import tools.AlcybeHBox;
import tools.ToolKit;

public class FreeForm2DController extends UIController implements Initializable{


	private Pane panel=null;
	private final HashMap<String, Integer> objectIndex=new HashMap<>();
	
	@FXML
	public void dragOver(DragEvent event) {
        /* data is dragged over the target */
        /* accept it only if it is  not dragged from the same node 
         * and if it has a string data */
        if (event.getGestureSource() != panel &&
                event.getDragboard().hasString()) {
            /* allow for both copying and moving, whatever user chooses */
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }    
	
	public void dragEntered(DragEvent event) {
        /**if (event.getGestureSource() != panel &&
                event.getDragboard().hasString()) {
            //target.setFill(Color.GREEN);
        }*/
        event.consume();
    }

	public void dragExited(DragEvent event) {
        event.consume();
    }
	
	public void dragDropped(DragEvent event) {
        /** 
         * data dropped
         * If any string data exists on dragboard, read it.
         * */
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasString()) {
        	
        	/**
        	 * Parsing tag of the simulation object.
        	 * Inserting the serial number of the object. (Variable named value)
        	 * */
        	String tag[] = db.getString().split("\\.");
        	String objectID=tag[tag.length-1];
        	Integer value=objectIndex.get(objectID);
        	if(value==null) {
        		value=1;
        	} else value++;
        	objectIndex.put(objectID, value); // Object index stores last serial number of simulation objects
        	
        	
        	/**
        	 * Creating the simulation object and add it to the panel.
        	 * */
        	final AlcybeHBox hb=ToolKit.getSimulationObject(objectID+value,
            		ToolKit.getMenuIconURI(objectID), 24, 24);
        	hb.setLayoutX(event.getX());
        	hb.setLayoutY(event.getY());
        	
        	hb.setOnMousePressed(new EventHandler<MouseEvent>() {
        		@Override public void handle(MouseEvent mouseEvent) {
        		    //Modifying cursor icon for the drag and drop operation.
        			hb.setCursor(Cursor.MOVE);
        		}
        	});
        	
        	hb.setOnMouseReleased(new EventHandler<MouseEvent>() {
        		@Override public void handle(MouseEvent mouseEvent) {
        		    //Setting default icon to cursor for finalizing the drag and drop operation.
        			hb.setCursor(Cursor.DEFAULT);
        		}
        	});
        	hb.setOnMouseDragged(new EventHandler<MouseEvent>() {
        		@Override public void handle(MouseEvent mouseEvent) {
        			//Setting new position of the selected simulation object on the panel.
        			hb.setLayoutX(hb.getLayoutX()+mouseEvent.getX());
        			hb.setLayoutY(hb.getLayoutY()+mouseEvent.getY());
        		}
        	});
        	hb.setOnMouseEntered(new EventHandler<MouseEvent>() {
        		@Override public void handle(MouseEvent mouseEvent) {
        			hb.setCursor(Cursor.HAND);
        		}
        	});
        	
            panel.getChildren().add(hb);
            success = true;
        }
        /* let the source know whether the string was successfully 
         * transferred and used */
        event.setDropCompleted(success);
        
        event.consume();
    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@Override
	public void loaded(Pane pane) {
		// TODO Auto-generated method stub
		this.panel=pane;
	}

}
