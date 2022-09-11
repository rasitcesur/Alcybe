package test;

import java.io.IOException;
import java.util.logging.Level;

import application.Globals;

public class LoggingTest {
	
	public static void main(String[] args) throws SecurityException, IOException, InterruptedException {

		
		Globals.logger.info("Logger Name: "+Globals.logger.getName());
		
		Globals.logger.warning("Can cause ArrayIndexOutOfBoundsException");
		
		//An array of size 3
		int []a = {1,2,3};
		int index = 4;
		Globals.logger.config("index is set to "+index);
		
		try{
			System.out.println(a[index]);
		}catch(ArrayIndexOutOfBoundsException ex){
			Globals.logger.log(Level.SEVERE, "Exception occur", ex);
		}
		for(int i=0;i<50;i++) {
			final int j=i;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					Globals.logger.log(Level.SEVERE, "Exception occur Thread "+j);
				}
			}).start();
		}
		Thread.sleep(5000);

	}


}
