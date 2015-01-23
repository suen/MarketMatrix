package com.daubajee.marketmatrix.agent.behaviour;

import com.daubajee.marketmatrix.agent.MarketAgent;

import jade.core.behaviours.Behaviour;

public class SatisfactionCheckBehaviour extends Behaviour {
	private MarketAgent marketAgent;
	private static final double MAX_SATISFACTION = 1.0;
	private static final double BOOST = 0.5;
	private long lastChange = 0;
	public SatisfactionCheckBehaviour(MarketAgent marketAgent) {
		this.marketAgent = marketAgent;
		marketAgent.printMsg(getClass().getSimpleName() + " initialised");
		lastChange = System.currentTimeMillis();
	}

	@Override
	public void action() {
		long curMillis = System.currentTimeMillis();
		
		long diff = curMillis - lastChange;

		// deciding whether or not it is time to produce the product, 
		// if last production was less than a timeUnit ago, return
		if (diff < MarketAgent.TIME_UNIT) {
			return;
		}
		lastChange += MarketAgent.TIME_UNIT;

		if(marketAgent.getAttribute().getConsumeProductStock() > 0){
			marketAgent.getAttribute().setSatisfaction(MAX_SATISFACTION);
		}else{
			double satisfaction = marketAgent.getAttribute().getSatisfaction();
			satisfaction -= marketAgent.getAttribute().getHungerCounter()/10.0;
			if(marketAgent.getAttribute().getWorkFreeCounter() == 1)
				satisfaction += BOOST;
			marketAgent.getAttribute().setSatisfaction(capSatisfaction(satisfaction));
		}
		block(4000);
	}

	@Override
	public boolean done() {
		return false;
	}
	
	private double capSatisfaction(double satisfaction){
		if(satisfaction >= MAX_SATISFACTION){
			satisfaction = MAX_SATISFACTION;
		}else if(satisfaction <= 0.0){
			satisfaction = 0.0;
		}
		return satisfaction;
	}
}
