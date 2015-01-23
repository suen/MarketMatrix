package com.daubajee.marketmatrix.agent.behaviour;

import com.daubajee.marketmatrix.agent.MarketAgent;

import jade.core.behaviours.Behaviour;

public class TraderLogicBehaviour extends Behaviour {
	private MarketAgent marketAgent;
	private final double MINIMAL_PRICE = 0.50;
	private final double MARGE = 0.50;
	private long lastChange = 0;
	public TraderLogicBehaviour(MarketAgent marketAgent) {
		marketAgent.printMsg(getClass().getSimpleName() + " initialised");
		this.marketAgent = marketAgent;
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
		
		double money = marketAgent.getAttribute().getMoney();
		double satisfaction = marketAgent.getAttribute().getSatisfaction();
		if(money > 0 && satisfaction > 0.0){
			raisePrice();
		}else if(money <= 0 && satisfaction <= 0.0){
			dropPrice();
		}
		
		block(4000);
	}

	@Override
	public boolean done() {
		return false;
	}

	private void dropPrice() {
		double actual_price = marketAgent.getAttribute().getPrice();
		double new_price  = floorPrice(actual_price - MARGE - generateRandomMarge());
		marketAgent.getAttribute().setPrice(new_price);
	}
	private void raisePrice() {
		double actual_price = marketAgent.getAttribute().getPrice();
		double new_price  = floorPrice(actual_price+MARGE + generateRandomMarge());
		marketAgent.getAttribute().setPrice(new_price);
		System.out.println("prix after update " + marketAgent.getAttribute().getPrice());
	}
	private double floorPrice(double new_price){
		if(new_price < MINIMAL_PRICE){
			return MINIMAL_PRICE;
		}else{
			return new_price;
		}
	}
	private double generateRandomMarge(){
		return Math.random() % MARGE;
	}
}
