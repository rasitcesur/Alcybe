package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import controller.ObservableResourceFactory;
import controller.UIController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;

public class Globals {
	
	public static final Locale systemLanguage=new Locale("en");

	public static final Logger logger = Logger.getLogger(Globals.class.getName());
	public static ObservableResourceFactory resourceFactory=new ObservableResourceFactory();
	private static HashMap<String,Node> tabContents=new HashMap<>();
	public static TabPane mainWindow;
	
	static {
		logger.setUseParentHandlers(false);
		try {
			FileHandler fh=new FileHandler("./logs/ui.log", true);
			logger.addHandler(fh);
		} catch (SecurityException | IOException e) {
			//TODO Logger initialization error message
		}
	}
	
	public static ResourceBundle getResourceBundle() {
		return resourceFactory.getResource();
	}
	
	public static void setTabContent(String tabTitle, Node node) {
		tabContents.put(tabTitle, node);
	}
	
	public static void setTabContent(Tab tab) throws Exception {
		Node n=tabContents.get(tab.getText());
		if(n==null) {
			FXMLLoader fxmlLoader = new FXMLLoader();
			Pane p = fxmlLoader.load(Main.getResource("/"+tab.getId()+".fxml").openStream());
			if(fxmlLoader.getController()!=null) {
				UIController c = (UIController)fxmlLoader.getController();
				c.loaded(p);
			}
			n=p;
			tabContents.put(tab.getText(), p);
		}
		tab.setContent(n);
	}
	
	public static Node getTabContent(String name) {
		return tabContents.get(name);
	}
	
	public static Tab getTab(String name) {
		Tab tab=null;
		for(Tab t:Globals.mainWindow.getTabs()) {
			if(t.getText().equals(name)) {
				tab=t;
				break;
			}
		}
		return tab;
	}
	
	public static Tab getTab(int index) {
		return Globals.mainWindow.getTabs().get(index);
	}
	
	public static Integer[] findTab(String name) {
		List<Integer> indexes=new ArrayList<>();
		int ix=0;
		for(Tab t:Globals.mainWindow.getTabs()) {
			if(t.getText().equals(name))
				indexes.add(ix);
			ix++;
		}
		return indexes.toArray(new Integer[0]);
	}
	
	public static void selectTab(Tab tab) {
		SingleSelectionModel<Tab> selectionModel = 
				mainWindow.getSelectionModel();
		selectionModel.select(tab); //select by object    
	}
	
	public static boolean isTabActive(Tab tab) {
		boolean found=false;
		for(Tab t:Globals.mainWindow.getTabs()) {
			found=t.equals(tab);
			if(found) break;
		}
		return found;
	}
	
	public static String getMenuIconURI(String text) {
		return "images/menu/"+text.replace(' ', '-').
				toLowerCase(Globals.systemLanguage)+".png";
		
	}
}
