package app;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utils.Scenes;
import utils.Utils;

import java.util.ArrayList;

import agents.Agent;

import static utils.Utils.fadeOut;
/**
 * Class containing simulation's logic.
 * 
 * @author MACIEJ KAZMIERCZYK
 * @author JANUSZ IGNASZAK
 *
 */
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
		
	public static int recovered = 0;
	public static int infected = 0;
	public static int dead = 0;
	public static AnimationTimer mainTimer;
	
	public static int age = 1;
	/**
	 * Method responsible for launching simulation logic.
	 * @param args Receives arguments.
	 */
	public static void launcher(String[] args) {
		Utils.parseArguments(args);
		Utils.loadFonts();
		launch(args);
	}
	/**
	 * Method responsible for simulation logic.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		window.setTitle("Symulacja wirusa");
		window.getIcons().add(new Image("https://i.imgur.com/nGafwbe.jpg"));
		window.setResizable(false);
		
		final ArrayList<Agent> agents = Utils.createAgents(YOUNG, ELDERLY, DOCTORS, INFECTED);
		final Pane root = Scenes.getMainScene(agents);
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
						agentsToAdd.clear();
					}
					
					if(infectious) {
						final int total = agents.size();
						System.out.printf("Infected: [%d/%d]     Dead: [%d/%d]     Recovered: [%d/%d]     \r",
								infected, total, dead, total, recovered, total);
						Utils.saveOutput();
						Scenes.iText.setStats(infected);
						Scenes.dText.setStats(dead);
						Scenes.rText.setStats(recovered);
						age++;
					}
					
					if(infected == 0 && infectious) {
						Utils.saveOutputFinal();
						Scenes.showSettings();
						agents.forEach(agent -> agent.setImmune(false));
						
						infectious = false;
						dead = 0;
						age=1;
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
