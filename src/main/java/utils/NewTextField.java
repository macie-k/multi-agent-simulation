package utils;

import java.util.ArrayList;

import agents.Agent;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class NewTextField extends TextField {
	
	private Color agentColor;
	
	public NewTextField(Color color, int defaultVal) {
		this(String.valueOf(defaultVal), color);
	}
	
	public NewTextField(String defaultVal, Color agentColor) {
		super(defaultVal);
		this.agentColor = agentColor;
		
		getStyleClass().add("agent-value");
		
		textProperty().addListener((obs, oldVal, newVal) -> {
			if(newVal.length() == 0)
				setText(defaultVal);
			if(newVal.length() > 3)
				setText(oldVal);
			return;
		});
	}
	
	public int getNumText() {
		return Integer.valueOf(getText());
	}
	
	public int getCurrAgentCount(ArrayList<Agent> agents) {
		int counter = 0;
		for(Agent a : agents) {
			if(a.getColor() == agentColor) counter++;
		}
		return counter;
	}
	
	public Color getAgentColor() {
		return agentColor;
	}
}
