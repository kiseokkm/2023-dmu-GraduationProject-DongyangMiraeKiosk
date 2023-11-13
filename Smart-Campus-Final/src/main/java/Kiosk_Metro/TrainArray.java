package Kiosk_Metro;

public class TrainArray {
	String sta_no;
	String train_updown;
	String train_no;
	String train_det;
	String train_state;
	String train_express;
	
	public TrainArray(String sta_no, String train_updown, String train_no, String train_det, String train_state, String train_express) {
		this.sta_no = sta_no;
		this.train_updown = train_updown;
		this.train_no = train_no;
		this.train_det = train_det;
		this.train_state = train_state;
		this.train_express = train_express;
	}
	public String getStationNum() {
		return this.sta_no;
	}
}
