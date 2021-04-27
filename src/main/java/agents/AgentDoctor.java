package agents;

import app.Window;

public class AgentDoctor extends Agent {

	public AgentDoctor() {
		super(AgentColor.DOCTOR);
	}

	@Override
	public void interact(Agent bump) {
		if(bump.isInfected() && !bump.isDeadlyInfected() && !infected) {
			bump.setInfected(false);
			bump.setImmune(true);
			bump.getDeadlyInfectedTimer().stop();
		}				
		
		if(!immune && !infected && lastInteraction != bump && Window.infectious) {
			/* chance of being infected by "Deadly Infected" agent = 20% */
			if(bump.isDeadlyInfected() && rnd.nextDouble() > 0.8) {
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
