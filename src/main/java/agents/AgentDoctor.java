package agents;

public class AgentDoctor extends Agent {

	public AgentDoctor() {
		super(AgentType.DOCTOR);
	}

	@Override
	protected void interact(Agent bump) {
		if(bump.isInfected()) {
			bump.setInfected(false);
			bump.setImmune(true);
		}
	}

	@Override
	public void setInfected(boolean value) {
		infected = value;
		setColor(infected ? AgentColor.INFECTED : AgentColor.DOCTOR);
	}

}
