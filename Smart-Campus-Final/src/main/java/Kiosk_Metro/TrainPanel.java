package Kiosk_Metro;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.swing.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TrainPanel extends JPanel implements ActionListener {

	ArrayList<TrainArray> arr_time = new ArrayList<>();
	String str_det;
	String str_num;
	String str_state;
	String str_updown;
	String str_line;
	String str_station;
	int i_num;
	int i_express;
	JButton btn_train;
	JLabel lbl;

	TrainPanel(String str, int x, int y, int train, String updown, String number, String state, String line, String station, int express) {
		setLayout(null);
		setBackground(Color.WHITE);
		setOpaque(false);
		this.str_det = str;
		this.str_num = number;
		this.str_line = line;
		this.str_station = station;
		this.i_express = express;
		if(!str_line.equals("1065")) {
			this.i_num = Integer.parseInt(str_num);
		}
		//��, �ϼ� ����
		if (updown.equals("0")) {
			this.str_updown = "1";
		} else {
			this.str_updown = "2";
		}
		//�뼱���� ���� �¿츦 �����Ͽ� ����ö�� �̵��ϴ� �������� ���
		btn_train = new JButton();
		if (updown.equals("0")) {
			if (train == 0) {
				btn_train.setIcon(new ImageIcon("images/train_left.png"));
			} else {
				btn_train.setIcon(new ImageIcon("images/train_right.png"));
			}
		} else {
			if (train == 0) {
				btn_train.setIcon(new ImageIcon("images/train_right.png"));
			} else {
				btn_train.setIcon(new ImageIcon("images/train_left.png"));
			}
		}

		//����ö�� ��Ȯ�� ��ġ�� �޾� ����, ����, ����� �����ϵ��� ������ �ణ�� �Ÿ��� ��
		if (state.equals("0")) {
			str_state = "진입";
			if (updown.equals("0")) {
				if (train == 0) {
					x += 10;
				} else {
					x -= 10;
				}
			} else {
				if (train == 0) {
					x -= 10;
				} else {
					x += 10;
				}
			}
		} else if (state.equals("1")) {
			str_state = "도착";
		} else {
			str_state = "출발";
			if (updown.equals("0")) {
				if (train == 0) {
					x -= 10;
				} else {
					x += 10;
				}
			} else {
				if (train == 0) {
					x += 10;
				} else {
					x -= 10;
				}
			}
		}
		//��ȯ�뼱���� Ȯ��
		if (str_det.equals("응암(하선-종착)")) {
			str_det = "응암순환";
		}
		
		if(line.equals("1009")) {
			if(i_express != 1 && updown.equals("0")) {
				y -= 25;
			} else if(i_express != 1 && updown.equals("1")) {
				y += 25;
			}
		}

		btn_train.setBorderPainted(false);
		btn_train.setFocusPainted(false);
		btn_train.setContentAreaFilled(false);
		btn_train.setBounds(0, 0, 60, 25);

		if(i_express == 1) {
			// 급행
			lbl = new JLabel(str_det, SwingConstants.CENTER);
			lbl.setForeground(Color.RED);
		} else {
			// 일반
			lbl = new JLabel(str_det, SwingConstants.CENTER);
			lbl.setForeground(Color.BLACK);
		}
		Font f = new Font("맑은 고딕", Font.BOLD, 9);
		lbl.setFont(f);
		lbl.setBounds(0, 2, 60, 18);

		add(lbl);
		add(btn_train);
		//��������� �׼Ǹ����ʷ� ���
		//btn_train.addActionListener(this);
		btn_train.addMouseListener(new MouseAdapter()
        {
            public void mouseEntered(MouseEvent evt)
            {
            	lbl.setText(str_num);
            }
            public void mouseExited(MouseEvent evt)
            {
            	lbl.setText(str_det);
            }
            public void mousePressed(MouseEvent evt)
            {
                
            }
            public void mouseReleased(MouseEvent evt)
            {
                
            }
        });
		setBounds(x, y, 60, 25);
	}

	//����ö �����͸� �ҷ��´�
	private void DataLoad(String s) {
		String url = s;
		String str_result = "";
		try {
			URL obj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);

			conn.setRequestMethod("GET");

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			JSONParser js = new JSONParser();
			JSONObject jo = (JSONObject) js.parse(response.toString());
			JSONObject jr = (JSONObject) jo.get("SearchViaSTNArrivalTimeByTrainService");
			JSONArray jsonArr = (JSONArray) jr.get("row");

			System.out.println(response.toString());
			System.out.println(jsonArr.toString());

			for (int i = 0; i < jsonArr.size(); i++) {
				String hobby = jsonArr.get(i).toString();
				JSONObject jj = (JSONObject) js.parse(hobby);
				str_result += jj.get("STATION_NM") + "역\t   시간: " + jj.get("ARRIVETIME") + "\n";
				System.out.println("[" + i + "] 시간: " + jj.get("ARRIVETIME") + "\t역 코드: " + jj.get("STATION_CD")
						+ "\t역: " + jj.get("STATION_NM"));
			}

			TrainDialog td = new TrainDialog(str_result, str_det, str_num, str_state, str_station);
			td.setVisible(true);

		} catch (Exception e) {
			TrainDialog td = new TrainDialog("시간표 정보를 불러올 수 없습니다..\n", str_det, str_num, str_state, str_station);
			td.setVisible(true);
		}
	}
	private String SetTrainNo(String s) {
		String str_return = "";

		switch (s) {
		case "1001":
			str_return = "K" + i_num;
			break;
		case "1003":
			str_return = "S" + i_num;
			break;
		case "1004":
			str_return = "K" + i_num;
			break;
		case "1063":
			str_return = "K" + i_num;
			break;
		case "1067":
			str_return = "K" + i_num;
			break;
		case "1071":
			str_return = "K" + i_num;
			break;
		case "1075":
			str_return = "K" + i_num;
			break;
		case "1073":
			str_return = "K" + i_num;
			break;
		default:
			str_return = i_num + "";
			break;
		}
		return str_return;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//����ö ��� Ŭ���� �� �ش� ������ ������ �ҷ���
		if (e.getSource() == btn_train) {
			Boolean bool_num = false;
			if(bool_num) {
				lbl.setText(str_det);
				bool_num = false;
			} else {
				lbl.setText(str_num);
				bool_num = true;
			}
			/*String str_url = "http://openAPI.seoul.go.kr:8088/sample/json/SearchViaSTNArrivalTimeByTrainService/1/5/";
			str_url += SetTrainNo(str_line) + "/1/" + str_updown;
			DataLoad(str_url);
			System.out.println(
					"열차번호: " + SetTrainNo(str_line) + "\t행선지: " + str_det + "행\t열차 상태: " + str_state + "\t열차 방향: " + str_updown);*/
		}
	}
}
