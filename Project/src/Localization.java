import lejos.nxt.LCD;
import lejos.nxt.Sound;


public class Localization extends Thread {
		
	private Odometer odometer;
	
	public static double ROTATION_SPEED = 15;
	public static double FORWARD_SPEED = 10;

	private static final int FALLING_EDGE_DISTANCE = 50;
	
	private TwoWheeledRobot robot;
	private Sensors sensors;
	
	private double distance;
	private double lastAngle;
	
	
	
	public Localization(Odometer odometer, Sensors sensors, TwoWheeledRobot robot){
		this.odometer = odometer;
		this.sensors = sensors;
		this.robot = robot;
	}
	
	public void run(){
		USLocalization();
		LSLocalization();
	}
	
	public void USLocalization() {
		//introduces the angles needed to determine angle rotation to turn the robot perfectly north
		double angleA, angleB, deviationAngle;
		
		
		//if the robot begins with seeing a wall in close range (walls are in close range to the ultrasonic sensor)
		while(distance < FALLING_EDGE_DISTANCE){ 
			// rotate the robot until it sees no wall
			robot.setRotationSpeed(ROTATION_SPEED);
		   distance = sensors.getUSDistance();
						   
		  }	
					
			sensors.ultrasoincSensorWait();  //Do not poll the ultrasonic sensor
					
			
			//if the robot is at a point where it is not facing a wall (walls are not in close range to the ultrasonic sensor)
			// keep rotating until the robot sees a wall, then latch the angle seen at that point
			while(distance >= FALLING_EDGE_DISTANCE) {
				robot.setRotationSpeed(ROTATION_SPEED);
				distance = sensors.getRightUSDistance();
				lastAngle = odometer.getTheta(); //continually obtaining angle readings 
			}
			robot.stop(); //stop robot in order to obtain angle of current position
			angleB = (odometer.getTheta() + lastAngle)/2; //calculates the average between the angle read before and after the robot recognizes the wall
			
			// switch direction and wait until it sees no wall
			while(distance < FALLING_EDGE_DISTANCE){ 
				   robot.setRotationSpeed(-ROTATION_SPEED);
				   distance = sensors.getUSDistance();
			  }
			sensors.ultrasoincSensorWait();

			// keep rotating until the robot sees a wall, then latch the angle
			while(distance >= FALLING_EDGE_DISTANCE) {
				robot.setRotationSpeed(-ROTATION_SPEED);
				distance = sensors.getLeftUSDistance();  //obtaining distance values
				lastAngle = odometer.getTheta(); //obtaining angle readings
				
			}
			 
			robot.stop(); //stop robot in order to obtain angle of current position
			
			angleA = (odometer.getTheta() + lastAngle)/2;  //calculates the average between the angle read before and after the robot recognizes the wall
		
			
			// angleA is clockwise from angleB, so assume the average of the
			// angles to the right of angleB is 45 degrees past 'north'
			 
			//calculations to determine the angle of rotation needed to make the robot face north
			if(angleA < angleB){                          
				  deviationAngle = 225 - ((angleA + angleB) / 2); 
			  }
			  else{
				  deviationAngle = 45 - ((angleA + angleB) / 2);  
			  }
			
			// update the odometer position 
			 double measuredTheta = (odometer.getTheta() + deviationAngle); //updating the angle of rotation that is needed
				//update robot's postion

			 		odometer.setPosition(new double [] {0.0, 0.0, measuredTheta}, new boolean [] {true, true, true});
					//rotate the robot to face north based on calculated angle of rotation
			 		robot.rotate(-measuredTheta);
					
		}
	
	public void LSLocalization() {
		
		
		
		while(sensors.isLine() == false){
			robot.setForwardSpeed(FORWARD_SPEED);
		}
		robot.stop();
		lockOntoLine();
		
		robot.rotate(90);
		
		while(sensors.isLine() == false){
			robot.setForwardSpeed(FORWARD_SPEED);
		}
		
		robot.stop();
		lockOntoLine();
		
		//updating odometer's position 
 		odometer.setPosition(new double [] {15.16, 15.16, 90}, new boolean [] {true, true, true});

	}
	
	private void lockOntoLine(){
		robot.stop();
		
		if(sensors.isLeftLSLine()){
			while(sensors.isRightLSLine() == false) {
				robot.setRotationSpeed(ROTATION_SPEED);
			}
			robot.stop();
		}
		
		if(sensors.isRightLSLine()){
			while(sensors.isLeftLSLine() == false) {
				robot.setRotationSpeed(ROTATION_SPEED);
			}
			robot.stop();
		}
	}

}

