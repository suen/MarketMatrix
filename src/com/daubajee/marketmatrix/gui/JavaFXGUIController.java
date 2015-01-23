package com.daubajee.marketmatrix.gui;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;

import com.daubajee.marketmatrix.agent.MarketAgent;

public class JavaFXGUIController {

	private Container container;
		
	private Map<MarketAgent, AgentGUI> registeredAgents = new HashMap<MarketAgent, AgentGUI>();
	
	private static JavaFXGUIController self = null;
	
	public static JavaFXGUIController getInstance(){
		if(self == null)
			self = new JavaFXGUIController();
		return self;
	}
	
	private JavaFXGUIController(){
		container = new Container();
	}
	
	public Container getRootNode(){
		return container;
	}
	
	public void registerAgent(final MarketAgent agent){
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				AgentGUI agui = new AgentGUI(agent.getLocalName());
				int aindex = container.addAgentGUI(agui);
				agui.setIndexInContainer(aindex);
				agui.setAgentAttribute(agent.getAttribute());
				registeredAgents.put(agent, agui);
			}
		});
	}
	
	public void updateAgents(MarketAgent agent){
		final AgentGUI agui = registeredAgents.get(agent);
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				container.updateAgent(agui.getIndexInContainer());
			}
		});
	}
	
	public void addMsg(final String msg){
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				container.addMsg(msg);
			}
		});
	}
	
	public void flushMsg(){
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				container.flushMsg();
			}
		});
	}

}
