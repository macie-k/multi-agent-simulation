package app;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import utils.Utils;

import java.util.ArrayList;

import agents.Agent;

import static utils.Utils.fadeOut;

public class Window extends Application {
	
	public static int YOUNG = 100;
	public static int ELDERLY = 100;
	public static int DOCTORS = 15;
	public static int INFECTED = 2;
	public static int DELTA_SPEED = 1;
	
	public static ArrayList<Agent> agentsToAdd = new ArrayList<>();
	public static boolean infectious = false;
	public static Stage window;				// main stage
	public static final int WIDTH = 1000;	// window width
	public static final int HEIGHT = 720;	// window height
	public static final int PANEL_WIDTH = 300;
	
	public static StackPane panelStack = null;
	
	private static int recovered = 0;
	private static int infected = 0;
	private static int dead = 0;
	public static AnimationTimer mainTimer;
	
	public static void launcher(String[] args) {
		Utils.parseArguments(args);
		Utils.loadFonts();
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		window.setTitle("Symulacja wirusa");
		window.getIcons().add(new Image("https://i.imgur.com/nGafwbe.jpg"));
		window.setResizable(false);
		
		final ArrayList<Agent> agents = Utils.createAgents(YOUNG, ELDERLY, DOCTORS, INFECTED);
		final Pane root = Utils.getMainScene(agents);
		final int total = agents.size();
		root.getChildren().addAll(agents);
		
//		idk?
//		for(Node n : root.getChildren()) {
//			if(n.getId() != null && n.getId().equals("panel-stack")) {
//				panelStack = (StackPane) n;
//				for(Node c : panelStack.getChildren()) {
//					if(c.getId() != null && c.getId().eqals("start-stack")
//						startStack = (StackPane) c;
//						break;
//				}
//			}
//		}
		
		mainTimer = new AnimationTimer() {
			private long lastUpdate = 0;

			@Override
			public void handle(long now) {	
				recovered = 0;
				infected = 0;
				
				if(now - lastUpdate >= 30_000_000) {
					for(Agent agent : agents) {
						agent.move();
						agent.detectBump(agents);
						
						if(agent.isInfected() && !agent.isDead()) infected++;
						if(agent.isImmune()) recovered++;
						if(agent.isDead() && !agent.isFading()) {
							if(infectious) dead++;
							
							agent.setFading(true);
							fadeOut(agent, 300, e -> {
								agents.remove(agent);
								root.getChildren().remove(agent);
							});
						}
					}
					
					if(agentsToAdd.size() > 0 && agentsToAdd.get(0) == null) {
						agentsToAdd.remove(0);
						agents.addAll(agentsToAdd);
						root.getChildren().addAll(agentsToAdd);
					}
					
					if(infectious) {
						System.out.printf("Infected: [%d/%d]     Dead: [%d/%d]     Recovered: [%d/%d]     \r",
								infected, total, dead, total, recovered, total);
					}
					
					if(infected == 0 && infectious) {
						Utils.setDisabledPanel(false);
						infectious = false;
					}
															
					lastUpdate = now;
				}				
			}
			
		}; mainTimer.start();		
				
		Scene scene = new Scene(root);
			scene.getStylesheets().add(Window.class.getResource("/styles/mainStyle.css").toExternalForm());
		
		window.setScene(scene);
		window.show();
	}
}
