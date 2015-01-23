package com.daubajee.marketmatrix.agent.behaviour;

import com.daubajee.marketmatrix.agent.MarketAgent;

import jade.core.behaviours.Behaviour;

public class TraderLogicBehaviour extends Behaviour {
	private MarketAgent marketAgent;
	private final double MINIMAL_PRICE = 0.50;
	private final double MARGE = 0.50;
	public TraderLogicBehaviour(MarketAgent marketAgent) {
		marketAgent.printMsg(getClass().getSimpleName() + " initialised");
		this.marketAgent = marketAgent;
	}

	@Override
	public void action() {
		double money = marketAgent.getAttribute().getMoney();
		double satisfaction = marketAgent.getAttribute().getSatisfaction();
		if(money > 0 && satisfaction > 0.0){
			raisePrice();
		}else if(money <= 0 && satisfaction <= 0.0){
			dropPrice();
		}
	}

	@Override
	public boolean done() {
		return false;
	}

	private void dropPrice() {
		double actual_price = marketAgent.getAttribute().getPrice();
		double new_price  = floorPrice(actual_price+MARGE + generateRandomMarge());
		marketAgent.getAttribute().setPrice(new_price);
	}
	private void raisePrice() {
		double actual_price = marketAgent.getAttribute().getPrice();
		double new_price  = floorPrice(actual_price+MARGE + generateRandomMarge());
		marketAgent.getAttribute().setPrice(new_price);
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
