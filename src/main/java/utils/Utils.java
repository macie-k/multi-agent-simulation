package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import agents.Agent;
import agents.AgentDoctor;
import agents.AgentElderly;
import agents.AgentYoung;
import app.Window;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.util.Duration;
import utils.argsparser.ArgType;
import utils.argsparser.ArgsParser;
import utils.argsparser.Argument;
/**
 * Class containing utility methods.
 * 
 * @author MACIEJ KAZMIERCZYK
 * @author JANUSZ IGNASZAK
 *
 */
public class Utils {
			
	/**
	 * Method for creating agents
	 * @param y number of young agents
	 * @param e number of elderly agents
	 * @param d number of dead agents
	 * @param i number of infected agents
	 * @return
	 */
	public static ArrayList<Agent> createAgents(int y, int e, int d, int i) {
		ArrayList<Agent> agents = new ArrayList<>();		
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
	
	public static void countAgents(ArrayList<Agent> agents) {
		int y = 0, e = 0, d = 0, i = 0;
		for(Agent a : agents) {
			if(a instanceof AgentYoung) y++;
			if(a instanceof AgentElderly) e++;
			if(a instanceof AgentDoctor) d++;
			if(a.isInfected()) i++;
		}
		
		Window.YOUNG = (y-i);
		Window.ELDERLY = e;
		Window.DOCTORS = d;
		Window.INFECTED = i;
		
		Log.success("Creating " + (y-i) + " young agents");
		Log.success("Creating " + e + " elderly agents");
		Log.success("Creating " + d + " doctor agents");
		Log.success("Creating " + i + " infected agents");
	}
	
	/**
	 * Method to apply CLI arguments 
	 * @param args arguments to parse
	 */
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
		
	public static void loadFonts() {
		/* list of required font names */
		String[] fontNames = {
				"Poppins-Light.ttf",
				"Poppins-Regular.ttf",
				"Poppins-SemiBold.ttf",
				"Raleway-Light.ttf",
				"Raleway-Regular.ttf",
				"Raleway-SemiBold.ttf",
		};
		
		/* load each font */
		for(String font : fontNames) {
			try {
				Font.loadFont(Utils.class.getResourceAsStream("/fonts/" + font), 20);
			} catch (Exception e) {
				Log.error(String.format("Unable to load font {%s}: %s", font, e.getMessage()));
			}
		}		
	}
	
	public static boolean isNumber(String val) {
		try {
			Double.parseDouble(val);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
		
	/**
	 * Animates color change 
	 * @param shape
	 * @param duration
	 * @param from
	 * @param to
	 */
	public static void fadeColors(Shape shape, int duration, Color from, Color to) {
		try {
			FillTransition ft = new FillTransition(Duration.millis(duration), shape, from, to);
			ft.play();
		} catch (Exception e) {}
		
	}
	
	public static void fadeIn(Node node, int duration, EventHandler<ActionEvent> callback) {
		fade(node, duration, node.getOpacity(), 100, callback);
	}
	
	public static void fadeOut(Node node, int duration, EventHandler<ActionEvent> callback) {
		fade(node, duration, node.getOpacity(), 0, callback);
	}
	
	/* */
	/**
	 * Fades given node 
	 * @param node
	 * @param duration
	 * @param from
	 * @param to
	 * @param callback
	 */
	public static void fade(Node node, int duration, double from, double to, EventHandler<ActionEvent> callback) {
		try {
		FadeTransition ft = new FadeTransition(Duration.millis(duration), node);
			ft.setFromValue(from);
		    ft.setToValue(to);
		    ft.play();
		    if(callback != null) {
		    	ft.setOnFinished(callback);
		    }
		} catch (Exception e) {}
	}
	
	public static void saveOutput() {
		File file = new File("full-data.csv");
		StringBuilder builder = new StringBuilder();
		if(!file.exists()) {
			builder.append("age,infected,dead,recovered\n");
		}
		try(PrintWriter writer = new PrintWriter(new FileWriter(file, true))){
			String s = String.format("%d,%d,%d,%d\n", Window.age, Window.infected, Window.dead, Window.recovered);
			builder.append(s);
			writer.print(builder.toString());
		} catch(IOException e) {
			Log.error(e.getMessage());
		}
	}
	
	public static void saveOutputFinal() {
		File file = new File("result.csv");
		int total =  Window.YOUNG+Window.ELDERLY+Window.DOCTORS;
		StringBuilder builder = new StringBuilder();
		if(!file.exists()) {
			builder.append("young,elderly,doctors,infected,total,recovered,dead,speed,max age\n");
		}
		try(PrintWriter writer = new PrintWriter(new FileWriter(file, true))){
			String s = String.format("%d,%d,%d,%d,%d,%d,%d,%d,%d\n", Window.YOUNG,Window.ELDERLY, Window.DOCTORS, Window.INFECTED, total, Window.recovered, Window.dead, Window.DELTA_SPEED, Window.age);
			builder.append(s);
			writer.print(builder.toString());
		} catch(IOException e) {
			Log.error(e.getMessage());
		}
	}
}
