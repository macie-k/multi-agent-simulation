package app;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utils.Log;
import utils.Utils;

import java.util.ArrayList;

import agents.Agent;

import static utils.Utils.fadeOut;

public class Window extends Application {
	
	public static int YOUNG = 200;
	public static int ELDERLY = 200;
	public static int DOCTORS = 25;
	public static int INFECTED = 20;
	public static int DELAY = 2;
	public static double DELTA_SPEED = 1;
	
	public static boolean infectious = false;
	public static Stage window;				// main stage
	public static final int WIDTH = 1150;	// window width
	public static final int HEIGHT = 720;	// window height
	
	private static int delay = 0;
	private static int recovered = 0;
	private static int infected = 0;
	private static int dead = 0;
	private static AnimationTimer delayTimer;
	private static AnimationTimer mainTimer;
	
	public static void launcher(String[] args) {
		Utils.parseArguments(args);
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		window.setTitle("Symulacja wirusa");
		window.getIcons().add(new Image("https://i.imgur.com/nGafwbe.jpg"));
		window.setResizable(false);
		
		final Pane root = new Pane();
			root.setStyle("-fx-background-color: #2F2F2F");
			root.setPrefSize(WIDTH, HEIGHT);
			
		final ArrayList<Agent> agents = Utils.createAgents(YOUNG, ELDERLY, DOCTORS, INFECTED);
		final int total = agents.size();
		root.getChildren().addAll(agents);
			
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
							dead++;
							agent.setFading(true);
							fadeOut(agent, 300, e -> {
								agents.remove(agent);
								root.getChildren().remove(agent);
							});
						}
					}
					
					if(infectious) {
						System.out.printf("Infected: [%d/%d]     Dead: [%d/%d]     Recovered: [%d/%d]     \r",
								infected, total, dead, total, recovered, total);
					}
					
					if(infected == 0) {
						mainTimer.stop();
					}
															
					lastUpdate = now;
				}				
			}
			
		}; mainTimer.start();		
		
		delayTimer = new AnimationTimer() {
			private long lastUpdate = 0;

			@Override
			public void handle(long now) {
				if(now - lastUpdate >= 1_000_000_000 / DELTA_SPEED) {
					if(delay++ > DELAY) {
						infectious = true;
						Log.success("Virus is now infectious");
						System.out.println();
						
						delayTimer.stop();
					}
					lastUpdate = now;
				}
			}
		}; delayTimer.start();
		
		window.setScene(new Scene(root));
		window.show();
	}
}
