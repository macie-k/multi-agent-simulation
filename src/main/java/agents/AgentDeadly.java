package agents;

import java.util.ArrayList;

public class AgentDeadly extends AgentInfected {
	
	public AgentDeadly() {
		super(true);
	}
	
	@Override
	public void interact(Agent bump, ArrayList<Agent> toAdd, ArrayList<Agent> toRemove) {}
}
