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

    public StartFrame() {
        initComponents();
        app.Global.setAppIcon(this);
        startDateTimeUpdater();
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
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlContainer.add(lblTitle, gridBagConstraints);

        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/app/customer-logo.jpeg"))); 
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
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
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        pnlContainer.add(btnStart, gridBagConstraints);

        // Add the current date and time label here
        lblCurrentDateTime.setFont(lblCurrentDateTime.getFont().deriveFont(lblCurrentDateTime.getFont().getSize() + 15f));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
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
}
