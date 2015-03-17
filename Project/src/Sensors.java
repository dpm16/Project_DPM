import java.util.Arrays;

import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.ColorSensor;
import lejos.util.TimerListener;
import lejos.util.Timer;

public class Sensors extends Thread implements TimerListener{


	private static final double LSThreshold = 550;
	
	private UltrasonicSensor left_us;
	private UltrasonicSensor right_us;
	
	private ColorSensor left_ls;
	private ColorSensor right_ls;
	
	private int LeftUSArrayCounter = 0;
	private int RightUSArrayCounter = 0;

	private int LeftLSArrayCounter = 0;
	private int RightLSArrayCounter = 0;
	
	boolean LeftUSArraySet = false;
	boolean RightUSArraySet = false;
	boolean LeftLSArraySet = false;
	boolean RightLSArraySet = false;
	
	private int[] LeftUSDistances = new int[10];
	private int[] RightUSDistances = new int[10];

	private double[] LeftLSValues = new double[10];
	private double[] RightLSValues = new double[10];

	
	private boolean readLeftUS = true;
	
	private int filteredLeftUSDistance;
	private int filteredRightUSDistance;
	
	private double filteredLeftLSValue;
	private double filteredRightLSValue;

	public Sensors(UltrasonicSensor RAW_LEFT_US, UltrasonicSensor RAW_RIGHT_US, ColorSensor RAW_LEFT_LS, ColorSensor RAW_RIGHT_LS, int USDelay){
		this.left_us = RAW_LEFT_US;
		this.right_us = RAW_RIGHT_US;
		
		this.left_ls = RAW_LEFT_LS;
		this.right_ls = RAW_RIGHT_LS;
		

		Timer timer = new Timer(USDelay, this);
		timer.start();
	}

	public void run(){
		while(true){
			if(LeftUSArrayCounter == 9){
				LeftUSArrayCounter = 0;
				LeftUSArraySet = true;
			}
			if(RightUSArrayCounter == 9){
				RightUSArrayCounter = 0;
				RightUSArraySet = true;
			}
			if(LeftLSArrayCounter == 9){
				LeftLSArrayCounter = 0;
				LeftLSArraySet = true;
			}
			if(RightLSArrayCounter == 9){
				RightLSArrayCounter = 0;
				RightLSArraySet = true;
			}
			
			
			if(readLeftUS){
				LeftUSDistances[LeftUSArrayCounter] = left_us.getDistance();
				LeftUSArrayCounter++;
			}
			else{
				RightUSDistances[RightUSArrayCounter] = right_us.getDistance();
				RightUSArrayCounter++;
			}
			
			LeftLSValues[LeftLSArrayCounter] = left_ls.getNormalizedLightValue();
			LeftLSArrayCounter++;
			RightLSValues[RightLSArrayCounter] = right_ls.getNormalizedLightValue();
			RightLSArrayCounter++;
		
		if(isSensorsSet()){
		filterLeftUS();
		filterRightUS();
		filterLeftLS();
		filterRightLS();
		}
		}
	}
	
	private void filterLeftLS(){
		Arrays.sort(this.LeftLSValues);
		double median;
		if (this.LeftLSValues.length % 2 == 0)
		    median = (this.LeftLSValues[this.LeftLSValues.length/2] + this.LeftLSValues[this.LeftLSValues.length/2 - 1])/2;
		else{
		    median = this.LeftLSValues[this.LeftLSValues.length/2];
		}
		
		this.filteredLeftLSValue = median;
	}
	
	private void filterRightLS(){
		Arrays.sort(this.RightLSValues);
		double median;
		if (this.RightLSValues.length % 2 == 0)
		    median = (this.RightLSValues[this.RightLSValues.length/2] + this.RightLSValues[this.RightLSValues.length/2 - 1])/2;
		else{
		    median = this.RightLSValues[this.RightLSValues.length/2];
		}
		
		this.filteredRightLSValue = median;
	}
	
	private void filterLeftUS(){
		double sum = 0;
	    for (int i = 0; i < this.LeftUSDistances.length; i++) {
	        sum += this.LeftUSDistances[i];
	    }
	    this.filteredLeftUSDistance = (int) (sum / this.LeftUSDistances.length);
	}
	
	private void filterRightUS(){
		double sum = 0;
	    for (int i = 0; i < this.RightUSDistances.length; i++) {
	        sum += this.RightUSDistances[i];
	    }
	    this.filteredRightUSDistance = (int) (sum / this.RightUSDistances.length);
	}
	
	public int getLeftUSDistance(){
		return this.filteredLeftUSDistance;
	}
	
	public int getRightUSDistance(){
		return this.filteredRightUSDistance;
	}
	
	public double getLeftLSValue(){
		return this.filteredLeftLSValue;
	}

	public double getRightLSValue(){
		return this.filteredRightLSValue;
	}
	
	public int getUSDistance(){
		return ((this.filteredLeftUSDistance + this.filteredRightUSDistance))/2;
	}
	
	public void ultrasoincSensorWait(){
		try { Thread.sleep(100); } catch (InterruptedException e) {}
	}
	
	public void lightSensorWait(){
		try { Thread.sleep(600); } catch (InterruptedException e) {}
	}
	
	public boolean isLine(){
		if(this.filteredLeftLSValue < LSThreshold){
			return true;
		}
		if(this.filteredRightLSValue < LSThreshold){
			return true;
		}
		
		return false;
	}
	
	public boolean isLeftLSLine(){
		if(this.filteredLeftLSValue < LSThreshold){
			return true;
		}
		return false;
	}
	
	public boolean isRightLSLine(){
		if(this.filteredRightLSValue < LSThreshold){
			return true;
		}
		return false;
	}
	
	public boolean isSensorsSet(){
		return(LeftUSArraySet && RightUSArraySet && LeftLSArraySet && RightLSArraySet);
	}
	
	@Override
	public void timedOut() {
		readLeftUS = !readLeftUS;
	}
	
	
}
