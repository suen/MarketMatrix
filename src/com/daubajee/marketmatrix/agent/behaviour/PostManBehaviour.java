package com.daubajee.marketmatrix.agent.behaviour;

import com.daubajee.marketmatrix.agent.MarketAgent;

import jade.core.behaviours.Behaviour;

public class PostManBehaviour extends Behaviour {

	public PostManBehaviour(MarketAgent marketAgent) {
		marketAgent.printMsg(getClass().getSimpleName() + " initialised");
	}

	@Override
	public void action() {

	}

	@Override
	public boolean done() {
		return true;
	}

}
