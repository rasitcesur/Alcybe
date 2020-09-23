package test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

public class IteratorTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double init=0, retrieve=0;
		long begin,end;
		
		for (int k = 0; k < 10; k++) {
			init=0;
			retrieve=0;
			for (int j = 0; j < 1000000; j++) {
				Vector<Integer> ts=new Vector<Integer>();
				begin=System.nanoTime();
				for (int i = 0; i < 1000; i++) {
					ts.add(i);
				}
				end=System.nanoTime();
				init+=(double)(end-begin)/1E9;
				
				int res=0;

				res=ts.get(999);
				end=System.nanoTime();
				retrieve+=(double)(end-begin)/1E9;
			}
			System.out.print(init+"\t"+retrieve);
			
			init=0;
			retrieve=0;
			for (int j = 0; j < 1000000; j++) {
				LinkedList<Integer> ts=new LinkedList<Integer>();
				begin=System.nanoTime();
				for (int i = 0; i < 1000; i++) {
					ts.add(i);
				}
				end=System.nanoTime();
				init+=(double)(end-begin)/1E9;
				
				int res=0;

				begin=System.nanoTime();
				res=ts.get(999);
				end=System.nanoTime();
				retrieve+=(double)(end-begin)/1E9;
			}
			System.out.print("\t"+init+"\t"+retrieve+"\t");
			
			init=0;
			retrieve=0;
			for (int j = 0; j < 1000000; j++) {
				List<Integer> ts=new ArrayList<Integer>();
				begin=System.nanoTime();
				for (int i = 0; i < 1000; i++) {
					ts.add(i);
				}
				end=System.nanoTime();
				init+=(double)(end-begin)/1E9;
				
				int res=0;

				begin=System.nanoTime();
				res=ts.get(999);
				end=System.nanoTime();
				retrieve+=(double)(end-begin)/1E9;
			}
			System.out.print(init+"\t"+retrieve+"\t");

			
		}
		
	}

}
