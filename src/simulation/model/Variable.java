package simulation.model;

public class Variable {
	String definition, name, defaultValue;
	
	public Variable() { }
	
	public Variable(String definition, String name, String defaultValue) { 
		this.definition = definition;
		this.name = name;
		this.defaultValue = defaultValue;
	}
	
}
