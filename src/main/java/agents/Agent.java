package agents;

import static app.Window.HEIGHT;
import static app.Window.WIDTH;

import java.util.ArrayList;
import java.util.Random;

import app.Window;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import utils.OpenSimplexNoise;

public abstract class Agent extends Circle implements Agentable {
	
	private static int ID = 0;							// custom noise seed for every agent
	private static final int MARGIN = 10;				// spawning margin
	private static final int RADIUS = 3;				// agent's shape radius
	protected static final Random rnd = new Random();
	
	private final OpenSimplexNoise osn;		// noise object
	private final int speed = Window.speed;	// agent's speed // soon to be a program's parameter
	private double vx;						// x direction & speed 'vector'
	private double vy;						// y direction & speed 'vector'
	private double x;						// current center x coordinate
	private double y;						// current center y coordinate
	private double angle;					// current direction angle
	private boolean dead = false;			// flag to count dead agents
	private Color color;
	private AnimationTimer interactionTimer;
	private int interactionDelay = 0;
	
	protected boolean infected = false;			// information if agent is infected
	protected boolean deadlyInfected = false;	// information if agent is deadly infected
	protected boolean immune = false;			// information if agent is immune
	protected Agent lastInteraction = null;
		
	protected AnimationTimer deadlyInfectedTimer = new AnimationTimer() {
		private long lastUpdate = 0;
		private int deadlyInfectedSeconds = 0;
		
		@Override
		public void handle(long now) {
			if(now - lastUpdate >= 1_000_000_000) {
				if(deadlyInfectedSeconds++ > getTimeToDeadlyInfected()) {
					setDeadlyInfected();
					deadlyInfectedTimer.stop();
				}
				lastUpdate = now;
			}
		}
	};
	protected AnimationTimer deathTimer = new AnimationTimer() {
		private long lastUpdate = 0;
		private int dyingSeconds = 0;
		
		@Override
		public void handle(long now) {
			if(now - lastUpdate >= 1_000_000_000) {
				if(dyingSeconds++ > getTimeToDie()) {
					kill();
					deathTimer.stop();
				}
				lastUpdate = now;
			}
		}
	};
	
	/* constructor for randomly placing an Agent withing the specified margin */
	public Agent(Color color) {
		this(rnd.nextInt(WIDTH-2*MARGIN) + MARGIN, rnd.nextInt(HEIGHT-2*MARGIN) + MARGIN, color);
	}
	
	/* main constructor */
	public Agent(double x, double y, Color color) {
		this.x = x;
		this.y = y;
		this.osn = new OpenSimplexNoise(ID++);
		this.angle = rnd.nextInt(360);
		this.color = color;
		
		setCenterX(x);
		setCenterY(y);
		setRadius(RADIUS);
		setFill(color);
	}
	
	/* method for moving an Agent */
	public void move() {
		final double scale = 0.007;								// smoothen the noise
		angle = (angle + osn.eval(x*scale, y*scale)) % 360;		// calculate angle based on noise value for current (x, y)

		final double rads = angle * Math.PI / 180;			// convert to radians
		vx = Math.cos(rads)*speed/60 + rnd.nextDouble();	// calculate x vector
		vy = Math.sin(rads)*speed/60 + rnd.nextDouble();	// calculate y vector
				
		boolean bounce = false;			// store information if the agent will bounce off in the next frame
		final double r = getRadius();
		
		/* math for shortening agent's path perfectly to the edge */
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
		
		/* apply calculated vectors & set new x, y */
		x += vx;
		y += vy;
		setCenterX(x);
		setCenterY(y);
		
		/* math for setting new, 'bounced' angle for the next frame */
		if(bounce) {
			if(x + r >= WIDTH || x - r <= 0) {
				angle = (angle-2*(90 - (360-angle))) % 360 + rnd.nextDouble()*2 - 1;
			}		
			
			if(y - r <= 0 || y + r >= HEIGHT) {
				angle = (360-angle) % 360 + rnd.nextDouble()*2 - 1;
			}
		}
	}

	/* method for detecting if the agent collided with anyone during the current frame */
	public void detectBump(ArrayList<Agent> agents) {
		if(isDeadlyInfected() || isDead()) return;
		
		for(Agent bump : agents) {
			if(bump != this) {
				if(Math.sqrt(Math.pow((x - bump.getX()), 2) + Math.pow(y - bump.getY(), 2)) <= getRadius()*2) {
					interact(bump);
					break;
				}
			}
		}
	}
		
	public abstract void interact(Agent bump);
		
	/* method for throttling interactions */
	public void throttleInteraction(Agent bump) {
		lastInteraction = bump;
		
		interactionTimer = new AnimationTimer() {
			private long lastUpdate = 0;

			@Override
			public void handle(long now) {
				if(now - lastUpdate >= 1_000_000_000) {
					if(interactionDelay++ > 2) {
						lastInteraction = null;
						interactionDelay = 0;
						interactionTimer.stop();
					}
					lastUpdate = now;
				}
			}
		}; interactionTimer.start();
	}
	
	public void setInfected(boolean value) {
		infected = value;
		if(infected) {
			setColor(AgentColor.INFECTED);
			deadlyInfectedTimer.start();
		} else {
			setColor(color);
		}
	}
	
	public void setDeadlyInfected() {
		deadlyInfected = true;
		setColor(AgentColor.DEADLY_INFECTED);
		deathTimer.start();
	}
	
	public void setColor(Color value) {
		setFill(value);
	}
	
	public void setImmune(boolean value) {
		immune = value;
	}
	
	public void kill() {
		setFill(Color.TRANSPARENT);
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
			
	public boolean isDead() {
		return dead;
	}	
	
	public double getX() {
		return getCenterX();
	}
	
	public double getY() {
		return getCenterY();
	}
	
	public AnimationTimer getDeadlyInfectedTimer() {
		return deadlyInfectedTimer;
	}
	
	public abstract int getTimeToDie();
	
	public abstract int getTimeToDeadlyInfected();
}
