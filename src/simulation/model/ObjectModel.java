package simulation.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class ObjectModel {
	
	static final HashMap<String, Boolean> typeIndex=new HashMap<>();
	final List<Variable> variables = new ArrayList<>();
	final HashMap<String, Boolean> variableIndex=new HashMap<>();
	
	static {
		typeIndex.put("boolean", true);
		typeIndex.put("byte", true);
		typeIndex.put("short", true);
		typeIndex.put("int", true);
		typeIndex.put("long", true);
		typeIndex.put("float", true);
		typeIndex.put("double", true);
		typeIndex.put("Boolean", true);
		typeIndex.put("Byte", true);
		typeIndex.put("Short", true);
		typeIndex.put("Integer", true);
		typeIndex.put("Long", true);
		typeIndex.put("Float", true);
		typeIndex.put("Double", true);
		//typeIndex.put("List", true);
		typeIndex.put("ArrayList", true);
		typeIndex.put("LinkedList", true);
		typeIndex.put("Queue", true);
		typeIndex.put("Stack", true);
	}
	
	public void addVariable(String definiton, String name, String defaultValue) {
		variables.add(new Variable(definiton, name, defaultValue));
	}
	
	public abstract String getSourceCode();
	
}
