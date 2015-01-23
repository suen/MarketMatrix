package com.daubajee.marketmatrix.agent.behaviour;

import com.daubajee.marketmatrix.agent.MarketAgent;
import com.daubajee.marketmatrix.agent.MarketAgentAttribute;

import jade.core.behaviours.Behaviour;

public class ProductionBehaviour extends Behaviour {

	private MarketAgent marketAgent;
	private long lastProduction;

	public ProductionBehaviour(MarketAgent marketAgent) {
		this.marketAgent = marketAgent;
		lastProduction = System.currentTimeMillis();
	}

	@Override
	public void action() {
		MarketAgentAttribute agentAttr = marketAgent.getAttribute();
		
		long curMillis = System.currentTimeMillis();
		
		long diff = curMillis - lastProduction;

		// deciding whether or not it is time to produce the product, 
		// if last production was less than a timeUnit ago, return
		if (diff < MarketAgent.TIME_UNIT) {
			return;
		}
		lastProduction += MarketAgent.TIME_UNIT;


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
	public boolean done() {
		return false;
	}
}
