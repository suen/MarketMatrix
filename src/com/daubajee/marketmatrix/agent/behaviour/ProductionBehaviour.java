package com.daubajee.marketmatrix.agent.behaviour;

import jade.core.behaviours.TickerBehaviour;

import com.daubajee.marketmatrix.agent.MarketAgent;
import com.daubajee.marketmatrix.agent.MarketAgentAttribute;

public class ProductionBehaviour extends TickerBehaviour{

	private MarketAgent marketAgent;
	private long lastRun;

	public ProductionBehaviour(MarketAgent marketAgent) {
		super(marketAgent, 1000);
		this.marketAgent = marketAgent;
		lastRun = System.currentTimeMillis();
	}

	public void agentAction() {
		MarketAgentAttribute agentAttr = marketAgent.getAttribute();
		
		long curMillis = System.currentTimeMillis();
		
		long diff = curMillis - lastRun;

		// deciding whether or not it is time to produce the product, 
		// if last production was less than a timeUnit ago, return
		if (diff < MarketAgent.TIME_UNIT) {
			return;
		}
		
		lastRun += MarketAgent.TIME_UNIT;
		
		if (MarketAgent.PAUSE){
			return;
		}
		

		int productStock = agentAttr.getProduceProductStock();
		double quantityToProduce = agentAttr.getProduceRate();
		int productStockMax = agentAttr.getProduceProductStockCapacity();
		
		int newStock = productStock + (int) quantityToProduce;
		
		if (newStock > productStockMax){
			newStock = productStockMax;
			
			//means there is no more place in the stock to the produced products
			//raise alert that the stock is full
			marketAgent.printMsg("Production Stock is full");
			agentAttr.setWorkFreeCounter(agentAttr.getWorkFreeCounter()+1);
		} else {
			agentAttr.setWorkFreeCounter(0);
		}
		
//		marketAgent.printMsg("Stock: " + newStock + "/" + productStock + " produced: " + quantityToProduce);
		
		agentAttr.setProduceProductStock(newStock);
		
		marketAgent.updateAgentGUI();
		
		block(4000);
		
	}


	@Override
	protected void onTick() {
		agentAction();
	}
}
