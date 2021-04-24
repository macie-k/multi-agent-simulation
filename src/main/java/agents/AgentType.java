package agents;

import javafx.scene.paint.Color;

public enum AgentType {
	YOUNG(AgentColor.YOUNG),
	ELDERLY(AgentColor.ELDERLY),
	DOCTOR(AgentColor.DOCTOR);
	
	public final Color color;

    private AgentType(Color color) {
        this.color = color;
    }
}
