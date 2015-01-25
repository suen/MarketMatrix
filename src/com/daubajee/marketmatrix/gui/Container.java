package com.daubajee.marketmatrix.gui;

import com.daubajee.marketmatrix.agent.MarketAgentCreator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Container extends BorderPane	{

	private HBox titleContainer = new HBox();
	private VBox leftcontainer = new VBox();
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
	

	
	private TextFlow textflow = new TextFlow();
	private MarketAgentCreator agentCreator;
	
	public Container(){
		initLayout();
		initStyle();
	}
	
	private void initLayout() {
		setTop(titleContainer);

		sp2.setContent(textflow);
		setRight(sp2);
		setCenter(leftcontainer);
		
		sp1.setContent(agentContainer);
		
		titleContainer.getChildren().add(title);		
		leftcontainer.getChildren().addAll(agentCreationBox, sp1);
		
		agentCreationBox.setHgap(10);
		
		Separator separator = new Separator();
		separator.setOrientation(Orientation.VERTICAL);
		Separator separator2 = new Separator();
		separator2.setOrientation(Orientation.VERTICAL);
		agentCreationBox.getChildren().addAll(createAgentLabel, 
				consumeLabel, consumeBox, consumeRateLabel, consumeRateField, 
				separator,
				produceLabel, producesBox, produceRateLabel, produceRateField,
				separator2, createBtn
				);
		
		ObservableList<String> list = FXCollections.observableArrayList();
		consumeBox.setItems(list);
		producesBox.setItems(list);
		list.addAll("Apple", "Banna", "Orange", "Rice", "Wheat", "Corn", "Barley");
		
/*		for(int i=0; i<10; i++){
			agentContainer.getChildren().add(new AgentGUI(String.valueOf(i)));
		}*/
	}
	
	public int addAgentGUI(AgentGUI agui){
		int nbAgent = agentContainer.getChildren().size();
		agentContainer.getChildren().add(nbAgent, agui);
		return nbAgent;
	}
	
	public void updateAgent(int index){
		AgentGUI node = (AgentGUI) agentContainer.getChildren().get(index);
		node.updateLabels();
	}
	
	public void addMsg(String msg) {
		textflow.getChildren().add(0, new Text(msg+"\n"));
	}
	
	public void flushMsg(){
		while(textflow.getChildren().size()>0)
			textflow.getChildren().remove(0);
	}
	
	
	private void initStyle(){
		
		setStyle("-fx-spacing: 10px;"
				+ "-fx-pref-height: 700px;");
		
		titleContainer.setStyle("-fx-background-color: #dfdfdf;"
				+ "-fx-pref-height: 50px;"
				+ "-fx-padding: 10px;");
		title.setStyle("-fx-text-fill: #000000; "
					+ "-fx-font-family: 'sans-serif';"
					+ "-fx-font-size: 2.5em; ");

		leftcontainer.setStyle("-fx-min-height: 800px;");
		
		sp1.setStyle("-fx-min-height: 500px;"
				+ "-fx-min-width: 800px;"
				+ "-fx-padding: 10px;");
		
		agentContainer.setVgap(10);
		agentContainer.setHgap(10);
		agentContainer.setStyle("-fx-pref-width: 800px;"
				+ "-fx-pref-height: 500px;"
				+ "-fx-spacing: 10px;");
		agentContainer.setMinWidth(sp1.getWidth());

		sp2.setStyle("-fx-pref-width: 500px;"
				+ "-fx-padding: 10px;");
		textflow.setStyle("-fx-pref-width: 400px;");
		textflow.setLineSpacing(5);
		
		
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
		
		createBtn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				String consumes = consumeBox.getSelectionModel().getSelectedItem();
				String produces = producesBox.getSelectionModel().getSelectedItem();
				
				if (produces.equals(consumes)){
					addMsg("Agents cannot consume and produce same product");
					return;
				}
				
				try {
					String consumeRateStr = consumeRateField.getText();
					String produceRateStr = produceRateField.getText();
					
					double consumeRate = Double.parseDouble(consumeRateStr);
					double produceRate = Double.parseDouble(produceRateStr);
				
					agentCreator.onAgentAdd(consumes, consumeRate, produces, produceRate);
					
				} catch (NumberFormatException e) {
					addMsg("Please check your data");
					e.printStackTrace();
					return;
				}
				
				
				
			}
		});
	}
	
	public void registerAgentCreator(MarketAgentCreator creator){
		this.agentCreator = creator;
	}

	
	
}
