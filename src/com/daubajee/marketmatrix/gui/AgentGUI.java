package com.daubajee.marketmatrix.gui;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import com.daubajee.marketmatrix.agent.MarketAgentAttribute;

public class AgentGUI extends Pane {

	private Label agentNameLabel = new Label();
	private Label producesLabel = new Label();
	private Label consumesLabel = new Label();
	private Label produceRateLabel = new Label();
	private Label consumesRateLabel = new Label();
	private Label stockLabel = new Label();
	private Label stockMaximumLabel = new Label();
	private Label satisfactionLabel = new Label();
	private Label moneyLabel = new Label();
	private Label priceProductLabel = new Label();

	private String produces = "Wheat";
	private String consumes = "Rice";
	private String produceRate = "4";
	private String consumesRate = "3";
	private String stock = "10";
	private String stockMaximum = "100";
	private String satisfaction = "50";
	private String money = "10";
	private String price = "3";
	
	private HBox titleBox = new HBox();
	private VBox rootLayout = new VBox();
	
	private MarketAgentAttribute attr = null;
	private int indexInContainer = 0;
	
	private String agentName;
	
	public AgentGUI(String name){
		this.agentName = name;
		initLayout();
		//updateLabels();
	}
	
	public void initLayout(){
		rootLayout.getChildren().addAll(agentNameLabel,
				consumesLabel, consumesRateLabel,
				producesLabel, produceRateLabel,
				priceProductLabel, stockLabel, stockMaximumLabel, 
				moneyLabel, satisfactionLabel);

		rootLayout.setStyle("-fx-pref-width: 250px; "
				+ "-fx-border-color: #dfdfdf;"
				+ "-fx-border-width: 3px;"
				+ "-fx-padding: 0px 0px 5px 0px;");
		
		agentNameLabel.setTextAlignment(TextAlignment.CENTER);
		agentNameLabel.setStyle("-fx-font-size: 1.6em;"
				+ "-fx-background-color: #dfdfdf;"
				+ "-fx-pref-width: 250px;"
				+ "");
		
		getChildren().add(rootLayout);
		
		agentNameLabel.setText("Agent: " + agentName);
	}
	
	public void updateLabels(){
		consumes = attr.getConsumes();
		consumesRate = String.valueOf(attr.getConsumeRate());
		produces = attr.getProduces();
		produceRate = String.valueOf(attr.getProduceRate());
		price = String.valueOf(attr.getPrice());
		stock = String.valueOf(attr.getStock());
		stockMaximum = String.valueOf(attr.getStockMaximum());
		money = String.valueOf(attr.getMoney());
		satisfaction = String.valueOf(attr.getSatisfaction());
		
		consumesLabel.setText("Consumes: " + consumes);
		consumesRateLabel.setText("Consumption Rate: " + consumesRate + " / day");
		producesLabel.setText("Produces: " + produces);
		produceRateLabel.setText("Production Rate: "+ produceRate + " / day");
		priceProductLabel.setText("Price of product: "+ price +" MU");
		stockLabel.setText("Product in stock: "+stock+" units");
		stockMaximumLabel.setText("Max stock capacity: "+ stockMaximum + " units");
		moneyLabel.setText("Money : "+money);
		satisfactionLabel.setText("Satisfaction: "+satisfaction);
	}
	
	public void setAgentAttribute(MarketAgentAttribute attr){
		this.attr = attr;
		
		updateLabels();
	}

	public int getIndexInContainer() {
		return indexInContainer;
	}

	public void setIndexInContainer(int indexInContainer) {
		this.indexInContainer = indexInContainer;
	}

	public void setProduces(String produces) {
		this.produces = produces;
	}

	public void setConsumes(String consumes) {
		this.consumes = consumes;
	}

	public void setProduceRate(String produceRate) {
		this.produceRate = produceRate;
	}

	public void setConsumesRate(String consumesRate) {
		this.consumesRate = consumesRate;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public void setStockMaximum(String stockMaximum) {
		this.stockMaximum = stockMaximum;
	}

	public void setSatisfaction(String satisfaction) {
		this.satisfaction = satisfaction;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	
}
	