package test;

import java.util.Calendar;
import java.util.Date;

import alcybe.simulation.Engine;
import alcybe.simulation.data.TaskNode;
import alcybe.simulation.events.DiscreteEvent;
import alcybe.simulation.events.EventType;
import alcybe.simulation.objects.Entity;
import alcybe.simulation.objects.Source;
import alcybe.simulation.objects.Workstation;
import alcybe.simulation.types.TransferableElementContainer;
import alcybe.simulation.types.Process;
import alcybe.simulation.types.TransferInfo;


public class SimulationTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*Source s =new Source();
		s.run();
		Entity e = new Entity();
		e.run();*/
		/*SortedSet<Integer> index=new TreeSet<>();
		index.add(3);
		index.add(4);
		index.add(5);
		for (Integer i : index.subSet(3, 5)) {
			System.out.println(i);
		}
		Date d1=new Date(1000),d2=new Date(2000);
		System.out.println(d1.compareTo(d2));*/
		
		
		/**
		Engine simEngine=new Engine();
		Workstation eb=new Workstation(),kb=new Workstation(),de=new Workstation(),
				st=new Workstation(),ay=new Workstation(),m=new Workstation();
		eb.setIdentifier("Ebatlama");
		kb.setIdentifier("Bantlama");
		de.setIdentifier("Delme");
		st.setIdentifier("Þerit Testere");
		ay.setIdentifier("Ayak Montajý");
		m.setIdentifier("Masa Montaj");
		//m3.bufferCapacity=3;
		List<SimulationEvent> events=new ArrayList<>();

		Entity ustTabla=new Entity(), ayak=new Entity(), perde=new Entity();
		ustTabla.setIdentifier("P1");
		ayak.setIdentifier("P2");
		p1.targetList.add(new TransferInfo(m1, "40"));
		p1.targetList.add(new TransferInfo(m3, "30"));
		p2.targetList.add(new TransferInfo(m2, "25"));
		p2.targetList.add(new TransferInfo(m3, "20"));
		Source src=new Source();
		src.setIdentifier("S1");
		src.entityBuffer.add(p1);
		src.entityBuffer.add(p2);
		src.timeBetweenArrivals="5";
		src.arrivalTreshold=3;
		Date beginDate=getDate();
		simEngine.initEvent(new SimulationEvent[]{new SimulationEvent(null,src, null, 
				new Date(0), 
				EventType.ArrivalEvent)}, getDate(beginDate,1));
		long begin=System.nanoTime();
		simEngine.run();
		long end=System.nanoTime();
		System.out.println(((double)end-begin)/1E9d);
		*/
		

		Engine simEngine=new Engine();
		Workstation m1=new Workstation(),m2=new Workstation(),m3=new Workstation(),
				m4=new Workstation();
		m1.setIdentifier("Ebatlama");
		m2.setIdentifier("Bantlama");
		m3.setIdentifier("Montaj\t");
		m4.setIdentifier("Paketleme");
		m3.bufferCapacity=1;
		//List<SimulationEvent> events=new ArrayList<>();
		
		Entity p1=new Entity(),p2=new Entity(),p3=new Entity(),p4=new Entity();
		
		TransferableElementContainer[] inputs=new TransferableElementContainer[] {new TransferableElementContainer(p1,1), new TransferableElementContainer(p2,1)}; 
		TransferableElementContainer[] outputs= {new TransferableElementContainer(p3,1,1),new TransferableElementContainer(p4,1,1)};
		
		
		p1.setIdentifier("P1");
		p2.setIdentifier("P2");
		p3.setIdentifier("P3");
		p4.setIdentifier("P4");
		
		
		p1.addTarget(new TransferInfo(new Process(new TaskNode(m1,"40")))); //output, new TaskNode(m3,"30", inputs)
		p1.addTarget(new TransferInfo(outputs, new Process(inputs, new TaskNode(m3,"30"))));
		p2.addTarget(new TransferInfo(new Process(new TaskNode(m2,"25"))));
		p2.addTarget(new TransferInfo(outputs,new Process(inputs, new TaskNode(m3,"30"))));
		p3.addTarget(new TransferInfo(new Process(new TaskNode(m3,"10"))));
		p4.addTarget(new TransferInfo(new Process(new TaskNode(m4,"15"))));
		
		
		/**
		p1.addTarget(new TransferInfo(new TaskNode(m1,"40"), 
				new TaskNode(m3,"30")));
		//p1.targetList.add(new TransferInfo(new OperableElementContainer(m3,"30")));
		p2.addTarget(new TransferInfo(new TaskNode(m2,"25"),new TaskNode(m3,"20"))); //
		//p2.addTarget(new TransferInfo(new TaskNode(m3,"20")));
		*/
		
		Source src=new Source();
		src.setIdentifier("S1\t");
		src.addElement(p1);
		src.addElement(p2);

		src.timeBetweenArrivals="5";
		src.arrivalTreshold=2;
		Date beginDate=getDate();
		simEngine.initDefaultBlocks();
		simEngine.initEvent(new DiscreteEvent[]{new DiscreteEvent(0,null,new TaskNode(src), 
				new Date(2018, 5, 28, 8, 0),//beginDate, 
				EventType.ArrivalEvent)}, new Date(2018, 5, 29, 8, 0));//getDate(beginDate,1));
		long begin=System.nanoTime();
		simEngine.run();
		long end=System.nanoTime();
		System.out.println(((double)end-begin)/1E9d);
		
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
