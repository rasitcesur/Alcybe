package test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;

import alcybe.simulation.Engine;
import alcybe.simulation.data.TaskNode;
import alcybe.simulation.events.DiscreteEvent;
import alcybe.simulation.events.EventType;
import alcybe.simulation.objects.Entity;
import alcybe.simulation.objects.Source;
import alcybe.simulation.objects.Workstation;
import alcybe.simulation.types.DispatchingStrategy;
import alcybe.simulation.types.Process;
import alcybe.simulation.types.TransferInfo;
import alcybe.utils.io.FileAgent;
import alcybe.simulation.types.Task.ElementSelectionRule;

public class LineBalancing {
	
	public static void initEngine(String fileName, int entityCount, DispatchingStrategy wsDispatching, 
			ElementSelectionRule eSelection) throws IOException, URISyntaxException {
		Engine simEngine=new Engine();
		
		
		Source src=new Source();
		src.setIdentifier("S1");
		src.timeBetweenArrivals="0";
		src.arrivalTreshold=1;
		
		Entity[] entityList=new Entity[entityCount];
		
		for(int i=0;i<entityList.length;i++) {
			entityList[i]=new Entity();
			entityList[i].setIdentifier("P"+(i+1));
			src.addElement(entityList[i]);
		}
		
		String data=FileAgent.readTextFile(new URI(fileName));
		HashMap<String, Workstation> wsList=new HashMap<>(); 
		String[] lines=data.split("\n");
		for (int i = 0; i < lines.length; i++) {
			if(!lines[i].equals("")) {
				String[] row=lines[i].split("\t");
				Workstation ws=null;
				if(!wsList.containsKey(row[0])) {
					ws=new Workstation();
					ws.identifier=row[0];
					ws.elementSelectionRule=eSelection;
					wsList.put(row[0], ws);
					
				} else
					ws=wsList.get(row[0]);
				
				for(int j=0;j<entityList.length;j++) {
					TaskNode tn=new TaskNode(ws,row[2]);
					tn.priority=j;
					entityList[j].addTarget(new TransferInfo(new Process(wsDispatching , tn)));
				}
				
			}
		}

		simEngine.showTrace=true;
		simEngine.initEvent(new DiscreteEvent[]{new DiscreteEvent(0,null,new TaskNode(src), 
				new Date(119, 0, 1, 8, 0),//beginDate, 
				EventType.ArrivalEvent)}, new Date(119, 11, 31, 8, 0));//getDate(beginDate,1));
		simEngine.initDefaultBlocks();
		long begin=System.nanoTime();
		simEngine.run();
		long end=System.nanoTime();
		System.out.print("\t"+((double)end-begin)/1E9d);
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		// TODO Auto-generated method stub
		initEngine("datasets_line/data.txt", 2, 
				DispatchingStrategy.FirstAvailable, ElementSelectionRule.LowestPriority);
		
	}

}
