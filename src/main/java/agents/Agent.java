package agents;

import app.OpenSimplexNoise;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static app.Window.WIDTH;

import java.util.ArrayList;
import java.util.Random;

import static app.Window.HEIGHT;

public abstract class Agent extends Circle implements Agentable {
	
	private static int counter = 0;
	private static final int MARGIN = 350;
	private final OpenSimplexNoise osn;
	
	private boolean dead;
	private double vx;
	private double vy;
	protected AgentType type;
	
	protected final int ID;
	protected final Color color;
	protected double x;
	protected double y;
	protected double angle;
	protected boolean immune = false;
	protected static final Random rnd = new Random();
		
	/* constructor for randomly placing agent */
	public Agent(AgentType type) {
		this(rnd.nextInt(WIDTH-2*MARGIN) + MARGIN, rnd.nextInt(HEIGHT-2*MARGIN) + MARGIN, -1, 360, type);
	}
		
	/* default constructor */
	public Agent(double x, double y, int ID, double angle, AgentType type) {
		this.x = x;
		this.y = y;
		this.ID = ID == -1 ? counter++ : ID;
		this.osn = new OpenSimplexNoise(this.ID);
		this.angle = angle == 360 ? rnd.nextInt(360) : angle;
		this.color = type.color;
		this.type = type;
						
		setCenterX(x);
		setCenterY(y);
		setRadius(5);
		setFill(color);
	}
			
	public void move(int speed) {
		final double scale = 0.007;
		angle = (angle + osn.eval(x*scale, y*scale)) % 360;

		final double rads = angle * Math.PI / 180;		
		vx = Math.cos(rads)*speed/60 + rnd.nextDouble();
		vy = Math.sin(rads)*speed/60 + rnd.nextDouble();
				
		boolean bounce = false;
		final double r = getRadius();
		
		if(x + r + vx >= WIDTH) {
			vx = WIDTH - x - r;
			bounce = true;
		}
		if(x - r + vx <= 0) {
			vx = r - x;
			bounce = true;
		}
		if(y + r + vy >= HEIGHT) {
			vy = HEIGHT - y - r;
			bounce = true;
		}
		if(y - r + vy <= 0) {					
			vy = r - y;
			bounce = true;
		}
		
		x += vx;
		y += vy;
		setCenterX(x);
		setCenterY(y);
		
		if(bounce) {
			if(x + r >= WIDTH || x - r <= 0) {
				angle = (angle-2*(90 - (360-angle))) % 360 + rnd.nextDouble()*2 - 1;
			}		
			
			if(y - r <= 0 || y + r >= HEIGHT) {
				angle = (360-angle) % 360 + rnd.nextDouble()*2 - 1;
			}
		}
	}
					
	public abstract void interact(Agent bump, ArrayList<Agent> toAdd, ArrayList<Agent> toRemove);
	
	public void detectBump(ArrayList<Agent> agents, ArrayList<Agent> toRemove, ArrayList<Agent> toAdd) {
		if(type == AgentType.DEADLY) return;
		
		for(Agent bump : agents) {
			if(bump != this) {
				if(Math.sqrt(Math.pow((x - bump.x), 2) + Math.pow(y - bump.y, 2)) <= getRadius()*2) {
					interact(bump, toAdd, toRemove);
				}
			}
		}
	}
	
	public void setImmune(boolean value) {
		immune = value;
	}
	
	public void kill() {
		dead = true;
	}
	
	public boolean isImmune() {
		return immune;
	}
		
	public AgentType getType() {
		return type;
	}
			
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public boolean isDead() {
		return dead;
	}	
}
