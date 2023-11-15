package Kiosk_Metro;

public class StationList {
	String str_code;
	String str_name;
	int i_type;
	int i_express;
	String str_html;
	int i_size;
	int x;
	int y;
	int train;
	public StationList(String str_code, String str_name, int i_type, int i_express, String str_html, int i_size, int x, int y, int train) {
		this.str_code = str_code;
		this.str_name = str_name;
		this.i_type = i_type;
		this.i_express = i_express;
		this.str_html = str_html;
		this.i_size = i_size;
		this.x = x;
		this.y = y;
		this.train = train;
	}
}
