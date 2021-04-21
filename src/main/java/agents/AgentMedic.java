package agents;

import java.util.ArrayList;

public class AgentMedic extends AgentHealthy {
	
	public AgentMedic() {
		super(true);
	}
	
	public AgentMedic(double x, double y, int ID, double angle) {
		super(x, y, ID, angle, true);		
	}
	
	@Override
	public void interact(Agent bump, ArrayList<Agent> toAdd, ArrayList<Agent> toRemove) {
		if(!immune && bump.type == AgentType.DEADLY && rnd.nextDouble() > 0.4) {
			AgentInfected newAgent = new AgentInfected(x, y, ID, angle);
			toAdd.add(newAgent);
			toRemove.add(this);
		}
	}
}
