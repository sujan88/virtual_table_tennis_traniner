package video;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;

import com.googlecode.javacv.cpp.avfilter.AVFilterPad.Get_audio_buffer;

/**
 * 
 * @author sujan
 *
 */
public class Calculater {
	

	double[] X_Sin_beta;
	double[] Y_Cos_beta;
	double[] vBall;
	double[] aRed;
	double[] vBlue;
	double[] aBlue;
	double[] gapArray;
	
	double[] velocity;
	
	int count=1;
	
	final static double tableLengthtMeter=2.8;
	final static int screenLengthtpixel=440;
	final static double  DIS_CONVERT;
	 Double distance;
	 
	 static{
		 DIS_CONVERT= tableLengthtMeter/screenLengthtpixel;
	 }
	public Calculater(){
		//DIS_CONVERT= screenheightMeter/screenheightpixel;
	}
	public double[] setVelBall(int [] XBall,int [] YBall,double frametime,int numframe) {
		int deltaX = 0;
		int deltaY = 0;

		velocity = new double[numframe];

		int arrayCount;
		double time=frametime;
		frametime=time;
		velocity[0]=0;
	
		 for (arrayCount = 1; arrayCount < numframe; arrayCount++) {

		

			deltaX = Math.abs(XBall[arrayCount] - XBall[arrayCount - 1]);
			deltaY = Math.abs(YBall[arrayCount] - YBall[arrayCount - 1]);
			
			if(deltaX ==0 & deltaY==0){ velocity[arrayCount]=velocity[arrayCount-1]; continue;}
			if(XBall[arrayCount]==0&YBall[arrayCount]==0){++count; continue;}
			

			int ZZ = deltaX * deltaX + deltaY * deltaY;
			double dist = Math.sqrt(ZZ);

			DecimalFormat decim = new DecimalFormat("0.00");
		   
		
			if(count>1)time=frametime*count;
			 
			distance = (dist / time)*DIS_CONVERT;
			velocity[arrayCount] =Double.parseDouble(decim.format(distance));
			count=1;
			time=frametime;
		}
		 return velocity;
	}

	
	

	
	
	
	public void calculateBeta(ArrayList<BufferedImage> a,ArrayList<Integer> untrackFramesBlue,int []XRed, int []YRed){
		double _X_Sin_beta=0;
		double _Y_Cos_beta=0;
		int arrayCount;
		X_Sin_beta=new double[a.size()];
		Y_Cos_beta=new double[a.size()];
		outerloop: for (arrayCount = 1; arrayCount < a.size(); arrayCount++) {

			for (int xB : untrackFramesBlue) {

				if (arrayCount == xB) {
					System.out
							.println("********* Reject #Blue# frame due to untracked ********  "
									+ arrayCount);
					continue outerloop;
				}

			}
			
			int X=XRed[arrayCount] - XRed[arrayCount - 1];
			int Y=YRed[arrayCount] - YRed[arrayCount - 1];
			int ZZ = X * X + Y * Y;
			double dist = Math.sqrt(ZZ);

			
			//Xi * Sin (beta)
			if(XRed[arrayCount] < XRed[arrayCount - 1] && YRed[arrayCount] <YRed[arrayCount - 1]){
			_X_Sin_beta=XRed[arrayCount-1]*Y/dist;
			_Y_Cos_beta=YRed[arrayCount - 1]*X/dist;
			}
			
			
			X_Sin_beta[arrayCount ]=_X_Sin_beta;
			Y_Cos_beta[arrayCount ]=_Y_Cos_beta;

		}

	}
	
	public void calGap(ArrayList<BufferedImage> a,int []XRed, int []YRed,int []XBlue, int []YBlue) {
		int gapX, gapY;
		double gap;
		gapArray = new double[a.size()];
		for (int p = 1; p < a.size(); p++) {
			gapX = Math.abs(XRed[p] - XBlue[p]);
			gapY = Math.abs(YRed[p] - YBlue[p]);
			gap = Math.sqrt(gapX * gapX + gapY * gapY);

			gapArray[p] = gap;

		}
	}



	public double[] getVelBall() {
		return vBall;
	}

	


	public double[] getGap() {
		return gapArray;
	}
	
	
}
