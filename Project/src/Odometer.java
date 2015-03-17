import lejos.nxt.Motor;

/*
 * Odometer.java
 */

public class Odometer extends Thread{
	// robot position
	private double x, y, theta;

	// odometer update period, in ms
	private static final long ODOMETER_PERIOD = 25;
	
	// lock object for mutual exclusion
	private Object lock;
	 
	private final double leftRadius = 2.05; //setting the value for the left and right radius of the wheels 
	private final double rightRadius = 2.05;
	private final double wheelWidth = 14.45; //distance between the two wheels
	private int lastLeftTachometer = 0;
	private int lastRightTachometer = 0; 
	
	private double deltaCenterPosition = 0; //initializing distance travelled by the center of the robot
	private double deltaTheta; //initializing the change in angle

	

	
	// default constructor
	public Odometer() {
		x = 0.0;
		y = 0.0;
		theta = 0.0;
		lock = new Object();
	}

	// run method (required for Thread)
	public void run() {
		long updateStart, updateEnd;

		while (true) {
			updateStart = System.currentTimeMillis();
			// put (some of) your odometer code here
			int currentLeftTachometer = Motor.B.getTachoCount(); //gets the tachometers of the left and right wheels
			int currentRightTachometer = Motor.C.getTachoCount();
			
			int deltaLeft = currentLeftTachometer - lastLeftTachometer; //update the change in degrees each of the wheels have turned using the new tachometer values
			int deltaRight = currentRightTachometer - lastRightTachometer;
			
			this.lastLeftTachometer = currentLeftTachometer; //setting the tachometer readings to be the latest tachometer values read
			this.lastRightTachometer = currentRightTachometer;
			
			double leftDistance = (Math.toRadians(deltaLeft)) * leftRadius; //calculating the distance travelled by each wheels in terms of radians
			double rightDistance = (Math.toRadians(deltaRight)) * rightRadius;
			
			deltaCenterPosition = (leftDistance + rightDistance)/2; //calculating the distance travelled by the center of the robot by dividing the two distances by 2
			deltaTheta = (leftDistance - rightDistance)/wheelWidth; //calculating the change in angle
			
			synchronized (lock) {
				// don't use the variables x, y, or theta anywhere but here!
				theta = this.theta + deltaTheta; //updating the angle  by adding the last angle measured with the change in theta
				
				
				//These two if blocks keep the angle in the range (-pi rads, pi rads]
				if(theta <= -Math.PI){
					theta = theta + 2*Math.PI;
				}
				if(theta > Math.PI){
					theta = theta - 2*Math.PI;
				}
				
				double deltaX = deltaCenterPosition*Math.sin(this.theta); //formula calculating the change in x and y travelled
				double deltaY = deltaCenterPosition*Math.cos(this.theta);

				this.x = this.x + deltaX; //updating the x and y readings with the new changes in x and y 
				this.y = this.y + deltaY;
				
			}

			// this ensures that the odometer only runs once every period
			updateEnd = System.currentTimeMillis();
			if (updateEnd - updateStart < ODOMETER_PERIOD) {
				try {
					Thread.sleep(ODOMETER_PERIOD - (updateEnd - updateStart));
				} catch (InterruptedException e) {
					// there is nothing to be done here because it is not
					// expected that the odometer will be interrupted by
					// another thread
				}
			}
		}
	}

	// accessors
	public void getPosition(double[] position, boolean[] update) {
		// ensure that the values don't change while the odometer is running
		synchronized (lock) {
			if (update[0])
				position[0] = x;
			if (update[1])
				position[1] = y;
			if (update[2])
				position[2] = Math.toDegrees(theta);
		}
	}

	public double getX() {
		double result;

		synchronized (lock) {
			result = x;
		}

		return result;
	}

	public double getY() {
		double result;

		synchronized (lock) {
			result = y;
		}

		return result;
	}

	public double getTheta() {
		double result;

		synchronized (lock) {
			result = theta;
		}

		return result;
	}

	// mutators
	public void setPosition(double[] position, boolean[] update) {
		// ensure that the values don't change while the odometer is running
		synchronized (lock) {
			if (update[0])
				x = position[0];
			if (update[1])
				y = position[1];
			if (update[2])
				theta = position[2];
		}
	}

	public void setX(double x) {
		synchronized (lock) {
			this.x = x;
		}
	}

	public void setY(double y) {
		synchronized (lock) {
			this.y = y;
		}
	}

	public void setTheta(double theta) {
		synchronized (lock) {
			this.theta = theta;
		}
	}
}