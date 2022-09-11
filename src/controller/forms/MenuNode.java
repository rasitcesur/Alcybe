package controller.forms;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class MenuNode {
	public String id;
	public EventHandler<MouseEvent> clickEvent;
	public boolean allowMultipleForms;
	
	public MenuNode() { }
	
	public MenuNode(String id, EventHandler<MouseEvent> clickEvent, boolean allowMultipleForms) {
		this.id=id;
		this.clickEvent=clickEvent;
		this.allowMultipleForms=allowMultipleForms;
	}

	public String getId() {
		return id;
	}

	public EventHandler<MouseEvent> getClickEvent() {
		return clickEvent;
	}

	public boolean isAllowMultipleForms() {
		return allowMultipleForms;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setClickEvent(EventHandler<MouseEvent> clickEvent) {
		this.clickEvent = clickEvent;
	}

	public void setAllowMultipleForms(boolean allowMultipleForms) {
		this.allowMultipleForms = allowMultipleForms;
	}

}
