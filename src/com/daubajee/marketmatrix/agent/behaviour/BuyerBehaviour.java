package com.daubajee.marketmatrix.agent.behaviour;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.daubajee.marketmatrix.agent.MarketAgent;
import com.daubajee.marketmatrix.agent.MarketAgentAttribute;

public class BuyerBehaviour extends TickerBehaviour {

	private MessageTemplate msgTemp;
	private MarketAgent marketAgent;

	private LinkedList<ACLMessage> proposeMsgQueue 
							= new LinkedList<ACLMessage>();
	
	private Map<String, List<ACLMessage>> proposalsReceived 
							= new HashMap<String, List<ACLMessage>>();
	
	private Map<String, Long> confirmationTimeOuts = new HashMap<String, Long>();

	
	private LinkedList<ACLMessage> confirmations
							= new LinkedList<ACLMessage>();
	
	private Map<String, Long> proposalTimeOuts = new HashMap<String, Long>();
	
	public BuyerBehaviour(MarketAgent marketAgent) {
		super(marketAgent, 1000);
		msgTemp = MessageTemplate.MatchConversationId("for-buyer");
		this.marketAgent = marketAgent;
	}

	public void agentAction() {
		if (MarketAgent.PAUSE){
			return;
		}

		ACLMessage message = marketAgent.receive(msgTemp);

		if (message == null) {

		} else if (message.getPerformative()==ACLMessage.PROPOSE) {
			proposeMsgQueue.add(message);
			//marketAgent.printMsg("Proposal received for : " + message.getReplyWith());
		} else if (message.getPerformative() == ACLMessage.CONFIRM) {
			marketAgent.printMsg("Confirmation received for : " + message.getReplyWith());
			confirmations.add(message);
		}

		initCFPMsg();

		sortProposeMsgQueue();
		
		treatProposeMsgQueue();
		
		treatConfirmations();

		block();
	}

	private void initCFPMsg() {
		
		if(proposalsReceived.size() != 0)
			return;
		
		List<AID> otherAgentList = marketAgent.getOtherAgentList();
		if(otherAgentList.size()==0)
			return;
		
		long currentTimeStamp = System.currentTimeMillis();
		String proposalId = "PROPOSAL-" + String.valueOf(currentTimeStamp);
		
		ACLMessage newcfp = new ACLMessage(ACLMessage.CFP);
		for (AID aid: otherAgentList){
			newcfp.addReceiver(aid);
		}
		
		String consumes = marketAgent.getAttribute().getConsumes();
		JSONObject msgContent = new JSONObject();
		msgContent.put("product", consumes);
		msgContent.put("quantity", "5");
		msgContent.put("proposal-id", proposalId);
		
		newcfp.setContent(msgContent.toString());
		newcfp.setConversationId("for-seller");
		newcfp.setReplyWith(proposalId);
		
		marketAgent.send(newcfp);
		
		proposalsReceived.put(proposalId, new ArrayList<ACLMessage>());
		proposalTimeOuts.put(proposalId, currentTimeStamp);
		marketAgent.printMsg("CFP "+proposalId + " for product '" + consumes + "' sent to " + otherAgentList.size()+ " agents");
	}

	private void sortProposeMsgQueue() {
		if (proposeMsgQueue.size() == 0)
			return;
		
		ACLMessage proposalMsg = proposeMsgQueue.pop();
		//If the message is a response for a CFP created by this buyer and not out of date then we add it to the list of proposal for the CFP
		for (String proposalId : proposalsReceived.keySet()) {
			if (!proposalMsg.getReplyWith().equals(proposalId)){
				//marketAgent.printMsg("A message with bad reply-to '" + proposalMsg.getReplyWith() + "' expected : '" +proposalId+ "'");
				continue;
			}
			proposalsReceived.get(proposalId).add(proposalMsg);
		}
	}
	
	/**
	 * Treats the proposals in proposalsReceived based on timeout
	 */
	private void treatProposeMsgQueue() {
		
		long currentTimeMillis = System.currentTimeMillis();
		
		List<String> finishedProposals = new ArrayList<String>();
		for(String proposalId: proposalTimeOuts.keySet()){
			Long proposalTime = proposalTimeOuts.get(proposalId);
			//if (TIME_UNIT*4) has not passed, we wait
			if ( (currentTimeMillis - proposalTime) < (MarketAgent.TIME_UNIT * 4))
				continue;

			List<ACLMessage> proposalList = proposalsReceived.get(proposalId);

			if (proposalList.size() == 0) {
				//turns out nobody replied to our proposal
				//marketAgent.printMsg("No proposal received for : " + proposalId);
				finishedProposals.add(proposalId);
				continue;
			}
			
			ACLMessage cheapestProposal = null;
			double cheapestSoFar = 0;
			double quantityForCheapest = 0;
			for(ACLMessage proposal: proposalList){

				String content = proposal.getContent();
				System.out.println("Msg content : " + content);
				JSONObject proposalJson = new JSONObject(content);
				
				if (!(proposalJson.has("product") && proposalJson.has("quantity")
						&& proposalJson.has("price"))){
					//reject all the proposal not in the right format
					marketAgent.printMsg("A mal formed proposal JSON detected");
					continue;
				}
				
				String priceStr = (String) proposalJson.get("price");
				double price = 0;
				try {
					price = Double.parseDouble(priceStr);
				} catch (NumberFormatException e) {
					marketAgent.printMsg("A proposal wrong price in wrong format detected");
					continue;
				}
				
				String quantityStr = (String) proposalJson.get("quantity");
				double quantity = 0.0;
				try {
					quantity = Double.parseDouble(quantityStr);
				} catch (NumberFormatException e) {
					marketAgent.printMsg("A proposal wrong quantity in wrong format detected");
					continue;
				}
				
				if (cheapestProposal==null){
					cheapestProposal = proposal;
					cheapestSoFar = price;
					//the first proposal
					continue;
				}
				if (cheapestSoFar > price){
					cheapestProposal = proposal;
					cheapestSoFar = price;
					quantityForCheapest = quantity;
				}
			} // end of iterator for Messages in a proposalId
		
			if (cheapestProposal==null){
				//means the list contained only malformed proposals or no proposal
				//marketAgent.printMsg("No proper proposal for: '" + proposalId + "'");
				finishedProposals.add(proposalId);
				continue;
			}
			double buyerMoney =  marketAgent.getAttribute().getMoney();
			if((cheapestSoFar*quantityForCheapest) > buyerMoney){
				marketAgent.printMsg("Price is too high for me : '" + proposalId + "' by " + cheapestProposal.getSender().getName());
				continue;
			}
			ACLMessage replyCheapestProposal = cheapestProposal.createReply();
			replyCheapestProposal.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
			replyCheapestProposal.setConversationId("for-seller");
			replyCheapestProposal.setReplyWith(proposalId);
			replyCheapestProposal.setContent(cheapestProposal.getContent());
			marketAgent.send(replyCheapestProposal);

			marketAgent.printMsg("For '"+proposalId + "', the cheapest deal is : " + cheapestSoFar);
			finishedProposals.add(proposalId);
			long currentTimeStamp = System.currentTimeMillis();
			confirmationTimeOuts.put(proposalId, currentTimeStamp);
		}

		//remove all the proposals from proposalTimeOuts
		//for which proposalsReceived has no proposalId
		for(String proposalId: finishedProposals){
			if (proposalTimeOuts.containsKey(proposalId)){
				proposalsReceived.remove(proposalId);
				proposalTimeOuts.remove(proposalId);
			}
		}
		
	}
	
	private void treatConfirmations() {
		
		if (confirmations.size()==0){
			return;
		}
		
		ACLMessage confirmationMsg = confirmations.pop();
		
		/*long currentTimeMillis = System.currentTimeMillis();
		
		List<String> finishedConfirmations = new ArrayList<String>();
		for(String confirmationId: confirmationTimeOuts.keySet()){
			Long confirmationTime = confirmationTimeOuts.get(confirmationId);
			//if (TIME_UNIT*4) has not passed, we wait
			if ( (currentTimeMillis - confirmationTime) < (MarketAgent.TIME_UNIT * 4))
				continue;
			*/
			String content = confirmationMsg.getContent();
			
			JSONObject contentJson = new JSONObject(content);
			
			String product = (String) contentJson.get("product");
			int quantity = Integer.parseInt((String)contentJson.get("quantity"));
			double price = Double.parseDouble((String) contentJson.get("price"));
			/*
			if(!confirmationMsg.getReplyWith().equals(confirmationId)){
				confirmationTimeOuts.remove(confirmationId);
				continue;
			}else{*/
				MarketAgentAttribute agentAttributes = marketAgent.getAttribute();
				
				if (!agentAttributes.getConsumes().equals(product)){
					// means we got the wrong ACCEPT message
					marketAgent.printMsg("CONFIRM for wrong product received");
					return;
				}
				
				double priceTotal = price * quantity;
				agentAttributes.cashOut(priceTotal);
				agentAttributes.consumeProductIn(quantity);
			//}
		//}
	}


	@Override
	protected void onTick() {
		agentAction();
	}

}
