package com.daubajee.marketmatrix.gui;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.scene.layout.Pane;

import com.daubajee.marketmatrix.agent.MarketAgent;
import com.daubajee.marketmatrix.agent.MarketAgentCreator;

public class JavaFXGUIController {

	private NewContainer container= new NewContainer();
		
	private Map<MarketAgent, NewAgentGUI> registeredAgents = new HashMap<MarketAgent, NewAgentGUI>();
	
	private static JavaFXGUIController self = null;
	
	public static JavaFXGUIController getInstance(){
		if(self == null)
			self = new JavaFXGUIController();
		return self;
	}
	
	private JavaFXGUIController(){
	}
	
	public Pane getRootNode(){
		return container;
	}
	
	public void registerAgent(final MarketAgent agent){
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				NewAgentGUI agui = new NewAgentGUI(agent.getLocalName());
				int aindex = container.addAgentGUI(agui);
				agui.setIndexInContainer(aindex);
				agui.setAgentAttribute(agent.getAttribute());
				registeredAgents.put(agent, agui);
			}
		});
	}
	
	public void registerAgentCreator(MarketAgentCreator creator){
		container.registerAgentCreator(creator);
	}
	
	public void updateAgents(MarketAgent agent){
		final NewAgentGUI agui = registeredAgents.get(agent);
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				container.updateAgent(agui.getIndexInContainer());
			}
		});
	}
	
	public void addMsg(MarketAgent agent, final String msg){
		final NewAgentGUI agui = registeredAgents.get(agent);
		
		if (agui==null){
			System.err.println("Message NOT PRINTED: " + msg );
			return;
		}
		
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				container.addMsg(agui, msg);
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
