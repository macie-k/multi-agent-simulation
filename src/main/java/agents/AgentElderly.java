package agents;

import app.Window;
import javafx.scene.layout.Pane;

public class AgentElderly extends Agent {

	public AgentElderly() {
		super(AgentColor.ELDERLY);
	}

	@Override
	public void interact(Agent bump) {
		if(!immune && !infected && Window.infectious && lastInteraction != bump) {
			boolean gotInfected = false;
			
			/* chance of being infected by "Infected" agent = 45% */
			if(bump.isInfected() && rnd.nextDouble() > 0.55) {
				gotInfected = true;
			}
			
			/* chance of being infected by "Deadly Infected" agent = 70% */
			if(bump.isDeadlyInfected() && rnd.nextDouble() > 0.3) {
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
	
	@Override
	public void clone(Pane root, int amount) {
		for(int i=0; i<amount; i++) {
			root.getChildren().add(new AgentElderly());
		}
	}
}
