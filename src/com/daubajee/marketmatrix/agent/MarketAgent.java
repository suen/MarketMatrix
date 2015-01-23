package com.daubajee.marketmatrix.agent;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.daubajee.marketmatrix.agent.behaviour.BuyerBehaviour;
import com.daubajee.marketmatrix.agent.behaviour.ConsumingBehaviour;
import com.daubajee.marketmatrix.agent.behaviour.DiscoverMarketAgentBehaviour;
import com.daubajee.marketmatrix.agent.behaviour.PostManBehaviour;
import com.daubajee.marketmatrix.agent.behaviour.SatisfactionCheckBehaviour;
import com.daubajee.marketmatrix.agent.behaviour.SellerBehaviour;
import com.daubajee.marketmatrix.gui.JavaFXGUIController;
import com.daubajee.marketmatrix.gui.JavaFXVisualisation;

public class MarketAgent extends Agent {

	private MarketAgentAttribute attribute = new MarketAgentAttribute();
	private JavaFXGUIController gui;
	
	private List<AID> otherAgents = new ArrayList<AID>();
	
	@Override
	protected void setup() {
		super.setup();
		
		// Run JavaFX Thread
		while (! JavaFXVisualisation.showGUI());
		gui = JavaFXGUIController.getInstance();

		//parse runtime parameters
		boolean allParametersSupplied = parseParameters();
		if (!allParametersSupplied)
			return;

		//register the agent with the GUI module
		gui.registerAgent(this);

		//register agent with the directory service
		registerWithDFService();
		
		// behaviour that consumes the resources that the agent feeds on
		addBehaviour(new ConsumingBehaviour(this));
		
		// behaviour that checks if agent is satisfied
		addBehaviour(new SatisfactionCheckBehaviour(this));

		// to check for new MarketAgent in the directory
		addBehaviour(new DiscoverMarketAgentBehaviour(this));
		
		// behaviour that deals with sales of products
		addBehaviour(new SellerBehaviour(this));

		// behaviour that deals with buying of products
		addBehaviour(new BuyerBehaviour(this));

		// behaviour for incoming messages
		addBehaviour(new PostManBehaviour(this));
		
		
/*		addBehaviour(new TickerBehaviour(this, 1000) {
			@Override
			protected void onTick() {
				MarketAgent marketAgent = (MarketAgent) myAgent;
				MarketAgentAttribute attr = marketAgent.getAttribute();
				int sat = attr.getSatisfaction() - 1;
				attr.setSatisfaction(sat);
				//System.out.println(sat);
				gui.updateAgents(marketAgent);
				//gui.addMsg(marketAgent.getLocalName() + " satisfaction: " + String.valueOf(sat));
			}
		});
*/
		//a gui notification on successful initialisation
		gui.addMsg("Agent: '" + this.getLocalName() + "' intialised successfully");
	}

	private boolean parseParameters(){
		
		Object[] arguments = getArguments();
		if(arguments!=null && arguments.length !=0 ) {
			Map<String, String> attrMap = new HashMap<String, String>();
			for(Object object: arguments){
				String attr = (String) object;
				//System.out.println(attr);
				if (!attr.contains("="))
					continue;
				String[] keyValue = attr.split("=");
				//System.out.println(keyValue[0] + " = " + keyValue[1]);
				attrMap.put(keyValue[0], keyValue[1]);
			}
			boolean attrInitialised = attribute.initialize(attrMap);
			if (!attrInitialised){
				System.err.println("All required attributes not supplied: ");
				return false;
			}
		} else {
			System.out.println("Agent attributes not supplied: ");
			doDelete();
			return false;
		}

		return true;
	}
	
	private void registerWithDFService(){
		String name = getName();
		ServiceDescription sd = new ServiceDescription();
		sd.setName(name);
		sd.setType("market-agent");

		DFAgentDescription dfd = new DFAgentDescription();
		
		dfd.addServices(sd);
		
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}
	
	public MarketAgentAttribute getAttribute() {
		return attribute;
	}

	public void setAttribute(MarketAgentAttribute attribute) {
		this.attribute = attribute;
	}
	
	public List<AID> getOtherAgentList(){
		return otherAgents;
	}
	
	public void printMsg(String msg){
		gui.addMsg("[" + this.getLocalName() + "] " + msg);
	}
	
	@Override
	public void doDelete() {
		super.doDelete();
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}
	
}
