package application;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public abstract class Events {
	public static EventHandler<MouseEvent>[] mouseEvents;
	
	abstract public void init();
}
