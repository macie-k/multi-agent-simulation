package agents;

import app.Window;
import javafx.scene.layout.Pane;

public class AgentDoctor extends Agent {
		
	public AgentDoctor() {
		super(AgentColor.DOCTOR);
	}

	@Override
	public void interact(Agent bump) {
		
		if(Window.infectious && !infected) {
			if(bump.isInfected() && !bump.isDeadlyInfected()) {
				bump.setInfected(false);
				bump.setImmune(true);
				bump.getDeadlyInfectedTimer().stop();
			}				
			
			if(!immune && lastInteraction != bump) {
				/* chance of being infected by "Deadly Infected" agent = 20% */
				if(bump.isDeadlyInfected() && rnd.nextDouble() > 0.8) {
					setInfected(true);
				}
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

	@Override
	public void clone(Pane root, int amount) {
		for(int i=0; i<amount; i++) {
			root.getChildren().add(new AgentDoctor());
		}
	}
}
