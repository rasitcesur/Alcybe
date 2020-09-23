package application;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public abstract class Events {
	public EventHandler<MouseEvent>[] mouseEvents;
	
	abstract public void init();
}
