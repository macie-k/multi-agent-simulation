package agents;

import app.Window;

public class AgentDoctor extends Agent {

	public AgentDoctor() {
		super(AgentType.DOCTOR);
	}

	@Override
	public void interact(Agent bump) {
		if(bump.infected && !bump.deadlyInfected && !infected) {
			bump.setInfected(false);
			bump.setImmune(true);
			bump.deadlyInfectedTimer.stop();
		}				
		
		if(!immune && !infected && lastInteraction != bump && Window.infectious) {
			
			/* chance of being infected by "Deadly Infected" agent = 20% */
			if(bump.deadlyInfected && rnd.nextDouble() > 0.8) {
				setInfected(true);
			}
		}
		
		throttleInteraction(bump);
	}
	
	@Override
	public int getTimeToDeadlyInfected() {
		return 25;
	}
	
	@Override
	public int getTimeToDie() {
		return 15;
	}
}
