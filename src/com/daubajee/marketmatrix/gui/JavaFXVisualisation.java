package com.daubajee.marketmatrix.gui;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class JavaFXVisualisation extends Application {
	
	public static boolean run = false;
	
	public synchronized static boolean showGUI(){
		
		if (run)
			return true;
		
		Thread th = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Application.launch(JavaFXVisualisation.class);
			}
		});
		th.setDaemon(true);
		th.start();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		run = true;
		return false;
	}
	
	@Override
	public void start(Stage stage) throws Exception {

		JavaFXGUIController controller = JavaFXGUIController.getInstance();

		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		
		stage.setX(primaryScreenBounds.getMinX());
		stage.setY(primaryScreenBounds.getMinY());
		stage.setWidth(primaryScreenBounds.getWidth());
		stage.setHeight(primaryScreenBounds.getHeight());
		
		Scene scene = new Scene(controller.getRootNode(), primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight());
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args) {
		Application.launch(JavaFXVisualisation.class);
	}

}
