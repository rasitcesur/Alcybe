package application;

import alcybe.tools.AlcybeHBox;
import alcybe.tools.ToolKit;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

public class Events {
	
	public static EventHandler<MouseEvent> dragDropSimObjects=new EventHandler<MouseEvent>() {
		
		@Override 
		public void handle(MouseEvent e) { 
			Tab t = new Tab("Layout");
			final Pane p = new Pane();

	        p.setOnDragOver(new EventHandler <DragEvent>() {
	            public void handle(DragEvent event) {
	                /* data is dragged over the target */
	                System.out.println("onDragOver");
	                
	                /* accept it only if it is  not dragged from the same node 
	                 * and if it has a string data */
	                if (event.getGestureSource() != p &&
	                        event.getDragboard().hasString()) {
	                    /* allow for both copying and moving, whatever user chooses */
	                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
	                }
	                
	                event.consume();
	            }
	        });

	        p.setOnDragEntered(new EventHandler <DragEvent>() {
	            public void handle(DragEvent event) {
	                /* the drag-and-drop gesture entered the target */
	                System.out.println("onDragEntered");
	                /* show to the user that it is an actual gesture target */
	                if (event.getGestureSource() != p &&
	                        event.getDragboard().hasString()) {
	                    //target.setFill(Color.GREEN);
	                }
	                
	                event.consume();
	            }
	        });

	        p.setOnDragExited(new EventHandler <DragEvent>() {
	            public void handle(DragEvent event) {
	                /* mouse moved away, remove the graphical cues */
	                //p.setFill(Color.BLACK);
	                
	                event.consume();
	            }
	        });
	        
	        
	        p.setOnDragDropped(new EventHandler <DragEvent>() {
	            public void handle(DragEvent event) {
	                /* data dropped */
	                System.out.println("onDragDropped");
	                /* if there is a string data on dragboard, read it and use it */
	                Dragboard db = event.getDragboard();
	                boolean success = false;
	                if (db.hasString()) {
	                	final AlcybeHBox hb=ToolKit.getVerticalMenuNode(db.getString(),
	                    		Globals.getMenuIconURI(db.getString()), 24, 24);
	                	hb.setLayoutX(event.getX());
	                	hb.setLayoutY(event.getY());
	                	
	                	hb.setOnMousePressed(new EventHandler<MouseEvent>() {
	                		@Override public void handle(MouseEvent mouseEvent) {
	                		    // record a delta distance for the drag and drop operation.
	                			hb.setCursor(Cursor.MOVE);
	                		}
	                	});
	                	
	                	hb.setOnMouseReleased(new EventHandler<MouseEvent>() {
	                		@Override public void handle(MouseEvent mouseEvent) {
	                			hb.setCursor(Cursor.DEFAULT);
	                		}
	                	});
	                	hb.setOnMouseDragged(new EventHandler<MouseEvent>() {
	                		@Override public void handle(MouseEvent mouseEvent) {
	                			hb.setLayoutX(hb.getLayoutX()+mouseEvent.getX());
	                			hb.setLayoutY(hb.getLayoutY()+mouseEvent.getY());
	                		}
	                	});
	                	hb.setOnMouseEntered(new EventHandler<MouseEvent>() {
	                		@Override public void handle(MouseEvent mouseEvent) {
	                			hb.setCursor(Cursor.HAND);
	                		}
	                	});
	                	
	                    p.getChildren().add(hb);
	                    success = true;
	                }
	                /* let the source know whether the string was successfully 
	                 * transferred and used */
	                event.setDropCompleted(success);
	                
	                event.consume();
	            }
	        });
			
			t.setContent(p);
			Globals.mainWindow.getTabs().add(t);
		} 
	}, homePage = new EventHandler<MouseEvent>() {
		@Override 
		   public void handle(MouseEvent e) { 
				boolean found=false;
				for(Tab t:Globals.mainWindow.getTabs()) {
					found=t.equals(Globals.homePage);
					if(found) break;
				}
				
				SingleSelectionModel<Tab> selectionModel = 
						Globals.mainWindow.getSelectionModel();
				//selectionModel.select(1); //select by index starting with 0
				//selectionModel.clearSelection(); //clear your selection
				
				if(!found) {
					Globals.mainWindow.getTabs().add(Globals.homePage);
				}
				selectionModel.select(Globals.homePage); //select by object
				//System.out.println("Hello World");           
		   } 
	}, noPageDialog = new EventHandler<MouseEvent>() {
		@Override 
		   public void handle(MouseEvent e) { 
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Form error!");
				alert.setHeaderText(null);
				alert.setContentText("This form has not ready yet.");
				alert.showAndWait();
		   } 
	};
	
}
