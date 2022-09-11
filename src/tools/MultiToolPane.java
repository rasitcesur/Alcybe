package tools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class MultiToolPane extends AnchorPane{
	
	private Node tool=null;
	private Class<?> cls=null;
	@SuppressWarnings("deprecation")
	public MultiToolPane(String toolDefinition) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		cls = Class.forName("javafx.scene.control."+toolDefinition);
		tool = (Node) cls.newInstance();
		getChildren().add(tool);
		setTopAnchor(tool, 0.0);
		setBottomAnchor(tool, 0.0);
		setRightAnchor(tool, 0.0);
		setLeftAnchor(tool, 0.0);
	}
	
	public MultiToolPane(String toolDefinition, ControlAttribute... attributes) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, NoSuchFieldException, SecurityException, InvocationTargetException {
		this(toolDefinition);
		for (ControlAttribute a : attributes) {
			Method method = null;
			while(method==null && cls!=null) {
				for(Method m:cls.getDeclaredMethods())
					if(m.getName().equals(a.name)) {
						method=m;
						break;
					}
				cls=cls.getSuperclass();
			}
			method.invoke(tool, a.value);
		}
	}

}
