package test;

import java.net.URI;

import alcybe.data.DataTable;
import alcybe.data.delegates.DelegateTemplate;
import alcybe.data.delegates.DoubleDelegate;
import alcybe.data.delegates.MillisecondsDelegate;
import alcybe.script.AlcybeScriptEngine.ScriptType;
import alcybe.simulation.objects.DigitalTwin;
import alcybe.simulation.objects.Sensor;

public class DigitalTwinTest {

	public static void main(String[] args) throws Exception {
		DataTable dt=new DataTable();
		DelegateTemplate[] columnDelegates=new DelegateTemplate[78];
		columnDelegates[0]=new MillisecondsDelegate();
		for (int i = 1; i < columnDelegates.length; i++) 
			columnDelegates[i]=new DoubleDelegate();
		dt.setColumnDelegates(columnDelegates);
		dt.readData(new URI("dataset_diesel_engine/engine_data.txt"), true);
		Sensor s1=new Sensor("s1",  dt, 5, 0);
		//SensorAdapter sa1=new SensorAdapter(s1, 500);
		Sensor s2=new Sensor("s2", dt, 6, 0);
		//SensorAdapter sa2=new SensorAdapter(s2, 500);
		
		Sensor pair=new Sensor("s3", dt, 7, 0);
		
		

		DigitalTwin twin=new DigitalTwin("twin1", pair, ScriptType.JavaScript, 
				"var measurement=0.938881161*s1-0.039310741*s2-0.018956059;", new Sensor[] {s1,s2}, 0.075);

		Thread.sleep(20);
		new Thread(twin).start();
		System.out.println("finished");
	}

}
