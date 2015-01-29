package com.daubajee.marketmatrix.agent.behaviour;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

import org.json.JSONObject;

import com.daubajee.marketmatrix.agent.MarketAgent;
import com.daubajee.marketmatrix.agent.MarketAgentAttribute;

public class SellerBehaviour extends TickerBehaviour{

	private MessageTemplate msgTemp;
	private MarketAgent marketAgent;
	
	private Map<ACLMessage, Integer> cfpReplied = new HashMap<ACLMessage, Integer>();
	
	private LinkedList<ACLMessage> cfpMsgQueue = new LinkedList<ACLMessage>();
	private LinkedList<ACLMessage> acceptMsgQueue = new LinkedList<ACLMessage>();
	private long lastrun;
	
	public SellerBehaviour(MarketAgent marketAgent) {
		super(marketAgent, 1000);
		msgTemp = MessageTemplate.MatchConversationId("for-seller");
		this.marketAgent = marketAgent;
		lastrun = System.currentTimeMillis();
	}

	int count = 0;
	
	public void agentAction() {
		
		if (MarketAgent.PAUSE){
			return;
		}
	
		ACLMessage message = marketAgent.receive(msgTemp);
		
		if (message == null) {
//			 System.out.println("No message on seller " + count++);
		}
		else if (message.getPerformative()==ACLMessage.CFP) {
			//marketAgent.printMsg("Message received for CFP");
			cfpMsgQueue.add(message);
		} else if (message.getPerformative()==ACLMessage.ACCEPT_PROPOSAL) {
			marketAgent.printMsg("received ACCEPT_PROPOSAL for "+message.getReplyWith() +" by " + message.getSender().getLocalName());
			acceptMsgQueue.add(message);
		} else if (message.getPerformative() == ACLMessage.CONFIRM) {
			marketAgent.printMsg("receveid CONFIRM for "+message.getReplyWith() +" by" + message.getSender().getLocalName());
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
		
		if (!product.equalsIgnoreCase(marketAgent.getAttribute().getProduces())) {
			System.out.println("Demand received for unsupported product: " + product);
			return;
		}
		
		//we ignore the quantity ask by the buyer and tell him how much we have
		int quantity = marketAgent.getAttribute().getProduceProductStock();
		
		ACLMessage reply = cfpMsg.createReply();
		reply.setConversationId("for-buyer");
		reply.setPerformative(ACLMessage.PROPOSE);
		reply.setReplyWith(cfpMsg.getReplyWith());
		
		JSONObject replyContent = new JSONObject();
		replyContent.put("product", marketAgent.getAttribute().getProduces());
		replyContent.put("quantity", String.valueOf(quantity));
		replyContent.put("price", String.format(Locale.ENGLISH,"%.02f",marketAgent.getAttribute().getPrice()));
		reply.setContent(replyContent.toString());
		
		marketAgent.send(reply);
		
		cfpReplied.put(reply, quantity);
	}
	
	private void treatAcceptProposal() {
		if (acceptMsgQueue.size()==0)
			return;
		ACLMessage acptMsg = acceptMsgQueue.pop();
		
		String content = acptMsg.getContent();
		
		JSONObject contentJson = new JSONObject(content);
		
		String product = (String) contentJson.get("product");
		int quantity = Integer.parseInt((String)contentJson.get("quantity"));
		double price = Double.parseDouble((String) contentJson.get("price"));
		
		MarketAgentAttribute agentAttributes = marketAgent.getAttribute();
		
		if (!agentAttributes.getProduces().equals(product)){
			// means we got the wrong ACCEPT message
			marketAgent.printMsg("ACCEPT_PROPOSAL for wrong product received");
			return;
		}
		
		if (agentAttributes.getProduceProductStock() < quantity){
			// means we have already sell part of our product to other agent
			marketAgent.printMsg("We no longer have the correct amount of product ");
			return;
		}
		double priceTotal = price * quantity;
		agentAttributes.cashIn(priceTotal);
		agentAttributes.produceProductOut(quantity);
		
		ACLMessage confirmReply = acptMsg.createReply();
		confirmReply.setPerformative(ACLMessage.CONFIRM);
		confirmReply.setConversationId("for-buyer");
		confirmReply.setContent(content);
		confirmReply.setReplyWith(acptMsg.getReplyWith());
		
		marketAgent.send(confirmReply);
	}


	@Override
	protected void onTick() {
		agentAction();		
	}

}
