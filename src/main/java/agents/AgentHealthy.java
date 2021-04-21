package agents;

import java.util.ArrayList;

import app.Window;

public class AgentHealthy extends Agent {
		
	public AgentHealthy() {
		this(false);
	}
	
	public AgentHealthy(boolean medic) {
		super(medic ? AgentType.MEDIC : AgentType.HEALTHY);
	}
	
	public AgentHealthy(double x, double y, int ID, double angle, boolean medic) {
		super(x, y, ID, angle, medic ? AgentType.MEDIC : AgentType.HEALTHY);		
	}
	
	public void interact(Agent bump, ArrayList<Agent> toAdd, ArrayList<Agent> toRemove) {
		if(!immune && Window.infectious) {
			boolean gotInfected = false;
			
			/* chance of being infected by "Infected" agent = 40% */
			if(bump.type == AgentType.INFECTED && rnd.nextDouble() > 0.6) {
				gotInfected = true;
			}
			
			/* chance of being infected by "Deadly Infected" agent = 60% */
			if(bump.type == AgentType.DEADLY && rnd.nextDouble() > 0.4) {
				gotInfected = true;
			}
			
			if(gotInfected) {
				toAdd.add(new AgentInfected(x, y, ID, angle));
				toRemove.add(this);
			}
		}
	}
}
