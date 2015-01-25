package com.daubajee.marketmatrix.agent.behaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.daubajee.marketmatrix.agent.MarketAgent;

public class BuyerBehaviour extends Behaviour {

	private MessageTemplate msgTemp;
	private MarketAgent marketAgent;

	private LinkedList<ACLMessage> 
				proposeMsgQueue = new LinkedList<ACLMessage>();
	private Map<String, List<ACLMessage>> 
				proposalsReceived = new HashMap<String, List<ACLMessage>>();
	private Map<String, Long> proposalTimeOuts = new HashMap<String, Long>();
	
	public BuyerBehaviour(MarketAgent marketAgent) {
		msgTemp = MessageTemplate.MatchConversationId("for-buyer");
		this.marketAgent = marketAgent;
	}

	@Override
	public void action() {
		ACLMessage message = marketAgent.receive(msgTemp);

		if (message == null) {

		} else if (message.getPerformative()==ACLMessage.PROPOSE) {
			proposeMsgQueue.add(message);
			marketAgent.printMsg("Proposal received");
		} else if (message.getPerformative() == ACLMessage.CONFIRM) {

		}

		initCFPMsg();

		sortProposeMsgQueue();
		
		treatProposeMsgQueue();

		block();
	}

	private void initCFPMsg() {
		
		if(proposalsReceived.size() != 0)
			return;
		
		List<AID> otherAgentList = marketAgent.getOtherAgentList();
		if(otherAgentList.size()==0)
			return;
		
		long currentTimeStamp = System.currentTimeMillis();
		String proposalId = "Proposal: " + String.valueOf(currentTimeStamp);
		
		ACLMessage newcfp = new ACLMessage(ACLMessage.CFP);
		for (AID aid: otherAgentList){
			newcfp.addReceiver(aid);
		}
		
		String consumes = marketAgent.getAttribute().getConsumes();
		JSONObject msgContent = new JSONObject();
		msgContent.put("product", consumes);
		msgContent.put("quantity", "1");
		msgContent.put("proposal-id", proposalId);
		
		newcfp.setContent(msgContent.toString());
		newcfp.setConversationId("for-seller");
		newcfp.setReplyWith(proposalId);
		
		marketAgent.send(newcfp);
		
		proposalsReceived.put(proposalId, new ArrayList<ACLMessage>());
		proposalTimeOuts.put(proposalId, currentTimeStamp);
		marketAgent.printMsg("CFP for product '" + consumes + "' sent to " + otherAgentList.size()+ " agents");
	}

	private void sortProposeMsgQueue() {
		if (proposeMsgQueue.size() == 0)
			return;
		
		ACLMessage proposalMsg = proposeMsgQueue.pop();
		
		for (String proposalId : proposalsReceived.keySet()) {
			if (!proposalMsg.getReplyWith().equals(proposalId))
				continue;
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
				marketAgent.printMsg("No proposal received for : " + proposalId);
				finishedProposals.add(proposalId);
			}
			
			ACLMessage cheapestProposal = null;
			double cheapestsofar = 0;
			for(ACLMessage proposal: proposalList){

				String content = proposal.getContent();
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
				
				if (cheapestProposal==null){
					cheapestProposal = proposal;
					cheapestsofar = price;
					//the first proposal
					continue;
				}
				if (cheapestsofar > price){
					cheapestProposal = proposal;
					cheapestsofar = price;
				}
			} // end of iterator for Messages in a proposalId
		
			if (cheapestProposal==null){
				//means the list contained only malformed proposals
				marketAgent.printMsg("No proper proposal for: '" + proposalId + "'");
				finishedProposals.add(proposalId);
				continue;
			}
			
			ACLMessage replyCheapestProposal = cheapestProposal.createReply();
			replyCheapestProposal.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
			replyCheapestProposal.setConversationId("for-seller");
			marketAgent.send(replyCheapestProposal);

			marketAgent.printMsg("For '"+proposalId + "', the cheapest deal is : " + cheapestsofar);
			finishedProposals.add(proposalId);
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

	@Override
	public boolean done() {
		return proposalsReceived.size()==0;
	}

}
