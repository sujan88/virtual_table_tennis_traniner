package video;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_core.cvInRangeS;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2HSV;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_GAUSSIAN;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvDilate;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvErode;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvGetCentralMoment;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvGetSpatialMoment;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvMoments;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvSmooth;

import java.nio.ByteBuffer;
import java.text.DecimalFormat;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_imgproc.CvMoments;

public class TTUtil {
	
	
	
	static String[] list= { "Player win by executing a shot",
		"Player win by missing the shot",
		"Player lose by executing a shot", 
		"Player lose by missing the shot", 
		"NO Points" };
	final static double tableLengthtMeter=2.8;
	final static int screenLengthtpixel=440;
	final static double   DIS_CONVERT= tableLengthtMeter/screenLengthtpixel;
	
	
	
	public TTUtil(){}
	
	public static IplImage rectangle(IplImage result,int s_X,int s_Y,int e_X,int e_Y) {
		//IplImage img = result;
		ByteBuffer buffer = result.getByteBuffer();

		for (int y1 = s_Y; y1 < e_Y; y1++) {
			for (int x1 = s_X; x1 < e_X; x1++) {
				int index = y1 * result.widthStep() + x1 * result.nChannels();

				buffer.put(index, (byte) 0);

			}
		}
		return result;
	}

	
	
	public static int[] getMoment(IplImage imgThreat) {

		double mom10, mom01, area;
		int[] pos = new int[2];

		CvMoments moments = new CvMoments();
		cvMoments(imgThreat, moments, 1);
		mom10 = cvGetSpatialMoment(moments, 1, 0);
		mom01 = cvGetSpatialMoment(moments, 0, 1);
		area = cvGetCentralMoment(moments, 0, 0);

		pos[0] = (int) (mom10 / area);
		pos[1] = (int) (mom01 / area);

		return pos;

	}

/*	public static void pauseTop(){
		TTTopVideo.running=false;
		TTSideVideo.running=true;
	}
	
	public static void pauseSide(){
		TTTopVideo.running=true;
		TTSideVideo.running=false;
	}*/
	public static IplImage getThresholdImage(IplImage orgImg, CvScalar min,
			CvScalar max) {
		IplImage hsv_frame = cvCreateImage(cvGetSize(orgImg), IPL_DEPTH_8U, 3);
		IplImage imgThreshold = cvCreateImage(cvGetSize(orgImg), 8, 1);
		
		
				
		cvCvtColor(orgImg, hsv_frame, CV_BGR2HSV);
		cvInRangeS(hsv_frame, min, max, imgThreshold);
		
		
		cvDilate(imgThreshold, imgThreshold, null, 4);
		

		// cvSmooth(imgThreshold, imgThreshold, CV_GAUSSIAN, 15);
		 cvErode(imgThreshold,imgThreshold,null,1) ;
		// cvDilate(imgThreshold, imgThreshold, null,300);
		// cvErode(imgThreshold, imgThreshold, null, 1);

		return imgThreshold;

	}

	public static int showMessageBox() throws InterruptedException{
		
		
	
			
		 JComboBox jcb = new JComboBox(list);
		
	
		 JOptionPane pane = new JOptionPane( jcb,	JOptionPane.INFORMATION_MESSAGE);
		
	   
		final JDialog d = pane.createDialog((JFrame)null, "select won player");
	    d.setLocation(10,10);
	    d.setVisible(true);
		
		
		int result = jcb.getSelectedIndex();

		
	
	return result;	
	}
	
	
	public static double velocityBall(int preX, int preY, int X,int Y,double frametime) {
		double vel = 0;
		

		DecimalFormat decim = new DecimalFormat("0.00");
	
		double distance = (Math.sqrt(Math.pow(Math.abs(X-preX), 2)  + Math.pow(Math.abs(Y-preY), 2))  / frametime)*DIS_CONVERT;
		vel=Double.parseDouble(decim.format(distance));
		return vel;

	}
}
