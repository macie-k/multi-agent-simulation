package utils;

import java.util.ArrayList;

import agents.Agent;
import agents.AgentDoctor;
import agents.AgentElderly;
import agents.AgentYoung;
import app.Window;
import argsparser.ArgsParser;
import argsparser.Argument;
import argsparser.ArgType;
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
		ArgsParser argParser = new ArgsParser();
		argParser.addArguments(
			new Argument("--nocolors", "disable colored logging", () -> Log.IDE = true, "Colored logging disabled"),			
			new Argument("--radius", "-r", "set agents radius", ArgType.INT, () -> {
				Agent.RADIUS = (int) argParser.getValue("radius");
				Log.success("Agents' radius set to: " + Agent.RADIUS);
			}),
			
			new Argument("--speed", "-s", "set agents speed", ArgType.INT, () -> {
				Window.DELTA_SPEED = (int) argParser.getValue("speed");
				Log.success("Agents' speed set to: " + argParser.getValue("speed"));
			}),
			
			new Argument("--delay", "set agents radius", ArgType.INT, () -> {
				Window.DELAY = (int) argParser.getValue("delay");
				Log.success("Agents' radius set to: " + Window.DELAY);
			}),
			
			new Argument("--young", "-y", "set young agents amount", ArgType.INT, () -> {
				Window.YOUNG = (int) argParser.getValue("young");
			}),
			new Argument("--elderly", "-e", "set elderly agents amount", ArgType.INT, () -> {
				Window.ELDERLY = (int) argParser.getValue("elderly");
			}),
			new Argument("--doctors", "-d", "set doctor agents amount", ArgType.INT, () -> {
				Window.DOCTORS = (int) argParser.getValue("doctors");
			}),
			new Argument("--infected", "-i", "set infected agents amount", ArgType.INT, () -> {
				Window.INFECTED = (int) argParser.getValue("infected");
			})
		);
		
		argParser.parse(args);
	}
	
	public static boolean isNumber(String val) {
		try {
			Double.parseDouble(val);
			return true;
		} catch(NumberFormatException e) {
			return false;
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
