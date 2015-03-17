import lejos.nxt.LCD;
import lejos.nxt.*;


public class Navigation {
	// put your navigation code here 
	
	private Odometer odo;
	private double dTheta , currTheta , dX , currX , dY , currY , turnAngle;
	private double [] pos = new double [3];
	private boolean isNavigating;
	private final double wheelRadius = 2.05 , wheelDistance = 14.45;
	private final int motorStraight = 200, motorTurn = 150;
	public Navigation(Odometer odo) {
		this.odo = odo;
	}
	
	public void travelTo(double x, double y) {
		isNavigating = true;
		odo.getPosition(pos,new boolean[] {true,true,true});
		currX = pos[0];
		currY = pos[1];
		dX = x - currX;
		dY = y - currY;
		//Calculate the angle to get from current position to desired position
		//Plus and minus pi to get right angles from arctan
		if(dY < 0 && dX < 0) { 
			turnAngle = Math.atan(dY / dX) + Math.PI;
		}
		else if (dY > 0 && dX < 0) { 
			turnAngle = Math.atan(dY / dX) - Math.PI; 
		}
		else {
			turnAngle = Math.atan(dY / dX);
		}
		isNavigating = true;
		//Turn to face the correct angle
		//The negative side to make it count clockwise and +90 to make the 0 angle at the positive y axis
		turnTo(-(turnAngle*180/Math.PI) + 90);	
		
		Motor.B.setSpeed(motorStraight);
		Motor.C.setSpeed(motorStraight);
		Motor.B.rotate(convertDistance(wheelRadius, Math.sqrt(Math.pow(dX,2) + Math.pow(dY,2))), true);
		Motor.C.rotate(convertDistance(wheelRadius, Math.sqrt(Math.pow(dX,2) + Math.pow(dY,2))), false);
							
		isNavigating = false;
		Motor.B.stop();
		Motor.C.stop();
		
	}
	
	public void turnTo(double theta) {
		isNavigating = true;
		odo.getPosition(pos , new boolean [] {true,true,true});
		currTheta = pos[2];
		dTheta = theta - currTheta;
		// Make sure this is always the shortest angle to turn
		if (dTheta > 180){
			dTheta -= 360;
		}else if (dTheta < -180){
			dTheta += 360;
		}
		//Make the robot turn on point
		Motor.C.setSpeed(motorTurn);
		Motor.B.setSpeed(motorTurn);
		Motor.C.rotate(-convertAngle(wheelRadius, wheelDistance, dTheta), true);
		Motor.B.rotate(convertAngle(wheelRadius, wheelDistance, dTheta), false);
		
		isNavigating = false;
	}
	//Changes distance into wheel angle rotation
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}
	//Changes the robots angle in wheel rotation angle (one wheel rotates forward and the other backwards)
	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
}