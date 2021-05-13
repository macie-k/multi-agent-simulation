package utils;

import static app.Window.HEIGHT;
import static app.Window.PANEL_WIDTH;
import static app.Window.WIDTH;

import java.util.ArrayList;

import agents.Agent;
import agents.AgentColor;
import agents.AgentDoctor;
import agents.AgentElderly;
import agents.AgentYoung;
import app.Window;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Scenes {
	
	public static StackPane panelStack;
	public static StackPane settingsStack;
	public static StackPane statsStack;
	
	public static NewText iText;
	public static NewText dText;
	public static NewText rText;
	
	public static ArrayList<NewTextField> settingInputs;

	
	public static Pane getMainScene(ArrayList<Agent> agents) {
		final Pane root = new Pane();
			root.setStyle("-fx-background-color: #2F2F2F");
			root.setPrefSize(WIDTH+PANEL_WIDTH, HEIGHT);
		
		panelStack = new StackPane();
			panelStack.setPrefSize(PANEL_WIDTH, HEIGHT);
			panelStack.setTranslateX(WIDTH);
			panelStack.setTranslateY(0);
			panelStack.setAlignment(Pos.TOP_LEFT);
			panelStack.setId("panel-stack");
						
		final Rectangle panelBg = new Rectangle(PANEL_WIDTH, HEIGHT);	
			panelBg.setId("panel-bg");
			panelBg.setTranslateX(0);
			panelBg.setTranslateY(0);
						
		settingsStack = getSettingsPanel(agents);		
		panelStack.getChildren().addAll(panelBg, settingsStack);
		
		root.getChildren().add(panelStack);
		return root;
	}
	
	public static void showStats() {
		panelStack.getChildren().remove(settingsStack);
		panelStack.getChildren().add(statsStack);
	}
	
	public static void showSettings() {
		panelStack.getChildren().remove(statsStack);
		panelStack.getChildren().add(settingsStack);

		Slider s = null;
		for(Node n : settingsStack.getChildren()) {
			if(n.getId() != null && n.getId().equals("speed-slider")) {
				 s = (Slider) n;
			}
		}
		
		for(NewTextField tf : settingInputs) {
			tf.requestFocus();
		}
		s.setValue(1);
		s.requestFocus();
	}
	
	@SuppressWarnings("static-access")
	public static StackPane getStatsPanel(ArrayList<Agent> agents) {
		final StackPane statsStack = new StackPane();
			statsStack.setPrefSize(PANEL_WIDTH, HEIGHT);
			statsStack.setAlignment(Pos.TOP_LEFT);
		
		final Text statsText = new Text("STATS");
			statsText.setId("stats-text");
			statsText.setTranslateY(50);
			statsStack.setAlignment(statsText, Pos.TOP_CENTER);
			
		final int agentTextX = 30;
		final int agentTextY = 160;
		final int agentTextSpacing = 30;
		int incr = 0;
			
		iText = new NewText("INFECTED", agentTextX, agentTextY + agentTextSpacing*incr++, agents.size());
		dText = new NewText("DEAD", agentTextX, agentTextY + agentTextSpacing*incr++, agents.size());
		rText = new NewText("RECOVERED", agentTextX, agentTextY + agentTextSpacing*incr++, agents.size());
		
		int infected = 0;
		for(Agent a : agents)
			if(a.isInfected()) infected++;
		iText.setStats(infected);
				
		statsStack.getChildren().addAll(statsText, iText, dText, rText);		
		
		return statsStack;
	}

	@SuppressWarnings("static-access")
	public static StackPane getSettingsPanel(ArrayList<Agent> agents) {		
		final StackPane settingsStack = new StackPane();
			settingsStack.setPrefSize(PANEL_WIDTH, HEIGHT);
			settingsStack.setAlignment(Pos.TOP_LEFT);
			
		final Text settingsText = new Text("SETTINGS");
			settingsText.setId("settings-text");
			settingsText.setTranslateY(50);
			settingsStack.setAlignment(settingsText, Pos.TOP_CENTER);
			
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
						
		settingsStack.setOnMousePressed(e -> {
			settingsText.requestFocus();
		});
		
		final StackPane startStack = new StackPane();
			startStack.setMaxSize(115, 40);
			startStack.setTranslateY(-50);
			settingsStack.setAlignment(startStack, Pos.BOTTOM_CENTER);
			startStack.setId("start-stack");
			
		final Color WHITE = Color.web("#FCFCFC");
		final Color DARK_GREY = Color.web("#515658");
						
		final Rectangle startBg = new Rectangle(115, 40, DARK_GREY);
		final Text startText = new Text("START");
			startText.setId("start-text");
						
		startStack.setOnMouseExited(e -> {
			Utils.fadeColors(startBg, 200, WHITE, DARK_GREY);
			Utils.fadeColors(startText, 200, DARK_GREY, WHITE);
		});
		
		startStack.setOnMouseEntered(e -> {
			Utils.fadeColors(startBg, 200, DARK_GREY, WHITE);
			Utils.fadeColors(startText, 200, WHITE, DARK_GREY);
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
		
		startStack.setOnMouseClicked(e -> {
			Utils.listAgents(agents);
			statsStack = getStatsPanel(agents);
			showStats();
			
			Window.infectious = true;
			Log.success("Virus is now infectious\n");			
		});
										
		startStack.getChildren().addAll(startBg, startText);
			
		settingsStack.getChildren().addAll(settingsText, startStack, speedSlider, speedText);
		settingsStack.getChildren().addAll(agentStacks);
		
		settingInputs = inputs;
		return settingsStack;
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
}
