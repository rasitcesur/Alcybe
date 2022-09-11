package controller.forms;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import alcybe.data.DataTable;
import alcybe.data.delegates.DelegateTemplate;
import alcybe.data.delegates.DoubleDelegate;
import alcybe.data.delegates.MillisecondsDelegate;
import alcybe.sensor.SensorMeasurementAgent;
import alcybe.sensor.SensorMeasurementContainer;
import alcybe.utils.io.sensoradapters.DataTableAdapter;
import controller.UIController;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Gauge.SkinType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import tools.ToolKit;
import tools.monitoring.MonitoringFormContainer;
import tools.monitoring.MonitoringFormDesignerContainer;

public class MonitoringForm extends UIController implements Initializable {
	
	@FXML
	AnchorPane panel;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		final MonitoringFormContainer c = ToolKit.getMonitoringForm(new MonitoringFormDesignerContainer(SkinType.GAUGE, 1500, 0, 0, 8, 1, "Torque", "TORK"),
				new MonitoringFormDesignerContainer(SkinType.LCD, 9999, 0, 0, 8, 1,"Pressure1", "PT-01"),
				new MonitoringFormDesignerContainer(SkinType.LCD, 9999, 0, 0, 8, 1,"Pressure2", "PT-02"));

		
		panel.getChildren().add(c.monitoringPane);
		
		AnchorPane.setTopAnchor(c.monitoringPane, 0.0);
		AnchorPane.setLeftAnchor(c.monitoringPane, 0.0);
		AnchorPane.setRightAnchor(c.monitoringPane, 0.0);
		AnchorPane.setBottomAnchor(c.monitoringPane, 0.0);
		
		DelegateTemplate[] columnType=new DelegateTemplate[77];
		columnType[0]=new MillisecondsDelegate();
		for (int i = 1; i < columnType.length; i++)
			columnType[i]=new DoubleDelegate();
		
		DataTable dt=new DataTable(columnType);
		try {
			dt.readData(new URI("engine_data.txt"), true);
		} catch (IOException | URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		DataTableAdapter tork=new DataTableAdapter(dt, "TORK");
		tork.setId("TORK");
		
		DataTableAdapter pt1=new DataTableAdapter(dt, "PT-01");
		pt1.setId("PT-01");
		
		DataTableAdapter pt2=new DataTableAdapter(dt, "PT-02");
		pt2.setId("PT-02");
		
		
		SensorMeasurementAgent agent=new SensorMeasurementAgent() {
			
			@Override
			public void measurementEvent() {
				// TODO Auto-generated method stub
				SensorMeasurementContainer[] measurements=measure();
				for(SensorMeasurementContainer m:measurements)
					System.out.println(m.id+": "+m.measurement);
				//dosyaya yazma
			}

			@Override
			public void measured(SensorMeasurementContainer[] containers) {
				// TODO Auto-generated method stub
				HashMap<String, SensorMeasurementContainer> measurements=new HashMap<>();
				for (SensorMeasurementContainer c : containers) 
					measurements.put(c.id, c);
				for(Gauge g:c.gauges)
					g.setValue((double)measurements.get(g.getId()).measurement);
			}
		};
		
		agent.addAdapter(tork);
		agent.addAdapter(pt1);
		agent.addAdapter(pt2);
		
		agent.start(99, System.currentTimeMillis(), 1000);
	}

	@Override
	public void loaded(Pane pane) {
		// TODO Auto-generated method stub
		
	}

}
