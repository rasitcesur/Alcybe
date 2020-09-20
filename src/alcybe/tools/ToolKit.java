package alcybe.tools;

import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class ToolKit {

	public static HBox getHorizontalMenu(String[] text, String[] imageURI, 
			int iconWidth, int iconHeight) {
		HBox hb=new HBox();
		
		for (int i = 0; i < text.length; i++) {
			VBox vb=new VBox();
			vb.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
			vb.setAlignment(Pos.BASELINE_CENTER);
			ImageView iv=new ImageView("file:"+imageURI[i]);
			iv.setFitWidth(iconWidth);
			iv.setFitHeight(iconHeight);
			vb.getChildren().add(iv);
			Label lbl=new Label(text[i]);
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
	
	public static VBox getVerticalMenu(String[] text, String[] imageURI, 
			int iconWidth, int iconHeight) {
		VBox vb=new VBox();
		
		for (int i = 0; i < text.length; i++) {
			HBox hb=new HBox();
			hb.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
			hb.setAlignment(Pos.CENTER_LEFT);
			ImageView iv=new ImageView("file:"+imageURI[i]);
			iv.setFitWidth(iconWidth);
			iv.setFitHeight(iconHeight);
			hb.getChildren().add(iv);
			Label lbl=new Label(text[i]);
			lbl.setPadding(new Insets(0,0,0,3));
			lbl.setWrapText(true);
			hb.getChildren().add(lbl);
			hb.autosize();
			hb.setPadding(new Insets(3, 0, 3, 5)); //top, right, bottom, left
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
