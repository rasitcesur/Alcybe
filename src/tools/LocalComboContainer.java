package tools;

import java.util.ResourceBundle;

public class LocalComboContainer {

	String text;
	ResourceBundle resource;
	
	public LocalComboContainer() { }
	
	public LocalComboContainer(String text, ResourceBundle resource) { 
		this.text=text;
		this.resource=resource;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public ResourceBundle getResource() {
		return resource;
	}

	public void setResource(ResourceBundle resource) {
		this.resource = resource;
	}
}
