package utils;

import java.util.ArrayList;

import agents.Agent;
import agents.AgentColor;
import agents.AgentDoctor;
import agents.AgentElderly;
import agents.AgentYoung;
import app.Window;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import utils.argsparser.ArgType;
import utils.argsparser.ArgsParser;
import utils.argsparser.Argument;

import static app.Window.HEIGHT;
import static app.Window.WIDTH;
import static app.Window.PANEL_WIDTH;

public class Utils {
		
	/** function for creating agents */
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
	
	@SuppressWarnings("static-access")
	public static Pane getMainScene(ArrayList<Agent> agents) {
		final Pane root = new Pane();
			root.setStyle("-fx-background-color: #2F2F2F");
			root.setPrefSize(WIDTH+PANEL_WIDTH, HEIGHT);
		
		final StackPane panelStack = new StackPane();
			panelStack.setPrefSize(PANEL_WIDTH, HEIGHT);
			panelStack.setTranslateX(WIDTH);
			panelStack.setTranslateY(0);
			panelStack.setAlignment(Pos.TOP_LEFT);
			panelStack.setId("panel-stack");
			
		final Rectangle panelBg = new Rectangle(PANEL_WIDTH, HEIGHT);	
			panelBg.setId("panel-bg");
			panelBg.setTranslateX(0);
			panelBg.setTranslateY(0);
			
		final Text settingsText = new Text("SETTINGS");
			settingsText.setId("settings-text");
			panelStack.setAlignment(settingsText, Pos.TOP_CENTER);
			settingsText.setTranslateY(50);
			
		final int agentY = 160;
		final int agentSpacing = 10;
		final int agentRadius = 15;
		final int agentX = 30;
		final int inputSpacing = 20;
		final int textX = agentX + agentRadius*2 + inputSpacing + 50;
		
		final ArrayList<NewTextField> inputs = new ArrayList<>();
		final ArrayList<StackPane> agentStacks = new ArrayList<>();
		int incr = 1;
		
		/* AGENT YOUNG STACK */
		
		final Circle yAgent = new Circle(agentRadius, AgentColor.YOUNG);
			yAgent.setTranslateX(agentX);
		final NewTextField yValue = new NewTextField(AgentColor.YOUNG, Window.YOUNG);	
			inputs.add(yValue);
		final Text yText = new Text("• YOUNG");
			yText.setTranslateX(textX);
			yText.getStyleClass().add("agent-text");
		final StackPane yStack = new StackPane();
			yStack.getChildren().addAll(yAgent, yValue, yText);
			agentStacks.add(yStack);
			
		/* AGENT ELDERLY STACK */
			
		final Circle eAgent = new Circle(agentRadius, AgentColor.ELDERLY);
			eAgent.setTranslateX(agentX);
		final NewTextField eValue = new NewTextField(AgentColor.ELDERLY, Window.ELDERLY);	
			inputs.add(eValue);
		final Text eText = new Text("• ELDERLY");
			eText.setTranslateX(textX);
			eText.getStyleClass().add("agent-text");
		final StackPane eStack = new StackPane();
			eStack.getChildren().addAll(eAgent, eValue, eText);
			agentStacks.add(eStack);
			
		final Circle dAgent = new Circle(agentRadius, AgentColor.DOCTOR);
			dAgent.setTranslateX(agentX);
		final NewTextField dValue = new NewTextField(AgentColor.DOCTOR, Window.DOCTORS);	
			inputs.add(dValue);
		final Text dText = new Text("• DOCTOR");
			dText.setTranslateX(textX);
			dText.getStyleClass().add("agent-text");
		final StackPane dStack = new StackPane();
			dStack.getChildren().addAll(dAgent, dValue, dText);
			agentStacks.add(dStack);
			
		final Circle iAgent = new Circle(agentRadius, AgentColor.INFECTED);
			iAgent.setTranslateX(agentX);
		final NewTextField iValue = new NewTextField(AgentColor.INFECTED, Window.INFECTED);	
			inputs.add(iValue);		
		final Text iText = new Text("• INFECTED");
			iText.setTranslateX(textX);
			iText.getStyleClass().add("agent-text");
		final StackPane iStack = new StackPane();
			iStack.getChildren().addAll(iAgent, iValue, iText);
			agentStacks.add(iStack);
						
		panelStack.setOnMousePressed(e -> {
			settingsText.requestFocus();
		});
		
		final StackPane startStack = new StackPane();
			startStack.setMaxSize(115, 40);
			startStack.setTranslateY(-50);
			panelStack.setAlignment(startStack, Pos.BOTTOM_CENTER);
			startStack.setId("start-stack");
			
		final Color WHITE = Color.web("#FCFCFC");
		final Color DARK_GREY = Color.web("#515658");
						
		final Rectangle startBg = new Rectangle(115, 40, DARK_GREY);
		final Text startText = new Text("START");
			startText.setId("start-text");
			
		Window.panelStack = panelStack;
			
		startStack.setOnMouseExited(e -> {
			fadeColors(startBg, 200, WHITE, DARK_GREY);
			fadeColors(startText, 200, DARK_GREY, WHITE);
		});
		
		startStack.setOnMouseEntered(e -> {
			fadeColors(startBg, 200, DARK_GREY, WHITE);
			fadeColors(startText, 200, WHITE, DARK_GREY);
		});
		
		startStack.setOnMouseClicked(e -> {
			listAgents(agents);
			setDisabledPanel(true);
			
			Window.infectious = true;
			Log.success("Virus is now infectious\n");			
		});
				
		incr = 0;
		for(StackPane sp : agentStacks) {
			sp.setTranslateX(0);
			sp.setTranslateY(agentY + (agentSpacing + agentRadius * 2) * incr++);
			sp.setMaxSize(PANEL_WIDTH, agentRadius*2);
			sp.setAlignment(Pos.CENTER_LEFT);
		}
		
		for(NewTextField tf : inputs) {
			tf.setMaxSize(40, 25);
			tf.setTranslateX(agentX + agentRadius*2 + inputSpacing);
			
			tf.focusedProperty().addListener((obs, focusOut, focus) -> {
				if(focusOut) {
					if(tf.getText().length() == 0) tf.setText(tf.getDefaultVal());
					if(tf.getNumText() != tf.getCurrAgentCount(agents)) {
						final int diff = tf.getNumText() - tf.getCurrAgentCount(agents);
						if(diff < 0) {
							removeAgents(agents, tf.getAgentColor(), -diff);
						} else {
							addAgents(tf.getAgentColor(), diff);
						}
						if(tf.getAgentColor() == AgentColor.INFECTED) {
							startStack.setDisable(tf.getNumText() == 0);
							startStack.setOpacity(tf.getNumText() == 0 ? 0.5 : 1);
						}
					}
				}
			});
		}
		
		Text speedText = new Text("SPEED");
			speedText.setTranslateY(360);
			speedText.setTranslateX(127);
			speedText.setId("speed-text");
		
		Slider speedSlider = new Slider(1, 5, 1);
			speedSlider.setMaxWidth(PANEL_WIDTH - 100); 
			speedSlider.setTranslateX(50);
			speedSlider.setTranslateY(380);
			speedSlider.setId("speed-slider");
			
		speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
			Window.DELTA_SPEED = newVal.intValue();
		});
										
		startStack.getChildren().addAll(startBg, startText);
		
		panelStack.getChildren().addAll(panelBg, settingsText, startStack, speedSlider, speedText);
		panelStack.getChildren().addAll(agentStacks);
		
		root.getChildren().add(panelStack);
		
		return root;
	}
	
	public static void setDisabledPanel(boolean value) {
		Window.panelStack.setDisable(value);
		Window.panelStack.setOpacity(value ? 0.5 : 1);
	}
	
	public static void listAgents(ArrayList<Agent> agents) {
		int y = 0, e = 0, d = 0, i = 0;
		for(Agent a : agents) {
			if(a instanceof AgentYoung) y++;
			if(a instanceof AgentElderly) e++;
			if(a instanceof AgentDoctor) d++;
			if(a.isInfected()) i++;
		}
		Log.success("Creating " + (y-i) + " young agents");
		Log.success("Creating " + e + " elderly agents");
		Log.success("Creating " + d + " doctor agents");
		Log.success("Creating " + i + " infected agents");
	}
	
	public static void addAgents(Color color, int amount) {
		Window.agentsToAdd.clear();
		if(color == AgentColor.YOUNG) {
			for(int i=0; i<amount; i++)
				Window.agentsToAdd.add(new AgentYoung());
		}
		
		if(color == AgentColor.ELDERLY) {
			for(int i=0; i<amount; i++)
				Window.agentsToAdd.add(new AgentElderly());
		}
		
		if(color == AgentColor.DOCTOR) {
			for(int i=0; i<amount; i++)
				Window.agentsToAdd.add(new AgentDoctor());
		}
		
		if(color == AgentColor.INFECTED) {
			for(int i=0; i<amount; i++) {
				Agent a = new AgentYoung(); a.setInfected(true);
				Window.agentsToAdd.add(a);
			}
		}
		Window.agentsToAdd.add(0, null);
	}
	
	public static void removeAgents(ArrayList<Agent> agents, Color color, int amount) {
		int counter = 0;
		for(Agent a : agents) {
			if(a.getColor() == color && counter++ < amount) {
				a.kill();
			}
		}
	}
	
	
	/* function to apply CLI arguments */
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
				"Raleway-Black.ttf",
				"Raleway-Bold.ttf",
				"Raleway-Light.ttf",
				"Raleway-Regular.ttf",
				"Raleway-SemiBold.ttf",
				"Raleway-ExtraBold.ttf"
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
