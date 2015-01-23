package com.daubajee.marketmatrix.agent.behaviour;

import jade.core.behaviours.Behaviour;

import com.daubajee.marketmatrix.agent.MarketAgent;
import com.daubajee.marketmatrix.agent.MarketAgentAttribute;

public class ConsumingBehaviour extends Behaviour {
	
	private MarketAgent marketAgent;
	private long lastConsumption;

	public ConsumingBehaviour(MarketAgent marketAgent) {
		this.marketAgent = marketAgent;
		lastConsumption = System.currentTimeMillis();
	}

	@Override
	public void action() {
		MarketAgentAttribute agentAttr = marketAgent.getAttribute();
		
		long curMillis = System.currentTimeMillis();
		
		long diff = curMillis - lastConsumption;

		// deciding whether or not it is time to consume product, 
		// if last consumption was less than a timeUnit ago, return
		if (diff < MarketAgent.TIME_UNIT) {
			return;
		}
		lastConsumption += MarketAgent.TIME_UNIT;

		int productStock = agentAttr.getConsumeProductStock();
		double quantityToConsume = agentAttr.getConsumeRate();
		
		int newStock = productStock - (int) quantityToConsume;
		
		if (newStock < 0){
			newStock = 0;
			
			//means there is no enough quantity in the stock to consume sufficiently
			//raise alert that the consumption requirement is not being met
			marketAgent.printMsg("Agent is hungry");
		} 
		
//		marketAgent.printMsg("Stock: " + newStock + "/" + productStock + " consumed: " + quantityToConsume);
		
		agentAttr.setConsumeProductStock(newStock);
		
		marketAgent.updateAgentGUI();
		
		block(4000);
	}

	@Override
	public boolean done() {
		return false;
	}

}
