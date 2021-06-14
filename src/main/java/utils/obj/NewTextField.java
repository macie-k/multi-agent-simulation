package utils.obj;

import java.util.ArrayList;

import agents.Agent;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
/**
 * Class responsible for creating new text field.
 * 
 * @author MACIEJ KAMIERCZYK
 * @author JANUSZ IGNASZAK
 *
 */
public class NewTextField extends TextField {
	
	private final Color agentColor;
	private final String defaultVal;
	
	public NewTextField(Color color, int defaultVal) {
		this(String.valueOf(defaultVal), color);
	}
	
	public NewTextField(String defaultVal, Color agentColor) {
		super(defaultVal);
		this.defaultVal = defaultVal;
		this.agentColor = agentColor;		
		
		getStyleClass().add("agent-value");
		
		textProperty().addListener((obs, oldVal, newVal) -> {
			try {
				if(newVal.length() > 3)
					setText(oldVal);
				Integer.parseInt(newVal);
			} catch (Exception e) {
				setText(oldVal);
			}
			return;
		});
	}
	
	public String getDefaultVal() {
		return defaultVal;
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
