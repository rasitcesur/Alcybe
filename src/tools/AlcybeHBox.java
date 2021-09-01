package tools;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class AlcybeHBox extends HBox {
	
	public Point2D referencePosition=null;
	public String textBinding;
	
	@Override
	public ObservableList<Node> getChildren() {
		// TODO Auto-generated method stub
		return super.getChildren();
	}
	
	@Override
	public String toString() {
		String res="";
		for(Node n:getChildren())
			if(n instanceof Label)
				res=((Label)n).getText();
		return res;
	}
}
