package agents;

import java.util.ArrayList;

import javafx.scene.paint.Color;

public interface Agentable {
	public void move();
	public void detectBump(ArrayList<Agent> agents);
	public abstract void interact(Agent bump);
	public void throttleInteraction(Agent bump);
	public abstract void setInfected(boolean value);
	public void setDeadlyInfected();
	public void setColor(Color value);
	public void setImmune(boolean value);
	public void kill();
	public boolean isInfected();
	public boolean isDeadlyInfected();
	public boolean isImmune();
	public AgentType getType();
	public boolean isDead();
}
