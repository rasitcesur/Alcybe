package application;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import controller.ObservableResourceFactory;
import javafx.scene.control.TabPane;

public class Globals {
	
	public static final Locale systemLanguage=new Locale("en");
	
	public static final Logger logger = Logger.getLogger(Globals.class.getName());
	public static ObservableResourceFactory resourceFactory=new ObservableResourceFactory();
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
	

}
