package kiosk;

import javax.swing.*;
import java.awt.*;
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
        initComponents();
        app.Global.setAppIcon(this);
        startDateTimeUpdater();
        updateWeatherInfo();
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlContainer = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblLogo = new javax.swing.JLabel();
        btnStart = new javax.swing.JButton();
        lblCurrentDateTime = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("WELCOME");
        setName(""); 
        setPreferredSize(new java.awt.Dimension(720, 860));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        pnlContainer.setPreferredSize(new java.awt.Dimension(480, 640));
        pnlContainer.setLayout(new java.awt.GridBagLayout());

        lblTitle.setFont(lblTitle.getFont().deriveFont(lblTitle.getFont().getSize()+20f));
        lblTitle.setText("Smart Campus");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlContainer.add(lblTitle, gridBagConstraints);

        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/app/customer-logo.jpeg"))); 
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(60, 60, 60, 60);
        pnlContainer.add(lblLogo, gridBagConstraints);

        btnStart.setText("동양미래대학교 방문을 환영합니다.");
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 35;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        pnlContainer.add(btnStart, gridBagConstraints);
        // Weather components initialization
        lblWeatherIcon = new javax.swing.JLabel();
        lblWeatherDescription = new javax.swing.JLabel();
        lblCurrentTemp = new javax.swing.JLabel();
        lblHumidity = new javax.swing.JLabel();
        lblWind = new javax.swing.JLabel();
        lblCity = new javax.swing.JLabel();
        lblCloud = new javax.swing.JLabel();
        lblTempMin = new javax.swing.JLabel();
        lblTempMax = new javax.swing.JLabel();

        // Position the weather components below the welcome button (assuming 'btnStart' is the welcome button)
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        
        gridBagConstraints.gridy += 1; // Adjusting the positioning to be below the welcome button
        pnlContainer.add(lblWeatherIcon, gridBagConstraints);
        
        gridBagConstraints.gridy++;
        pnlContainer.add(lblWeatherDescription, gridBagConstraints);
        
        gridBagConstraints.gridy++;
        pnlContainer.add(lblCurrentTemp, gridBagConstraints);
        
        gridBagConstraints.gridy++;
        pnlContainer.add(lblHumidity, gridBagConstraints);
        
        gridBagConstraints.gridy++;
        pnlContainer.add(lblWind, gridBagConstraints);
        
        gridBagConstraints.gridy++;
        pnlContainer.add(lblCity, gridBagConstraints);
        
        gridBagConstraints.gridy++;
        pnlContainer.add(lblCloud, gridBagConstraints);
        
        gridBagConstraints.gridy++;
        pnlContainer.add(lblTempMin, gridBagConstraints);
        
        gridBagConstraints.gridy++;
        pnlContainer.add(lblTempMax, gridBagConstraints);
        lblCurrentDateTime.setFont(lblCurrentDateTime.getFont().deriveFont(lblCurrentDateTime.getFont().getSize() + 15f));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlContainer.add(lblCurrentDateTime, gridBagConstraints);

        getContentPane().add(pnlContainer, new java.awt.GridBagConstraints());

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

    private javax.swing.JButton btnStart;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlContainer;

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
            
            // Resize the image to 300x300
            java.awt.Image originalImage = new javax.swing.ImageIcon(getClass().getResource(iconPath)).getImage();
            java.awt.Image resizedImage = originalImage.getScaledInstance(50, 50, java.awt.Image.SCALE_DEFAULT);
            
            lblWeatherIcon.setIcon(new javax.swing.ImageIcon(resizedImage));


           
            lblCurrentTemp.setText(Math.round(main.getDouble("temp") - 273.15) + "º");
            lblHumidity.setText("습도: " + main.getInt("humidity") + "%");
            lblWind.setText("바람: " + wind.getDouble("speed") + " m/s");
            lblCity.setText("서울");
            lblCloud.setText("구름: " + clouds.getInt("all") + "%");
            lblTempMin.setText("최저 온도: " + Math.round(main.getDouble("temp_min") - 273.15) + "º");
            lblTempMax.setText("최고 온도: " + Math.round(main.getDouble("temp_max") - 273.15) + "º");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}