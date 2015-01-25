package com.daubajee.marketmatrix.agent.behaviour;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONObject;

import com.daubajee.marketmatrix.agent.MarketAgent;

public class SellerBehaviour extends Behaviour {

	private MessageTemplate msgTemp;
	private MarketAgent marketAgent;
	
	private Map<ACLMessage, Integer> cfpReplied = new HashMap<ACLMessage, Integer>();
	
	private LinkedList<ACLMessage> cfpMsgQueue = new LinkedList<ACLMessage>();
	private LinkedList<ACLMessage> acceptMsgQueue = new LinkedList<ACLMessage>();
	private long lastrun;
	
	public SellerBehaviour(MarketAgent marketAgent) {
		msgTemp = MessageTemplate.MatchConversationId("for-seller");
		this.marketAgent = marketAgent;
		lastrun = System.currentTimeMillis();
	}

	int count = 0;
	@Override
	public void action() {
	
		ACLMessage message = marketAgent.receive(msgTemp);
		
		if (message == null) {
//			 System.out.println("No message on seller " + count++);
		}
		else if (message.getPerformative()==ACLMessage.CFP) {
			marketAgent.printMsg("Message received for CFP");
			cfpMsgQueue.add(message);
		} else if (message.getPerformative()==ACLMessage.ACCEPT_PROPOSAL) {
			marketAgent.printMsg("Message received for ACCEPT_PROPOSAL");
			acceptMsgQueue.add(message);
		} else if (message.getPerformative() == ACLMessage.CONFIRM) {
			
		} else {
			System.out.println("Message!!");
		}
		
		treatcfpMsg();
		
		treatAcceptProposal();
		
		//TODO make call for proposal based on satisfaction level
		
		block();
	}
	
	public void treatcfpMsg(){
		
		if (cfpMsgQueue.size()==0)
			return;
		
		ACLMessage cfpMsg = cfpMsgQueue.pop();
		if (cfpMsg==null)
			return;
		
		String content = cfpMsg.getContent();
		JSONObject contentjson = new JSONObject(content);
					
		if(!contentjson.has("product") || !contentjson.has("quantity") ){
			System.err.println("Content format unknown");
			return;
		}
		
		String product = (String) contentjson.get("product");
		int quantity = Integer.valueOf( (String) contentjson.get("quantity"));
		
		if (!product.equalsIgnoreCase(marketAgent.getAttribute().getProduces())) {
			System.out.println("Demand received for unsupported product: " + product);
			return;
		}
		
		if (marketAgent.getAttribute().getProduceProductStock() < quantity){
			System.out.println("Quantity unavailable: " + quantity );
			return;
		}
		
		ACLMessage reply = cfpMsg.createReply();
		reply.setConversationId("for-buyer");
		reply.setPerformative(ACLMessage.PROPOSE);
		
		JSONObject replyContent = new JSONObject();
		replyContent.put("product", marketAgent.getAttribute().getProduces());
		replyContent.put("quantity", String.valueOf(quantity));
		replyContent.put("price", String.valueOf(marketAgent.getAttribute().getPrice()));
		
		marketAgent.send(reply);
		
		cfpReplied.put(reply, quantity);
	}
	
	private void treatAcceptProposal() {
		if (acceptMsgQueue.size()==0)
			return;
		
		ACLMessage acptMsg = acceptMsgQueue.pop();
		if (acptMsg!=null){
		
			
			acptMsg = acceptMsgQueue.pop();
		}
	}

	@Override
	public boolean done() {
		return false;
	}

}
