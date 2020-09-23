package application;

import java.util.Locale;

import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;

public class Globals {
	
	public static Locale language=new Locale("en");
	
	public static Tab homePage;
	public static TabPane mainWindow;
	
	final static Events events=new Events() {
		
		@SuppressWarnings("unchecked")
		@Override
		public void init() {
			// TODO Auto-generated method stub
			this.mouseEvents = new EventHandler[] {
					new EventHandler<MouseEvent>() {
						@Override 
						   public void handle(MouseEvent e) { 
								boolean found=false;
								for(Tab t:Globals.mainWindow.getTabs()) {
									found=t.equals(Globals.homePage);
									if(found) break;
								}
								if(!found) {
									Globals.mainWindow.getTabs().add(Globals.homePage);
								}
								//System.out.println("Hello World");           
						   } 
					}
			};
		}
	};
	
	public static String getMenuIconURI(String text) {
		return "images/menu/"+text.replace(' ', '-').
				toLowerCase(Globals.language)+".png";
		
	}
}
