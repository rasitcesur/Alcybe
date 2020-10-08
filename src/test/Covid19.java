package test;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import alcybe.analytics.RandomNumberAgent;
import alcybe.computation.ComputationAgent;
import alcybe.data.InMemoryDatabase;
import alcybe.simulation.Engine;
import alcybe.simulation.data.TaskNode;
import alcybe.simulation.events.ContinuousEvent;
import alcybe.simulation.events.DiscreteEvent;
import alcybe.simulation.events.EventType;
import alcybe.simulation.objects.Entity;
import alcybe.simulation.objects.EntityInstanceFactory;
import alcybe.simulation.objects.Store;
import alcybe.simulation.types.TransferableElement;
import alcybe.utils.data.TimeUnit;

public class Covid19 {
	
	public static final InMemoryDatabase database=new InMemoryDatabase();
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Engine simEngine=new Engine();
		int population=4000;
		int houseCount=1000;
		final int deliveryManCount=5;
		int divider=population/houseCount;
		Store houses[][][]=new Store[population][1][1];
		long[][][] stayTime=new long[population][1][1];
		
		//Shop
		Store ws=new Store();
		ws.setIdentifier(1000000);
		ws.bufferCapacity=4000;
		simEngine.objectList.add(ws);
		
		//Hospital
		ws=new Store();
		ws.setIdentifier(4444444);
		ws.bufferCapacity=4000;
		simEngine.objectList.add(ws);
		
		//Cemetry
		ws=new Store();
		ws.setIdentifier(9999999);
		ws.bufferCapacity=4000;
		simEngine.objectList.add(ws);
		
		
		ws.insertUDEventAtTheBeginning(6, 1);
		for (int i = 0; i < houses.length; i++) {
			if(i%divider==0) {
				ws=new Store();
				ws.setIdentifier(i/divider);
				ws.bufferCapacity=divider;
			}
			houses[i][0][0]=ws;
			simEngine.objectList.add(ws);
			stayTime[i][0][0]=28800;
			if(i%1000==0)
				System.out.println(i);
		}
		System.out.println("workstations are created!");
		
		//EventList.events[0].setTarget(target);
		
		/**Source src=new Source();
		src.setIdentifier("Source");
		src.timeBetweenArrivals="5";
		src.arrivalTreshold=1;*/
		
		final EntityInstanceFactory eif=new EntityInstanceFactory();
		
		eif.workstationList=houses;
		eif.operationTime=stayTime;
		/**Entity[] population=new Entity[4000000];
		for (int i = 0; i < population.length; i++) {
			population[i]=new Entity();
			population[i].setIdentifier(i);
			population[i].addTarget(new TransferInfo(new Process(new TaskNode(houses[i/4], 8l))));
			src.addElement(population[i]);
			if(i%2500==0)
				System.out.println(i);
		}*/
		
		System.gc();
		//Date beginDate=getDate();
		final Date beginDate=new Date(121, 5, 28, 8, 0);
		EventList.day=beginDate.getTime();
		simEngine.showTrace=true;
		simEngine.initDefaultBlocks();
		eif.initArrival(population, simEngine, beginDate, new int[] {6}, new int[] {0}, "Healthy");
		EventList.entities=eif.entities;
		LinkedList<Integer> beginning=new LinkedList<Integer>();
		beginning.add(9);
		for (int i = 0; i < EventList.entities.length; i++) {
			EventList.entities[i].setAttribute("Shopping", i%divider==0);
			EventList.entities[i].setAttribute("UnderQuarantine", false);
			EventList.entities[i].atTheBeginning.put(EventType.TransferEvent.getIndex(),beginning);
		}
		eif.identifier="EIF";
		
		final List<Integer> entityIndex=new LinkedList<>();
		for (int i = 0; i < EventList.entities.length; i++) 
			entityIndex.add(i);
		
		simEngine.addEvent(new ContinuousEvent(8, beginDate, 86400000) {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			
			@Override
			public void act(Engine engine, Date simulationTime) {
				// TODO Auto-generated method stub
				
				long d=simulationTime.getTime();
				int dS=(int)((d-engine.getSimulationBeginTime().getTime())/EventList.dayMS);
				int dE=(int)((EventList.day-engine.getSimulationBeginTime().getTime())/EventList.dayMS);
				if(dS!=dE+1) {
					EventList.day=d;
					EventList.count=0;
				}
				
				double infected=EventList.numOfInfected[dS];
				infected/=100.0;
				infected=Math.round(infected);
				
				int size=(int) (infected-EventList.count);
				for (int i = 0; i < Math.min(size,entityIndex.size()); i++) {
					int r=(int) RandomNumberAgent.generateUniformRandom(0, entityIndex.size());
					int ix=entityIndex.get(r);
					entityIndex.remove(r);
					eif.entities[ix].setState("Infected");
					int hours=EventList.generateTimeSpan(3, 5, 0, 24);
					simEngine.addEvent(new DiscreteEvent(10, eif.entities[ix], new TaskNode((TransferableElement) eif.entities[ix]),
							ComputationAgent.addTime(simulationTime, 
									TimeUnit.HOUR, hours), 3, EventType.UserDefinedEvent));
				}
			}
		});
		
		simEngine.setSimulationEndTime(new Date(121, 7, 5, 8, 0));
		
		System.out.println("beginnig....");
		/**simEngine.initEvent(new DiscreteEvent[]{new DiscreteEvent(0,null,new TaskNode(src), 
				new Date(2018, 5, 28, 8, 0),//beginDate, 
				EventType.ArrivalEvent)}, new Date(2018, 5, 29, 8, 0));//getDate(beginDate,1));*/
		long begin=System.currentTimeMillis();
		simEngine.run();
		long end=System.currentTimeMillis();
		System.out.println(begin+" "+end+" "+(((double)(end-begin))/1E3));

		for (Entity e : eif.entities) {
			System.out.println(e.getState());
		}
		
	}
	
	private static Date getDate(){
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}
	
	private static Date getDate(Date begin, int timespan){
		Calendar cal = Calendar.getInstance();
		cal.setTime(begin);
		cal.add(Calendar.YEAR, timespan);
		return cal.getTime();
	}

}