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
import alcybe.simulation.types.DispatchingStrategy;
import alcybe.simulation.types.Process;
import alcybe.simulation.types.TransferInfo;

public class SimulationScheduling {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Engine simEngine=new Engine();
		Workstation ebat=new Workstation(),st=new Workstation(),bant=new Workstation(),
				del=new Workstation(), aym=new Workstation(), mon=new Workstation();
		ebat.setIdentifier("1.Ebatlama");
		st.setIdentifier("2.Serit Testere");
		bant.setIdentifier("3.Bantlama");
		del.setIdentifier("4.Delme");
		aym.setIdentifier("5.Ayak Montaj");
		mon.setIdentifier("6.Montaj");
		//m3.bufferCapacity=1;
		//List<SimulationEvent> events=new ArrayList<>();

		Entity sehpa=new Entity(),masa=new Entity(),s_ust_tabla=new Entity(),s_ayak1=new Entity(),s_ayak2=new Entity(),
				s_ayak_komplesi=new Entity(), m_ayak_komplesi=new Entity(), 
				m_ust_tabla=new Entity(),m_ayak1=new Entity(),m_ayak2=new Entity(),m_perde=new Entity();
		
		
		s_ust_tabla.setIdentifier("1.S_Ust_Tabla"); 
		s_ayak1.setIdentifier("2.S_Ayak1");
		s_ayak2.setIdentifier("2.S_Ayak2");
		s_ayak_komplesi.setIdentifier("3.S_Ayak_Komplesi");
		sehpa.setIdentifier("4.Sehpa");
		m_ust_tabla.setIdentifier("5.M_Ust_Tabla"); 
		m_ayak1.setIdentifier("7.M_Ayak1");
		m_ayak2.setIdentifier("7.M_Ayak2");
		m_perde.setIdentifier("6.M_Perde");
		m_ayak_komplesi.setIdentifier("8.M_Ayak_Komplesi");
		masa.setIdentifier("9.Masa");
				
		//BomElement[] inputs=new BomElement[] {new BomElement(p1,1), new BomElement(p2,1)}; 
		TransferableElementContainer[] s_outputs= {new TransferableElementContainer(sehpa,1,1)};
		TransferableElementContainer[] s_inputs=new TransferableElementContainer[] {new TransferableElementContainer(s_ust_tabla,1), new TransferableElementContainer(s_ayak_komplesi,1)}; 
		
		TransferableElementContainer[] sa_outputs= {new TransferableElementContainer(s_ayak_komplesi,1,1)};
		TransferableElementContainer[] sa_inputs=new TransferableElementContainer[] {new TransferableElementContainer(s_ayak1,1), 
				new TransferableElementContainer(s_ayak2,1)}; 
		
		TransferableElementContainer[] m_outputs= {new TransferableElementContainer(masa,1,1)};
		TransferableElementContainer[] m_inputs=new TransferableElementContainer[] {new TransferableElementContainer(m_ust_tabla,1), new TransferableElementContainer(m_ayak_komplesi,1), 
				new TransferableElementContainer(m_perde,1)}; 
		
		TransferableElementContainer[] ma_outputs= {new TransferableElementContainer(m_ayak_komplesi,1,1)};
		TransferableElementContainer[] ma_inputs=new TransferableElementContainer[] {new TransferableElementContainer(m_ayak1,1), 
				new TransferableElementContainer(m_ayak2,1)}; 
				
		s_ust_tabla.addTarget(new TransferInfo(new Process(new TaskNode(ebat,"30"))));
		s_ust_tabla.addTarget(new TransferInfo(new Process(new TaskNode(bant,"60"))));
		s_ust_tabla.addTarget(new TransferInfo(new Process(new TaskNode(del,"90"))));
		s_ust_tabla.addTarget(new TransferInfo(s_outputs, new Process(1, s_inputs,new TaskNode(mon,"120")))); 
		
		s_ayak1.addTarget(new TransferInfo(new Process(new TaskNode(st,"40"))));
		s_ayak1.addTarget(new TransferInfo(new Process(new TaskNode(del,"90"))));
		s_ayak1.addTarget(new TransferInfo(sa_outputs, new Process(2, sa_inputs, new TaskNode(aym,"100"))));
		
		s_ayak2.addTarget(new TransferInfo(new Process(new TaskNode(st,"40"))));
		s_ayak2.addTarget(new TransferInfo(new Process(new TaskNode(del,"90"))));
		s_ayak2.addTarget(new TransferInfo(sa_outputs, new Process(3, sa_inputs, new TaskNode(aym,"100"))));
		
		s_ayak_komplesi.addTarget(new TransferInfo(s_outputs, new Process(4, s_inputs,new TaskNode(mon,"120")))); 
		
		
		m_ust_tabla.addTarget(new TransferInfo(new Process(new TaskNode(ebat,"100"))));
		m_ust_tabla.addTarget(new TransferInfo(new Process(new TaskNode(bant,"120"))));
		m_ust_tabla.addTarget(new TransferInfo(new Process(new TaskNode(del,"60"))));
		m_ust_tabla.addTarget(new TransferInfo(m_outputs, new Process(5, m_inputs,new TaskNode(mon,"300")))); 
		
		m_perde.addTarget(new TransferInfo(new Process(new TaskNode(ebat,"90"))));
		m_perde.addTarget(new TransferInfo(new Process(new TaskNode(bant,"70"))));
		m_perde.addTarget(new TransferInfo(new Process(new TaskNode(del,"60"))));
		m_perde.addTarget(new TransferInfo(m_outputs, new Process(6, m_inputs,new TaskNode(mon,"300")))); 
		
		m_ayak1.addTarget(new TransferInfo(new Process(new TaskNode(st,"140"))));
		m_ayak1.addTarget(new TransferInfo(new Process(new TaskNode(del,"120"))));
		m_ayak1.addTarget(new TransferInfo(ma_outputs, new Process(7, ma_inputs, new TaskNode(aym,"120"))));
		
		m_ayak2.addTarget(new TransferInfo(new Process(new TaskNode(st,"140"))));
		m_ayak2.addTarget(new TransferInfo(new Process(new TaskNode(del,"120"))));
		m_ayak2.addTarget(new TransferInfo(ma_outputs, new Process(8, ma_inputs, new TaskNode(aym,"120"))));
		
		m_ayak_komplesi.addTarget(new TransferInfo(m_outputs, new Process(9, m_inputs,new TaskNode(mon,"300")))); 
		
		
		/**
		p1.addTarget(new TransferInfo(new Bom(new TaskNode(m1,"40")), new Bom(new TaskNode(m3,"30"))));
		//p1.addTarget(new TransferInfo(new Bom(new TaskNode(m3,"30"))));
		p2.addTarget(new TransferInfo(new Bom(new TaskNode(m2,"25"))));// , new Bom(new TaskNode(m3,"20"))));
		p2.addTarget(new TransferInfo(new Bom(new TaskNode(m3,"20"))));
		*/
		
		Source src=new Source();
		src.setIdentifier("S1");
		
		//aym.bufferCapacity=2;
		
		src.addElement(s_ayak1);
		src.addElement(m_ayak1);
		src.addElement(m_ayak2);
		src.addElement(m_perde);
		src.addElement(m_ust_tabla);
		
		src.addElement(s_ust_tabla);
		src.addElement(s_ayak2);

		
		
		src.timeBetweenArrivals="0";
		src.arrivalTreshold=1;
		//Date beginDate=getDate();
		simEngine.initDefaultBlocks();
		simEngine.initEvent(new DiscreteEvent[]{new DiscreteEvent(0,null,new TaskNode(src), 
				new Date(2018, 5, 28, 8, 0),//beginDate, 
				EventType.ArrivalEvent)}, new Date(2018, 5, 29, 8, 0));//getDate(beginDate,1));
		simEngine.initDefaultBlocks();
		long begin=System.nanoTime();
		simEngine.run();
		long end=System.nanoTime();
		System.out.println(((double)end-begin)/1E9d);
	}
	
	public static Date getDate(){
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}
	
	public static Date getDate(Date begin, int timespan){
		Calendar cal = Calendar.getInstance();
		cal.setTime(begin);
		cal.add(Calendar.YEAR, timespan);
		return cal.getTime();
	}
}
