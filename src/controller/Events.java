package controller;

import application.Globals;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import tools.AlcybeVBox;
import tools.ToolKit;

public class Events {
	
	public static boolean newTabMode=false;
	
	static class TabContainer {
		public Tab tab;
		public boolean newTab;
		
		public TabContainer(Tab tab, boolean newTab) {
			this.tab=tab;
			this.newTab=newTab;
		}
		
	}
	
	private static TabContainer addTab(Object source) {
		AlcybeVBox menuItem=(AlcybeVBox)source;
		String tabDef=menuItem.toString();
		Integer[] tabs=ToolKit.findTab(tabDef);
		boolean firstTab=tabs.length==0;
		boolean newTab = firstTab || newTabMode;
		Tab t=null;
		if(newTab) {
			t=new Tab(tabDef);
			String[] tag = menuItem.textBinding.split("\\.");
			t.setId(tag[tag.length-1]);
			t.textProperty().bind(Globals.resourceFactory.getStringBinding(menuItem.textBinding));
			try {
				ToolKit.setTabContent(t);
			} catch (Exception e1) { }
			newTabMode=false;
			Globals.mainWindow.getTabs().add(t);
		} else
			t=ToolKit.getTab(tabs[0]);
		
		/**t.setOnCloseRequest(new EventHandler<Event>()
		{
		    @Override
		    public void handle(Event arg0) {
		        
		    }
		});*/
		
		ToolKit.selectTab(t);
		
		return new TabContainer(t, newTab);
	}
	
	public static EventHandler<MouseEvent> menuItemClick = new EventHandler<MouseEvent>() {
		@Override 
		   public void handle(MouseEvent e) { 
				addTab(e.getSource());
		   } 
	}, noPageDialog = new EventHandler<MouseEvent>() {
		@Override 
		   public void handle(MouseEvent e) { 
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Form error!");
				alert.setHeaderText(null);
				alert.setContentText("This form has not ready yet.");
				alert.showAndWait();
		   } 
	};
	
}
