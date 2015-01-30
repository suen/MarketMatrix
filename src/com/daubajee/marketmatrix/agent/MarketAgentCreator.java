package com.daubajee.marketmatrix.agent;

import com.daubajee.marketmatrix.gui.JavaFXGUIController;
import com.daubajee.marketmatrix.gui.JavaFXVisualisation;

import jade.core.Agent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

/**
 * @author surendra
 * Creates n MarketAgents and terminates
 */
public class MarketAgentCreator extends Agent {

	private String[] names = {"alex", "julien", "moly", "patrick", "karine", "etien",
								"thiery", "fred", "ludo", "paul", "daniel", "remy", "jean"};
	private String[] params = {
			"produces=Wheat,produceRate=6,consumes=Potatoes,consumeRate=4,"
				+ "consumeStock=32,consumeStockCapacity=400,produceStock=80,produceStockCapacity=400,"
				+ "price=2,money=3,satisfaction=50",
			"produces=Potatoes,produceRate=4,consumes=Rice,consumeRate=3,"
				+ "consumeStock=20,consumeStockCapacity=100,produceStock=40,produceStockCapacity=200,"
				+ "price=3,money=10,satisfaction=50",
			"produces=Rice,produceRate=2,consumes=Wheat,consumeRate=10,"
				+ "consumeStock=32,consumeStockCapacity=100,produceStock=32,produceStockCapacity=100,"
				+ "price=4,money=15,satisfaction=50",
			"produces=Wheat,produceRate=8,consumes=Rice,consumeRate=4,"
				+ "consumeStock=32,consumeStockCapacity=100,produceStock=60,produceStockCapacity=100,"
				+ "price=2,money=14,satisfaction=50",
			"produces=Potatoes,produceRate=5,consumes=Meat,consumeRate=10,"
				+ "consumeStock=32,consumeStockCapacity=100,produceStock=40,produceStockCapacity=100,"
				+ "price=3,money=6,satisfaction=50",
			"produces=Meat,produceRate=2,consumes=Rice,consumeRate=3,"
				+ "consumeStock=40,consumeStockCapacity=100,produceStock=60,produceStockCapacity=100,"
				+ "price=6,money=50,satisfaction=50",
			"produces=Rice,produceRate=1,consumes=Potatoes,consumeRate=10,"
				+ "consumeStock=32,consumeStockCapacity=100,produceStock=32,produceStockCapacity=100,"
				+ "price=2,money=0,satisfaction=50"
			};
	private JavaFXGUIController gui;
	private int agentCounter = 0;
	
	@Override
	protected void setup() {
		super.setup();

		while (! JavaFXVisualisation.showGUI());
		gui = JavaFXGUIController.getInstance();

		gui.registerAgentCreator(this);
		
	    AgentContainer c = getContainerController();

	    for(int i=0; i<0; i++){
	    	try {
	    		AgentController a = c.createNewAgent( names[i], 
	    				"com.daubajee.marketmatrix.agent.MarketAgent", params[i].split(","));
	    		a.start();
	    	}
	    	catch (Exception e){}
	    }
		
	}
	
	public void onAgentAdd(String consumes, double consumeRate,
					String produces, double produceRate){
	    AgentContainer c = getContainerController();
	    
	    String agentParam = "produces="+produces+",produceRate="+String.valueOf(produceRate)
	    		+",consumes="+consumes+",consumeRate="+String.valueOf(consumeRate)+","
				+ "consumeStock=10,consumeStockCapacity=100,produceStock=10,produceStockCapacity=200,"
				+ "price=2,money=10,satisfaction=1";
	    
	    String agentName = names[(agentCounter++)%13]+String.valueOf(agentCounter);
	    
		try {
			AgentController a = c.createNewAgent( agentName, "com.daubajee.marketmatrix.agent.MarketAgent", agentParam.split(","));
			a.start();
		} catch (StaleProxyException e) {
			//gui.addMsg("Could not create agent, StaleProxyException thrown");
			e.printStackTrace();
		}
		
	}
	


	
}
