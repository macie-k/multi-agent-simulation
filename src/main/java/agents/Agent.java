package agents;

import static app.Window.HEIGHT;
import static app.Window.WIDTH;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import utils.OpenSimplexNoise;

public abstract class Agent extends Circle {
	
	private static int ID = 0;
	private static final int MARGIN = 350;
	private static final int RADIUS = 5;
	
	private final OpenSimplexNoise osn;
	private AgentType type;
	private double vx;
	private double vy;
	private Color color;
	private double x;
	private double y;
	private double angle;
	private boolean dead = false;
	
	protected static final Random rnd = new Random();
	protected boolean infected = false;
	protected boolean deadlyInfected = false;
	protected boolean immune = false;
	
	public Agent(AgentType type) {
		this(rnd.nextInt(WIDTH-2*MARGIN) + MARGIN, rnd.nextInt(HEIGHT-2*MARGIN) + MARGIN, type);
	}
	
	public Agent(double x, double y, AgentType type) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.color = type.color;
		this.osn = new OpenSimplexNoise(ID++);
		this.angle = rnd.nextInt(360);
		
		setCenterX(x);
		setCenterY(y);
		setRadius(RADIUS);
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

	public void detectBump(ArrayList<Agent> agents) {
		if(isDeadlyInfected()) return;
		
		for(Agent bump : agents) {
			if(bump != this) {
				if(Math.sqrt(Math.pow((x - bump.x), 2) + Math.pow(y - bump.y, 2)) <= getRadius()*2) {
					interact(bump);
					break;
				}
			}
		}
	}
		
	protected abstract void interact(Agent bump);
	
	public abstract void setInfected(boolean value);
	
	public void setDeadlyInfected() {
		deadlyInfected = true;
		setColor(AgentColor.DEADLY);
	}
	
	public void setColor(Color value) {
		color = value;
		setFill(color);
	}
	
	public void setImmune(boolean value) {
		immune = value;
	}
	
	public void kill() {
		dead = true;
	}
	
	public boolean isInfected() {
		return infected;
	}
	
	public boolean isDeadlyInfected() {
		return deadlyInfected;
	}
	
	public boolean isImmune() {
		return immune;
	}
	
	public AgentType getType() {
		return type;
	}
		
	public boolean isDead() {
		return dead;
	}	
}
