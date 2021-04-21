package app;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;

import agents.Agent;
import agents.AgentType;

public class Window extends Application {
	
	public static boolean infectious = false;
	public static Stage window;				// main stage
	public static final int WIDTH = 1150;	// window width
	public static final int HEIGHT = 720;	// window height
	
	private static int delay = 0;
	private static int recovered = 0;
	private static int infected = 0;
	private static int dead = 0;
	private static AnimationTimer delayTimer;
	
	public static void main(String[] args) {
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
			
		final ArrayList<Agent> agents = Utils.createAgents(240, 10, 2, 1);
		root.getChildren().addAll(agents);
		
		System.out.println();		
		AnimationTimer timer = new AnimationTimer() {
			private long lastUpdate = 0;

			@Override
			public void handle(long now) {
				final ArrayList<Agent> toRemove = new ArrayList<>();
				final ArrayList<Agent> toAdd = new ArrayList<>();
				
				recovered = 0;
				infected = 0;
				
				if(now - lastUpdate >= 30_000_000) {
					for(Agent agent : agents) {
						agent.move(320);
						agent.detectBump(agents, toRemove, toAdd);
						
						if(agent.getType() == AgentType.HEALTHY && agent.isImmune()) recovered++;
						if(agent.getType() == AgentType.INFECTED) infected++;
						if(agent.isDead()) dead++;
					}
					
					System.out.printf("Infected: [%d/%d]     Dead: [%d/%d]     Recovered: [%d/%d]     \r",
							infected, agents.size(), dead, agents.size(), recovered, agents.size());
					
					root.getChildren().removeAll(toRemove);
					root.getChildren().addAll(toAdd);
					agents.removeAll(toRemove);
					agents.addAll(toAdd);
					
					lastUpdate = now;
				}				
			}
		}; timer.start();		
		
		delayTimer = new AnimationTimer() {
			private long lastUpdate = 0;

			@Override
			public void handle(long now) {
				if(now - lastUpdate >= 1_000_000_000) {
					if(delay++ > 2) {
						infectious = true;
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
