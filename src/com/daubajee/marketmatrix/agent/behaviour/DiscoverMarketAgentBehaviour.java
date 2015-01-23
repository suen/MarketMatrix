package com.daubajee.marketmatrix.agent.behaviour;

import java.util.List;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import com.daubajee.marketmatrix.agent.MarketAgent;

public class DiscoverMarketAgentBehaviour extends Behaviour {
	
	private MarketAgent marketAgent;

	public DiscoverMarketAgentBehaviour(MarketAgent marketAgent) {
		this.marketAgent = marketAgent;
	}

	@Override
	public void action() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("market-agent");
		template.addServices(sd);

		List<AID> agentList = marketAgent.getOtherAgentList();
		int intialSize = agentList.size();
		try {
			DFAgentDescription[] result = DFService.search(myAgent, template);
			
			for (int i = 0; i < result.length; ++i) {
				 AID name = result[i].getName();
				 if (! agentList.contains(name) && !marketAgent.getAID().equals(name))
					 agentList.add(name);
			}
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		if (agentList.size() > intialSize){
			marketAgent.printMsg(String.valueOf((agentList.size() - intialSize))
					+ " new agents found in directory");
		}
		block(10000);
	}

	@Override
	public boolean done() {
		return false;
	}

}
