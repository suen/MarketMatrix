package com.daubajee.marketmatrix.agent;

import java.util.HashMap;
import java.util.Map;

import com.daubajee.marketmatrix.gui.JavaFXGUIController;
import com.daubajee.marketmatrix.gui.JavaFXVisualisation;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class MarketAgent extends Agent {

	private MarketAgentAttribute attribute = new MarketAgentAttribute();
	private JavaFXGUIController gui;
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
		
		// add a behavior for dealing with incoming message;
		
		// add a behavior for selling behavior
		
		// add a behavior for buying behavior
		
		addBehaviour(new TickerBehaviour(this, 100) {
			@Override
			protected void onTick() {
				MarketAgent marketAgent = (MarketAgent) myAgent;
				MarketAgentAttribute attr = marketAgent.getAttribute();
				int sat = attr.getSatisfaction() - 1;
				attr.setSatisfaction(sat);
				//System.out.println(sat);
				gui.updateAgents(marketAgent);
				gui.addMsg(marketAgent.getLocalName() + " satisfaction: " + String.valueOf(sat));
			}
		});

		//a gui notification on successful initialisation
		gui.addMsg(this.getLocalName() + " intialised successfully");

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
	
	
	
}
