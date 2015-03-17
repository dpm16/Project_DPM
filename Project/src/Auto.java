import lejos.nxt.*;

public class Auto {

	private static final int SWITCH_US_DELAY = 50;
	
	private static final double LEFT_WHEEL_RADIUS = 2.11;
	private static final double RIGHT_WHEEL_RADIUS = 2.11;
	private static final double WHEEL_WIDTH = 16;
		
	private static final MotorPort LEFT_MOTOR_SENSOR_PORT = MotorPort.B;
	private static final MotorPort RIGHT_MOTOR_SENSOR_PORT = MotorPort.C;
	private static final MotorPort LAUNCHER_MOTOR_SENSOR_PORT = MotorPort.A;

	private static final SensorPort LEFT_ULTRASONIC_SENSOR_PORT = SensorPort.S2;
	private static final SensorPort RIGHT_ULTRASONIC_SENSOR_PORT = SensorPort.S3;
	private static final SensorPort LEFT_LIGHT_SENSOR_PORT = SensorPort.S1;
	private static final SensorPort RIGHT_LIGHT_SENSOR_PORT = SensorPort.S4;
	
	private static final NXTRegulatedMotor LEFT_MOTOR = new NXTRegulatedMotor(LEFT_MOTOR_SENSOR_PORT);
	private static final NXTRegulatedMotor RIGHT_MOTOR = new NXTRegulatedMotor(RIGHT_MOTOR_SENSOR_PORT);
	private static final NXTRegulatedMotor LAUNCHER_MOTOR = new NXTRegulatedMotor(LAUNCHER_MOTOR_SENSOR_PORT);
	
	private static final UltrasonicSensor RAW_LEFT_US = new UltrasonicSensor(LEFT_ULTRASONIC_SENSOR_PORT);
	private static final UltrasonicSensor RAW_RIGHT_US = new UltrasonicSensor(RIGHT_ULTRASONIC_SENSOR_PORT);
	
	private static final ColorSensor RAW_LEFT_LS = new ColorSensor(LEFT_LIGHT_SENSOR_PORT);
	private static final ColorSensor RAW_RIGHT_LS = new ColorSensor(RIGHT_LIGHT_SENSOR_PORT);


	public static void main(String[] args) {
		int buttonChoice;
		
		RAW_LEFT_LS.setFloodlight(true);
		RAW_RIGHT_LS.setFloodlight(true);
		
		Sensors sensors = new Sensors(RAW_LEFT_US, RAW_RIGHT_US, RAW_LEFT_LS, RAW_RIGHT_LS, SWITCH_US_DELAY);
		TwoWheeledRobot twoWheeledRobot = new TwoWheeledRobot(LEFT_MOTOR, RIGHT_MOTOR, WHEEL_WIDTH, LEFT_WHEEL_RADIUS, RIGHT_WHEEL_RADIUS);
		Odometer odometer = new Odometer();
		OdometryDisplay odometryDisplay = new OdometryDisplay(odometer, sensors);
		Navigation navigation = new Navigation(odometer);
		Localization localization = new Localization(odometer, sensors, twoWheeledRobot);
		
	
		do {
			// clear the display
			LCD.clear();

			LCD.drawString("       DPM       ", 0, 0);
			LCD.drawString("     Group 11    ", 0, 1);
			LCD.drawString("      V.0.1      ", 0, 2);
			LCD.drawString("                 ", 0, 3);
			LCD.drawString("   Press Enter   ", 0, 4);
			
			buttonChoice = Button.waitForAnyPress();
		}
		while (buttonChoice != Button.ID_ENTER);
		
		LCD.clear();
		
		sensors.start();
		odometer.start();
		odometryDisplay.start();
		
		//localization.start();
		navigation.travelTo(0, 60);
		navigation.travelTo(60, 60);
		navigation.travelTo(60, 0);
		navigation.travelTo(0, 0);
		navigation.turnTo(0);
		while (Button.waitForAnyPress() != Button.ID_ESCAPE);
		System.exit(0);
	}
	
	


	
}
