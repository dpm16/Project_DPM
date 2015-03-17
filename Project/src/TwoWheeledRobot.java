import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;

public class TwoWheeledRobot{
	public static final double DEFAULT_LEFT_RADIUS = 2.21;
	public static final double DEFAULT_RIGHT_RADIUS = 2.21;
	public static final double DEFAULT_WIDTH = 16.35;
	private NXTRegulatedMotor leftMotor, rightMotor;
	private double leftRadius, rightRadius, width;
	private double forwardSpeed, rotationSpeed;

	public TwoWheeledRobot(NXTRegulatedMotor leftMotor,
						   NXTRegulatedMotor rightMotor,
						   double width,
						   double leftRadius,
						   double rightRadius) {
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		this.leftRadius = leftRadius;
		this.rightRadius = rightRadius;
		this.width = width;
		

		
	}
	
	public TwoWheeledRobot(NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor) {
		this(leftMotor, rightMotor, DEFAULT_WIDTH, DEFAULT_LEFT_RADIUS, DEFAULT_RIGHT_RADIUS);
	}
	
	public TwoWheeledRobot(NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor, double width) {
		this(leftMotor, rightMotor, width, DEFAULT_LEFT_RADIUS, DEFAULT_RIGHT_RADIUS);
	}
	
	// accessors
	public double getDisplacement() {
		return (leftMotor.getTachoCount() * leftRadius +
				rightMotor.getTachoCount() * rightRadius) *
				Math.PI / 360.0;
	}
	
	public double getHeading() {
		return (leftMotor.getTachoCount() * leftRadius -
				rightMotor.getTachoCount() * rightRadius) / width;
	}
	
	public void getDisplacementAndHeading(double [] data) {
		int leftTacho, rightTacho;
		leftTacho = leftMotor.getTachoCount();
		rightTacho = rightMotor.getTachoCount();
		
		data[0] = (leftTacho * leftRadius + rightTacho * rightRadius) *	Math.PI / 360.0;
		data[1] = (leftTacho * leftRadius - rightTacho * rightRadius) / width;
	}
	
	// mutators
	public void setForwardSpeed(double speed) {
		forwardSpeed = speed;
		setSpeeds(forwardSpeed,0);
	}
	
	public void setRotationSpeed(double speed) {
		rotationSpeed = speed;
		setSpeeds(0, rotationSpeed);
	}
	
	public void setSpeeds(double forwardSpeed, double rotationalSpeed) {
		double leftSpeed, rightSpeed;

		this.forwardSpeed = forwardSpeed;
		this.rotationSpeed = rotationalSpeed; 

		leftSpeed = (forwardSpeed + rotationalSpeed * width * Math.PI / 360.0) *
				180.0 / (leftRadius * Math.PI);
		rightSpeed = (forwardSpeed - rotationalSpeed * width * Math.PI / 360.0) *
				180.0 / (rightRadius * Math.PI);

		// set motor directions
		if (leftSpeed > 0.0)
			leftMotor.forward();
		else {
			leftMotor.backward();
			leftSpeed = -leftSpeed;
		}
		
		if (rightSpeed > 0.0)
			rightMotor.forward();
		else {
			rightMotor.backward();
			rightSpeed = -rightSpeed;
		}
		
		// set motor speeds
		if (leftSpeed > 900.0)
			leftMotor.setSpeed(900);
		else
			leftMotor.setSpeed((int)leftSpeed);
		
		if (rightSpeed > 900.0)
			rightMotor.setSpeed(900);
		else
			rightMotor.setSpeed((int)rightSpeed);
	}
	
	//Additional methods taken from lab 1 to make using this class easier
	  
	// This finds the number of rotations of the wheels needed to make the robot go forward a certain distance
		  private  int convertDistance(double radius, double distance) {
		      return (int) ((180.0 * distance) / (Math.PI * radius));
		  } 
		  
		  // This finds the number of rotations of the wheels needed to make the robot rotate a certain angle
		  private  int convertAngle(double radius, double width, double angle) {
		      return convertDistance(radius, Math.PI * width * angle / 360.0);
		  } 
		  
		  
	  //drive forward for a certain distance
	  public void forward(double distance){
		  leftMotor.rotate(convertDistance(leftRadius, distance), true);
		  rightMotor.rotate(convertDistance(rightRadius, distance), false);
	  }
	  
	  //Rotate the robot to a specified angle
	  public void rotate(double angle){
	      leftMotor.rotate(convertAngle(leftRadius, width, angle), true);     
	      rightMotor.rotate(-convertAngle(rightRadius, width, angle), false); 
	  } 
	  
	  // Stop the robot
	  public void stop() {
		 rightMotor.stop();
		 leftMotor.stop();
	   }
	  
	  
}
