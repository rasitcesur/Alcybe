package tools;

public class ControlAttribute{
	public String name;
	public Object value;
	
	public ControlAttribute() { }
	
	public ControlAttribute(String name, Object value) {
		this.value = value;
		this.name = name;
	}
}