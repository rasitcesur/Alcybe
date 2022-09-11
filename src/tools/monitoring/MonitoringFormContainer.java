package tools.monitoring;

import eu.hansolo.medusa.Gauge;
import javafx.scene.layout.FlowPane;

public class MonitoringFormContainer {
	
	public FlowPane monitoringPane;
	public Gauge[] gauges;
	
	public MonitoringFormContainer() { }
	
	public MonitoringFormContainer(FlowPane monitoringPane, Gauge[] gauges) { 
		this.monitoringPane = monitoringPane;
		this.gauges = gauges;
	}

}
