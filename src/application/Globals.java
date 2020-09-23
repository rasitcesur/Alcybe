package application;

import java.util.Locale;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class Globals {
	
	public static Locale language=new Locale("en");
	
	public static Tab homePage;
	public static TabPane mainWindow;
	
	public static String getMenuIconURI(String text) {
		return "images/menu/"+text.replace(' ', '-').
				toLowerCase(Globals.language)+".png";
		
	}
}
