package agents;

import app.Window;
import javafx.scene.layout.Pane;

public class AgentYoung extends Agent {
			
	public AgentYoung() {
		super(AgentColor.YOUNG);
	}

	@Override
	public void interact(Agent bump) {
		if(!immune && !infected && Window.infectious && lastInteraction != bump) {
			boolean gotInfected = false;
			
			/* chance of being infected by "Infected" agent = 30% */
			if(bump.isInfected() && rnd.nextDouble() > 0.7) {
				gotInfected = true;
			}
			
			/* chance of being infected by "Deadly Infected" agent = 50% */
			if(bump.isDeadlyInfected() && rnd.nextDouble() > 0.5) {
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
		return 20;
	}
	
	@Override
	public int getTimeToDie() {
		return 15;
	}
	
	@Override
	public void clone(Pane root, int amount) {
		for(int i=0; i<amount; i++) {
			root.getChildren().add(new AgentYoung());
		}
	}
}
