package Kiosk_Metro;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.JPanel;

import javax.swing.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main extends JPanel implements ActionListener, Runnable {
    static Main panel;
    JPanel background;
    JLabel jb;
    ArrayList<StationList> arr_list = new ArrayList<>();
    ArrayList<TrainArray> arr_train = new ArrayList<>();
    JButton btn_refresh;
    JComboBox jcb;
    JCheckBox ckb_refresh;
    TrainPanel tpl[] = null;
    String arr_line[] = { "1호선", "2호선", "3호선", "4호선", "5호선", "6호선", "7호선", "8호선", "9호선", "경의중앙선", "분당선", "수인선", "신분당선", "경춘선", "공항철도" };
    ImageIcon icon;
    String str_line = "1001";
    String str_img = "images/bg_line1.png"; 
    int i_arr = 0; 
    boolean bool_thread = true;

    public Main() {
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception exc) {
            System.err.println("Theme error");
        }
        
        setLayout(null);
        setPreferredSize(new Dimension(969, 621));

        jcb = new JComboBox(arr_line);
        jcb.setBounds(790, 10, 152, 30);
        add(jcb);
        jcb.addActionListener(this);

        btn_refresh = new JButton("새로고침");
        btn_refresh.setBounds(685, 10, 100, 30);
        add(btn_refresh);
        btn_refresh.addActionListener(e -> {
            icon = new ImageIcon(str_img);
            new Thread(() -> {
                System.err.println("버튼 클릭");
                btn_refresh.setEnabled(false);
                btn_refresh.setText("새로고침 중");
                loadData(arr_line[i_arr]);
                setData();
                jb.setIcon(icon);
                btn_refresh.setEnabled(true);
                btn_refresh.setText("새로고침");
            }).start();
        });
        ckb_refresh = new JCheckBox("자동 새로고침");
        ckb_refresh.setBounds(570, 12, 115, 25);
        ckb_refresh.setSelected(true);
        add(ckb_refresh);
        ckb_refresh.addActionListener(e -> {
            if (bool_thread) {
                bool_thread = false;
            } else {
                bool_thread = true;
                new Thread(panel).start();
            }
        });
        background = new JPanel() {
            protected void paintComponent(Graphics g) {
                setOpaque(false);
                super.paintComponent(g);
            }
        };
        background.setLayout(null);
        background.setBounds(0, 0, 969, 621);
        add(background);

        loadData("1호선");
        setData();

        jb = new JLabel();
        jb.setIcon(icon);
        jb.setBounds(0, 50, 969, 571);
        add(jb);
    }
   public void setData() {
      arr_list = new ArrayList<>();
      background.removeAll();
      DataTest dt = new DataTest(str_line);
      arr_list = dt.arr;

      for (int i = 0; i < arr_list.size(); i++) {
         String str = arr_list.get(i).str_html;
         int x = arr_list.get(i).x;
         int y = arr_list.get(i).y;
         int i_size = arr_list.get(i).i_size;
         background.add(new StaLine(str, x, y + 50, i_size, str_line));

         tpl = new TrainPanel[arr_train.size()];
         for (int j = 0; j < arr_train.size(); j++) {
            if (arr_list.get(i).str_code.equals(arr_train.get(j).sta_no)) {
               if (str_line.equals("1002")) {
                  if (Integer.parseInt(arr_list.get(i).str_code) > 1002000217
                        && Integer.parseInt(arr_list.get(i).str_code) < 1002000240) {
                     if (arr_train.get(j).train_updown.equals("0")) {
                        if (arr_train.get(j).train_det.equals("성수종착")) {
                           arr_train.get(j).train_det = "내선순환";
                        }
                        tpl[j] = new TrainPanel(arr_train.get(j).train_det, x - 13, y + 50 - 26,
                              arr_list.get(i).train, arr_train.get(j).train_updown, arr_train.get(j).train_no,
                              arr_train.get(j).train_state, str_line, arr_list.get(i).str_name, Integer.parseInt(arr_train.get(j).train_express));
                     } else {
                        if (arr_train.get(j).train_det.equals("성수종착")) {
                           arr_train.get(j).train_det = "외선순환";
                        }
                        tpl[j] = new TrainPanel(arr_train.get(j).train_det, x - 13, y + 50 + 35,
                              arr_list.get(i).train, arr_train.get(j).train_updown, arr_train.get(j).train_no,
                              arr_train.get(j).train_state, str_line, arr_list.get(i).str_name, Integer.parseInt(arr_train.get(j).train_express));
                     }
                  } else {
                     if (arr_train.get(j).train_updown.equals("1")) {
                        if (arr_train.get(j).train_det.equals("성수종착")) {
                           arr_train.get(j).train_det = "외선순환";
                        }
                        tpl[j] = new TrainPanel(arr_train.get(j).train_det, x - 13, y + 50 - 26,
                              arr_list.get(i).train, arr_train.get(j).train_updown, arr_train.get(j).train_no,
                              arr_train.get(j).train_state, str_line, arr_list.get(i).str_name, Integer.parseInt(arr_train.get(j).train_express));
                     } else {
                        if (arr_train.get(j).train_det.equals("성수종착")) {
                           arr_train.get(j).train_det = "내선순환";
                        }
                        tpl[j] = new TrainPanel(arr_train.get(j).train_det, x - 13, y + 50 + 35,
                              arr_list.get(i).train, arr_train.get(j).train_updown, arr_train.get(j).train_no,
                              arr_train.get(j).train_state, str_line, arr_list.get(i).str_name, Integer.parseInt(arr_train.get(j).train_express));
                     }
                  }
               } else {
                  if (arr_train.get(j).train_updown.equals("0")) {
                     tpl[j] = new TrainPanel(arr_train.get(j).train_det, x - 13, y + 50 - 26,
                           arr_list.get(i).train, arr_train.get(j).train_updown, arr_train.get(j).train_no,
                           arr_train.get(j).train_state, str_line, arr_list.get(i).str_name, Integer.parseInt(arr_train.get(j).train_express));
                  } else {
                     tpl[j] = new TrainPanel(arr_train.get(j).train_det, x - 13, y + 50 + 35,
                           arr_list.get(i).train, arr_train.get(j).train_updown, arr_train.get(j).train_no,
                           arr_train.get(j).train_state, str_line, arr_list.get(i).str_name, Integer.parseInt(arr_train.get(j).train_express));
                  }
               }
               background.add(tpl[j]);
            }
         }
      }
   }
   public void loadData(String str) {
      arr_train = new ArrayList<>();
      String url = "http://swopenAPI.seoul.go.kr/api/subway/4747575451646f6e37384d63705664/json/realtimePosition/0/1000/1%ED%98%B8%EC%84%A0/json/realtimePosition/0/200/";
      try {
         String url_encode = URLEncoder.encode(str, "UTF-8");
         try {
            URL obj = new URL(url + url_encode);
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
            JSONArray jsonArr = (JSONArray) jo.get("realtimePositionList");

            //System.out.println(response.toString());

            for (int i = 0; i < jsonArr.size(); i++) {
               String hobby = jsonArr.get(i).toString();
               JSONObject jj = (JSONObject) js.parse(hobby);
               arr_train.add(new TrainArray(jj.get("statnId").toString(), jj.get("updnLine").toString(),
                     jj.get("trainNo").toString(), jj.get("statnTnm").toString(),
                     jj.get("trainSttus").toString(), jj.get("directAt").toString()));
               /*System.out.println("[" + i + "]" + "\t코드: " + arr_train.get(i).sta_no + "\t\t열차방향: "
                     + arr_train.get(i).train_updown + "\t열차번호: " + arr_train.get(i).train_no + "\t도착지: "
                     + arr_train.get(i).train_det + "\t열차상태: " + arr_train.get(i).train_state + "\t급행: " + arr_train.get(i).train_express);
            */
            }
         } catch (Exception e) {
            e.printStackTrace();
         }
      } catch (UnsupportedEncodingException e1) {
         // TODO Auto-generated catch block
         e1.printStackTrace();
      }
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      // TODO Auto-generated method stub
      //  ư            ߻  ϴ   ̺ Ʈ   ó   Ѵ .
      /*if (e.getSource() == btn[0]) {
         str_img = "images/bg_line1.png";
         icon = new ImageIcon(str_img);
         str_line = "1001";
         i_arr = 0;
         loadData(arr_line[i_arr]);
         System.out.println("1호선");
      }
      if (e.getSource() == btn[1]) {
         str_img = "images/bg_line2.png";
         icon = new ImageIcon(str_img);
         str_line = "1002";
         i_arr = 1;
         loadData(arr_line[i_arr]);
         System.out.println("2호선");
      }
      if (e.getSource() == btn[2]) {
         str_img = "images/bg_line3.png";
         icon = new ImageIcon(str_img);
         str_line = "1003";
         i_arr = 2;
         loadData(arr_line[i_arr]);
         System.out.println("3호선");
      }
      if (e.getSource() == btn[3]) {
         str_img = "images/bg_line4.png";
         icon = new ImageIcon(str_img);
         str_line = "1004";
         i_arr = 3;
         loadData(arr_line[i_arr]);
         System.out.println("4호선");
      }
      if (e.getSource() == btn[4]) {
         str_img = "images/bg_line5.png";
         icon = new ImageIcon(str_img);
         str_line = "1005";
         i_arr = 4;
         loadData(arr_line[i_arr]);
         System.out.println("5호선");
      }
      if (e.getSource() == btn[5]) {
         str_img = "images/bg_line6.png";
         icon = new ImageIcon(str_img);
         str_line = "1006";
         i_arr = 5;
         loadData(arr_line[i_arr]);
         System.out.println("6호선");
      }
      if (e.getSource() == btn[6]) {
         str_img = "images/bg_line7.png";
         icon = new ImageIcon(str_img);
         str_line = "1007";
         i_arr = 6;
         loadData(arr_line[i_arr]);
         System.out.println("7호선");
      }
      if (e.getSource() == btn[7]) {
         str_img = "images/bg_line8.png";
         icon = new ImageIcon(str_img);
         str_line = "1008";
         i_arr = 7;
         loadData(arr_line[i_arr]);
         System.out.println("8호선");
      }
      if (e.getSource() == btn[8]) {
         str_img = "images/bg_line9.png";
         icon = new ImageIcon(str_img);
         str_line = "1009";
         i_arr = 8;
         loadData(arr_line[i_arr]);
         System.out.println("9호선");
      }
      if (e.getSource() == btn[9]) {
         str_img = "images/bg_linekj.png";
         icon = new ImageIcon(str_img);
         str_line = "1063";
         i_arr = 9;
         loadData(arr_line[i_arr]);
         System.out.println("경의중앙선");
      }
      if (e.getSource() == btn[10]) {
         str_img = "images/bg_linebd.png";
         icon = new ImageIcon(str_img);
         str_line = "1075";
         i_arr = 10;
         loadData(arr_line[i_arr]);
         System.out.println("분당선");
      }
      if (e.getSource() == btn[11]) {
         str_img = "images/bg_linesi.png";
         icon = new ImageIcon(str_img);
         str_line = "1071";
         i_arr = 11;
         loadData(arr_line[i_arr]);
         System.out.println("수인선");
      }
      if (e.getSource() == btn[12]) {
         str_img = "images/bg_linesb.png";
         icon = new ImageIcon(str_img);
         str_line = "1077";
         i_arr = 12;
         loadData(arr_line[i_arr]);
         System.out.println("신분당선");
      }
      if (e.getSource() == btn[13]) {
         str_img = "images/bg_linekc.png";
         icon = new ImageIcon(str_img);
         str_line = "1067";
         i_arr = 13;
         loadData(arr_line[i_arr]);
         System.out.println("경춘선");
      }
      if (e.getSource() == btn[14]) {
         str_img = "images/bg_linegc.png";
         icon = new ImageIcon(str_img);
         str_line = "1065";
         i_arr = 14;
         loadData(arr_line[i_arr]);
         System.out.println("공항철도");
      }*/
      
      if(e.getSource() == jcb) {
         switch(jcb.getSelectedIndex()) {
         case 0:
            str_img = "images/bg_line1.png";
            icon = new ImageIcon(getClass().getResource("/images/bg_line1.png"));
            str_line = "1001";
            i_arr = 0;
            loadData(arr_line[i_arr]);
            System.out.println("1호선");
            break;
         case 1:
            str_img = "images/bg_line2.png";
            icon = new ImageIcon(str_img);
            str_line = "1002";
            i_arr = 1;
            loadData(arr_line[i_arr]);
            System.out.println("2호선");
            break;         
         case 2:
            str_img = "images/bg_line3.png";
            icon = new ImageIcon(str_img);
            str_line = "1003";
            i_arr = 2;
            loadData(arr_line[i_arr]);
            System.out.println("3호선");
            break;
            
         case 3:
            str_img = "images/bg_line4.png";
            icon = new ImageIcon(str_img);
            str_line = "1004";
            i_arr = 3;
            loadData(arr_line[i_arr]);
            System.out.println("4호선");
            break;
            
         case 4:
            str_img = "images/bg_line5.png";
            icon = new ImageIcon(str_img);
            str_line = "1005";
            i_arr = 4;
            loadData(arr_line[i_arr]);
            System.out.println("5호선");
            break;
            
         case 5:
            str_img = "images/bg_line6.png";
            icon = new ImageIcon(str_img);
            str_line = "1006";
            i_arr = 5;
            loadData(arr_line[i_arr]);
            System.out.println("6호선");
            break;
            
         case 6:
            str_img = "images/bg_line7.png";
            icon = new ImageIcon(str_img);
            str_line = "1007";
            i_arr = 6;
            loadData(arr_line[i_arr]);
            System.out.println("7호선");
            break;
            
         case 7:
            str_img = "images/bg_line8.png";
            icon = new ImageIcon(str_img);
            str_line = "1008";
            i_arr = 7;
            loadData(arr_line[i_arr]);
            System.out.println("8호선");
            break;
            
         case 8:
            str_img = "images/bg_line9.png";
            icon = new ImageIcon(str_img);
            str_line = "1009";
            i_arr = 8;
            loadData(arr_line[i_arr]);
            System.out.println("9호선");
            break;
         
         case 9:
            str_img = "images/bg_linekj.png";
            icon = new ImageIcon(str_img);
            str_line = "1063";
            i_arr = 9;
            loadData(arr_line[i_arr]);
            System.out.println("경의중앙선");
            break;
            
         case 10:
            str_img = "images/bg_linebd.png";
            icon = new ImageIcon(str_img);
            str_line = "1075";
            i_arr = 10;
            loadData(arr_line[i_arr]);
            System.out.println("분당선");
            break;
            
         case 11:
            str_img = "images/bg_linesi.png";
            icon = new ImageIcon(str_img);
            str_line = "1071";
            i_arr = 11;
            loadData(arr_line[i_arr]);
            System.out.println("수인선");
            break;
            
         case 12:
            str_img = "images/bg_linesb.png";
            icon = new ImageIcon(str_img);
            str_line = "1077";
            i_arr = 12;
            loadData(arr_line[i_arr]);
            System.out.println("신분당선");
            break;
            
         case 13:
            str_img = "images/bg_linekc.png";
            icon = new ImageIcon(str_img);
            str_line = "1067";
            i_arr = 13;
            loadData(arr_line[i_arr]);
            System.out.println("경춘선");
            break;
            
         case 14:
            str_img = "images/bg_linegc.png";
            icon = new ImageIcon(str_img);
            str_line = "1065";
            i_arr = 14;
            loadData(arr_line[i_arr]);
            System.out.println("공항철도");
            break;
            
         default:
            break;
         
         }
      }
      setData();
      jb.setIcon(icon);
   }
   public void run() {
      while(bool_thread) {
         btn_refresh.setEnabled(false);
         btn_refresh.setText("새로고침 중");
         icon = new ImageIcon(str_img);
         loadData(arr_line[i_arr]);
         setData();
         jb.setIcon(icon);
         btn_refresh.setEnabled(true);
         btn_refresh.setText("새로고침");
         try {
            Thread.sleep(10000);
         } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }
    public static void main(String[] args) {
        JFrame frame = new JFrame("실시간 지하철");
        Main panel = new Main(); // Main은 이제 JPanel을 상속받음
        frame.setContentPane(panel); // frame에 panel을 컨텐트 팬으로 설정
        frame.setSize(969, 621); // 또는 panel.setPreferredSize(new Dimension(969, 621)); 사용
        frame.setLocationRelativeTo(null); // 화면 중앙에 위치
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        Thread t1 = new Thread(panel); // panel은 Runnable을 구현하므로 Thread에 직접 전달할 수 있음
        t1.start();
    }   
    
}

