package agents;

import javafx.scene.paint.Color;

public enum AgentType {
	HEALTHY(AgentColor.HEALTHY),
	INFECTED(AgentColor.INFECTED),
	MEDIC(AgentColor.MEDIC),
	DEADLY(AgentColor.DEADLY);
	
	public final Color color;

    private AgentType(Color color) {
        this.color = color;
    }
}
