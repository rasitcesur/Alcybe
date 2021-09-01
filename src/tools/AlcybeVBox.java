package tools;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AlcybeVBox extends VBox {
	
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
