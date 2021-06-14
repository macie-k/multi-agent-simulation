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
import utils.Utils;

import static utils.Utils.fadeColors;
import static app.Window.DELTA_SPEED;

public abstract class Agent extends Circle implements Agentable {
	
	public static int RADIUS = 4;		// first assign default value, then possibly change with a slider
	
	protected static int ID = 0;							// custom noise seed for every agent
	private static final int MARGIN = 10;					// spawning margin
	protected static final Random rnd = new Random();
		
	private final OpenSimplexNoise osn;		// noise object
	private double vx;						// x direction & speed 'vector'
	private double vy;						// y direction & speed 'vector'
	private double x;						// current center x coordinate
	private double y;						// current center y coordinate
	private double angle;					// current direction angle
	private boolean dead = false;			// flag to count dead agents
	private Color color;
	private final Color originalColor;
	private AnimationTimer interactionTimer;
	private int interactionDelay = 0;
	private boolean fading = false;			// flag to avoid overlapping fading animations

	protected boolean infected = false;			// information if agent is infected
	protected boolean deadlyInfected = false;	// information if agent is deadly infected
	protected boolean immune = false;			// information if agent is immune
	protected Agent lastInteraction = null;		// information about last interacted agent // for throttling
	
	protected int yCounter = 0;
	protected int eCounter = 0;
	protected int dCounter = 0;
	protected int iCounter = 0;
		
	/* timer for changing the agents state to deadly infected after specified time */
	protected AnimationTimer deadlyInfectedTimer = new AnimationTimer() {
		private long lastUpdate = 0;
		private int deadlyInfectedSeconds = 0;
		
		@Override
		public void handle(long now) {
			if(Window.infectious && now - lastUpdate >= 1_000_000_000 / DELTA_SPEED) {
				if(deadlyInfectedSeconds++ > getTimeToDeadlyInfected()) {
					setDeadlyInfected();
					deadlyInfectedTimer.stop();
				}
				lastUpdate = now;
			}
		}
	};
	
	/* timer for killing the agents after specified time */
	protected AnimationTimer deathTimer = new AnimationTimer() {
		private long lastUpdate = 0;
		private int dyingSeconds = 0;
		
		@Override
		public void handle(long now) {
			if(now - lastUpdate >= 1_000_000_000 / DELTA_SPEED) {
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
		this.originalColor = color;
		
		setOpacity(0);
		setCenterX(x);
		setCenterY(y);
		setRadius(RADIUS);
		setFill(color);
		Utils.fadeIn(this, 300, null);
	}
	
	/* method for moving an Agent */
	public void move() {
		final int speed = 300 * DELTA_SPEED;
		final double scale = 0.007;								// smoothen the noise
		angle = (angle + osn.eval(x*scale, y*scale)) % 360;		// calculate angle based on noise value for current (x, y)
		
		final double rads = angle * Math.PI / 180;			// convert to radians
		vx = Math.cos(rads)*speed/60 + rnd.nextDouble();	// calculate x vector
		vy = Math.sin(rads)*speed/60 + rnd.nextDouble();	// calculate y vector
				
		boolean bounce = false;			// store information if the agent will bounce off in the next frame
		final double r = getRadius();
		
		/* math for shortening agent's path perfectly to the edge if about to bounce */
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
				angle = (angle-2*(90 - (360-angle))) % 360 + rnd.nextDouble()*2*DELTA_SPEED - 1;
			}		
			
			if(y - r <= 0 || y + r >= HEIGHT) {
				angle = (360-angle) % 360 + rnd.nextDouble()*2*DELTA_SPEED - 1;
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
					if(interactionDelay++ > 3) {
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
			fadeColors(this, 2000, originalColor, AgentColor.INFECTED);
			color = AgentColor.INFECTED;
			deadlyInfectedTimer.start();
		} else {
			fadeColors(this, 2000, AgentColor.INFECTED, originalColor);
			color = originalColor;
		}
	}
	
	public void setDeadlyInfected() {
		deadlyInfected = true;
		fadeColors(this, 500, AgentColor.INFECTED, AgentColor.DEADLY_INFECTED);
		deathTimer.start();
	}
	
	public void setColor(Color value) {
		setFill(value);
	}
		
	public void setImmune(boolean value) {
		immune = value;
	}
	
	public void setFading(boolean value) {
		fading = value;
	}
	
	public void kill() {
		dead = true;
	}
	
	public Color getColor() {
		return color;
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
		
	public boolean isFading() {
		return fading;
	}
	
	public abstract int getTimeToDie();
	
	public abstract int getTimeToDeadlyInfected();
}
