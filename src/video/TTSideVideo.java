/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TTSideVideo.java
 *
 * Created on Sep 22, 2013, 2:18:16 AM
 */
package video;

import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;
import static com.googlecode.javacv.cpp.opencv_highgui.CV_CAP_PROP_FPS;
import static com.googlecode.javacv.cpp.opencv_highgui.CV_CAP_PROP_FRAME_COUNT;
import static com.googlecode.javacv.cpp.opencv_highgui.CV_CAP_PROP_FRAME_HEIGHT;
import static com.googlecode.javacv.cpp.opencv_highgui.CV_CAP_PROP_FRAME_WIDTH;
import static com.googlecode.javacv.cpp.opencv_highgui.cvCreateFileCapture;
import static com.googlecode.javacv.cpp.opencv_highgui.cvGetCaptureProperty;
import static com.googlecode.javacv.cpp.opencv_highgui.cvQueryFrame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui.CvCapture;
import javax.swing.JPanel;

/**
 * 
 * @author sujan
 */
public class TTSideVideo extends Thread {
	
	final static int COUNT_VEL = 1;
	ShotRecord lefttoRightShot;
	ShotRecord righttoLeftShot;
	static String[] list = { "Player", "Opponent", "NO Point" };
	public static boolean running = true;
	int b;
	static JComboBox jcb;
	private int[] XYellow;
	CvCapture capture_side;
	int x, y;
	static boolean painyellow = true;
	boolean xx;
	private int[] YYellow;
	double frametime;
	int frameH;
	int frameW;
	int fps;
	int numFrames;
	IplImage imgs;
	final static int TABLE_HEIGHT = 200; // height from floor to table
	Graphics g;


	
	private CvScalar white_min = cvScalar(94, 38, 209, 0);
	private CvScalar white_max = cvScalar(100, 67, 232, 0); // H: 0 - 180, S: 0
															// - 255, V: 0 - 255

	public int[] getXYellow() {
		return XYellow;
	}

	public void setXYellow(int[] xYellow) {
		XYellow = xYellow;
	}

	public int[] getYYellow() {
		return YYellow;
	}

	public void setYYellow(int[] yYellow) {
		YYellow = yYellow;
	}

	public CvCapture getCapture_side() {
		return capture_side;
	}

	public void setCapture_side(CvCapture capture_side) {
		this.capture_side = capture_side;
	}

	/** Creates new form TTSideVideo */
	public TTSideVideo() {

		xx = false;

		initComponents();

	}

	public void init() {
		numFrames = (int) cvGetCaptureProperty(this.capture_side,
				CV_CAP_PROP_FRAME_COUNT);
		frameH = (int) cvGetCaptureProperty(this.capture_side,
				CV_CAP_PROP_FRAME_HEIGHT);
		frameW = (int) cvGetCaptureProperty(this.capture_side,
				CV_CAP_PROP_FRAME_WIDTH);
		fps = (int) cvGetCaptureProperty(this.capture_side, CV_CAP_PROP_FPS);
		frametime = (double) 1 / fps;

	}

	@Override
	public void run() {

		playSide();
	}

	public void playSide() {
		init();
		
		

		boolean R_after_before_vel = false;
		boolean L_after_before_vel = false;
		boolean R2_after_before_vel = false;
		boolean L2_after_before_vel = false;
		int after_before_vel_count = 0;
		g = jLabel4.getGraphics();
		g.setColor(Color.CYAN);
		boolean changeRight = false;
		boolean changeLeft = false;
		try {

			boolean isleftToRight = true;
			boolean isRightToLeft = true;

			BufferedImage resizedImage;
			imgs = cvQueryFrame(getCapture_side());
			int[] posYellow = new int[2];

			IplImage detectThrs = cvCreateImage(imgs.cvSize(), 8, 1);
			JComboBox<String> jcb;
			IplImage frame = null;

			PreparedStatement stmt = null;

			Connection connection = DB_connection.getDbCon().conn;
		
			for (b = 0; b <= numFrames; b++) {

				
				while (!running)
					join();
				frame = cvQueryFrame(capture_side);

				if (frame == null) {
					JOptionPane.showMessageDialog(null, "Complete", "complete video", JOptionPane.INFORMATION_MESSAGE);
					
					break;
				}
				resizedImage = resize(frame.getBufferedImage(),
						jLabel3.getWidth(), jLabel3.getHeight());

				frame = IplImage.createFrom(resizedImage);

				detectThrs = TTUtil.getThresholdImage(frame, white_min,
						white_max);
				
				 detectThrs = TTUtil.rectangle(detectThrs,210,190,240,210);
				
				 //jLabel3.setIcon(new ImageIcon(detectThrs.getBufferedImage()));
				jLabel3.setIcon(new ImageIcon(resizedImage));
				posYellow = TTUtil.getMoment(detectThrs);

				
				if ((posYellow[0] > 10) && (posYellow[0] < 440)) {

					// firat tracked frame
					if (painyellow) {
						x = posYellow[0];
						y = posYellow[1];

						XYellow[b] = x;
						YYellow[b] = y;
					
						painyellow = false;
						// continue;
					}

					/******************************
					 * check whether Right to Left
					 * ******************************* *
					 ******************************************************************************************/
					if ((x - posYellow[0]) > 0) {

						g.drawString("<", posYellow[0], posYellow[1]);
						
						
						
						isleftToRight = true;
						/*********** check starting point at right to left ***************************************/
						if (isRightToLeft) {
							after_before_vel_count = 0;
							TTSideVideo.resumeThread();
							TTTopVideo.pauseThread();
							righttoLeftShot = new ShotRecord();
							righttoLeftShot
									.setStarting_h((TABLE_HEIGHT - posYellow[1]));
							righttoLeftShot.setTime(b * frametime);
							isRightToLeft = false;

						}
						/*********** check pitch at right to left ************************************************/
						else if (((TABLE_HEIGHT - posYellow[1]) < 7)) {

							/********** check after service pitch at right to left ********/
							if (posYellow[0] > 225) {
								R_after_before_vel = true;
								
								righttoLeftShot.setBefore_pitch_velocity_service(TTUtil.velocityBall(XYellow[b- COUNT_VEL], YYellow[b- COUNT_VEL],
														posYellow[0],
														posYellow[1], frametime
																* COUNT_VEL));
								righttoLeftShot.setPitch_z_service_time(b
										* frametime);

						
							}
							/********** check regular pitch at right to left ************/
							else {
								L_after_before_vel = true;

								righttoLeftShot
										.setBefore_pitch_velocity_regular(TTUtil
												.velocityBall(XYellow[b
														- COUNT_VEL], YYellow[b
														- COUNT_VEL],
														posYellow[0],
														posYellow[1], frametime
																* COUNT_VEL));
								righttoLeftShot.setPitch_z_regular_time(b
										* frametime);
							}
							g.drawString("time "+b* frametime, posYellow[0],posYellow[1]+15);
							g.drawString("velocity "+righttoLeftShot.getBefore_pitch_velocity_regular(), posYellow[0],posYellow[1]+30);
						}
						
						/******************** change side after pass the table from left side **************************************/
						
						righttoLeftShot.setEnding_z(TABLE_HEIGHT - posYellow[1]);
						

					}
					/******************************
					 * check whether L to R
					 * ************************************** *
					 ******************************************************************************************/
					else if ((x - posYellow[0]) < 0) {

						

						isRightToLeft = true;
						/*********** check whether starting point at L to R ***************************************/
						if (isleftToRight) {
							
							after_before_vel_count = 0;
							TTSideVideo.pauseThread();
							TTTopVideo.resumeThread();
							lefttoRightShot = new ShotRecord();
							lefttoRightShot
									.setStarting_h((TABLE_HEIGHT - posYellow[1]));
							lefttoRightShot.setTime(b * frametime);
							isleftToRight = false;
							
						}
						/*********** check pitch at L to R ************************************************/
						else if ((TABLE_HEIGHT - posYellow[1]) < 8) {

							/*********** check pitch after service at L to R ***********************/
							if (posYellow[0] < 220) {
								
							
								lefttoRightShot
										.setBefore_pitch_velocity_service(TTUtil
												.velocityBall(XYellow[b
														- COUNT_VEL], YYellow[b
														- COUNT_VEL],
														posYellow[0],
														posYellow[1], frametime
																* COUNT_VEL));
								lefttoRightShot.setPitch_z_service_time(b
										* frametime);
								
								g.drawString("time "+b* frametime, posYellow[0],posYellow[1]+15);
								g.drawString("velocity "+lefttoRightShot.getBefore_pitch_velocity_service(), posYellow[0],posYellow[1]+30);
								

							}
							/*********** check pitch regular at L to R ****************************/
							else {
								
									
								lefttoRightShot.setBefore_pitch_velocity_regular(TTUtil.velocityBall((XYellow[b- COUNT_VEL]==0)?XYellow[b- (COUNT_VEL+1)]:XYellow[b- COUNT_VEL],(YYellow[b- COUNT_VEL]==0)?YYellow[b- (COUNT_VEL+1)]: YYellow[b- COUNT_VEL],
														posYellow[0],
														posYellow[1],(XYellow[b- COUNT_VEL]==0)?frametime* (1+COUNT_VEL): (frametime* COUNT_VEL)));
								lefttoRightShot.setPitch_z_regular_time(b
										* frametime);
								g.drawString("time "+b* frametime, posYellow[0],posYellow[1]+15);
								g.drawString("velocity "+lefttoRightShot.getBefore_pitch_velocity_regular(),posYellow[0],posYellow[1]+30);
								
							}
						}

						
						g.drawString(">", posYellow[0], posYellow[1]);
						lefttoRightShot.setEnding_z(TABLE_HEIGHT - posYellow[1]);

						/******************** change side after pass the table from right side **************************************/
						if (posYellow[0] >= 430) {
							
							TTTopVideo.resumeThread();
							TTSideVideo.pauseThread();
						}

					}
					paintyellow(posYellow[0], posYellow[1], b);

					x = posYellow[0];
					y = posYellow[1];

				}

			}// cvReleaseCapture(capture);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public void paintyellow(int posX, int posY, int b) {

		if (xx) {
			x = posX;
			y = posY;
			xx = false;
		}
		if (Math.abs(x - posX) < 500) {
			g.setColor(Color.CYAN);
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

	private static BufferedImage resize(BufferedImage image, int width,
			int height) {

		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TRANSLUCENT);
		Graphics2D g2d = (Graphics2D) bi.createGraphics();
		g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY));
		g2d.drawImage(image, 0, 0, width, height, null);
		g2d.dispose();
		return bi;
	}

	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		sidePanel = new JPanel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();

		sidePanel.setBackground(new java.awt.Color(51, 51, 51));

		javax.swing.GroupLayout aLayout = new javax.swing.GroupLayout(sidePanel);
		sidePanel.setLayout(aLayout);
		aLayout.setHorizontalGroup(aLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						aLayout.createSequentialGroup()
								.addGap(49, 49, 49)
								.addGroup(
										aLayout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														jLabel4,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														450,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(
														jLabel3,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														450,
														javax.swing.GroupLayout.PREFERRED_SIZE))
								.addContainerGap(36, Short.MAX_VALUE)));
		aLayout.setVerticalGroup(aLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						aLayout.createSequentialGroup()
								.addGap(20, 20, 20)
								.addComponent(jLabel3,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										258,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jLabel4,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										258,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(32, 32, 32)));

	}// </editor-fold>//GEN-END:initComponents

	public static boolean pauseThread() throws InterruptedException {
		running = false;
		return running;
	}

	public static boolean resumeThread() {
		running = true;
		return running;
	}

	public ShotRecord getRightToLeft() {
		return righttoLeftShot;
	}

	public ShotRecord getLeftToRight() {
		return lefttoRightShot;
	}

	public javax.swing.JLabel jLabel3;
	public javax.swing.JLabel jLabel4;
	public javax.swing.JPanel sidePanel;
	// End of variables declaration//GEN-END:variables

}
