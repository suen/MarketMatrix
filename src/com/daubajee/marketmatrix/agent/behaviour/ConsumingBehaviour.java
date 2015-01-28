package com.daubajee.marketmatrix.agent.behaviour;

import jade.core.behaviours.TickerBehaviour;

import com.daubajee.marketmatrix.agent.MarketAgent;
import com.daubajee.marketmatrix.agent.MarketAgentAttribute;

public class ConsumingBehaviour extends TickerBehaviour{
	
	private MarketAgent marketAgent;
	private long lastRun;

	public ConsumingBehaviour(MarketAgent marketAgent) {
		super(marketAgent, 1000);
		this.marketAgent = marketAgent;
		lastRun = System.currentTimeMillis();
	}

	public void agentAction() {
		MarketAgentAttribute agentAttr = marketAgent.getAttribute();
		
		long curMillis = System.currentTimeMillis();
		
		long diff = curMillis - lastRun;

		// deciding whether or not it is time to consume product, 
		// if last consumption was less than a timeUnit ago, return
		if (diff < MarketAgent.TIME_UNIT) {
			return;
		}

		lastRun += MarketAgent.TIME_UNIT;

		if (MarketAgent.PAUSE){
			return;
		}		

		int productStock = agentAttr.getConsumeProductStock();
		double quantityToConsume = agentAttr.getConsumeRate();
		
		int newStock = productStock - (int) quantityToConsume;
		
		if (newStock < 0){
			newStock = 0;
			
			//means there is no enough quantity in the stock to consume sufficiently
			//raise alert that the consumption requirement is not being met
			marketAgent.printMsg("Agent is hungry");
			agentAttr.setHungerCounter(agentAttr.getHungerCounter()+1);
		} else {
			agentAttr.setHungerCounter(0);
		}
		
//		marketAgent.printMsg("Stock: " + newStock + "/" + productStock + " consumed: " + quantityToConsume);
		
		agentAttr.setConsumeProductStock(newStock);
		
		marketAgent.updateAgentGUI();
		
		block(4000);
	}

	@Override
	protected void onTick() {
		agentAction();
	}



}
