package com.daubajee.marketmatrix.gui;

import java.io.File;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import com.daubajee.marketmatrix.agent.MarketAgentAttribute;
import com.sun.javafx.scene.control.skin.ProgressIndicatorSkin;

public class NewAgentGUI extends VBox {

	private HBox rootContainer = new HBox();
	private VBox leftContainer = new VBox();
	private VBox rightContainer = new VBox();
	
	private Label agentNameLabel = new Label("Agent1");
	private Label consumesLabel = new Label("Produces 2 Apple / TU");
	private Label consumeStockLabel = new Label("Apple: ");
	private Label producesLabel = new Label("Consumes 2 Banana / TU");
	private Label priceProductLabel = new Label("Price : ");
	private Label produceStockLabel = new Label("Banana : ");
	private Label moneyLabel = new Label("Money: ");

	private Label cStockField = new Label("1");
	private Label pStockField = new Label("2");
	private Label priceField = new Label("34");
	private Label moneyField = new Label("23");

	private FlowPane attrPane = new FlowPane();		

	private VBox msgBoardContainer = new VBox();
	private Label msgBoardLabel = new Label("Messages:");
	private ScrollPane sp = new ScrollPane();
	private VBox messageBoard = new VBox();

	
	private String produces = "Wheat";
	private String consumes = "Rice";
	private String produceRate = "4";
	private String consumesRate = "3";
	private String produceStock = "10";
	private String produceStockMaximum = "100";
	private String consumeStock = "10";
	private String consumeStockMaximum = "100";
	private double satisfaction = 0;
	private String money = "10";
	private String price = "3";	
	
	private ImageView avatar = new ImageView();
	ProgressBar satisfIndicator = new ProgressBar();

	
	public NewAgentGUI(String name) {
		agentNameLabel.setText(name);
		initLayout();
		initStyle();
	}
	
	private void initLayout() {
		getChildren().addAll(satisfIndicator, rootContainer);
		
		rootContainer.getChildren().addAll(leftContainer, rightContainer);

		HBox c1 = new HBox();
		c1.getChildren().addAll(consumeStockLabel, cStockField);
		HBox c2 = new HBox();
		c2.getChildren().addAll(produceStockLabel, pStockField);
		HBox c3 = new HBox();
		c3.getChildren().addAll(priceProductLabel, priceField);
		HBox c4 = new HBox();
		c4.getChildren().addAll(moneyLabel, moneyField);

		attrPane.getChildren().addAll(c1, c2, c3, c4);		

		leftContainer.getChildren().addAll(agentNameLabel, avatar);
		rightContainer.getChildren().addAll(consumesLabel, producesLabel, attrPane, msgBoardContainer);
		
		msgBoardContainer.getChildren().addAll(msgBoardLabel, sp);
		sp.setContent(messageBoard);
		
		satisfIndicator.setProgress(1);
		
		File imgFile = new File("img/farmer.png");
		Image img = new Image(imgFile.toURI().toString());
		avatar.setFitWidth(100);
		avatar.setPreserveRatio(true);
		avatar.setImage(img);
	}
	
	private void initStyle(){
		setStyle("-fx-border-width: 2px;"
				+ "-fx-border-color: #dfdfdf;"
				+ "-fx-max-width: 550px;");
		
		agentNameLabel.setTextAlignment(TextAlignment.CENTER);
		agentNameLabel.setStyle("-fx-font-size: 1.6em;"
				+ "-fx-background-color: #dfdfdf;"
				+ "-fx-padding: 5px 10px 5px 10px;");
		
				
		leftContainer.setAlignment(Pos.CENTER);
		leftContainer.setStyle("-fx-padding: 10px 10px 0px 10px;"
				+ "-fx-max-width: 150px;");
		
		rightContainer.setStyle("-fx-spacing: 10px;"
				+ "-fx-padding: 5px;");
		
		attrPane.setHgap(10);
		
		String vFieldStyle = "-fx-text-fill: #000000;"
				+ "-fx-padding: 3px 10px 3px 10px;"
				+ "-fx-border-width: 1px;"
				+ "-fx-border-color: #dfdfdf;"
				+ "-fx-background-color: #ffffff;";
		
		cStockField.setStyle(vFieldStyle);
		pStockField.setStyle(vFieldStyle);
		priceField.setStyle(vFieldStyle);
		moneyField.setStyle(vFieldStyle);
		
		satisfIndicator.setStyle("-fx-progress-color: rgb(137,137,137); -fx-min-width: 500px;");
		
		
		msgBoardContainer.setStyle("-fx-border-width: 1px;"
				+ "-fx-border-color: #dfdfdf;"
				+ "-fx-padding: 5px;"
				+ "-fx-max-width: 400px;"
				+ "-fx-min-height: 200px;"
				+ "-fx-spacing: 10px;");
		sp.setStyle("-fx-max-width: 400px;"
				+ "-fx-min-height: 200px;");
		messageBoard.setStyle("-fx-max-width: 380px;");
		
		msgBoardLabel.setStyle("-fx-font-size: 1.3em;"
				+ "-fx-font-weight: bold;");
	}
	
	private boolean odd = true;
	private MarketAgentAttribute attr;
	private int indexInContainer;
	public void addMsg(String msg) {
		Label label = new Label(msg);
		label.setWrapText(true);
		if (odd){
			label.setStyle("-fx-background-color: #efefef;"
					+ "-fx-padding: 10px;");
			odd = false;
		} else {
			label.setStyle("-fx-background-color: #ffffff;"
					+ "-fx-padding: 10px;");
			odd = true;
		}
		messageBoard.getChildren().add(0, label);
	}
	

	public void updateLabels(){
		consumes = attr.getConsumes();
		consumesRate = String.valueOf(attr.getConsumeRate());
		consumeStock = String.valueOf(attr.getConsumeProductStock());
		consumeStockMaximum = String.valueOf(attr.getConsumeProductStockCapacity());
		produces = attr.getProduces();
		produceRate = String.valueOf(attr.getProduceRate());
		price = String.format("%.02f", attr.getPrice());
		produceStock = String.valueOf(attr.getProduceProductStock());
		produceStockMaximum = String.valueOf(attr.getProduceProductStockCapacity());
		money = String.format("%.02f", attr.getMoney());
		satisfaction = attr.getSatisfaction();
		
		consumesLabel.setText("Consumes " + consumes + " at " + consumesRate+ "/TU");
		consumeStockLabel.setText(consumes + " :");
		producesLabel.setText("Produces " + produces + " at " + produceRate + "/TU");
		produceStockLabel.setText(produces +" :");
		priceProductLabel.setText("Price:");
		moneyLabel.setText("Money:");
		
		pStockField.setText(produceStock);
		cStockField.setText(consumeStock);
		priceField.setText(price);
		moneyField.setText(money);
		satisfIndicator.setProgress(satisfaction);
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
		this.produceStock = stock;
	}

	public void setStockMaximum(String stockMaximum) {
		this.produceStockMaximum = stockMaximum;
	}

	public void setSatisfaction(double satisfaction) {
		this.satisfaction = satisfaction;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
}
