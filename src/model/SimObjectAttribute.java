package model;

import java.lang.reflect.InvocationTargetException;

import javafx.beans.property.SimpleStringProperty;
import tools.MultiToolPane;

public class SimObjectAttribute {
	
    private final SimpleStringProperty attributeName;
    private MultiToolPane attributeValue;

    public SimObjectAttribute(String attributeName, String toolDefinition) {
        this.attributeName = new SimpleStringProperty(attributeName);
        try {
			this.attributeValue = new MultiToolPane(toolDefinition);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) { }
    }
    
    public SimObjectAttribute(String attributeName, String toolDefinition, 
    		tools.ControlAttribute... attributes) {
        this.attributeName = new SimpleStringProperty(attributeName);
        try {
			this.attributeValue = new MultiToolPane(toolDefinition, attributes);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException | NoSuchFieldException | SecurityException | InvocationTargetException e) { }
    }


	public String getAttributeName() {
		return attributeName.get();
	}
	
	public void setAttributeName(String attributeName) {
		this.attributeName.set(attributeName);
	}

	public MultiToolPane getAttributeValue() {
		return attributeValue;
	}
	
	public void setAttributeValue(MultiToolPane attributeValue) {
		this.attributeValue=attributeValue;
	}
	
	
	
	@Override
	public String toString() {
		return getAttributeName()+": "+getAttributeValue();
	}

}
