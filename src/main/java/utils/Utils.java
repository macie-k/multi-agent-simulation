package utils;

import java.util.ArrayList;

import agents.Agent;
import agents.AgentDoctor;
import agents.AgentElderly;
import agents.AgentYoung;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class Utils {
		
	/** function for creating agents */
	public static ArrayList<Agent> createAgents(int y, int e, int d, int i) {
		ArrayList<Agent> agents = new ArrayList<>();		
		Log.success("Creating " + y + " young agents");
		Log.success("Creating " + e + " elderly agents");
		Log.success("Creating " + d + " doctor agents");
		Log.success("Creating " + i + " infected agents");

		for(int j=0; j<y; j++) agents.add(new AgentYoung());
		for(int j=0; j<e; j++) agents.add(new AgentElderly());
		for(int j=0; j<d; j++) agents.add(new AgentDoctor());
		for(int j=0; j<i; j++) {
			Agent a = new AgentYoung();
				a.setInfected(true);
			agents.add(a);
		}
		return agents;
	}
	
	/** function to apply CLI arguments */
	public static void parseArguments(String[] args) {
		if(args.length > 0) {
			for(String arg : args) {
				switch(arg) {
					case "--nocolors":		// disables logging coloring
						Log.IDE = true;
						Log.success("Colored logging is disabled");
						break;
				}
			}
		}
	}
	
	/* animates color change */
	public static void fadeColors(Shape shape, int duration, Color from, Color to) {
		FillTransition ft = new FillTransition(Duration.millis(duration), shape, from, to);
			ft.play();
	}
	
	public static void fadeIn(Node node, int duration, EventHandler<ActionEvent> callback) {
		fade(node, duration, node.getOpacity(), 100, callback);
	}
	
	public static void fadeOut(Node node, int duration, EventHandler<ActionEvent> callback) {
		fade(node, duration, node.getOpacity(), 0, callback);
	}
	
	/* fades given node */
	public static void fade(Node node, int duration, double from, double to, EventHandler<ActionEvent> callback) {
		FadeTransition ft = new FadeTransition(Duration.millis(duration), node);
			ft.setFromValue(from);
		    ft.setToValue(to);
		    ft.play();
		    if(callback != null) {
		    	ft.setOnFinished(callback);
		    }
	}
}
