package agents;

import java.util.ArrayList;

public interface Agentable {
	public void move(int speed);
	public void detectBump(ArrayList<Agent> agents, ArrayList<Agent> toRemove, ArrayList<Agent> toAdd);
	public abstract void interact(Agent bump, ArrayList<Agent> toAdd, ArrayList<Agent> toRemove);
	public void kill();
	public boolean isImmune();
	public AgentType getType();
	public double getX();
	public double getY();
	public double getAngle();
	public boolean isDead();
}
