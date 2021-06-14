package agents;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
/**
 * 
 * @author MACIEJ KAèMIERCZYK
 * @author JANUSZ IGNASZAK
 *
 */
public interface Agentable {
	public void move();
	public void detectBump(ArrayList<Agent> agents);
	public abstract void interact(Agent bump);
	public void throttleInteraction(Agent bump);
	public abstract void setInfected(boolean value);
	public void setDeadlyInfected();
	public void setColor(Color value);
	public void setImmune(boolean value);
	public void setFading(boolean value);
	public void kill();
	public boolean isInfected();
	public boolean isDeadlyInfected();
	public boolean isImmune();
	public boolean isDead();
	public double getX();
	public double getY();
	public AnimationTimer getDeadlyInfectedTimer();
	public boolean isFading();
	public abstract int getTimeToDie();
	public abstract int getTimeToDeadlyInfected();
}
