package kiosk;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class StartFrame extends javax.swing.JFrame {

    private String appName;
    private javax.swing.JLabel lblCurrentDateTime;
    private Timer dateTimeTimer;
    private javax.swing.JLabel lblWeatherIcon;
    private javax.swing.JLabel lblWeatherDescription;
    private javax.swing.JLabel lblCurrentTemp;
    private javax.swing.JLabel lblHumidity;
    private javax.swing.JLabel lblWind;
    private javax.swing.JLabel lblCity;
    private javax.swing.JLabel lblCloud;
    private javax.swing.JLabel lblTempMin;
    private javax.swing.JLabel lblTempMax;


    public StartFrame() {
       getContentPane().setBackground(new Color(96, 140, 255));
        initComponents();
        app.Global.setAppIcon(this);
        startDateTimeUpdater();
        updateWeatherInfo();
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlContainer = new javax.swing.JPanel();
        pnlContainer.setBackground(new Color(96, 140, 255));
        // 실시간 콘텐츠 담을 Jpanel
        pnlRealtimeWeatherContainer = new javax.swing.JPanel();
        pnlRealtimeWeatherContainer.setBackground(new Color(96, 140, 255));
        
        pnlRealtimeWeatherTextContainer = new javax.swing.JPanel();
        pnlRealtimeWeatherTextContainer.setBackground(new Color(96, 140, 255));
        
        lblTitle = new javax.swing.JLabel();
        lblTitle.setForeground(new Color(255, 255, 255));
        lblWelcomeText = new javax.swing.JLabel();
        lblWelcomeText.setForeground(new Color(237, 254, 136));
        lblWeatherText = new javax.swing.JLabel();
        lblWeatherText.setForeground(new Color(255,255,255));
        lblRealTimeText = new javax.swing.JLabel();
        lblRealTimeText.setForeground(new Color(255,255,255));
        lblLogo = new javax.swing.JLabel();
        btnStart = new javax.swing.JButton();

        lblCurrentDateTime = new javax.swing.JLabel(); lblCurrentDateTime.setForeground(new Color(255,255,255));
        lblCurrentDateTime.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        
        lblWeatherIcon = new javax.swing.JLabel(); 
        lblWeatherDescription = new javax.swing.JLabel(); 
        
        lblCurrentTemp = new javax.swing.JLabel(); lblCurrentTemp.setForeground(new Color(255,255,255));
        lblCurrentTemp.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        
        lblHumidity = new javax.swing.JLabel(); lblHumidity.setForeground(new Color(255,255,255));
        lblHumidity.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        
        lblWind = new javax.swing.JLabel(); lblWind.setForeground(new Color(255,255,255));
        lblWind.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        
        lblCity = new javax.swing.JLabel(); lblCity.setForeground(new Color(255,255,255));
        lblCity.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        
        lblCloud = new javax.swing.JLabel();
        
        lblTempMin = new javax.swing.JLabel(); lblTempMin.setForeground(new Color(255,255,255));
        lblTempMin.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        
        lblTempMax = new javax.swing.JLabel(); lblTempMax.setForeground(new Color(255,255,255));
        lblTempMax.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("WELCOME");
        setName(""); 
        setPreferredSize(new java.awt.Dimension(720, 900));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        pnlContainer.setPreferredSize(new java.awt.Dimension(640, 620));
        GridBagLayout gbl_pnlContainer = new GridBagLayout();
        gbl_pnlContainer.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        gbl_pnlContainer.columnWeights = new double[]{1.0};
        pnlContainer.setLayout(gbl_pnlContainer);

        // SMART CAMPUS Text
        lblTitle.setFont(new Font("Arial", Font.BOLD, 40));
        lblTitle.setText("SMART CAMPUS");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(0, 40, 5, 0);
        pnlContainer.add(lblTitle, gridBagConstraints);
      
        // 동미대환영 Text
        lblWelcomeText.setFont(new Font("맑은 고딕", Font.BOLD, 32));
        lblWelcomeText.setText("안녕하세요 동양미래대학교입니다.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(0, 40, 60, 0);
        pnlContainer.add(lblWelcomeText, gridBagConstraints);
        
        // 실시간 기상정보
        lblWeatherText.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        lblWeatherText.setText("실시간 기상정보");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(0, 40, 10, 0);
        pnlContainer.add(lblWeatherText, gridBagConstraints);
        
        
        // 실시간 기상정보 패널 -- Start
        pnlRealtimeWeatherContainer.setLayout(new GridBagLayout());
        
        
        
        // Weather 아이콘
        java.awt.Image originalImage = new javax.swing.ImageIcon(getClass().getResource("/icons/sunny.png")).getImage();
       int newWidth = 90; // 원하는 너비 설정
       int newHeight = 90; // 원하는 높이 설정
       java.awt.Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);
        lblLogo.setIcon(new javax.swing.ImageIcon(resizedImage)); 
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
        gridBagConstraints.insets = new Insets(0, 0, 0, 0);
        pnlRealtimeWeatherContainer.add(lblLogo, gridBagConstraints);

        
        //실시간 기상정보 텍스트 패널
        pnlRealtimeWeatherTextContainer.setLayout(new GridBagLayout());
        
        // 도시
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(5, 5, 5, 10);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlRealtimeWeatherTextContainer.add(lblCity, gridBagConstraints);
        
        // 바람
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(5, 5, 5, 10);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlRealtimeWeatherTextContainer.add(lblWind, gridBagConstraints);
        
        // 습도
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(5, 5, 5, 10);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlRealtimeWeatherTextContainer.add(lblHumidity, gridBagConstraints);
        
        // 현재온도
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(5, 5, 5, 10);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlRealtimeWeatherTextContainer.add(lblCurrentTemp, gridBagConstraints);
        
        // 최저온도
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(5, 5, 5, 10);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlRealtimeWeatherTextContainer.add(lblTempMin, gridBagConstraints);
        
        // 최고온도
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(5, 5, 5, 10);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlRealtimeWeatherTextContainer.add(lblTempMax, gridBagConstraints);
        

        // 실시간 텍스트 패널 => 실시간 패널에 추가 (pnlRealtimeWeatherContainer)
        GridBagConstraints gbc_pnl = new GridBagConstraints();
        gbc_pnl.gridx = 1;
        gbc_pnl.gridy = 0;
        gbc_pnl.anchor = java.awt.GridBagConstraints.WEST;
        gbc_pnl.insets = new Insets(0, 20, 0, 0);
        
        pnlRealtimeWeatherContainer.add(pnlRealtimeWeatherTextContainer, gbc_pnl);
        
        
        // 실시간 정보 패널 => pnlContainer에 추가
        GridBagConstraints gbc_pnlRealtimeWeatherContainer = new GridBagConstraints();
        gbc_pnlRealtimeWeatherContainer.gridx = 0;
        gbc_pnlRealtimeWeatherContainer.gridy = 6;
        gbc_pnlRealtimeWeatherContainer.anchor = java.awt.GridBagConstraints.WEST;
        gbc_pnlRealtimeWeatherContainer.insets = new Insets(0, 40, 40, 0);
        pnlContainer.add(pnlRealtimeWeatherContainer,gbc_pnlRealtimeWeatherContainer);
        // 실시간 기상정보 패널 -- Done
        
        
        // 현재시각 Label
        lblRealTimeText.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        lblRealTimeText.setText("현재시각");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(0, 40, 10, 0);
        pnlContainer.add(lblRealTimeText, gridBagConstraints);
        
        // 현재시간 xx년 xx월 xx일 xx시 ~~~
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(0, 40, 100, 0);
        pnlContainer.add(lblCurrentDateTime, gridBagConstraints);
        
        
        btnStart = new javax.swing.JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        btnStart.setOpaque(false); // 투명도 설정
        btnStart.setContentAreaFilled(false); // 내용 영역 채우기 설정
        btnStart.setBorderPainted(false); // 테두리 그리기 설정
        btnStart.setText("동양미래대학교 방문을 환영합니다.");
        btnStart.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        btnStart.setForeground(new Color(255, 255, 255)); // 텍스트 색상
        btnStart.setBackground(new Color(255, 255, 255, 128)); // 배경 색상 (투명도 포함)
        btnStart.setPreferredSize(new Dimension(400, 70));
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });
        gridBagConstraints_2 = new java.awt.GridBagConstraints();
        gridBagConstraints_2.insets = new Insets(0, 0, 0, 5);
        gridBagConstraints_2.gridx = 0;
        gridBagConstraints_2.gridy = 16;
        gridBagConstraints_2.anchor = java.awt.GridBagConstraints.SOUTH;
        pnlContainer.add(btnStart, gridBagConstraints_2);
        

        // Position the weather components below the welcome button (assuming 'btnStart' is the welcome button)
//        Integer gridyValue = 1;
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridx = 0;
//        gridBagConstraints.gridy = gridyValue;
//        
//        // Adjusting the positioning to be below the welcome button
//        pnlContainer.add(lblWeatherIcon, gridBagConstraints);
//        
//        gridyValue++;
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridx = 0;
//        gridBagConstraints.gridy = gridyValue;
//        pnlContainer.add(lblWeatherDescription, gridBagConstraints);
//        
//        gridyValue++;
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridx = 0;
//        gridBagConstraints.gridy = gridyValue;
//        pnlContainer.add(lblCurrentTemp, gridBagConstraints);
//        
//        gridyValue++;
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridx = 0;
//        gridBagConstraints.gridy = gridyValue;
//        pnlContainer.add(lblHumidity, gridBagConstraints);
//        
//        gridyValue++;
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridx = 0;
//        gridBagConstraints.gridy = gridyValue;
//        pnlContainer.add(lblWind, gridBagConstraints);
//        
//        gridyValue++;
//        
//        
//        gridyValue++;
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridx = 0;
//        gridBagConstraints.gridy = gridyValue;
//        pnlContainer.add(lblCloud, gridBagConstraints);
//        
//        gridyValue++;
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridx = 0;
//        gridBagConstraints.gridy = gridyValue;
//        pnlContainer.add(lblTempMin, gridBagConstraints);
//        
//        gridyValue++;
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridx = 0;
//        gridBagConstraints.gridy = gridyValue;
//        pnlContainer.add(lblTempMax, gridBagConstraints);
//        lblCurrentDateTime.setFont(lblCurrentDateTime.getFont().deriveFont(lblCurrentDateTime.getFont().getSize() + 15f));
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridx = 0;
//        gridBagConstraints.gridy = 10;
//        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
//        pnlContainer.add(lblCurrentDateTime, gridBagConstraints);

        GridBagConstraints gbc_pnlContainer = new GridBagConstraints();
        gbc_pnlContainer.insets = new Insets(0, 0, 5, 0);
        gbc_pnlContainer.gridy = 0;
        gbc_pnlContainer.gridx = 0;
        getContentPane().add(pnlContainer, gbc_pnlContainer);

        getAccessibleContext().setAccessibleName("");

        pack();
        setLocationRelativeTo(null);
    }

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            StateManager.reset();
            new MenuFrame().setVisible(true);
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startDateTimeUpdater() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
        lblCurrentDateTime.setText(sdf.format(new Date()));
        
        dateTimeTimer = new Timer();
        dateTimeTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                lblCurrentDateTime.setText(sdf.format(new Date()));
            }
        }, 0, 1000);
    }

    public static void main(String args[]) {
        app.Global.setDefaultTheme();

        java.awt.EventQueue.invokeLater(() -> {
            new StartFrame().setVisible(true);
        });
    }
    
    private static javax.swing.JButton createRoundedButton() {
        javax.swing.JButton roundedButton = new JButton("둥근 버튼") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int arc = 20; // 둥근 모서리의 크기 조정
                int width = getWidth();
                int height = getHeight();
                RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(0, 0, width, height, arc, arc);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(96, 140, 255)); // 배경색 설정
                g2d.fill(roundedRectangle);
            }
        };
        roundedButton.setPreferredSize(new Dimension(150, 50)); // 크기 설정
        return roundedButton;
    }

    private javax.swing.JButton btnStart;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblWelcomeText;
    private javax.swing.JLabel lblWeatherText;
    private javax.swing.JLabel lblRealTimeText;
    private javax.swing.JLabel btn_text;
    private javax.swing.JPanel pnlContainer;
    private javax.swing.JPanel pnlRealtimeWeatherContainer;
    private javax.swing.JPanel pnlRealtimeWeatherTextContainer;
    private javax.swing.JPanel ButtonPanel;
    private GridBagConstraints gridBagConstraints_1;
    private GridBagConstraints gridBagConstraints_2;

    private void updateWeatherInfo() {
        String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=seoul&appid=20c2fc60cba00523d17f5e65e232b5af";
        try {
            java.net.URL url = new java.net.URL(apiUrl);
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            
            org.json.JSONObject jsonObject = new org.json.JSONObject(response.toString());
            org.json.JSONObject main = jsonObject.getJSONObject("main");
            org.json.JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
            org.json.JSONObject wind = jsonObject.getJSONObject("wind");
            org.json.JSONObject clouds = jsonObject.getJSONObject("clouds");
            
            String iconCode = weather.getString("icon").substring(0, 2);
            
            // Set the weather icon based on the weather condition
            String weatherMain = weather.getString("main").toLowerCase();
            String iconPath;
            switch (weatherMain) {
                case "clear":
                    iconPath = "/icons/sunny.png";
                    break;
                case "clouds":
                    iconPath = "/icons/cloud.png";
                    break;
                case "rain":
                    iconPath = "/icons/rain.png";
                    break;
                default:
                    iconPath = "/icons/weather.png"; 
                    break;
            }
            
            java.awt.Image originalImage = new javax.swing.ImageIcon(getClass().getResource(iconPath)).getImage();
            java.awt.Image resizedImage = originalImage.getScaledInstance(50, 50, java.awt.Image.SCALE_DEFAULT);
            
            lblWeatherIcon.setIcon(new javax.swing.ImageIcon(resizedImage));


           
            lblCity.setText("지역 : 서울");
            lblWind.setText("바람 : " + wind.getDouble("speed") + " m/s");
            lblHumidity.setText("습도 : " + main.getInt("humidity") + "%");
            lblCurrentTemp.setText("현재온도 : " + Math.round(main.getDouble("temp") - 273.15) + "도");
            lblTempMin.setText("최저온도 : " + Math.round(main.getDouble("temp_min") - 273.15) + "도");
            lblTempMax.setText("최고온도 : " + Math.round(main.getDouble("temp_max") - 273.15) + "도");
            lblCloud.setText("구름 : " + clouds.getInt("all") + "%");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}