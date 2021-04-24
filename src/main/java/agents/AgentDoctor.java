package agents;

import app.Window;

public class AgentDoctor extends Agent {

	public AgentDoctor() {
		super(AgentType.DOCTOR);
	}

	@Override
	public void interact(Agent bump) {
		if(bump.isInfected() && !infected) {
			bump.setInfected(false);
			bump.setImmune(true);
		}				
		
		if(!immune && !infected && lastInteraction != bump && Window.infectious) {
			
			/* chance of being infected by "Deadly Infected" agent = 50% */
			if(bump.deadlyInfected  && rnd.nextDouble() > 0.5) {
				setInfected(true);
			}
		}
		
		throttleInteraction(bump);
	}

	@Override
	public void setInfected(boolean value) {
		infected = value;
		setColor(infected ? AgentColor.INFECTED : AgentColor.DOCTOR);
	}
}
