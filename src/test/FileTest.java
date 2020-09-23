package test;

import java.awt.Point;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import alcybe.utils.io.FileAgent;


public class FileTest {


	private void parseData() throws IOException, URISyntaxException {
		String data=FileAgent.readTextFile(new URI("data.txt"));
		data=data.replace("\r", "");
		String[] rows = data.split("\n");
		int statIndex=0;
		String result="";
		for (int i = 0; i < rows.length; i++) {
			String[] cells=rows[i].split("\t");
			if(cells.length==1)
				cells=rows[i].split(" ");
			statIndex=0;
			for (String c : cells) {
				if(!c.equals("")) {
					String[] cellContent=c.split(":");
					if(cellContent.length==2)
						result+=i+"\t"+cellContent[0]+"\t"+cellContent[1]+"\n";
					else if(cellContent.length>2){
						String temp="",prefix="";
						for (int j = 1; j < cellContent.length; j++) {
							temp+=prefix+cellContent[j];
							prefix=":";
						}
						result+=i+"\t"+cellContent[0]+"\t"+temp+"\n";
					} else{
						cellContent=c.split("=");
						if(cellContent.length==2)
							result+=i+"\t"+cellContent[0]+"\t"+cellContent[1]+"\n";
						else {
							if(statIndex == 0)
								result+=i+"\t"+"Header"+"\t"+cellContent[0]+"\n";
							else if(statIndex == 1)
								result+=i+"\t"+"Status"+"\t"+cellContent[0]+"\n";
							statIndex++;
						}
					}
				}
			}
		}
		FileAgent.writeFile(new URI("result.csv"), result);
		System.out.println("OK");
	}
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		// TODO Auto-generated method stub
		String data=FileAgent.readTextFile(new URI("BLAST_search_BC.html"));
		System.out.println(data);
		String[] resultSet=data.split("<PRE>\n><a name = ");
		String row;
		
		
		
		Object[][] rs=new Object[1][1];
		Object[] c={"ID","Explanation"};

		DefaultTableModel model=new DefaultTableModel(rs,c);
		JTable pivot=new JTable(model);

		int columnIndex=-1;
		
		for (int i = 1; i < resultSet.length; i++) {
			String[] rsArray=resultSet[i].split("</a>");
			row=rsArray[2].split("</PRE>")[0];
			String id = rsArray[1].split("target=_blank>")[1];
			String[] rowData=row.split("=");
			int index=rowData[0].lastIndexOf(" ", rowData[0].length()-5), index2;
			String explanation=rowData[0].substring(0, index).trim().replace("\r", "").replace("\n", "").
					replace("           ", "");
			model.addRow(new Object[model.getColumnCount()]);
			model.setValueAt(id, i-1, 0);
			model.setValueAt(explanation, i-1, 1);
			for (int j = 1; j < rowData.length; j++) {
				String column=rowData[j-1].substring(index, rowData[j-1].length()).trim();
				index=rowData[j].lastIndexOf(" ", rowData[j].length()-5);
				index2=rowData[j].indexOf("\n", 0);
				if(index2>-1)
					index=Math.min(index, index2);
				String value=rowData[j].substring(0, index).trim();
				if(value.charAt(value.length()-1)==',')
					value=value.substring(0, value.length()-1);
				columnIndex=model.findColumn(column);
				if(columnIndex==-1) {
					columnIndex=model.getColumnCount();
					model.addColumn(column);
				}
				model.setValueAt(value, i-1, columnIndex);
			}
		}
		
		String result="";
		for (int j = 0; j < model.getColumnCount(); j++) {
			result+=model.getColumnName(j)+"\t";
		}
		result+="\r\n";
		
		for (int i = 0; i < model.getRowCount(); i++) {
			for (int j = 0; j < model.getColumnCount(); j++) {
				result+=model.getValueAt(i, j)+"\t";
			}
			result+="\r\n";
		}
		FileAgent.writeFile(new URI("blast_result.csv"), result);
		
	}

}
