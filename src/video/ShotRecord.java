package video;

public class ShotRecord {

	private int starting_x;
	private int starting_y;
	private int starting_h;

	private int pitch_x_service;
	private int pitch_y_service;


	private double pitch_z_service_time;
	private double pitch_z_regular_time;

	private int pitch_x_regular;
	private int pitch_y_regular;


	private int ending_x;
	private int ending_y;
	private int ending_z;

	private double before_pitch_velocity_service;
	private double after_pitch_velocity_service;

	private double before_pitch_velocity_regular;
	private double after_pitch_velocity_regular;

	private double calorie;

	

	private int pid;
	private int result;
	private int mid;
	private int rid;
	private double time;

	public ShotRecord() {

	}

	
	
	public double getCalorie() {
		return calorie;
	}

	public void setCalorie(double calorie) {
		this.calorie = calorie;
	}
	public double getPitch_z_service_time() {
		return pitch_z_service_time;
	}

	public void setPitch_z_service_time(double pitch_z_service_time) {
		this.pitch_z_service_time = pitch_z_service_time;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public double getPitch_z_regular_time() {
		return pitch_z_regular_time;
	}

	public void setPitch_z_regular_time(double pitch_z_regular_time) {
		this.pitch_z_regular_time = pitch_z_regular_time;
	}

	public int getStarting_x() {
		return starting_x;
	}

	public void setStarting_x(int starting_x) {
		this.starting_x = starting_x;
	}

	public int getStarting_y() {
		return starting_y;
	}

	public void setStarting_y(int starting_y) {
		this.starting_y = starting_y;
	}

	public int getStarting_h() {
		return starting_h;
	}

	public void setStarting_h(int starting_h) {
		this.starting_h = starting_h;
	}

	public int getPitch_x_service() {
		return pitch_x_service;
	}

	public void setPitch_x_service(int pitch_x_service) {
		this.pitch_x_service = pitch_x_service;
	}

	public int getPitch_y_service() {
		return pitch_y_service;
	}

	public void setPitch_y_service(int pitch_y_service) {
		this.pitch_y_service = pitch_y_service;
	}


	public int getPitch_x_regular() {
		return pitch_x_regular;
	}

	public void setPitch_x_regular(int pitch_x_regular) {
		this.pitch_x_regular = pitch_x_regular;
	}

	public int getPitch_y_regular() {
		return pitch_y_regular;
	}

	public void setPitch_y_regular(int pitch_y_regular) {
		this.pitch_y_regular = pitch_y_regular;
	}

	

	
	public int getEnding_x() {
		return ending_x;
	}

	public void setEnding_x(int ending_x) {
		this.ending_x = ending_x;
	}

	public int getEnding_y() {
		return ending_y;
	}

	public void setEnding_y(int ending_y) {
		this.ending_y = ending_y;
	}

	public int getEnding_z() {
		return ending_z;
	}

	public void setEnding_z(int ending_z) {
		this.ending_z = ending_z;
	}

	public double getBefore_pitch_velocity_service() {
		return before_pitch_velocity_service;
	}

	public void setBefore_pitch_velocity_service(
			double before_pitch_velocity_service) {
		this.before_pitch_velocity_service = before_pitch_velocity_service;
	}

	public double getAfter_pitch_velocity_service() {
		return after_pitch_velocity_service;
	}

	public void setAfter_pitch_velocity_service(double after_pitch_velocity_service) {
		this.after_pitch_velocity_service = after_pitch_velocity_service;
	}

	public double getBefore_pitch_velocity_regular() {
		return before_pitch_velocity_regular;
	}

	public void setBefore_pitch_velocity_regular(
			double before_pitch_velocity_regular) {
		this.before_pitch_velocity_regular = before_pitch_velocity_regular;
	}

	public double getAfter_pitch_velocity_regular() {
		return after_pitch_velocity_regular;
	}

	public void setAfter_pitch_velocity_regular(double after_pitch_velocity_regular) {
		this.after_pitch_velocity_regular = after_pitch_velocity_regular;
	}

	


	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

}
