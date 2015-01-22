package com.daubajee.marketmatrix.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Container extends BorderPane	{

	private HBox titleContainer = new HBox();
	private ScrollPane sp1 = new ScrollPane();
	private ScrollPane sp2 = new ScrollPane();	
	private FlowPane agentContainer = new FlowPane();
	
	private final String appName = "Market Matrix";
	private Label title = new Label(appName);

	private TextFlow textflow = new TextFlow();
	
	public Container(){
		initLayout();
		initStyle();
	}
	
	private void initLayout() {
		setTop(titleContainer);

		sp2.setContent(textflow);
		setRight(sp2);
		setCenter(sp1);
		
		sp1.setContent(agentContainer);
		
		titleContainer.getChildren().add(title);		

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
		
	}
	
	
}
