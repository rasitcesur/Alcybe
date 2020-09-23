package test;

import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import alcybe.analytics.RandomNumberAgent;
import alcybe.computation.ComputationAgent;
import alcybe.simulation.Engine;
import alcybe.simulation.data.TaskNode;
import alcybe.simulation.events.DiscreteEvent;
import alcybe.simulation.events.EventType;
import alcybe.simulation.events.UserDefinedEvent;
import alcybe.simulation.objects.Entity;
import alcybe.simulation.objects.SimulationObject;
import alcybe.simulation.types.Task;
import alcybe.simulation.types.TransferableElement;
import alcybe.utils.data.TimeUnit;

public class EventList {
	
	public static Entity[] entities;
	public static long day;
	public static int count=0, dIndex=0;
	public static final long dayMS=86400000l;
	public static final int[] numOfInfected = {1, 0, 4, 1, 12, 29, 41, 93, 168, 311, 277, 289, 293, 343, 561, 1196, 2069, 1704, 1815, 1610, 
			2704, 2148, 2456, 2786, 3013, 3135, 3148, 3892, 4117, 4056, 4747, 5138, 4789, 4093, 4062, 4281, 4801, 4353, 
			3783, 3977, 4674, 4611, 3083, 3116, 3122, 2861, 2357, 2131, 2392, 2936, 2615, 2188, 1983, 1670, 1614, 1832, 
			2253, 1977, 1848};
	
	public static int generateTimeSpan(int dMin, int dMax, int hMin, int hMax) {
		double days= RandomNumberAgent.generateUniformRandom(dMin, dMax);
		return (int)Math.round(days*24+8+Math.round(RandomNumberAgent.generateUniformRandom(hMin, hMax)));
	}
	
	public static void infect(Engine engine, SimulationObject source, double random) {
		if(random>85 && source.getState().equals("Healthy")) {
			long d=engine.getSimulationTime().getTime();
			int dS=(int)((d-engine.getSimulationBeginTime().getTime())/dayMS);
			int dE=(int)((day-engine.getSimulationBeginTime().getTime())/dayMS);
			if(dS!=dE) {
				day=d;
				count=0;
			}
			
			double infected=EventList.numOfInfected[dS];
			infected/=100.0;
			infected=Math.round(infected);
			
			if(count<infected) {
				source.setState("Infected");
				int hours=generateTimeSpan(3, 5, 0, 24);
				engine.addEvent(new DiscreteEvent(10, source, new TaskNode((TransferableElement) source),
						ComputationAgent.addTime(engine.getSimulationTime(), 
								TimeUnit.HOUR, hours), 3, EventType.UserDefinedEvent));
				count++;
			}
		}
	}
	
	public static void checkInfected(Engine engine, Task task, SimulationObject entity, double random) {
		boolean hasInfected=false;
		for (Entry<Object, List<TransferableElement>> e : task.elementBuffer.entrySet()) {
			for(TransferableElement t : e.getValue()) {
				hasInfected=t.getState().equals("Infected")|| t.getState().equals("Asymptomatic");
				if(hasInfected) break;
			}
			if(hasInfected) break;
		}
		
		if(entity.getState().equals("Infected")|| entity.getState().equals("Asymptomatic")) {
			for (Entry<Object, List<TransferableElement>> e : task.elementBuffer.entrySet()) 
				for(TransferableElement t : e.getValue()) 
					infect(engine, t,random);
		} else if(hasInfected)
			infect(engine, entity,random);
	}
	
	public static void initTransferToShop(Engine simulationEngine, TaskNode currentNode) {
		int hours=generateTimeSpan(7, 15, 0, 11);
		Date d=simulationEngine.getSimulationTime();
		hours-=d.getHours();
		simulationEngine.addEvent(new DiscreteEvent(10, currentNode.entity, currentNode,
				ComputationAgent.addTime(d, TimeUnit.HOUR, hours), 2, EventType.UserDefinedEvent));
	}
	
	public static UserDefinedEvent[] events= {
		new UserDefinedEvent() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			//Home 0
			@SuppressWarnings("deprecation")
			@Override
			public void act(Engine simulationEngine, TaskNode currentNode) {
				
				Task task=(Task)currentNode.target;
				checkInfected(simulationEngine, task, currentNode.entity,100);
				
				initTransferToShop(simulationEngine, currentNode);
				
			}
		},
		
		//Shop 1
		new UserDefinedEvent() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void act(Engine simulationEngine, TaskNode currentNode) {
				
				//Task task=(Task)simulationEngine.objectList.get(0);
				Task task=currentNode.target;
				double r=RandomNumberAgent.generateUniformRandom(0, 100);
				checkInfected(simulationEngine, task, currentNode.entity,r);
				
				simulationEngine.addEvent(new DiscreteEvent(10, currentNode.entity, currentNode,
						ComputationAgent.addTime(simulationEngine.getSimulationTime(), TimeUnit.HOUR, 1), 
						2, EventType.UserDefinedEvent));
			}
		},
		
		//Transfer to Home & Shop 2
		new UserDefinedEvent() {
					
			/**
			 * 
		 	*/
			private static final long serialVersionUID = 1L;
			
			@Override
			public void act(Engine simulationEngine, TaskNode currentNode) {
				
				Object state=currentNode.entity.getState();
				if(state.equals("Healthy")||state.equals("Infected")||state.equals("Asymptomatic")||state.equals("Recovered")) {
					Task task=(Task)simulationEngine.objectList.get(0);
					if(currentNode.target.equals(task)) {
						simulationEngine.transfer(currentNode.entity, currentNode, new TaskNode(currentNode.entity.home), 
								simulationEngine.getSimulationTime());
					} else if((boolean)currentNode.entity.getAttribute("Shopping")) {
						if((boolean)currentNode.entity.getAttribute("UnderQuarantine")) {
							initTransferToShop(simulationEngine, currentNode);
						}else {
							simulationEngine.transfer(currentNode.entity, currentNode, new TaskNode(task), 
									simulationEngine.getSimulationTime());
						}
					}
				}
			}
		},
		
		//Transfer to Hospital 3
		new UserDefinedEvent() {
							
			/**
			 * 
		 	 */
			private static final long serialVersionUID = 1L;
			
			@Override
			public void act(Engine simulationEngine, TaskNode currentNode) {
				double r=RandomNumberAgent.generateUniformRandom(0, 100);
				currentNode.entity.setAttribute("UnderQuarantine",true);
				if(r<=95) {
					currentNode.entity.setState("Symptomatic");
					Task task=(Task)simulationEngine.objectList.get(1);
					simulationEngine.transfer(currentNode.entity, currentNode, new TaskNode(task), 
							simulationEngine.getSimulationTime());
					
					simulationEngine.addEvent(new DiscreteEvent(10, currentNode.entity, new TaskNode(currentNode.entity)
							,simulationEngine.getSimulationTime(), 7, EventType.UserDefinedEvent));
				} else {
					currentNode.entity.setState("Asymptomatic");
					int hours=generateTimeSpan(10, 12, 0, 1);
					simulationEngine.transfer(currentNode.entity, currentNode, new TaskNode(currentNode.entity.home), 
							simulationEngine.getSimulationTime());
					simulationEngine.addEvent(new DiscreteEvent(10, source, new TaskNode(currentNode.entity),
							ComputationAgent.addTime(simulationEngine.getSimulationTime(), 
									TimeUnit.HOUR, hours), 4, EventType.UserDefinedEvent));
				}
				for (Entry<Object, List<TransferableElement>> e : currentNode.entity.home.elementBuffer.entrySet()) {
					for(TransferableElement t : e.getValue()) {
				
						if(!(boolean)t.getAttribute("UnderQuarantine")) {
							t.setAttribute("UnderQuarantine",true);
							simulationEngine.transfer(t, currentNode, new TaskNode(t.home), 
									simulationEngine.getSimulationTime());
						}
					}
				}
				
			}
		},
		
		//Heal in home 4
		new UserDefinedEvent() {
									
			/**
			 * 
		 	 */
			private static final long serialVersionUID = 1L;
			
			@Override
			public void act(Engine simulationEngine, TaskNode currentNode) {
				Entity entity=(Entity) currentNode.entity;
				entity.setAttribute("UnderQuarantine",false);
				entity.setState("Recovered");
			}
		},
		
		//Heal in hospital 5
		new UserDefinedEvent() {
									
			/**
			 * 
		 	 */
			private static final long serialVersionUID = 1L;
			
			@Override
			public void act(Engine simulationEngine, TaskNode currentNode) {
				Entity entity=(Entity) currentNode.entity;
				entity.setState("Recovered");
				entity.setAttribute("UnderQuarantine",false);
				simulationEngine.transfer(currentNode.entity, currentNode, new TaskNode(currentNode.entity.home), 
						simulationEngine.getSimulationTime());
			}
		},
		
		//Died in hospital 6
		new UserDefinedEvent() {
									
			/**
			 * 
		 	 */
			private static final long serialVersionUID = 1L;
			
			@Override
			public void act(Engine simulationEngine, TaskNode currentNode) {
				Entity entity=(Entity) currentNode.entity;
				entity.setState("Dead");
				Task task=(Task)simulationEngine.objectList.get(2);
				simulationEngine.transfer(currentNode.entity, currentNode, new TaskNode(task), 
						simulationEngine.getSimulationTime());
			}
		},
		
		//Hospital Covid19 Test 7
		new UserDefinedEvent() {
											
			/**
			 * 
		 	 */
			private static final long serialVersionUID = 1L;
			
			@Override
			public void act(Engine simulationEngine, TaskNode currentNode) {

				//Mild, Svere, Intensive Care, Intubated, Death, Recovered
				int hours=generateTimeSpan(1, 3, 0, 1);
				simulationEngine.addEvent(new DiscreteEvent(10, source, new TaskNode(currentNode.entity),
						ComputationAgent.addTime(simulationEngine.getSimulationTime(), 
								TimeUnit.HOUR, hours), 8, EventType.UserDefinedEvent));
			}
		},
		
		//Hospital Care 8
		new UserDefinedEvent() {
											
			/**
			 * 
		 	 */
			private static final long serialVersionUID = 1L;
			
			private void healEvent(Engine simulationEngine, TaskNode currentNode, 
					int dMin, int dMax, int hMin, int hMax) {
				int hours=generateTimeSpan(dMin, dMax, hMin, hMax);
				simulationEngine.addEvent(new DiscreteEvent(10, source, new TaskNode(currentNode.entity),
						ComputationAgent.addTime(simulationEngine.getSimulationTime(), 
								TimeUnit.HOUR, hours), 5, EventType.UserDefinedEvent));
			}
			
			@Override
			public void act(Engine simulationEngine, TaskNode currentNode) {

				//Mild, Svere, Intensive Care, Intubated, Death, Recovered
				double r=RandomNumberAgent.generateUniformRandom(0, 100);
				if(r<60) {
					currentNode.entity.setState("Mild");
					simulationEngine.transfer(currentNode.entity, currentNode, new TaskNode(currentNode.entity.home), 
							simulationEngine.getSimulationTime());

					int hours=generateTimeSpan(7, 9, 0, 1);
					simulationEngine.addEvent(new DiscreteEvent(10, source, new TaskNode(currentNode.entity),
							ComputationAgent.addTime(simulationEngine.getSimulationTime(), 
									TimeUnit.HOUR, hours), 4, EventType.UserDefinedEvent));
				} else if(r<20) {
					currentNode.entity.setState("Svere");
					healEvent(simulationEngine, currentNode, 10,12,0,1);
				} else if(r<15) {
					currentNode.entity.setState("ICU");
					healEvent(simulationEngine, currentNode, 14,18,0,1);
				} else {

					int hours=generateTimeSpan(16, 22, 0, 12);
					simulationEngine.addEvent(new DiscreteEvent(10, source, new TaskNode(currentNode.entity),
							ComputationAgent.addTime(simulationEngine.getSimulationTime(), 
									TimeUnit.HOUR, hours), 6, EventType.UserDefinedEvent));
				}
			}
		},				
		
		//Before Transfer to Market 9
		new UserDefinedEvent() {
													
			/**
			 * 
		 	 */
			private static final long serialVersionUID = 1L;
					
			@Override
			public void act(Engine simulationEngine, TaskNode currentNode) {
					//Mild, Svere, Intensive Care, Intubated, Death, Recovered
				this.cancelSimulationEvent = (int)currentNode.target.getIdentifier()==1000000 &&
						!( currentNode.entity.getState().equals("Asymptomatic") || currentNode.entity.getState().equals("Infected") || 
								currentNode.entity.getState().equals("Healthy") || currentNode.entity.getState().equals("Recovered"));
				
				/**if(this.cancelSimulationEvent) {
					System.out.println("............");
					System.out.println("Transferring to shop is cancelled!");
					System.out.println("............");
				}*/
			
			}
		}				
	};
}
