package agents;

import java.util.ArrayList;

public class AgentInfected extends Agent {
	
	public static int amount;
	
	public AgentInfected() {
		this(false);
	}
		
	public AgentInfected(boolean deadly) {
		super(deadly ? AgentType.DEADLY : AgentType.INFECTED);
	}
	
	public AgentInfected(double x, double y, int ID, double angle) {
		super(x, y, ID, angle, AgentType.INFECTED);		
	}
			
	public void interact(Agent bump, ArrayList<Agent> toAdd, ArrayList<Agent> toRemove) {
		if(bump.type == AgentType.MEDIC) {
			AgentHealthy newAgent = new AgentHealthy(x, y, ID, angle, false);
				newAgent.setImmune(true);
				
			toAdd.add(newAgent);
			toRemove.add(this);
		}
	}
}
