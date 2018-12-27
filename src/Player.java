/*
Defines the player
*/
public class Player extends Sprite {
	public static final int STATE_NORMAL = 0;
	public static final int STATE_CRASHED = 1;
	private int state;

	// values used to calculate player positioning and update movement
	private double velocity_X = 0;
	private double velocity_Y = 0;
	private double acceleration_x = 0;
	private double acceleration_y = 0;
	private double x = 0;
	private double y = 0;
	private double angle = 0;

	// constants used throughout calculations
	private static final double ROTATING = .5;
	private static final double PLAYER_SPEED = 1;
	private double GRAVITY = 0.3;

  	private AnimatedSprite moveLeft;
  	private AnimatedSprite moveRight;

  	public Player(AnimatedSprite left, AnimatedSprite right) {
    	super(right);
        this.moveLeft = left;
        this.moveRight = right;
        setState(0);
  	}

	//uses user input to update player movement, positioning, and state, input represents movement types
    public void update(long elapsedTime, int input) {
		//update player angle based on left and right input
		if (input == 3) {
			angle -= ROTATING;
		} else if (input == 4) {
			angle += ROTATING;
		}
		if (angle > 90) {
			angle -= 15;
		} else if (angle < -90) {
			angle += 15;
		}
		//get both X and Y direction of player from angle
		float directionX = (float) Math.cos((angle * Math.PI)/180);
		float directionY = (float) Math.sin((angle * Math.PI)/180);
		//update player position based on up and down input and current direction
		if (input == 0) { }
		if (input == 1) {
			y -= directionY * PLAYER_SPEED;
			x += directionX * PLAYER_SPEED;
		}
		if (input == 2) {
			y += directionY * PLAYER_SPEED;
			x += directionX * PLAYER_SPEED;
		}
		//always modify Y position by gravity constant
		y += GRAVITY;
    }

	//used to stop updating player position after collision
    public void stop() {
    	velocity_X = 0;
    	velocity_Y = 0;
    	acceleration_x = 0;
    	acceleration_y = 0;
    	angle = 0;
    }

	public void setAccelerationX(double aX) {
		this.acceleration_x = aX;
	}

	public void setAccelerationY(double aY) {
		this.acceleration_y = aY;
	}

	public double getAccelerationX() {
		return acceleration_x;
	}

	public double getAccelerationY() {
		return acceleration_y;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public void setVelocityX(double vX) {
		this.velocity_X = vX;
	}

	public void setVelocityY(double vY) {
		this.velocity_Y = vY;
	}

	public double getVelocityX() {
		return velocity_X;
	}

	public double getVelocityY() {
		return velocity_Y;
	}

	public double getY() {
		return y;
	}

	public double getX() {
		return x;
	}

	public void setX(double d) {
		this.x = d;
	}

	public void setY(double y) {
		this.y = y;
	}

    public void setState(int state) {
    	this.state = state;
    }

    public int getState() {
    	return state;
    }

}
