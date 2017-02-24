/*
 * TT_Tracker.java
 *
 * Created on Sep 15, 2013, 3:53:58 PM
 */
package video;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_core.cvInRangeS;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;
import static com.googlecode.javacv.cpp.opencv_highgui.CV_CAP_PROP_FPS;
import static com.googlecode.javacv.cpp.opencv_highgui.CV_CAP_PROP_FRAME_COUNT;
import static com.googlecode.javacv.cpp.opencv_highgui.CV_CAP_PROP_FRAME_HEIGHT;
import static com.googlecode.javacv.cpp.opencv_highgui.CV_CAP_PROP_FRAME_WIDTH;
import static com.googlecode.javacv.cpp.opencv_highgui.cvCreateFileCapture;
import static com.googlecode.javacv.cpp.opencv_highgui.cvGetCaptureProperty;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvQueryFrame;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2HSV;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvDilate;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvEqualizeHist;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvGetCentralMoment;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvGetSpatialMoment;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvMoments;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


import player.PlayerInfo;

import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui.CvCapture;
import com.googlecode.javacv.cpp.opencv_imgproc.CvMoments;
import com.sun.media.ui.MessageBox;
import javax.swing.JFrame;

import match.Match;
/**
 * 
 * @author sujan
 */
public class TTTopVideo extends  Thread implements
		ActionListener {
	
	FineTune finetune;
	final double CLORIETOJUL=4.184;
	final static double RACKET_MASS = 1.7;
	final static double BALL_MASS = 0.0027;
	ShotRecord lefttoRightShot;
	ShotRecord righttoLeftShot;
	public static boolean running = true;
	boolean captureData ;
	boolean swap = false;
	
	Statement stmt2 = null;
	PreparedStatement stmt = null;
	int vleft, vright, vchest;
	int b = 0;
	static TTTopVideo cot;
	JFrame tela; // tela principal
	boolean click = false;
	boolean painyellow = true;
	static JComboBox jcb;
	String strDate;
	int startX, startY, endX, endY;
	static int x;
	static int y;
	int xRed;
	int yRed;
	static Thread th, th2;
	static Graphics g;
	int i = 0;
	JLabel path2;
	IplImage img;
	CvCapture capture;
	ArrayList<Integer> result_p = new ArrayList<Integer>();
	ArrayList<Integer> untrackFramesYellow = new ArrayList<Integer>();
	int[] XYellow;
	int[] YYellow;
	
	IplImage blank;
	int ii = 0;
	IplImage imgs;
	double frametime;
	//private  CvScalar white_min = cvScalar(100, 55, 200, 0);
	//private	 CvScalar white_max = cvScalar(112, 84, 252, 0); // H: 0 - 180, S: 0 - 255, V: 0 - 255
	
	
	
	private  CvScalar white_min = cvScalar(102, 46, 192, 0);
	private	 CvScalar white_max = cvScalar(111, 100, 250, 0);
	int numFrames;
	static double  TABLE_LENGTH_RATIO ;
	static double TABLE_WIDTH_RATIO ;
	
	private BufferedImage resizedImage;
	CvCapture capture_side;
	TTSideVideo sideVideoFrame;
	public static int rallyResult;
		  
	PlayerInfo playerinfo = null;
	PlayerInfo opponentInfo = null;
	Match match=null;
	boolean isShowedMessageBox=false;
	boolean isleftToRight = true;
	boolean isRightToLeft = true;
	static int rid=0;
	public TTTopVideo() {
	
		this("video/Top/2.wmv", "video/Side/2.wmv");
	}

	public TTTopVideo(String topVideo, String sideVideo) {
		
		playerinfo = PlayerInfo.getInstance();
		opponentInfo= PlayerInfo.getInstance();
		
		try{
		capture = cvCreateFileCapture(topVideo);
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		

		int frameH = (int) cvGetCaptureProperty(capture,
				CV_CAP_PROP_FRAME_HEIGHT);
		int frameW = (int) cvGetCaptureProperty(capture,
				CV_CAP_PROP_FRAME_WIDTH);
		int fps = (int) cvGetCaptureProperty(capture, CV_CAP_PROP_FPS);
		numFrames = (int) cvGetCaptureProperty(capture, CV_CAP_PROP_FRAME_COUNT);
		frametime = (double) 1 / fps;
		System.out.println("Number of rows " + frameH);
		System.out.println("Number of columns " + frameW);
		System.out.println("frames per second " + fps);
		System.out.println("Number of frames " + numFrames);
		

		sideVideoFrame = new TTSideVideo();

		sideVideoFrame.setCapture_side(cvCreateFileCapture(sideVideo));
		sideVideoFrame.init();
		
		initComponents();
		
		fhSide.setText("" + sideVideoFrame.frameH);
	
		fwSide.setText("" + sideVideoFrame.frameW);
		fpsSide.setText("" + sideVideoFrame.fps);
		nofSide.setText("" + sideVideoFrame.numFrames);
		ttSide.setText("" + sideVideoFrame.numFrames*sideVideoFrame.frametime);
		tpfSide.setText("" +sideVideoFrame.frametime);

		fhTop.setText(""+frameH);
		fwTop.setText(""+frameW);
		fpsTop.setText("" + fps);
		nofTop.setText("" + numFrames);
		ttTop.setText("" + numFrames*frametime);
		tpfTop.setText("" +frametime);
		
		sideVideoFrame.setXYellow(new int[sideVideoFrame.numFrames]);
		sideVideoFrame.setYYellow(new int[sideVideoFrame.numFrames]);

		loadVideo();
		g = jLabel2.getGraphics();

		ResultSet rs = null;
		try {
			rs = DB_connection.query("Select * from player",DB_connection.getDbCon().conn);
			playerlistComboBox.addItem("Select player");
			opponentlistComboBox.addItem("Select opponent");
			while (rs.next()) {

				playerlistComboBox.addItem(rs.getString("pid") + "-"
						+ rs.getString("name"));
				opponentlistComboBox.addItem(rs.getString("pid") + "-"
						+ rs.getString("name"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try {
				rs.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}

		handsideComboBox.addItem("Select HandSide");
		handsideComboBox.addItem("RHS");
		handsideComboBox.addItem("LHS");
		
		TABLE_LENGTH_RATIO=(double)408/(jLabel1.getWidth()-15);
		TABLE_WIDTH_RATIO=(double)210/(jLabel1.getHeight()-78);
		
		match=Match.getinstatnce();
		match.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		match.getWinnerComboBox().addItem("Select the Winner");
	
	 ResultSet rs2 = null;
		try {
		Connection	con=DB_connection.getDbCon().conn;
			rs2 = DB_connection.query("Select TOP 1  * from playrecord ORDER BY rid DESC",con);

			rs2.next();
     	rid=rs2.getInt("rid")+1;
     
     	
     	rs2.close();
con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void loadVideo() {

		System.out.println("Loading your Video Top. . . .");
		imgs = cvQueryFrame(capture);
		System.out.println("<<<<<<<<<<<<  Loaded video Top!>>>>>>>>>>>>");

		System.out.println("Loading your Video side. . . .");
		IplImage imgs_side = cvQueryFrame(sideVideoFrame.getCapture_side());
		System.out.println("<<<<<<<<<<  Loaded video side >>>>>>>>>>>>>>");

		XYellow = new int[numFrames];
		YYellow = new int[numFrames];

		IplImage ratio = cvLoadImage("images/TTGRID.jpg");
		resizedImage = resize(imgs.getBufferedImage(), jLabel1.getWidth(),
				jLabel1.getHeight());

		jLabel1.setIcon(new ImageIcon(resizedImage));

		jLabel2.setSize(jLabel1.getWidth(), jLabel1.getHeight());
		resizedImage = resize(ratio.getBufferedImage(), jLabel1.getWidth(),
				jLabel1.getHeight());
		jLabel2.setIcon(new ImageIcon(resizedImage));

		sideVideoFrame.jLabel3.setSize(jLabel1.getWidth(), jLabel1.getHeight());
		resizedImage = resize(imgs_side.getBufferedImage(), jLabel1.getWidth(),
				jLabel1.getHeight());
		sideVideoFrame.jLabel3.setIcon(new ImageIcon(resizedImage));

		sideVideoFrame.jLabel4.setSize(jLabel1.getWidth(), jLabel1.getHeight());
		ratio = cvLoadImage("images/HEIGHT.jpg");
		resizedImage = resize(ratio.getBufferedImage(), jLabel1.getWidth(),
				jLabel1.getHeight());
		sideVideoFrame.jLabel4.setIcon(new ImageIcon(resizedImage));
		
	}

	public void run() {

		play();
		th.stop();
		System.out.println("end****************************************end");
	}

	private void clear() {
		playerinfo = null;
		XYellow = null;
		YYellow = null;
		painyellow = true;
		untrackFramesYellow = null;

		jLabel2.setIcon(new ImageIcon("images/TTGRID.jpg"));
		click = false;
		
		System.gc();

	}


	public void play() {
		
	if(finetune!=null){
		 white_min = cvScalar(FineTune.H, FineTune.S,FineTune.V, 0);
		 white_max = cvScalar(FineTune.H2, FineTune.S2, FineTune.V2, 0); 
	}
		String out="";
		 boolean service_R = true;
		 boolean service_L = false;
		double calorie = 0;
		boolean changeRight=false;
		boolean changeLeft=false;
		
		Graphics gnew=jLabel2.getGraphics();
		try {
			
			TTTopVideo.pauseThread();
			boolean R_after_before_vel = false;
			boolean L_after_before_vel = false;
			boolean R2_after_before_vel = false;
			boolean L2_after_before_vel = false;
			int after_before_vel_count = 0;
			
		
			
			double time_mid_out = frametime;
			double time_mid_in = frametime;
			int[] posYellow = new int[2];

			IplImage detectThrs = cvCreateImage(imgs.cvSize(), 8, 1);
			
			IplImage frame = null;
			ShotRecord shotRecord=new ShotRecord();
			
			cvQueryFrame(capture);
			cvQueryFrame(capture);
			for (b =0; b <= numFrames; b++) {
		
			
				
			while (!running)
	                join();   
				
				frame = cvQueryFrame(capture);

				if (frame == null||b==(numFrames-1)) {
					JOptionPane.showMessageDialog(null, "Complete", "complete video", JOptionPane.INFORMATION_MESSAGE);
					
					break;
				}
				resizedImage = resize(frame.getBufferedImage(),
						jLabel1.getWidth(), jLabel1.getHeight());

			
				frame = IplImage.createFrom(resizedImage);

				detectThrs = TTUtil.getThresholdImage(frame, white_min, white_max);

				detectThrs = TTUtil.rectangle(detectThrs,0,220,450,230);
				detectThrs = TTUtil.rectangle(detectThrs,0,123,450,136);
				detectThrs = TTUtil.rectangle(detectThrs,210,35,230,220);
			   // jLabel1.setIcon(new ImageIcon(detectThrs.getBufferedImage()));
		
				jLabel1.setIcon(new ImageIcon(resizedImage));
				//gnew.drawImage(new ImageIcon(resizedImage).getImage(), 0, 0, jLabel1.getWidth(), jLabel1.getHeight(), jLabel1);

				posYellow = TTUtil.getMoment(detectThrs);

				if ((posYellow[0] > 5)&&posYellow[0]<430) {
				
					
					if (painyellow ) {
						
						x = posYellow[0];
						y = posYellow[1];

						XYellow[b] = x;
						YYellow[b] = y;
						
						
						
						painyellow = false;
						if (isRightToLeft&&sideVideoFrame.getRightToLeft().getTime()==(b*frametime)) {
							TTSideVideo.pauseThread();
							TTTopVideo.resumeThread();
							
							righttoLeftShot = new ShotRecord();				
							righttoLeftShot.setStarting_x((int)(posYellow[0]*TABLE_LENGTH_RATIO));
							righttoLeftShot.setStarting_y((int)(posYellow[1]*TABLE_WIDTH_RATIO));
							righttoLeftShot.setTime(b * frametime);
							
							isRightToLeft = false;
							
						
						}
						
					}
	
			/*************************check player record***********************************/		
					
				//	System.out.println(sideVideoFrame.getRightToLeft().getTime()+" prrp "+(b*frametime));
					/****************************** check whether Right to Left *******************************
					 *                                                                                        *
					 ******************************************************************************************/
	
					if ((x - posYellow[0]) > 0) {
						
						
						if(!isleftToRight){
							
						/*	if(!(rallyResult==2||rallyResult==3)){
								jLabel2.repaint();
								sideVideoFrame.jLabel4.repaint();
								JOptionPane.showMessageDialog(null,"R1","warning",JOptionPane.INFORMATION_MESSAGE);
							}*/
							
							
									System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR");
						System.out.println(" start  time :"+sideVideoFrame.getLeftToRight().getTime());
						System.out.println(" start  x service :"+lefttoRightShot.getStarting_x());
						System.out.println(" start  y service :"+lefttoRightShot.getStarting_y());
						System.out.println(" start  z service :"+sideVideoFrame.getLeftToRight().getStarting_h());
						System.out.println(" pitch x service :"+lefttoRightShot.getPitch_x_service());
						System.out.println("pitch   y service :"+lefttoRightShot.getPitch_y_service());
					
						System.out.println(" pitch x regular :"+lefttoRightShot.getPitch_x_regular());
						System.out.println("pitch   y regular :"+lefttoRightShot.getPitch_y_regular());
						
						System.out.println(" velocity before service :"+sideVideoFrame.getLeftToRight().getBefore_pitch_velocity_service());
						System.out.println(" velocity after service :"+sideVideoFrame.getLeftToRight().getAfter_pitch_velocity_service());
						System.out.println(" velocity before regular :"+sideVideoFrame.getLeftToRight().getBefore_pitch_velocity_regular());
						System.out.println(" velocity after regular :"+sideVideoFrame.getLeftToRight().getAfter_pitch_velocity_service());
						
						System.out.println(" calorie  :"+sideVideoFrame.getLeftToRight().getCalorie());						
						System.out.println("*************************************************************************");
						
						}
						
					
						
						
						isleftToRight = true;
						
						
						if (isRightToLeft&&sideVideoFrame.getRightToLeft().getTime()==(b*frametime)) {
							TTSideVideo.pauseThread();
							TTTopVideo.resumeThread();
							
							righttoLeftShot = new ShotRecord();				
							righttoLeftShot.setStarting_x((int)(posYellow[0]*TABLE_LENGTH_RATIO));
							righttoLeftShot.setStarting_y((int)(posYellow[1]*TABLE_WIDTH_RATIO));
							righttoLeftShot.setTime(b * frametime);
							
							isRightToLeft = false;
							
						
						}
						/*******************    set x y and combination velocity at before pitch at  service R to L **********************************************/
						else if (sideVideoFrame.getRightToLeft().getPitch_z_service_time()==(b*frametime)) {
							R_after_before_vel=true;
							righttoLeftShot.setPitch_x_service((int)(TABLE_LENGTH_RATIO *posYellow[0]));
							righttoLeftShot.setPitch_y_service((int)(TABLE_WIDTH_RATIO*posYellow[1]));
							
							// get top view velocity
							righttoLeftShot.setBefore_pitch_velocity_service(TTUtil.velocityBall(XYellow[b- TTSideVideo.COUNT_VEL], YYellow[b- TTSideVideo.COUNT_VEL],posYellow[0],posYellow[1], frametime
											* TTSideVideo.COUNT_VEL));
							//get combination of both top view and side view velocity
							righttoLeftShot.setBefore_pitch_velocity_service(Math.sqrt(Math.pow(righttoLeftShot.getBefore_pitch_velocity_service(),2)+Math.pow(sideVideoFrame.getRightToLeft().getBefore_pitch_velocity_service(),2)));
							
							
						}
						
						/*******************    set x y and combination velocity at before pitch at regular R to L **********************************************/
						else if (sideVideoFrame.getRightToLeft().getPitch_z_regular_time()==(b*frametime)) {
							L_after_before_vel=true;
							if(righttoLeftShot==null){
								System.out.println("service point has not been tracked ");
							}
							righttoLeftShot.setPitch_x_regular((int)(TABLE_LENGTH_RATIO* posYellow[0]));
							righttoLeftShot.setPitch_y_regular((int)(TABLE_WIDTH_RATIO*posYellow[1]));
							righttoLeftShot.setBefore_pitch_velocity_regular(TTUtil.velocityBall(XYellow[b- TTSideVideo.COUNT_VEL], YYellow[b- TTSideVideo.COUNT_VEL],	posYellow[0],posYellow[1], frametime* TTSideVideo.COUNT_VEL));
							//get combination of both top view and side view velocity
							righttoLeftShot.setBefore_pitch_velocity_regular(Math.sqrt(Math.pow(righttoLeftShot.getBefore_pitch_velocity_regular(),2)+Math.pow(sideVideoFrame.getRightToLeft().getBefore_pitch_velocity_regular(),2)));
							
							
							/*******************    set calorie at  right side **********************************************/
							if (service_R) {
								calorie = (RACKET_MASS/2)*Math.pow(((RACKET_MASS*(righttoLeftShot.getBefore_pitch_velocity_service()))+ (BALL_MASS*(righttoLeftShot.getBefore_pitch_velocity_service())))/(2*RACKET_MASS),2)/CLORIETOJUL; 
								righttoLeftShot.setCalorie(calorie);
								service_R = false;
								g.drawString("calorie "+calorie, posYellow[0], posYellow[1]+20);
							} else {
								double v1 = TTUtil.velocityBall(XYellow[b- TTSideVideo.COUNT_VEL], YYellow[b- TTSideVideo.COUNT_VEL],posYellow[0],posYellow[1], frametime* TTSideVideo.COUNT_VEL);
								double v2 = lefttoRightShot.getAfter_pitch_velocity_regular();
								calorie = (RACKET_MASS/2)*Math.pow(((RACKET_MASS*(v1-v2))+ (BALL_MASS*(v1+v2)))/(2*RACKET_MASS),2)/CLORIETOJUL;
								
								righttoLeftShot.setCalorie(calorie);
								g.drawString("calorie "+calorie, posYellow[0], posYellow[1]+20);
							}
							/** <<<<<<<<<<<<<<<<<<<<<<<<   END  calorie at  right side   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>***/
							
						}
						
						
						
						
							
						else	if (R_after_before_vel) {
							 ++after_before_vel_count;
							 if(after_before_vel_count==TTSideVideo.COUNT_VEL){
							 after_before_vel_count=0;
							R_after_before_vel = false;
							righttoLeftShot.setAfter_pitch_velocity_service(TTUtil.velocityBall(posYellow[0],posYellow[1], XYellow[b- TTSideVideo.COUNT_VEL],YYellow[b - TTSideVideo.COUNT_VEL],
													frametime * TTSideVideo.COUNT_VEL));
							//get combination of both top view and side view velocity
							righttoLeftShot.setAfter_pitch_velocity_service(Math.sqrt(Math.pow(righttoLeftShot.getAfter_pitch_velocity_service(),2)+Math.pow(sideVideoFrame.getRightToLeft().getAfter_pitch_velocity_service(),2)));
							
							
							 }
						} else if (L_after_before_vel) {
							
							 ++after_before_vel_count;
							 if(after_before_vel_count==TTSideVideo.COUNT_VEL){
							 after_before_vel_count=0;
							L_after_before_vel = false;
							righttoLeftShot.setAfter_pitch_velocity_regular(TTUtil.velocityBall(posYellow[0],posYellow[1], XYellow[b- TTSideVideo.COUNT_VEL],YYellow[b - TTSideVideo.COUNT_VEL],
													frametime * TTSideVideo.COUNT_VEL));
							//get combination of both top view and side view velocity
							righttoLeftShot.setAfter_pitch_velocity_regular(Math.sqrt(Math.pow(righttoLeftShot.getAfter_pitch_velocity_regular(),2)+Math.pow(sideVideoFrame.getRightToLeft().getAfter_pitch_velocity_regular(),2)));
							
							 }
							
							 
							 
						}	
						g.drawString("<", posYellow[0], posYellow[1]);
						/*********************************************************** END POINT **************************************/
						
						if(righttoLeftShot!=null){
						righttoLeftShot.setEnding_x((int)(TABLE_LENGTH_RATIO *posYellow[0]));
						righttoLeftShot.setEnding_y((int)(TABLE_WIDTH_RATIO *posYellow[1]));
						}
					} 
					
					
					
					
					
					/****************************** check whether L to R **************************************
					 *                                                                                        *
					 ******************************************************************************************/
		
					else if ((x - posYellow[0]) < 0) {
						
						
						System.out.println(sideVideoFrame.getLeftToRight().getPitch_z_regular_time()+"  time  "+(b*frametime));
						
						
						if(!isRightToLeft){
							
							jLabel2.repaint();
							sideVideoFrame.jLabel4.repaint();
							
						
							/*****************************  INSERT DB L1****************************************/
							insertDB(righttoLeftShot,sideVideoFrame.getRightToLeft(), playerinfo);	
							/***************************** END INSERT DB****************************************/
							
						System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
						System.out.println(" start  time :"+sideVideoFrame.getRightToLeft().getTime());
						System.out.println(" start  x service :"+righttoLeftShot.getStarting_x());
						System.out.println(" start  y service :"+righttoLeftShot.getStarting_y());
						System.out.println(" start  z service :"+sideVideoFrame.getRightToLeft().getStarting_h());
						System.out.println(" pitch x service :"+righttoLeftShot.getPitch_x_service());
						System.out.println("pitch   y service :"+righttoLeftShot.getPitch_y_service());
					
						System.out.println(" pitch x regular :"+righttoLeftShot.getPitch_x_regular());
						System.out.println("pitch   y regular :"+righttoLeftShot.getPitch_y_regular());
					
						System.out.println(" velocity before service :"+sideVideoFrame.getRightToLeft().getBefore_pitch_velocity_service());
						System.out.println(" velocity after service :"+sideVideoFrame.getRightToLeft().getAfter_pitch_velocity_service());
						System.out.println(" velocity before regular :"+sideVideoFrame.getRightToLeft().getBefore_pitch_velocity_regular());
						System.out.println(" velocity after regular :"+sideVideoFrame.getRightToLeft().getAfter_pitch_velocity_service());
						
						System.out.println(" calorie  :"+sideVideoFrame.getRightToLeft().getCalorie());	
						System.out.println("*************************************************************************");
						
							
						System.out.println(" v1 "+righttoLeftShot.getBefore_pitch_velocity_service());
						System.out.println(" v2 "+righttoLeftShot.getAfter_pitch_velocity_service());
						System.out.println(" v3 "+righttoLeftShot.getBefore_pitch_velocity_regular());
						System.out.println(" v4 "+righttoLeftShot.getAfter_pitch_velocity_regular());
						}
						
						
						
						
						  
						
						isRightToLeft = true;
						if(isleftToRight&&(rallyResult==2||rallyResult==3)){
							jLabel2.repaint();
							sideVideoFrame.jLabel4.repaint();
							
							/*****************************  INSERT DB L2****************************************/
							insertDB(righttoLeftShot,sideVideoFrame.getRightToLeft(), playerinfo);	
							/***************************** END INSERT DB****************************************/
						}
						if (isleftToRight) {
							
							
							TTSideVideo.resumeThread();
							TTTopVideo.pauseThread();
							lefttoRightShot = new ShotRecord();
							lefttoRightShot.setStarting_x((int)(posYellow[0]*TABLE_LENGTH_RATIO));
							lefttoRightShot.setStarting_y((int)(posYellow[1]*TABLE_WIDTH_RATIO));
							
							isleftToRight = false;
						}

						else if (sideVideoFrame.getLeftToRight().getPitch_z_service_time()==(b*frametime)) {
							lefttoRightShot.setPitch_x_service((int)(TABLE_LENGTH_RATIO *posYellow[0]));
							lefttoRightShot.setPitch_y_service((int)(TABLE_WIDTH_RATIO*posYellow[1]));
							lefttoRightShot.setBefore_pitch_velocity_service(TTUtil.velocityBall(XYellow[b- TTSideVideo.COUNT_VEL], YYellow[b- TTSideVideo.COUNT_VEL],posYellow[0],posYellow[1], frametime* TTSideVideo.COUNT_VEL));
							//get combination of both top view and side view velocity
							lefttoRightShot.setBefore_pitch_velocity_service(Math.sqrt(Math.pow(lefttoRightShot.getBefore_pitch_velocity_service(),2)+Math.pow(sideVideoFrame.getLeftToRight().getBefore_pitch_velocity_service(),2)));
							
						
						}

						else if (sideVideoFrame.getLeftToRight().getPitch_z_regular_time()==(b*frametime)) {
							
							lefttoRightShot.setPitch_x_regular((int)(TABLE_LENGTH_RATIO* posYellow[0]));
							lefttoRightShot.setPitch_y_regular((int)(TABLE_WIDTH_RATIO*posYellow[1]));
							
							lefttoRightShot.setBefore_pitch_velocity_regular(TTUtil.velocityBall(XYellow[b- TTSideVideo.COUNT_VEL], YYellow[b- TTSideVideo.COUNT_VEL],posYellow[0],posYellow[1], frametime* TTSideVideo.COUNT_VEL));
							//get combination of both top view and side view velocity
							lefttoRightShot.setBefore_pitch_velocity_regular(Math.sqrt(Math.pow(lefttoRightShot.getBefore_pitch_velocity_regular(),2)+Math.pow(sideVideoFrame.getLeftToRight().getBefore_pitch_velocity_regular(),2)));
							R2_after_before_vel=true;
							
							/*******************   set calorie at Left side****************************************************/
							if (service_L) {
								calorie =(RACKET_MASS/2)*Math.pow(((RACKET_MASS*(lefttoRightShot.getBefore_pitch_velocity_service()))+ (BALL_MASS*(lefttoRightShot.getBefore_pitch_velocity_service())))/(2*RACKET_MASS),2)/CLORIETOJUL; 
								
								lefttoRightShot.setCalorie(calorie);
								service_L = false;
								g.drawString("calorie "+calorie, posYellow[0], posYellow[1]+20);
							} else {
								double v1 = TTUtil.velocityBall(XYellow[b- TTSideVideo.COUNT_VEL], YYellow[b- TTSideVideo.COUNT_VEL],posYellow[0],posYellow[1], frametime* TTSideVideo.COUNT_VEL);
								double v2 = righttoLeftShot.getAfter_pitch_velocity_regular();
								calorie = ((RACKET_MASS/2)* Math.pow(((RACKET_MASS*(v1-v2))+ (BALL_MASS*(v1+v2)))/(2*RACKET_MASS),2))/CLORIETOJUL;
							
								lefttoRightShot.setCalorie(calorie);
								g.drawString("calorie "+calorie, posYellow[0], posYellow[1]+20);
							}
							L2_after_before_vel=true;
						 /** <<<<<<<<<<<<<<<<<<<<<<<<<<<<< END calorie at Left side >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> **/
						}
						
						
						/********************************** After pitch velocities ***************************/
						
						
						else if (L2_after_before_vel) {
							
							  ++after_before_vel_count;
							  if(after_before_vel_count==TTSideVideo.COUNT_VEL){
							 after_before_vel_count=0;
							 
							L2_after_before_vel = false;
							lefttoRightShot.setAfter_pitch_velocity_service(TTUtil.velocityBall(posYellow[0],posYellow[1], XYellow[b- TTSideVideo.COUNT_VEL],YYellow[b - TTSideVideo.COUNT_VEL],frametime * TTSideVideo.COUNT_VEL));
							//get combination of both top view and side view velocity
							lefttoRightShot.setAfter_pitch_velocity_service(Math.sqrt(Math.pow(lefttoRightShot.getAfter_pitch_velocity_service(),2)+Math.pow(sideVideoFrame.getLeftToRight().getAfter_pitch_velocity_service(),2)));
							
							

							 }
						}
						
						else if (R2_after_before_vel) {

							
							  ++after_before_vel_count;
							  if(after_before_vel_count==TTSideVideo.COUNT_VEL){
							  after_before_vel_count=0;
							 
							R2_after_before_vel = false;
							lefttoRightShot.setAfter_pitch_velocity_regular(TTUtil.velocityBall(posYellow[0],posYellow[1], XYellow[b- TTSideVideo.COUNT_VEL],YYellow[b - TTSideVideo.COUNT_VEL],frametime * TTSideVideo.COUNT_VEL));
							//get combination of both top view and side view velocity
							lefttoRightShot.setAfter_pitch_velocity_regular(Math.sqrt(Math.pow(lefttoRightShot.getAfter_pitch_velocity_regular(),2)+Math.pow(sideVideoFrame.getLeftToRight().getAfter_pitch_velocity_regular(),2)));
							

							 }
						}
						
						
		
						
						/**********************************End  After pitch velovcities ***************************/
			
						
					
						
						if(posYellow[0]>=440){
							
							
							jLabel2.repaint();
							sideVideoFrame.jLabel4.repaint();
						
							/*****************************  INSERT DB L4****************************************/
							insertDB(lefttoRightShot,sideVideoFrame.getLeftToRight(), opponentInfo);	
							/***************************** END INSERT DB****************************************/
							
							TTTopVideo.pauseThread();
							TTSideVideo.resumeThread();
							
						}
						g.drawString(">", posYellow[0], posYellow[1]);
						
						/************************************************** END POINT ***********************************************/
						lefttoRightShot.setEnding_x((int)(TABLE_LENGTH_RATIO *posYellow[0]));
						lefttoRightShot.setEnding_y((int)(TABLE_WIDTH_RATIO *posYellow[1]));

					}
					
					
					
			/******************************finished checking player record*********************************/		
					
					/******************************painting ball path*********************************/	
						paintyellow(posYellow[0], posYellow[1], b);

						x = posYellow[0];
						y = posYellow[1];
				
			
				}

				
			/********************* check player status by message box after ball out*************************************************/	
				
			if((posYellow[0]< 400 &&posYellow[0]>30))
				isShowedMessageBox=true;	
			
					if(!(posYellow[0]< 380 &&posYellow[0]>20)){
					time_mid_out += frametime;
				
				}else{
					time_mid_out= frametime;
					
				}
		  /**********************      Pop up Message Box  *******************************/
				if((time_mid_out>1)&&isShowedMessageBox){
					isShowedMessageBox=false;
					++rid;
					 rallyResult=TTUtil.showMessageBox();
					
					
					if(rallyResult==0||rallyResult==1)
					{ service_R=true;
					}
					else if (rallyResult==2||rallyResult==3){service_L=true;}
					captureData = false;
					jLabel2.repaint();
					sideVideoFrame.jLabel4.repaint();
					painyellow=true;
					TTSideVideo.painyellow=true;
					
					time_mid_out = frametime;
					time_mid_in = frametime;
				if(!isleftToRight){
					insertDB(lefttoRightShot,sideVideoFrame.getLeftToRight(), opponentInfo);
					}
				else if(!isRightToLeft){
						insertDB(righttoLeftShot,sideVideoFrame.getRightToLeft(), playerinfo);	
					}
				if(rallyResult==1||rallyResult==3){
					ShotRecord temp=new ShotRecord();
					
					insertDB(temp,temp, rallyResult==1?playerinfo:opponentInfo);	
					
				}
					TTSideVideo.resumeThread();
					TTTopVideo.pauseThread();
					
					
					
					
				}
				
				/************************** Finished checkig player statuse, close popup message box *************************************/
			
			}// End of foor loop

			

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	
	private void insertDB(ShotRecord topRalley,ShotRecord sideRalley,PlayerInfo player){ 
		String insertQuery;
		int result;
		Connection con=DB_connection.getDbCon().conn;
				 try {

					

if(topRalley.getStarting_x()<204){
					 insertQuery = String.format("INSERT INTO playrecord  (" +
					 		"starting_x,starting_y,starting_h," +
					 		"pitched_x_service,pitched_y_service," +
					 		"pitched_x_regular,pitched_y_regular," +
					 		"ending_x,ending_y,ending_h," +
					 		"before_pitch_velocity_service,after_pitch_velocity_service," +
					 		"before_pitch_velocity_regular,after_pitch_velocity_regular," +
					 		"calorie,pid,result,rid,mid)"
			                    + " values(%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%f,%f,%f,%f,%f,%d,%d,%d,%d)",
			                    topRalley.getStarting_x(),
			                    topRalley.getStarting_y(),
			                    sideRalley.getStarting_h(),
			                    (topRalley.getPitch_x_service()==0)?null:topRalley.getPitch_x_service(),
			                    (topRalley.getPitch_y_service()==0)?null:topRalley.getPitch_y_service(),
			                    topRalley.getPitch_x_regular(),
			                    topRalley.getPitch_y_regular(),
			                    topRalley.getEnding_x(),
			                    topRalley.getEnding_y(),
			                    sideRalley.getEnding_z(),
			                   ( topRalley.getBefore_pitch_velocity_service()==0)?null: topRalley.getBefore_pitch_velocity_service(),
			                   ( topRalley.getAfter_pitch_velocity_service()==0)?null:topRalley.getAfter_pitch_velocity_service(),
			                    topRalley.getBefore_pitch_velocity_regular(),
			                    topRalley.getAfter_pitch_velocity_regular(),
			                    topRalley.getCalorie(),
			                    player.getPlayeId(),
			                    (!isShowedMessageBox&&rallyResult<4&&rallyResult>=0)?0:1,
			                    rid,
			                    match.getMatchId());

}else {

	 insertQuery = String.format("INSERT INTO playrecord  (" +
	 		"starting_x,starting_y,starting_h," +
	 		"pitched_x_service,pitched_y_service," +
	 		"pitched_x_regular,pitched_y_regular," +
	 		"ending_x,ending_y,ending_h," +
	 		"before_pitch_velocity_service,after_pitch_velocity_service," +
	 		"before_pitch_velocity_regular,after_pitch_velocity_regular," +
	 		"calorie,pid,result,rid,mid)"
               + " values(%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%f,%f,%f,%f,%f,%d,%d,%d,%d)",
              408- topRalley.getStarting_x(),
              210- topRalley.getStarting_y(),
               sideRalley.getStarting_h(),
            ( 408-  topRalley.getPitch_x_service())==408?null:408-  topRalley.getPitch_x_service(),
            ( 210-  topRalley.getPitch_y_service())==210?null:210- topRalley.getPitch_y_service(),
             408-  topRalley.getPitch_x_regular(),
             210-  topRalley.getPitch_y_regular(),
             408-  topRalley.getEnding_x(),
             210-  topRalley.getEnding_y(),
               sideRalley.getEnding_z(),
              ( topRalley.getBefore_pitch_velocity_service()==0)?null:topRalley.getBefore_pitch_velocity_service(),
              ( topRalley.getAfter_pitch_velocity_service()==0)?null:topRalley.getAfter_pitch_velocity_service(),
               topRalley.getBefore_pitch_velocity_regular(),
               topRalley.getAfter_pitch_velocity_regular(),
               topRalley.getCalorie(),
               player.getPlayeId(),
               (!isShowedMessageBox&&rallyResult<4&&rallyResult>=0)?0:1,
               rid,
               match.getMatchId());

}
			            stmt = (PreparedStatement) con.prepareStatement(insertQuery);

			            stmt.executeUpdate();

			            stmt.close();
			            con.close();
			         

			        } catch (Exception e1) {
			            e1.printStackTrace();
			        }
				

	}
	
	static BufferedImage resize(BufferedImage image, int width, int height) {

		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TRANSLUCENT);
		Graphics2D g2d = (Graphics2D) bi.createGraphics();
		g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY));
		g2d.drawImage(image, 0, 0, width, height, null);
		g2d.dispose();
		return bi;
	}

	

	
	static boolean xx = false;

	public void paintyellow(int posX, int posY, int b) {

		if (xx) {
			x = posX;
			y = posY;
			xx = false;
		}
		if (Math.abs(x - posX) < 500) {
			g.setColor(Color.GREEN);
		
			g.drawLine(x, y, posX, posY);
			XYellow[b] = posX;
			YYellow[b] = posY;

			// hYellow[b] = (imgs.height() - posY);

			x = posX;
			y = posY;
		} else {
			x = 0;
			y = 0;
			xx = true;
		}

	}


	
	public IplImage Equalize(BufferedImage bufferedimg) {
		IplImage iploriginal = IplImage.createFrom(bufferedimg);
		IplImage srcimg = IplImage.create(iploriginal.width(),
				iploriginal.height(), IPL_DEPTH_8U, 1);
		IplImage destimg = IplImage.create(iploriginal.width(),
				iploriginal.height(), IPL_DEPTH_8U, 1);
		cvCvtColor(iploriginal, srcimg, CV_BGR2GRAY);
		cvEqualizeHist(srcimg, destimg);
		return destimg;
	}



	public static void main(String args[]) {
		cot = new TTTopVideo();
		cot.topFrame.setVisible(true);
		// th = new Thread(cot);

	}

	@SuppressWarnings("unchecked")
	private void initComponents() {
		
		  try {
	            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
	                if ("Nimbus".equals(info.getName())) {
	                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
	                    break;
	                }
	            }
	        } catch (ClassNotFoundException ex) {
	            java.util.logging.Logger.getLogger(TTTopVideo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	        } catch (InstantiationException ex) {
	            java.util.logging.Logger.getLogger(TTTopVideo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	        } catch (IllegalAccessException ex) {
	            java.util.logging.Logger.getLogger(TTTopVideo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
	            java.util.logging.Logger.getLogger(TTTopVideo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	        }
		  
		  
		topFrame=new JFrame();
		playerIdText = new JTextField();
		playerNameLabel = new JLabel();
		playerNameComboLabel = new JLabel();

		jPanel1 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jPanel2 = new javax.swing.JPanel();
		loadTopButton = new javax.swing.JButton();
		playTopButton = new javax.swing.JButton();
		analyzeButton = new javax.swing.JButton();
		stopTopButton = new javax.swing.JButton();

		jPanel3 = new javax.swing.JPanel();
		finetuneButton = new javax.swing.JButton();
	

		jPanel4 = new javax.swing.JPanel();

		newPlayerNameText = new javax.swing.JTextField();
		playerNameLabel = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		playerlistComboBox = new JComboBox<String>();
		opponentlistComboBox= new JComboBox<String>();
		jLabel7 = new javax.swing.JLabel();
		heightText = new javax.swing.JTextField();
		jLabel8 = new javax.swing.JLabel();
		handsideComboBox = new javax.swing.JComboBox<String>();
		jLabel9 = new javax.swing.JLabel();
		jLabel10 = new javax.swing.JLabel();
		jLabel11 = new javax.swing.JLabel();
		jLabel12 = new javax.swing.JLabel();
		jLabel13 = new javax.swing.JLabel();
		jLabel14 = new javax.swing.JLabel();
		jLabel15 = new javax.swing.JLabel();
		jLabel16 = new javax.swing.JLabel();
		fpsTop = new javax.swing.JLabel();
		fpsSide = new javax.swing.JLabel();
		nofTop = new javax.swing.JLabel();
		nofSide = new javax.swing.JLabel();
		fwTop = new javax.swing.JLabel();
		fwSide = new javax.swing.JLabel();
		fhTop = new javax.swing.JLabel();
		fhSide = new javax.swing.JLabel();
		ttTop = new javax.swing.JLabel();
		ttSide = new javax.swing.JLabel();
		jLabel17 = new javax.swing.JLabel();
		tpfSide = new javax.swing.JLabel();
		tpfTop = new javax.swing.JLabel();
		matchButton= new javax.swing.JButton();
		playerNameComboBoxLabel = new JLabel("Select Player");
		opponentNameLabel= new JLabel("Select Opponent");
		tpfTop.setForeground(Color.WHITE);
		tpfSide.setForeground(Color.WHITE);
		jLabel17.setForeground(Color.WHITE);
		ttSide.setForeground(Color.WHITE);
		ttTop.setForeground(Color.WHITE);
		fhSide.setForeground(Color.WHITE);

		fhTop.setForeground(Color.WHITE);
		fwSide.setForeground(Color.WHITE);
		fwTop.setForeground(Color.WHITE);
		nofSide.setForeground(Color.WHITE);
		nofTop.setForeground(Color.WHITE);
		fpsSide.setForeground(Color.WHITE);
		fpsTop.setForeground(Color.WHITE);

		jLabel10.setForeground(Color.WHITE);
		jLabel11.setForeground(Color.WHITE);
		jLabel12.setForeground(Color.WHITE);
		jLabel13.setForeground(Color.WHITE);
		jLabel14.setForeground(Color.WHITE);
		jLabel15.setForeground(Color.WHITE);
		jLabel16.setForeground(Color.WHITE);

		jLabel7.setForeground(Color.WHITE);
		jLabel8.setForeground(Color.WHITE);
		jLabel9.setForeground(Color.CYAN);
		jLabel17.setForeground(Color.WHITE);
		playerNameComboBoxLabel.setForeground(Color.WHITE);
		opponentNameLabel.setForeground(Color.WHITE);
		playerIdText.setSize(100, 20);
		playerNameLabel.setForeground(Color.WHITE);
		playerNameComboLabel.setForeground(Color.WHITE);
		topFrame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		playerNameLabel.setText("player name");

		jLabel6.setText("Player name");

		jLabel7.setText("height");

		jLabel8.setText("Hand side");

		jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
		jLabel9.setText("New Player");

		jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
		jLabel10.setText("Top ");

		jLabel11.setText("Frame Rate     ");

		jLabel12.setText("Total Frames   ");

		jLabel13.setText("Frame Width    ");

		jLabel14.setText("Frame Hieght   ");

		jLabel15.setText("Total Time     ");

		jLabel16.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
		jLabel16.setText("Side");

		jLabel17.setText("Time per Frame");

		jPanel1.setBackground(new java.awt.Color(51, 51, 51));

		jLabel1.setBackground(new java.awt.Color(255, 255, 255));

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(
				jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout
				.setHorizontalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addGap(29, 29, 29)
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(
																jLabel2,
																javax.swing.GroupLayout.Alignment.LEADING,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																450,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																jLabel1,
																javax.swing.GroupLayout.Alignment.LEADING,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																450,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(29, 29, 29)));
		jPanel1Layout
				.setVerticalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addGap(21, 21, 21)
										.addComponent(
												jLabel1,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												258,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(
												jLabel2,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												258,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap(31, Short.MAX_VALUE)));

		loadTopButton.setText("Load Video");
		playTopButton.setIcon(new ImageIcon("images/play.png")); // NOI18N
		analyzeButton.setText("  Analyze  ");
		stopTopButton.setText("   Stop   ");
		finetuneButton.setText(" Fine-Tune ");
		matchButton.setText("  Match  ");
		
	
		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(
				jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel2Layout.createSequentialGroup().addContainerGap()
						.addComponent(loadTopButton).addGap(18, 18, 18)
						.addComponent(playTopButton).addGap(18, 18, 18)
						.addComponent(stopTopButton).addGap(18, 18, 18)
						.addComponent(analyzeButton).addGap(18, 18, 18)
						.addComponent(finetuneButton).addGap(18, 18, 18)
						.addComponent(matchButton)
						.addContainerGap(56, Short.MAX_VALUE)));
		jPanel2Layout
				.setVerticalGroup(jPanel2Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel2Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel2Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																loadTopButton)
														.addComponent(
																playTopButton)
														.addComponent(stopTopButton)
														.addComponent(analyzeButton)
														.addComponent(finetuneButton)
														.addComponent(matchButton))
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		

	

	

		
		
		jPanel4.setBackground(new java.awt.Color(10, 20, 10));

		javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(
				jPanel4);
		jPanel4.setLayout(jPanel4Layout);
		jPanel4Layout
				.setHorizontalGroup(jPanel4Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel4Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel4Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																jLabel8,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																61,
																Short.MAX_VALUE)
														.addComponent(
																playerNameLabel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																61,
																Short.MAX_VALUE)
														.addComponent(
																playerNameComboBoxLabel,
																javax.swing.GroupLayout.Alignment.TRAILING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																61,
																Short.MAX_VALUE)
																
														.addComponent(
																opponentNameLabel,
																		javax.swing.GroupLayout.Alignment.TRAILING,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		61,
																		Short.MAX_VALUE)
														.addComponent(
																jLabel7,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																61,
																Short.MAX_VALUE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel4Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																playerlistComboBox,
																javax.swing.GroupLayout.Alignment.TRAILING,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																116,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																opponentlistComboBox,
																javax.swing.GroupLayout.Alignment.TRAILING,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																116,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																handsideComboBox,
																0, 116,
																Short.MAX_VALUE)
														.addComponent(
																heightText,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																116,
																Short.MAX_VALUE)
														.addComponent(
																newPlayerNameText,
																javax.swing.GroupLayout.Alignment.TRAILING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																116,
																Short.MAX_VALUE))
										.addGap(67, 67, 67))
						.addGroup(
								jPanel4Layout
										.createSequentialGroup()
										.addGap(86, 86, 86)
										.addComponent(
												jLabel9,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												83,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap(89, Short.MAX_VALUE))
						.addGroup(
								jPanel4Layout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												jLabel12,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												101,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addGap(15, 15, 15)
										.addComponent(
												
												nofTop,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												36,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(30, 30, 30)
										.addComponent(
												nofSide,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												36,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap(51, Short.MAX_VALUE))
						.addGroup(
								jPanel4Layout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												jLabel13,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												101,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(
												fwTop,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												36,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(30, 30, 30)
										.addComponent(
												fwSide,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												36,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap(47, Short.MAX_VALUE))
						.addGroup(
								jPanel4Layout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												jLabel14,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												101,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
												.addGap(15, 15, 15)
										.addComponent(
												fhTop,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												36,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(30, 30, 30)
										.addComponent(
												fhSide,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												36,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap(47, Short.MAX_VALUE))
						.addGroup(
								jPanel4Layout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												jLabel15,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												101,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
											.addGap(15,15,15)
										.addComponent(
												
												ttTop,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												36,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(23, 23, 23)
										.addComponent(
												ttSide,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												36,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap(51, Short.MAX_VALUE))
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								jPanel4Layout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(jLabel11)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												24, Short.MAX_VALUE)
										.addGroup(
												jPanel4Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																jPanel4Layout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel10)
																		.addGap(36,
																				36,
																				36)
																		.addComponent(
																				jLabel16))
														.addGroup(
																jPanel4Layout
																		.createSequentialGroup()
																		.addComponent(
																				fpsTop,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				36,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addGap(30,
																				30,
																				30)
																		.addComponent(
																				fpsSide,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				36,
																				javax.swing.GroupLayout.PREFERRED_SIZE)))
										.addGap(48, 48, 48))
						.addGroup(
								jPanel4Layout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												jLabel17,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												101,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addGap(15, 15, 15)
										.addComponent(
												tpfTop,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												36,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(30, 30, 30)
										.addComponent(
												tpfSide,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												36,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap(51, Short.MAX_VALUE)));
		jPanel4Layout
				.setVerticalGroup(jPanel4Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel4Layout
										.createSequentialGroup()
										.addGap(35, 35, 35)
										.addGroup(
												jPanel4Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																playerlistComboBox,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																playerNameComboBoxLabel)
										.addGap(34, 34, 34)						)
										.addGroup(
												jPanel4Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																opponentlistComboBox,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																opponentNameLabel)
																)
										.addGap(34, 34, 34)
										.addComponent(jLabel9)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(
												jPanel4Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(
																playerNameLabel,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																17,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																newPlayerNameText,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel4Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																heightText,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																jLabel7,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																17,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(
												jPanel4Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																jLabel8,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																17,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																handsideComboBox,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGroup(
												jPanel4Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING,
																false)
														.addGroup(
																jPanel4Layout
																		.createSequentialGroup()
																		.addGap(34,
																				34,
																				34)
																		.addGroup(
																				jPanel4Layout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.BASELINE)
																						.addComponent(
																								jLabel10)
																						.addComponent(
																								jLabel16))
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addGroup(
																				jPanel4Layout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.TRAILING)
																						.addComponent(
																								fpsSide,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								21,
																								Short.MAX_VALUE)
																						.addComponent(
																								fpsTop,
																								javax.swing.GroupLayout.Alignment.LEADING,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)))
														.addGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																jPanel4Layout
																		.createSequentialGroup()
																		.addGap(61,
																				61,
																				61)
																		.addComponent(
																				jLabel11)))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel4Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(jLabel12)
														.addComponent(
																nofSide,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																20,
																Short.MAX_VALUE)
														.addComponent(
																nofTop,
																javax.swing.GroupLayout.Alignment.LEADING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																20,
																Short.MAX_VALUE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel4Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(jLabel13)
														.addComponent(
																fwSide,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																20,
																Short.MAX_VALUE)
														.addComponent(
																fwTop,
																javax.swing.GroupLayout.Alignment.LEADING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																20,
																Short.MAX_VALUE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel4Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(jLabel14)
														.addComponent(
																fhSide,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																20,
																Short.MAX_VALUE)
														.addComponent(
																fhTop,
																javax.swing.GroupLayout.Alignment.LEADING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																20,
																Short.MAX_VALUE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel4Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(jLabel15)
														.addComponent(
																ttSide,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																20,
																Short.MAX_VALUE)
														.addComponent(
																ttTop,
																javax.swing.GroupLayout.Alignment.LEADING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																20,
																Short.MAX_VALUE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel4Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(jLabel17)
														.addComponent(
																tpfSide,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																20,
																Short.MAX_VALUE)
														.addComponent(
																tpfTop,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																20,
																Short.MAX_VALUE))
										.addGap(240, 240, 240)));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				topFrame.getContentPane());
		topFrame.getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		jPanel1,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		497,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		sideVideoFrame.sidePanel,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		javax.swing.GroupLayout.PREFERRED_SIZE))
												.addGroup(
														layout.createSequentialGroup()
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		jPanel2,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		javax.swing.GroupLayout.PREFERRED_SIZE)

																.addComponent(
																		jPanel3,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		javax.swing.GroupLayout.PREFERRED_SIZE)))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jPanel4,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														sideVideoFrame.sidePanel,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(
														jPanel1,
														javax.swing.GroupLayout.Alignment.TRAILING,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE))
								.addGap(18, 18, 18)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING,
												false)
												.addComponent(
														jPanel2,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(
														jPanel3,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE))
								.addGap(29, 29, 29))
				.addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

		loadTopButton.addActionListener(this);

	
		

		stopTopButton.addActionListener(this);
		playTopButton.addActionListener(this);
		playerlistComboBox.addActionListener(this);
		opponentlistComboBox.addActionListener(this);
		matchButton.addActionListener(this);
		analyzeButton.addActionListener(this);
		finetuneButton.addActionListener(this);
		topFrame.pack();
	}// </editor-fold>



	@Override
	public void actionPerformed(ActionEvent e) {
	
		if (e.getSource() == playTopButton) {
			
			if (opponentInfo.getPlayeId()==0) {
				//playerinfo.getPlayeId() == 0&& 
				JOptionPane.showMessageDialog(null,
						"Select player and opponent !", "select  player",
						JOptionPane.INFORMATION_MESSAGE);
			
			} else {
				
				
				System.out.println("playing . . .");

				th = new Thread(cot);
				th2 = new Thread(sideVideoFrame);
				
				th.start();
			
				th2.start();
				
			}

		} else if (e.getSource() == stopTopButton) {
			System.out.println("Stop playing Video Top elevation");
			
			th.stop();
			th2.stop();

		} 
		
		else if (e.getSource() == loadTopButton) {
			System.out.println("Reloading Video Top elevation");

			JFileChooser openFile = new JFileChooser("video/Top/");
			openFile.showOpenDialog(null);
			openFile.setDialogTitle("Select The Top elevation video");
			File f_top = openFile.getSelectedFile();

			JFileChooser openFile2 = new JFileChooser("video/Side/");
			openFile2.showOpenDialog(null);
			openFile2.setDialogTitle("Select The Side elevation video");
			File f_side = openFile2.getSelectedFile();

			if (f_top != null && f_side != null) {
				clear();
				
				clear();
				cot.topFrame.setVisible(false);
				cot = new TTTopVideo(f_top.toString(), f_side.toString());
				cot.topFrame.setVisible(true);
				th = new Thread(cot);

				System.gc();
			}
		}

		else if (e.getSource() == playerlistComboBox) {
			if (playerlistComboBox.getSelectedIndex() > 0) {
				
				String result = (String) playerlistComboBox.getSelectedItem();
				match.getWinnerComboBox().addItem(result);
				String[] player = result.split("-");
				playerinfo.setPlayeId(Integer.parseInt(player[0]));
				playerinfo.setPlayerName(player[1]);
			}
		}

	
		else if (e.getSource() == opponentlistComboBox) {
			if (opponentlistComboBox.getSelectedIndex() > 0) {
				
				String result = (String) opponentlistComboBox.getSelectedItem();
				match.getWinnerComboBox().addItem(result);
				String[] opponent = result.split("-");
				opponentInfo.setPlayeId(Integer.parseInt(opponent[0]));
				opponentInfo.setPlayerName(opponent[1]);
			}
		}
		else if (e.getSource() == matchButton) {
			
			match.setVisible(true);
			
		}
		else if(e.getSource() ==analyzeButton){
			try{Runtime runtime=Runtime.getRuntime();
			Process process=runtime.exec("C:/Program Files (x86)/ultrasoft/VTTI/VTTI_SW.exe");
			}catch(IOException ex){}
		}
		else if(e.getSource() == finetuneButton){
			System.out.println("mmmmmmnk");
			finetune=FineTune.getInstanse();
			finetune.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			finetune.setVisible(true);
			
		}
	}

	public static boolean pauseThread() throws InterruptedException
    {
        running = false;
        return running;
    }

    public static boolean  resumeThread()
    {
        running = true;
        return running;
    }
    
	/**
	 * @param args
	 *            the command line arguments
	 */
	// Variables declaration - do not modify//GEN-BEGIN:variables

    private javax.swing.JButton matchButton;
	private javax.swing.JButton loadTopButton;
	private javax.swing.JButton playTopButton;
	private javax.swing.JButton analyzeButton;
	private javax.swing.JButton stopTopButton;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;

	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JButton finetuneButton;

	
	private JTextField playerIdText;
	private JLabel playerNameLabel;
	private JLabel playerNameComboBoxLabel;
	private JLabel playerNameComboLabel;

	private javax.swing.JTextField newPlayerNameText;
	private javax.swing.JLabel nofSide;
	private javax.swing.JLabel nofTop;

	private JComboBox<String> playerlistComboBox;
	private JComboBox<String> opponentlistComboBox;
	private JLabel opponentNameLabel;
	private javax.swing.JLabel tpfSide;
	private javax.swing.JLabel tpfTop;
	private javax.swing.JLabel ttSide;
	private javax.swing.JLabel ttTop;

	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel11;
	private javax.swing.JLabel jLabel12;
	private javax.swing.JLabel jLabel13;
	private javax.swing.JLabel jLabel14;
	private javax.swing.JLabel jLabel15;
	private javax.swing.JLabel jLabel16;
	private javax.swing.JLabel jLabel17;
	private javax.swing.JLabel fhSide;
	private javax.swing.JLabel fhTop;
	private javax.swing.JLabel fpsSide;
	private javax.swing.JLabel fpsTop;
	private javax.swing.JLabel fwSide;
	private javax.swing.JLabel fwTop;
	private javax.swing.JComboBox<String> handsideComboBox;
	private javax.swing.JTextField heightText;
	private javax.swing.JFrame topFrame;
	// End of variables declaration


}
