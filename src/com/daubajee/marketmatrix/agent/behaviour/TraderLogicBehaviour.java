package com.daubajee.marketmatrix.agent.behaviour;

import jade.core.behaviours.TickerBehaviour;

import com.daubajee.marketmatrix.agent.MarketAgent;

public class TraderLogicBehaviour extends TickerBehaviour {
	private MarketAgent marketAgent;
	private final double MINIMAL_PRICE = 0.50;
	private final double MARGE = 0.50;
	private final double SAFETY_MONEY = 10;
	
	private long lastRun = 0;
	public TraderLogicBehaviour(MarketAgent marketAgent) {
		super(marketAgent, 1000);
		this.marketAgent = marketAgent;
		lastRun = System.currentTimeMillis();
	}

	public void agentAction() {
		long curMillis = System.currentTimeMillis();
		
		long diff = curMillis - lastRun;

		if (diff < MarketAgent.TIME_UNIT) {
			return;
		}
		lastRun += MarketAgent.TIME_UNIT;
		
		if (MarketAgent.PAUSE){
			return;
		}
		
		double money = marketAgent.getAttribute().getMoney();
		double satisfaction = marketAgent.getAttribute().getSatisfaction();
		if(money > SAFETY_MONEY && satisfaction > 0.0){
			raisePrice();
		}else if(money <= SAFETY_MONEY && satisfaction <= 0.0){
			dropPrice();
		}
		
		block(4000);
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
		//System.out.println("prix after update " + marketAgent.getAttribute().getPrice());
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

	@Override
	protected void onTick() {
		agentAction();
	}
}
