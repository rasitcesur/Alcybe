package application;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class MenuContainer {
	public String[] text;
	public EventHandler<MouseEvent>[] events;
	
	public MenuContainer() { }
	
	public MenuContainer(String[] text, EventHandler<MouseEvent>[] events) { 
		this.text=text;
		this.events=events;
	}
	
}
