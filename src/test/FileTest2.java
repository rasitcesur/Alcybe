package test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import alcybe.utils.io.FileAgent;
import alcybe.utils.io.FileAgent.FileCreateMode;

public class FileTest2 {


	public static void main(String[] args) throws IOException, URISyntaxException {
		// TODO Auto-generated method stub
		String data=FileAgent.readTextFile(new URI("kppr_down.txt"));
		//System.out.println(data);
		
		String[] querySet=data.split("Query= ");
		Object[][] rs= {{"","",""}};
		Object[] c={"ID","Name", "Explanation"};

		DefaultTableModel model=new DefaultTableModel(rs,c);
		
		@SuppressWarnings("unused")
		JTable pivot=new JTable(model);
		
		int cursor=0;
		for(int k=1;k<querySet.length;k++) {
			String q=querySet[k];
			String id=q.split("\n")[0];

			String[] resultSet=q.split("> ");
			
			String row;
			int columnIndex=-1;
			int idIndex, expIndex;
			for (int i = 1; i < resultSet.length; i++) {
				row=resultSet[i].split("Query")[0];
				idIndex=row.indexOf(' ');
				expIndex=row.lastIndexOf(' ', row.indexOf('='));
				
				String name = row.substring(0, idIndex);
				System.out.print(id+"\t"+name+" "+"\t");
				String explanation=row.substring(idIndex+1, expIndex).replace("\r", "").replace("\n", "");
				
				String[] rowData=row.substring(expIndex+1).replace("\r", "").replace("\n", " ").split("=");
				int index=0, index2;
				model.setValueAt(id, cursor, 0);
				model.setValueAt(name, cursor, 1);
				model.setValueAt(explanation, cursor, 2);
				for (int j = 1; j < rowData.length; j++) {
					String column=rowData[j-1].substring(index, rowData[j-1].length()).trim();
					rowData[j]=rowData[j].trim();
					index=rowData[j].lastIndexOf(" ", rowData[j].length());
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
					model.setValueAt(value, cursor, columnIndex);
					System.out.print(value+"\t");
				}
				System.out.println();
				cursor++;
				model.addRow(new Object[model.getColumnCount()]);
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
			if(result.length()>5000) {
				FileAgent.writeFile(new URI("blast_res.csv"), result, FileCreateMode.IfNotExists, true);
				result="";
			}
		}
		if(result.equals(""))
			FileAgent.writeFile(new URI("blast_res.csv"), result, FileCreateMode.IfNotExists, true);

		System.out.println("Bitti!");
	}

}
