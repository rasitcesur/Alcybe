package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

public class VariablesController extends UIController implements Initializable{


	private Pane panel=null;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@Override
	public void loaded(Pane pane) {
		this.panel=pane;
	}

}
