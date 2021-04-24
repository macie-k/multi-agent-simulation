package agents;

import app.Window;

public class AgentYoung extends Agent {
	public AgentYoung() {
		super(AgentType.YOUNG);
	}

	@Override
	public void interact(Agent bump) {
		if(!immune && Window.infectious) {
			boolean gotInfected = false;
			
			/* chance of being infected by "Infected" agent = 25% */
			if(bump.infected && rnd.nextDouble() > 0.75) {
				gotInfected = true;
			}
			
			/* chance of being infected by "Deadly Infected" agent = 50% */
			if(bump.deadlyInfected && rnd.nextDouble() > 0.5) {
				gotInfected = true;
			}
			
			if(gotInfected) {
				setInfected(true);
			}
		}
	}

	@Override
	public void setInfected(boolean value) {
		infected = value;
		setColor(infected ? AgentColor.INFECTED : AgentColor.YOUNG);
	}
}
