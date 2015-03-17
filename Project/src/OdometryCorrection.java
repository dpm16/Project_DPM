import lejos.nxt.ColorSensor;
import lejos.nxt.Sound;

/* 
 * OdometryCorrection.java
 */

public class OdometryCorrection extends Thread {
	
	private static final long CORRECTION_PERIOD = 10;
	private static final double sensorToCenter = 11.5;
		
	private static final double horizontalLineOne = 15.16;
	private static final double horizontalLineTwo = 45.48;
	private static final double horizontalLineThree = 75.80;
	
	
	private static final double verticalLineOne = 15.16;
	private static final double verticalLineTwo = 45.48;
	private static final double verticalLineThree = 75.80;

	private static final int lightThreshold = 520;

	
	private double currentPositionX = 0;
	private double currentPositionY = 0;
	private double theta = 0;
	
	private boolean firstHorizontalLineCrossed = false;
	private boolean firstVerticalLineCrossed = false;
	
	public int lineFilter = 0;
	
	public String debug = " ";
	
	private Odometer odometer;
	
	private Sensors sensors;

	// constructor
	public OdometryCorrection(Odometer odometer, Sensors sensors) {
		this.odometer = odometer;
		this.sensors = sensors;
	}

	// run method (required for Thread)
	public void run() {
		
		while (true) {
			//Correction here
		}
	
	}
}

