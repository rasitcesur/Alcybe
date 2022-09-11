package simulation.model;

import java.util.HashMap;

public class SensorModel extends ObjectModel {
	
	public String type, name;
	public String[] parameters;
	
	static final String packageName="alcybe.utils.io.sensoradapters";
	final HashMap<String, Boolean> adapterIndex = new HashMap<>();
	
	static {}
	
	public SensorModel(String type, String name, String... parameters) {
		this.type = type;
		this.name = name;
		this.parameters = parameters;
	}

	@Override
	public String getSourceCode() {
		String params="";
		if(parameters.length>0) {
			params=parameters[0];
			for(int i=1;i<parameters.length;i++)
				params+=", "+parameters[i];
		}
		return type + " " + name + " = new " + type + "("+params+");\n";
	}
}
