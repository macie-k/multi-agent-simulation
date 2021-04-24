package agents;

import app.Window;

public class AgentElderly extends Agent {

	public AgentElderly() {
		super(AgentType.ELDERLY);
	}

	@Override
	public void interact(Agent bump) {
		if(!immune && !infected && Window.infectious && lastInteraction != bump) {
			boolean gotInfected = false;
			
			/* chance of being infected by "Infected" agent = 45% */
			if(bump.infected && rnd.nextDouble() > 0.55) {
				gotInfected = true;
			}
			
			/* chance of being infected by "Deadly Infected" agent = 70% */
			if(bump.deadlyInfected && rnd.nextDouble() > 0.3) {
				gotInfected = true;
			}
			
			if(gotInfected) {
				setInfected(true);
			}
		}
		throttleInteraction(bump);
	}
		
	@Override
	public int getTimeToDeadlyInfected() {
		return 15;
	}
	
	@Override
	public int getTimeToDie() {
		return 10;
	}
}
