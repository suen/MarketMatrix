package com.daubajee.marketmatrix.agent;

import jade.core.Agent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

/**
 * @author surendra
 * Creates n MarketAgents and terminates
 */
public class MarketAgentCreator extends Agent {

	private String[] names = {"stephane", "julien", "moly", "patrick",
								"ninda", "salina", "luna"};
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
				+ "price=2,money=40,satisfaction=50"
			};
	
	@Override
	protected void setup() {

	    AgentContainer c = getContainerController();

	    for(int i=0; i<7; i++){
	    	try {
	    		AgentController a = c.createNewAgent( names[i], 
	    				"com.daubajee.marketmatrix.agent.MarketAgent", params[i].split(","));
	    		a.start();
	    	}
	    	catch (Exception e){}
	    }
		
		super.setup();
	}

}
