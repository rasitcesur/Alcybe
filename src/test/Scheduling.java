package test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


public class Scheduling {

	public static void main(String[] args) throws IOException, URISyntaxException {
		// TODO Auto-generated method stub
		
		for(int i=0; i<10;i++) {
			Date beginDate=new Date(119, 0, 1, 8, 0);
			Object[] simInfo= initEngine("Barnes_mt10c1.fjs", 
					DispatchingStrategy.FirstAvailable,
					ElementSelectionRule.HighestPriority, beginDate);
			List<TaskNode> taskList = (List<TaskNode>) simInfo[1];
			for (TaskNode tn : taskList) {
				tn.priority=Math.round(Math.random()*10);
			}
			
			Engine simEngine=new Engine();
			//Date beginDate=getDate();
			simEngine.showTrace=false;
			
			Date upperBound=new Date(119, 11, 31, 23, 59);
			simEngine.initEvent(new DiscreteEvent[]{ (DiscreteEvent)simInfo[0] }, 
					upperBound);//getDate(beginDate,1));
			//long begin=System.nanoTime();
			simEngine.run();
			//long end=System.nanoTime();
			System.out.println(i+":"+simEngine.getSimulationTime());
		}
	}
	
	public static Object[] initEngine(String fileName, DispatchingStrategy wsDispatching, 
			ElementSelectionRule eSelection, Date beginDate) 
					throws IOException, URISyntaxException {

		final List<TaskNode> taskList=new ArrayList<>();
		
		String data=FileAgent.readTextFile(new URI("datasets/"+fileName)); //Barnes_mt10xx.fjs
		String[] lines=data.replaceAll("\r", "").split("\n");
		String[] lengthData=lines[0].replaceAll("   "," ").replaceAll("  ", " ").split(" ");
		int numEntity=Integer.parseInt(lengthData[0]),
				numWS=Integer.parseInt(lengthData[1]);
		
		Source src=new Source();
		src.setIdentifier("S1");
		src.timeBetweenArrivals="0";
		src.arrivalTreshold=1;
		Entity[] entityList=new Entity[numEntity];
		for(int i=0;i<entityList.length;i++) {
			entityList[i]=new Entity();
			entityList[i].setIdentifier("P"+(i+1));
			src.addElement(entityList[i]);
		}
		
		Workstation[] wsList=new Workstation[numWS];
		for(int i=0;i<wsList.length;i++) {
			wsList[i]=new Workstation();
			//wsList[i].priority=1;
			wsList[i].elementSelectionRule = eSelection;
			wsList[i].setIdentifier("W"+(i+1));
		}
		
		for(int i=1,e=0;e<numEntity;i++,e++) {
			String[] routeData=lines[i].replaceAll("   ", "  ").replaceAll("  ", " ").split(" ");			
			for(int j=1;j<routeData.length;j++) {
				if(!routeData[j].equals("")) {
					int taskNum=Integer.parseInt(routeData[j]);
					TaskNode[] tn=new TaskNode[taskNum];
					j++;
					for(int k=0;k<taskNum;k++) {
						int target=Integer.parseInt(routeData[j])-1;
						tn[k]=new TaskNode(wsList[target],routeData[j+1]);
						taskList.add(tn[k]);
						//tn[k].priority=1;
						j+=2;
						
					}
					entityList[e].addTarget(new TransferInfo(new Process(wsDispatching, tn)));
					j--;
				}
			}
		}

		return new Object[] { new DiscreteEvent(0,null,new TaskNode(src), 
				beginDate, EventType.ArrivalEvent), taskList, wsList};
	}

}
