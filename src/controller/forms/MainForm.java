package controller.forms;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.SimObjectAttribute;
import tools.MultiToolPane;
import tools.ControlAttribute;

public class MainForm implements Initializable{

	@FXML
	public TableView<SimObjectAttribute> attributeTable;
	
	@FXML
	public TableColumn<SimObjectAttribute, String> attributeColumn;
	
	@FXML
	public TableColumn<SimObjectAttribute, MultiToolPane> valueColumn;
	
	
	public void tableMouseClicked(MouseEvent e) {
		//attributeTable.edit(attributeTable.getSelectionModel().getSelectedIndex(), valueColumn);
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		attributeTable.setEditable(true);
		attributeTable.getSelectionModel().cellSelectionEnabledProperty().set(true);		
		
		attributeColumn.setCellValueFactory(
				new PropertyValueFactory<SimObjectAttribute,String>("attributeName"));
		valueColumn.setCellValueFactory(
				new PropertyValueFactory<SimObjectAttribute,MultiToolPane>("attributeValue"));

		valueColumn.setOnEditCommit(
	            new EventHandler<CellEditEvent<SimObjectAttribute, MultiToolPane>>() {
	                @Override
	                public void handle(CellEditEvent<SimObjectAttribute, MultiToolPane> t) {
	                    ((SimObjectAttribute) t.getTableView().getItems().get( t.getTablePosition().getRow())).setAttributeValue(t.getNewValue());
	                }
	            }
	    );
		valueColumn.setStyle( "-fx-alignment: BOTTOM-RIGHT;"); //to alight text next to textArea
		
		
		System.out.println("initialized..........");
		attributeTable.setItems(FXCollections.observableArrayList(
			    new SimObjectAttribute("a","TextField", new ControlAttribute("setText","asdfgfds")),
			    new SimObjectAttribute("c","ComboBox"))
			);
		
		System.out.println("values are set...");
		
		
	}

}
