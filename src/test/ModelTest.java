package test;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import alcybe.simulation.model.Model;

public class ModelTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file = new File("/home/storm/");

		try {
		    // Convert File to a URL
		    URL url = file.toURI().toURL();
		    URL[] urls = new URL[]{url};

		    // Create a new class loader with the directory
		    @SuppressWarnings("resource")
			ClassLoader cl = new URLClassLoader(urls);
		    
		    // Load in the class; MyClass.class should be located in
		    // the directory file:/c:/myclasses/com/mycompany		    h.run();

		    Class cls = cl.loadClass("alcybe.simulation.model.HelloWorld");
		    Model h=(Model) cls.newInstance();
		    h.run();
		} catch (Exception e) { 
			e.printStackTrace();
			
		}
	}
}