package com.daubajee.marketmatrix.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

import com.daubajee.marketmatrix.agent.MarketAgent;
import com.daubajee.marketmatrix.agent.MarketAgentCreator;

public class NewContainer extends BorderPane	{

	private VBox headerContainer = new VBox();
	private StackPane leftcontainer = new StackPane();
	private FlowPane agentCreationBox = new FlowPane();
	private ScrollPane sp1 = new ScrollPane();
	private ScrollPane sp2 = new ScrollPane();	
	private FlowPane agentContainer = new FlowPane();
	
	private final String appName = "Market Matrix";
	private Label title = new Label(appName);
	
	private ChoiceBox<String> consumeBox = new ChoiceBox<String>();
	private ChoiceBox<String> producesBox = new ChoiceBox<String>();
	Label createAgentLabel = new Label("Create Agent");
	Label consumeLabel = new Label("Consumes");
	Label consumeRateLabel = new Label("Rate: ");
	TextField consumeRateField = new TextField("1");
	Label produceLabel = new Label("Produces");
	Label produceRateLabel = new Label("Rate");
	TextField produceRateField = new TextField("1");
	Button createBtn = new Button("Create");
	
	private Label pulseLabel = new Label("Pulse :");
	private TextField pulseField = new TextField();
	private Button pulseSet = new Button("Set");
	private Button pulsePlayPause = new Button("Pause");
	
	private VBox textflow = new VBox();
	private MarketAgentCreator agentCreator;
	
	public NewContainer(){
		initLayout();
		initStyle();
	}
	
	private void initLayout() {
		setTop(headerContainer);

		sp2.setContent(textflow);
		setCenter(leftcontainer);
		
		sp1.setContent(agentContainer);
		
		headerContainer.getChildren().addAll(title, new Separator(), agentCreationBox);		
		leftcontainer.getChildren().addAll(sp1);
		
		agentCreationBox.setHgap(10);
		
		Separator separator = new Separator();
		separator.setOrientation(Orientation.VERTICAL);
		Separator separator2 = new Separator();
		separator2.setOrientation(Orientation.VERTICAL);
		Separator separator3 = new Separator();
		separator3.setOrientation(Orientation.VERTICAL);
		agentCreationBox.getChildren().addAll(createAgentLabel, 
				consumeLabel, consumeBox, consumeRateLabel, consumeRateField, 
				separator,
				produceLabel, producesBox, produceRateLabel, produceRateField,
				separator2, createBtn,
				separator3, pulseLabel, pulseField, new Label("ms"), pulseSet, 
				pulsePlayPause
				);
		pulseField.setText(String.valueOf(MarketAgent.TIME_UNIT));
		
		ObservableList<String> list = FXCollections.observableArrayList();
		consumeBox.setItems(list);
		producesBox.setItems(list);
		list.addAll("Apple", "Banna", "Orange", "Rice", "Wheat", "Corn", "Barley");
		
		for(int i=0; i<13; i++){
		//	agentContainer.getChildren().add(new NewAgentGUI(String.valueOf(i)));
		}
	}
	
	public int addAgentGUI(NewAgentGUI agui){
		int nbAgent = agentContainer.getChildren().size();
		agentContainer.getChildren().add(nbAgent, agui);
		return nbAgent;
	}
	
	public void updateAgent(int index){
		NewAgentGUI node = (NewAgentGUI) agentContainer.getChildren().get(index);
		node.updateLabels();
	}

	private boolean odd = true;
	public void addMsgs(String msg) {
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
		textflow.getChildren().add(0, label);
	}
	
	public void addMsg(NewAgentGUI agui, String msg) {
		agui.addMsg(msg);
	}
	
	public void flushMsg(){
		while(textflow.getChildren().size()>0)
			textflow.getChildren().remove(0);
	}
	
	
	private void initStyle(){

		double width = Screen.getPrimary().getBounds().getWidth();
		double height = Screen.getPrimary().getBounds().getHeight();

		setStyle("-fx-spacing: 10px;"
				+ "-fx-pref-height: 700px;");
		
		headerContainer.setStyle("-fx-background-color: #dfdfdf;"
				+ "-fx-pref-height: 50px;"
				+ "-fx-padding: 10px;");
		title.setStyle(""
					+ "-fx-text-fill: #000000; "
					+ "-fx-font-family: 'sans-serif';"
					+ "-fx-font-size: 2.5em; ");

//		leftcontainer.setStyle("-fx-min-height: 800px;");
		
		sp1.setStyle("-fx-min-height: 600px;"
//				+ "-fx-min-width: 800px;"
				+ "-fx-padding: 10px;");
		
		agentContainer.setVgap(10);
		agentContainer.setHgap(10);
		agentContainer.setStyle(
				"-fx-pref-width: "+String.valueOf(width)+"px;"
				+ "-fx-pref-height: 500px;"
				+ "-fx-spacing: 25px;");

		agentContainer.setMinWidth(sp1.getWidth());

		sp2.setStyle("-fx-pref-width: 500px;"
				+ "-fx-padding: 5px;");
		textflow.setStyle("-fx-pref-width: 400px;"
				+ "-fx-spacing: 1px;");
		//textflow.setLineSpacing(5);
		
		
		createAgentLabel.setStyle("-fx-font-size: 1.8em;");
		consumeRateField.setStyle("-fx-max-width: 30px;");
		produceRateField.setStyle("-fx-max-width: 30px;");
		producesBox.setStyle("-fx-min-width: 100px;");
		consumeBox.setStyle("-fx-min-width: 100px;");
		consumeLabel.setStyle("-fx-font-size: 1.0em;"
				+ "-fx-font-weight: bold;");
		produceLabel.setStyle("-fx-font-size: 1.0em;"
				+ "-fx-font-weight: bold;");
		createBtn.setStyle("-fx-font-size: 1.3em;");
		
		pulseLabel.setStyle("-fx-font-size: 1.0em;"
				+ "-fx-font-weight: bold;");
		pulseSet.setStyle("-fx-font-weight: bold;");
		pulseField.setStyle("-fx-max-width: 60px;");
		
		createBtn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				String consumes = consumeBox.getSelectionModel().getSelectedItem();
				String produces = producesBox.getSelectionModel().getSelectedItem();
				
				if (produces.equals(consumes)){
				//	addMsg("Agents cannot consume and produce same product");
					return;
				}
				
				try {
					String consumeRateStr = consumeRateField.getText();
					String produceRateStr = produceRateField.getText();
					
					double consumeRate = Double.parseDouble(consumeRateStr);
					double produceRate = Double.parseDouble(produceRateStr);
				
					agentCreator.onAgentAdd(consumes, consumeRate, produces, produceRate);
					
				} catch (NumberFormatException e) {
				//	addMsg("Please check your data");
					e.printStackTrace();
					return;
				}
			}
		});
		
		pulseSet.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent evnt) {
				String pulseStr = pulseField.getText();
				
				int pulse = 0;
				try {
					pulse = Integer.parseInt(pulseStr);
				} catch (NumberFormatException e) {
					System.err.println("Number Format Exception "
							+ "while parsing pulse value");
					return;
				}
				
				MarketAgent.TIME_UNIT = pulse;
					
			}
		});
		
		pulsePlayPause.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent evnt) {
				String pausePlayStr = pulsePlayPause.getText();
				if (pausePlayStr.equals("Play")){
					pulsePlayPause.setText("Pause");
					MarketAgent.PAUSE = false;
				} else if (pausePlayStr.equals("Pause")){
					pulsePlayPause.setText("Play");
					MarketAgent.PAUSE = true;
				}
			}
		});
	}
	
	public void registerAgentCreator(MarketAgentCreator creator){
		this.agentCreator = creator;
	}

	
	
}
